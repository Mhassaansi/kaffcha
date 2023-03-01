package com.fictivestudios.tafcha.networkSetup.apiinterface


import com.fictivestudios.tafcha.models.Login.LoginResultpojo
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_compelete.Compelete
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_detail.ChallengeDeails
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_pending.ChallengePending
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_recived.challengeRecived
import com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent.ChallengeSent
import com.fictivestudios.tafcha.models.commonresponse.CommonResponseDC
import com.fictivestudios.tafcha.models.content.Content
import com.fictivestudios.tafcha.models.event.createevent.CreateEventResponse
import com.fictivestudios.tafcha.models.event.event_item.EventItem
import com.fictivestudios.tafcha.models.event.event_list.EventList
import com.fictivestudios.tafcha.models.excercies.ExerCiesList
import com.fictivestudios.tafcha.models.feed.Feed
import com.fictivestudios.tafcha.models.logout.LogoutResponse
import com.fictivestudios.tafcha.models.signup.SignupResponseUserIdData
import com.fictivestudios.tafcha.models.freinds.allusers.AllUsers
import com.fictivestudios.tafcha.models.freinds.friendlist.FreindList
import com.fictivestudios.tafcha.models.freinds.pending.PendingFriends
import com.fictivestudios.tafcha.models.leaderBorad.LeaderBorad
import com.fictivestudios.tafcha.models.notification.Notification
import com.fictivestudios.tafcha.models.profile.trainer_profile.TrainerProfileData
import com.fictivestudios.tafcha.models.profile.trainer_user.TrianerUser
import com.fictivestudios.tafcha.models.profile.userprofile.UserProfileData
import com.fictivestudios.tafcha.models.reminder.ReminderResponse.ReminderResponse
import com.fictivestudios.tafcha.models.reminder.reminderlist.ReminderList
import com.fictivestudios.tafcha.models.reward.Reward
import com.fictivestudios.tafcha.models.trainers.allUsers.AllUsersForTrainers
import com.fictivestudios.tafcha.models.trainers.alltrainers.Alltrainers
import com.fictivestudios.tafcha.models.trainers.mytrainers.MyTrainers
import com.fictivestudios.tafcha.models.trainers.trainerprofile.TrainerProfile
import com.fictivestudios.tafcha.models.trainers.viewmyusers.ViewMyUsers
import com.fictivestudios.tafcha.networkSetup.url.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    /////////////////////////REGISTRATION MODULE//////////////////////////////////

    @Multipart
    @POST(Create_Profile_Trainer)
    suspend fun SignupTrainer(
        @Part part1: MultipartBody.Part?,
        @Part part: MultipartBody.Part?,
        @PartMap hashMap: HashMap<String, RequestBody>
    ): Response<LoginResultpojo>


    @Multipart
    @POST(Create_Profile_User)
    suspend fun SignupUser(
        @Part part: MultipartBody.Part?,
        @PartMap hashMap: HashMap<String, RequestBody>
    ): Response<LoginResultpojo>

    @POST(SIGN_UP)
    suspend fun Signup(@Body requestBody: RequestBody):Response<SignupResponseUserIdData>


    @Multipart
    @POST(Update_Profile)
    suspend fun UpdateUser(
        @Part part: MultipartBody.Part?,
        @PartMap hashMap: HashMap<String, RequestBody>
    ): Response<LoginResultpojo>

    @Multipart
    @POST(Update_Profile)
    suspend fun updateTrainerProfile(
        @Part part1: MultipartBody.Part?,
        @Part part: MultipartBody.Part?,
        @PartMap hashMap: HashMap<String, RequestBody>
    ): Response<LoginResultpojo>

    @POST(LOGIN)
    suspend fun login(@Body requestBody: RequestBody): Response<LoginResultpojo>

    @POST(Verification)
    suspend fun verifyOtp(@Body requestBody: RequestBody): Response<LoginResultpojo>

    @POST(Resend_Verification)
    suspend fun resend_verifyOtp(@Body requestBody: RequestBody): Response<CommonResponseDC>

    @POST(Forget_Password)
    suspend fun forgetPassword(@Body requestBody: RequestBody): Response<CommonResponseDC>

    @GET(View_Profile)
    suspend fun getTrainerProfileData(): Response<TrainerProfileData>

    @GET(View_Profile)
    suspend fun getUserProfileData() :Response<UserProfileData>

    @POST(Change_Password)
    suspend fun changePassword(@Body requestBody: RequestBody): Response<CommonResponseDC>

    @GET(Logout)
    suspend fun logout():Response<LogoutResponse>

    @POST(Forgot_Password_Verify)
    suspend fun forgetPasswordVerify(@Body requestBody: RequestBody):Response<CommonResponseDC>

    @POST(Reset_Password)
    suspend fun resetPassword(@Body requestBody: RequestBody):Response<CommonResponseDC>

    @GET(CONTENT)
    suspend fun getContent(
        @Query("type") type:String
    ):Response<Content>

    @POST(Social_Login)
    suspend fun socialLogin(@Body requestBody: RequestBody):Response<LoginResultpojo>

    @GET(DEL_ACCOUNT)
    suspend fun delaccount():Response<CommonResponseDC>

    ////////////////////////////////////////TRAINERS///////////////////////////////

     @GET(ADD_TRAINER)
     suspend fun addTrainer(
         @Query("trainer_id") trainer_id:Int
     ):Response<CommonResponseDC>

     @GET(MY_TRAINER_LIST)
     suspend fun myTrainerList():Response<MyTrainers>

     @GET(ALL_TRAINER_LIST)
     suspend fun allTrainerList():Response<Alltrainers>

     @GET(VIEW_TRAINER_PROFILE)
     suspend fun trainerProfile(
         @Query("trainer_id") trainer_id:Int
     ):Response<TrainerProfile>

    ///////////////////////////////////////FRIENDS/////////////////////////////////////

    @GET(ADD_FREIEND)
    suspend fun addFriend(
        @Query("friend_id") friend_id:Int
    ):Response<CommonResponseDC>

    @GET(ALL_USERS_Friends)
    suspend fun allUsers():Response<AllUsers>

    @GET(VIEW_FRIEND_LIST)
    suspend fun friendList():Response<FreindList>

    @GET(FRIEND_PROFILE)
    suspend fun friendProfile(
        @Query("friend_id") friend_id:Int
    ):Response<TrianerUser>

    @GET(PENDING_FRIENDS)
    suspend fun pendingFriendList():Response<PendingFriends>

    @GET(UPDATE_REQUEST)
    suspend fun updateRequest(
        @Query("request_id") request_id:Int,
        @Query("type") type:String
    ):Response<CommonResponseDC>

    ///////////////////////////////Trainers->Users//////////////////////////////////////

    @GET(My_Users)
    suspend fun getViewUsers():Response<ViewMyUsers>

    @GET(All_Users)
    suspend fun getViewAllUsers():Response<AllUsersForTrainers>

    @GET(Add_User)
    suspend fun addUserTrainers(
        @Query("user_id") user_id:Int
    ):Response<CommonResponseDC>

    @GET(VIEW_SPECIFIC_PROFILE)
    suspend fun getTrainerUserData(
        @Query("user_id") trainer_id:Int
    ): Response<TrianerUser>

    ///////////////////////////////////Events//////////////////////////////////////////

    @Multipart
    @POST(CREATE_EVEVENT)
    suspend fun createEvent(
        @Part part1: MultipartBody.Part?,
        @PartMap hashMap: HashMap<String, RequestBody>

    ):Response<CreateEventResponse>


    @Multipart
    @POST(UPDATE_EVENT)
    suspend fun  updateEvent(
        @Part part1: MultipartBody.Part?,
        @PartMap hashMap: HashMap<String, RequestBody>
    ):Response<CommonResponseDC>


    @GET(View_EVENTS)
    suspend fun viewEventsCurrent(
        @Query("event") event:String
    ):Response<EventList>

    @GET(View_EVENTS)
    suspend fun viewEventsHistory(
        @Query("event") event:String
    ):Response<EventList>

    @GET(VIEW_EVENT)
    suspend fun viewEvent(
        @Query("event_id") event_id:Int
    ):Response<EventItem>

//////////////////////////////CHALLENGE///////////////////////////////////////////////////

    @GET(View_Challenge)
    suspend fun challangeSentDetails(
        @Query("type") type:String
    ):Response<ChallengeSent>

    @GET(View_Challenge)
    suspend fun ChallangeRecivedDetails(
        @Query("type") type:String
    ):Response<challengeRecived>


    @GET(View_Challenge)
    suspend fun ChallengePending(
        @Query("type") type:String
    ):Response<ChallengePending>


    @GET(View_Challenge)
    suspend fun ChallengeResult(
        @Query("type") type:String
    ):Response<Compelete>

    @Multipart
    @POST(Update_Challenge)
    suspend fun  acceptChallenge(
        @Part part1: MultipartBody.Part?,
        @PartMap hashMap: HashMap<String, RequestBody>
    ):Response<CommonResponseDC>

    @POST(Update_Challenge)
    suspend fun rejectChallenge(@Body requestBody: RequestBody):Response<CommonResponseDC>

    @GET(Challenge_Detail)
    suspend fun challengeDetails(
        @Query("challenge_id") challenge_id:Int
    ):Response<ChallengeDeails>

//////////////////////////////CHALLENGES FEED///////////////////////////////////////////////////

    @GET(VIEW_FEED)
    suspend fun viewFeed():Response<Feed>
//
    @GET(UPDATE_FEED)
    suspend fun vote(
    @Query("challenge_id") challenge_id:Int,
    @Query("vote") type:String
    ):Response<CommonResponseDC>


//////////////////////////////CALENDAR///////////////////////////////////////////////////

    @POST(Create_Reminder)
    suspend fun createReminder(@Body requestBody: RequestBody):Response<ReminderResponse>

    @POST(Update_Reminder)
    suspend fun updateReminder(@Body requestBody: RequestBody):Response<CommonResponseDC>

    @GET(VIEW_Reminder)
    suspend fun viewReminderList():Response<ReminderList>

    @GET(Delete_Reminder)
    suspend fun deleteReminder(
        @Query("reminder_id") reminder_id:Int
    ):Response<CommonResponseDC>


////////////////////////////////TRAINING////////////////////////////////////////////////

    @GET(Exercies_List)
    suspend fun exerciesList():Response<ExerCiesList>


    @Multipart
    @POST(Create_Challenge)
    suspend fun createChallage(
        @Part part1: MultipartBody.Part?,
        @PartMap hashMap: HashMap<String, RequestBody>
    ):Response<CommonResponseDC>

/////////////////////////////////LeaderBoard//////////////////////////////////////

    @GET(LEADER_BOARD)
    suspend fun leaderBorad():Response<LeaderBorad>

 //////////////////////////////Notification///////////////////////////////////////

    @GET(NOTIFICATION)
    suspend fun notification():Response<Notification>

    @GET(NOTIFICATIONSTATUS)
    suspend fun notyStatus(
        @Query("notification") notification:String
    ):Response<CommonResponseDC>


    /////////////////////////////Reward//////////////////////////////////////////
    @GET(REWARD)
    suspend fun getRewardDetails(
        @Query("user_id") user_id: Int
    ):Response<Reward>


  ///////////////////////////////////Live Streaming///////////////////////////////
  @GET(LIVE_STREMAING)
  suspend fun startLiveStream(@Query("event_id") event_id: Int):Response<CommonResponseDC>




}