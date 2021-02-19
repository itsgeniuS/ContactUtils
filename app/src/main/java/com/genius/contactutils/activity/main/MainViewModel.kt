package com.genius.contactutils.activity.main

import com.genius.contactutils.base.BaseViewModel

/**
 * Created by geniuS on 19/02/2021.
 */
class MainViewModel :
    BaseViewModel<MainNavigator>() {

    fun checkPermission() {
        getNavigator()?.checkPermissions()
    }

}
