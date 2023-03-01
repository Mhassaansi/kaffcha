package com.fictivestudios.tafcha.Utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.fictivestudios.tafcha.R
import com.fictivestudios.tafcha.activities.MainActivity

import kotlinx.android.synthetic.main.titlebar.view.*


class Titlebar : RelativeLayout {

   private lateinit var mView:View

    constructor(context: Context) : super(context) {
        initLayout(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initLayout(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initLayout(context)
    }

    fun initLayout(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(R.layout.titlebar, this, true)
    }

    fun hideTitleBar() {
        resetTitlebar()
    }

    fun resetTitlebar() {
        mView?.rlTitlebarMainLayout?.setVisibility(View.GONE)
    }

    fun setHomeTitle(titleText:String,
                     leftBtnListener: OnClickListener?,
                     drawableLeft: Int,
                     rightBtnListener: OnClickListener,
                     drawableRight: Int)
    {

        mView?.tvTitle?.text = titleText

        mView?.ivBack!!.visibility = View.GONE
        mView?.ivProfile!!.visibility = View.VISIBLE
        mView?.ivProfile!!.setImageResource(drawableLeft)
        mView?.ivProfile!!.setOnClickListener(leftBtnListener)

        mView?.iv_notification!!.visibility = View.VISIBLE
        mView?.ivProfileRight!!.visibility = View.GONE
        mView?.iv_notification!!.setImageResource(drawableRight)
        mView?.iv_notification!!.setOnClickListener(rightBtnListener)
    }

    fun setMainTitle(titleText:String,
                     leftBtnListener: OnClickListener?,
                     drawableLeft: Int,
                     rightBtnListener: OnClickListener,
                     drawableRight: Int)
    {
        mView?.tvTitle?.text = titleText

        mView?.ivBack!!.visibility = View.VISIBLE
        mView?.ivProfile!!.visibility = View.GONE
        mView?.ivBack!!.setImageResource(drawableLeft)
        mView?.ivBack!!.setOnClickListener(leftBtnListener)

        mView?.iv_notification!!.visibility = View.VISIBLE
        mView?.ivProfileRight!!.visibility = View.GONE
        mView?.iv_notification!!.setImageResource(drawableRight)
        mView?.iv_notification!!.setOnClickListener(rightBtnListener)
    }


    fun setBtnBack( listener: OnClickListener?) {

            mView?.ivBack!!.visibility = View.VISIBLE
            mView?.ivProfile!!.visibility = View.GONE
           // mView?.ivBack!!.setImageResource(drawable)
            mView?.ivBack!!.setOnClickListener(listener)
    }

    fun setBtnProfile(/*drawable: Int,*/ listener: OnClickListener?) {

            mView?.ivBack!!.visibility = View.GONE
            mView?.ivProfile!!.visibility = View.VISIBLE
           // mView?.ivProfile!!.setImageResource(drawable)
            mView?.ivProfile!!.setOnClickListener(listener)


    }

    fun setBtnRight(drawable: Int, listener: OnClickListener?) {
        mView?.iv_notification!!.visibility = View.VISIBLE
        mView?.ivProfileRight!!.visibility = View.GONE
        mView?.iv_notification!!.setImageResource(drawable)
        mView?.iv_notification!!.setOnClickListener(listener)

    }

    fun setBtnRightUser(drawable: Int, listener: OnClickListener?) {
        mView?.iv_notification!!.visibility = View.GONE
        mView?.ivProfileRight!!.visibility = View.VISIBLE
        mView?.ivProfileRight!!.setImageResource(drawable)
        mView?.ivProfileRight!!.setOnClickListener(listener)

    }


    fun setTitle(getActivityContext: MainActivity, title: String) {
        mView?.rlTitlebarMainLayout?.setVisibility(View.VISIBLE)
        mView?.tvTitle?.text = title
     /*   mView?.ivBack?.visibility = View.GONE

        mView?.iv_notification?.visibility = View.VISIBLE*/


    }

    fun setBackTitle(getActivityContext: MainActivity, title: String) {
        mView?.rlTitlebarMainLayout?.setVisibility(View.VISIBLE)
        mView?.tvTitle?.text = title
        mView?.ivBack?.visibility = View.VISIBLE
/*        mView?.iv_notification?.visibility = View.GONE*/


        mView?.ivBack?.setOnClickListener {
            getActivityContext.onBackPressed()
        }
    }

    fun setHideTitle() {
        resetTitlebar()
        mView?.rlTitlebarMainLayout?.setVisibility(View.VISIBLE)
        mView?.ivBack?.visibility = View.INVISIBLE
    }
}