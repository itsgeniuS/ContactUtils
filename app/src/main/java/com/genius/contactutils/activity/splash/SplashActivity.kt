package com.genius.contactutils.activity.splash

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.genius.contactutils.BR
import com.genius.contactutils.R
import com.genius.contactutils.activity.main.MainActivity
import com.genius.contactutils.base.SuperCompatActivity
import com.genius.contactutils.databinding.ActivitySplashBinding
import com.genius.contactutils.utils.openActivity

/**
 * Created by geniuS on 19/02/2021.
 */
class SplashActivity : SuperCompatActivity<ActivitySplashBinding, SplashViewModel>(),
    SplashNavigator {

    private lateinit var viewModel: SplashViewModel
    lateinit var binding: ActivitySplashBinding

    override fun getBindingVariable(): Int {
        return BR.splashViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun getViewModel(): SplashViewModel {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doBind()
        doInit()
    }

    private fun doBind() {
        viewModel.setNavigator(this)
        binding = getViewDataBinding()
    }

    private fun doInit() {
        Handler().postDelayed({
            openActivity(this, MainActivity::class.java, true)
        }, 2000)
    }

}