package com.fictivestudios.tafcha.networkSetup.retrofitsetup

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.Utils.Constants
import com.fictivestudios.tafcha.Utils.PreferenceData
import com.fictivestudios.tafcha.Utils.PreferenceUtils
import com.fictivestudios.tafcha.Utils.validationMessage
import com.fictivestudios.tafcha.activities.MainActivity
import com.fictivestudios.tafcha.activities.RegisterationActivity
import com.fictivestudios.tafcha.databinding.ProgressLoaderBinding
import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface
import com.fictivestudios.tafcha.networkSetup.callhandler.CallHandler
import com.fictivestudios.tafcha.networkSetup.url.BASE_URL
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitSetup {
   //  lateinit var  networkMonitor: NetworkConnectionInterceptor
    private lateinit var dialog: AlertDialog
    fun <T> callApi(
        context: Context,
        progress: Boolean,
        token: String,
        callHandler: CallHandler<T>
    ) {


        val okHttpClient: OkHttpClient = if (token.isEmpty()) {
            OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(

                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()


        } else {

            OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .addInterceptor {
                        chain ->
                 HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header("accept","application/json")
                        .header("Authorization", "Bearer " + token)
                        .method(original.method, original.body)
                        .build()
                        chain.proceed(request)

                }
                .build()

        }


        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            //.baseUrl(BASE_URL)
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        val apiInterface = retrofit.create(ApiInterface::class.java)

        /**
         * Coroutine Exception Handler
         * */
        val coRoutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()

            CoroutineScope(Dispatchers.Main).launch {
                throwable.message.let { callHandler.error(it ?: "") }
            }
        }


        /**
         * Call Api
         * */
        CoroutineScope(Dispatchers.IO + coRoutineExceptionHandler + Job()).launch {
            flow {
                withContext(Dispatchers.Main) {
                    context.loader()
                }
                emit(callHandler.sendRequest(apiInterFace = apiInterface) as Response<*>)
            }.flowOn(Dispatchers.IO).collect { response ->

                withContext(Dispatchers.Main) {
                    hideLoader()
                    if (response.isSuccessful) {
                        callHandler.success(response as T)
                    } else {
                        response.errorBody()?.string()?.let {
                            val jsonObject = JSONObject(it)
                            if (jsonObject.has("message")) {
                                if(jsonObject.getString("message")=="Unauthenticated."){
                                    PreferenceData.clearPreference(context)
                                    PreferenceUtils.saveBoolean(Constants.IS_LOGIN,false)
                                    PreferenceUtils.saveString("token",null.toString())
                                    PreferenceUtils.saveString("nStatus","")
                                    context.startActivity(Intent(context, RegisterationActivity::class.java))
                                    MainActivity().finish().javaClass::class.java
                                    Toast.makeText(context,"Logout Successfully...",Toast.LENGTH_LONG).show()
                                }
                                else{
                                    context.validationMessage(jsonObject.getString("message"))
                                    callHandler.error(jsonObject.getString("message"))
                                }

                            }
                        }
                    }
                }
            }
        }

    }


    private fun Context.loader() = try {
        val alertBuilder = AlertDialog.Builder(this)
        val layoutView = ProgressLoaderBinding.inflate(LayoutInflater.from(this))
        alertBuilder.setCancelable(true)
        alertBuilder.setView(layoutView.root)
        dialog = alertBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }


    fun hideLoader() {
        if (::dialog.isInitialized && dialog.isShowing) {
            dialog.dismiss()
        }
    }

}