package com.genius.contactutils.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import com.genius.contactutils.activity.popup.CallPopUpActivity
import com.genius.contactutils.models.DbData


/**
 * Created by geniuS on 19/02/2021.
 */
open class CallDetectorReceiver : BroadcastReceiver() {

    inner class MyPhoneStateListener(private val context: Context) : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {

            /*
            * This state detects incoming call
            * */
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                Runnable {
                    Log.d("DEBUG", "RINGING : $phoneNumber")
                    val dbHelper = DbHelper(context)

                    val name = dbHelper.ifExistsGetName(DbData._TABLE, DbData.NUMBER, phoneNumber)
                    if(!TextUtils.isEmpty(name)) {
                        Log.e("debug", "data exists!")
                        Log.e("debug", "name of caller is $name")

                        val popupIntent = Intent(context, CallPopUpActivity::class.java)
                        popupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        popupIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        popupIntent.putExtra("phone", phoneNumber)
                        popupIntent.putExtra("name", name)
                        context.startActivity(popupIntent)
                    }
                }.run()
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val phoneStateListener = MyPhoneStateListener(context!!)
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephony.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)


//        try {
//            val state = intent!!.getStringExtra(TelephonyManager.EXTRA_STATE)
//            val number =
//                intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
//            when {
//                state.equals(TelephonyManager.EXTRA_STATE_RINGING, ignoreCase = true) -> {
//                    Log.e("debug", "Ringing $number")
//
//                    if (context != null) {
//                        Log.e("debug", "context not null")
//
//                        val popupIntent = Intent()
////                        popupIntent.setClassName(
////                            "com.genius.contactutils",
////                            "com.genius.contactutils.activity.main.Pop"
////                        )
////                        popupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                        //popupIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
////                        popupIntent.putExtra("phone", number)
////
////                        Handler().postDelayed(Runnable {
////                            Toast.makeText(context, "Incoming call", Toast.LENGTH_SHORT).show()
////                            context.startActivity(popupIntent)
////                        }, 2000)
//

//                    } else {
//                        Log.e("debug", "context null")
//                    }
    }
//                state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK, ignoreCase = true) -> {
//                    Log.e("debug", "Received $number")
//                }
//                state.equals(TelephonyManager.EXTRA_STATE_IDLE, ignoreCase = true) -> {
//                    Log.e("debug", "Idle $number")
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//}
}