package com.genius.contactutils.base

import androidx.lifecycle.ViewModel
import com.genius.contactutils.utils.DataStorage
import java.lang.ref.WeakReference

/**
 * Created by geniuS on 19/02/2021.
 */
abstract class BaseViewModel<N> : ViewModel() {

    private lateinit var mNavigator: WeakReference<N>

    fun setNavigator(navigator: N) {
        mNavigator = WeakReference(navigator)
    }

    fun getNavigator(): N? {
        return mNavigator.get()
    }
}