package com.genius.contactutils.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.genius.contactutils.utils.DataStorage
import com.genius.contactutils.utils.DbHelper
import com.genius.contactutils.utils.permissions.PermissionsHelper

/**
 * Created by geniuS on 19/02/2021.
 */
abstract class SuperCompatActivity<T : ViewDataBinding, V : BaseViewModel<*>> :
    AppCompatActivity() {

    private lateinit var viewDataBinding: T
    private lateinit var viewModel: V

    lateinit var dataStorage: DataStorage
    lateinit var permissionsHelper: PermissionsHelper
    lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performDataBinding()
        initialize()
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = getViewModel()
        viewDataBinding.setVariable(getBindingVariable(), viewModel)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()
    }

    abstract fun getBindingVariable(): Int

    open fun getViewDataBinding(): T {
        return viewDataBinding
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    private fun initialize() {
        dataStorage = DataStorage(this)
        permissionsHelper = PermissionsHelper(this)
        dbHelper = DbHelper(this)
    }

}