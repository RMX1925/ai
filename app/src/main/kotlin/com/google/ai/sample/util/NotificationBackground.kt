package com.google.ai.sample.util

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import com.google.ai.sample.BuildConfig
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class NotificationBackground : NotificationListenerService() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.apiKey,
        generationConfig = generationConfig { temperature = 0.7f },
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH),
        ),

    )

    private val TAG : String = "NotificationService"

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("Connected", "Listener Connected")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val packageName = "com.whatsapp"
//        val packageName = "com.google.android.apps.messaging"
        if(sbn == null || sbn.packageName != packageName){
            return
        }

        val notification = sbn.notification
        val ticker = notification?.tickerText
        val bundle : Bundle? = notification?.extras
        val title : String = when (val titleObj = bundle?.get("android.title")) {
            is String -> titleObj
            is SpannableString -> titleObj.toString()
            else -> null.toString()
        }
//        editing only here
//        var photo = bundle?.getBundle("android.messagingStyleUser")
//
//
//        Log.d(TAG, "onNotificationPosted: ${photo}")
//        var icon = notification.getLargeIcon()
//        Log.d(TAG, "onNotificationPosted: ${icon?.uri?.path}")

        val body : String = bundle?.getCharSequence("android.text").toString()
        val appInfo = applicationContext.packageManager.getApplicationInfo(
            sbn.packageName,
            PackageManager.GET_META_DATA
        )

        val appName = applicationContext.packageManager.getApplicationLabel(appInfo)

        if(body.startsWith("/ask")){
            getAIReply(sbn, body.removePrefix("/ask"), title)
        }
    }

    private fun reply(sbn : StatusBarNotification, message: String) {
        val action: Action = NotificationUtils.getQuickReplyAction(sbn.notification, packageName)
            ?: return
        try {
            action.sendReply(applicationContext, message)
        } catch (e: PendingIntent.CanceledException) {
            Log.d("Reply: ", "Not replied")
        }

        this.cancelNotification(sbn.key)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getAIReply(sbn: StatusBarNotification, message: String, title: String) {
        GlobalScope.launch {
            try {
                val chat = generativeModel.startChat()
                val response = chat.sendMessage(message)
                val fromGroup = title.contains(": ")
                val mention = title.split(":").last().trim()

                response.text?.let {modelResponse ->
                    if(fromGroup){
                        reply(sbn, "@$mention\n$modelResponse")
                    }
                    else {
                        reply(sbn, modelResponse)
                    }

                }

            } catch (_: Exception) {
                reply(sbn, "ERROR GETTING YOUR REQUEST")
            }
        }
    }

//  main code here
//    @OptIn(DelicateCoroutinesApi::class)
//    private fun getAIReply(sbn: StatusBarNotification, message: String) {
////        val generativeModel = GenerativeModel(
////            modelName = "gemini-pro",
////            apiKey = BuildConfig.apiKey,
////            generationConfig = generationConfig { temperature = 0.7f }
////        )
//
//            GlobalScope.launch {
//            try {
//                val chat = generativeModel.startChat()
//                val response = chat.sendMessage(message)
//                response.text?.let {modelResponse ->
//                    reply(sbn, modelResponse)
//                }
//            } catch (_: Exception) {
//                reply(sbn, "ERROR GETTING YOUR REQUEST")
//            }
//        }
//    }



    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}