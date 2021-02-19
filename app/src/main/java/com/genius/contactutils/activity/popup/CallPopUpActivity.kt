package com.genius.contactutils.activity.popup

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import com.genius.contactutils.R
import com.genius.contactutils.utils.DbHelper
import kotlinx.android.synthetic.main.activity_call_pop_up.*

/**
 * Created by geniuS on 19/02/2021.
 */
class CallPopUpActivity : Activity() {

    lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);

        setContentView(R.layout.activity_call_pop_up)

        init()
    }

    private fun init() {
        dbHelper = DbHelper(this)

        val number = intent.extras?.getString("phone")
        val name = intent.extras?.getString("name")

        if(TextUtils.isEmpty(number)) {
            finish()
        } else {
            phone_tv.text = number
            name_tv.text = name
        }

        close_iv.setOnClickListener {
            finish()
        }
    }
}