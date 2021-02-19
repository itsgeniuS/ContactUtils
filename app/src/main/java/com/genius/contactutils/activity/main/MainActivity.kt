package com.genius.contactutils.activity.main

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genius.contactutils.BR
import com.genius.contactutils.R
import com.genius.contactutils.activity.main.adapter.ContactsListAdapter
import com.genius.contactutils.base.SuperCompatActivity
import com.genius.contactutils.databinding.ActivityMainBinding
import com.genius.contactutils.dialog.InfoDialog
import com.genius.contactutils.models.Contact
import com.genius.contactutils.models.DbData
import com.genius.contactutils.utils.*
import com.genius.contactutils.utils.permissions.Permissions
import com.genius.contactutils.utils.permissions.PermissionsHelper
import com.google.android.material.snackbar.Snackbar

/**
 * Created by geniuS on 19/02/2021.
 */
class MainActivity : SuperCompatActivity<ActivityMainBinding, MainViewModel>(), MainNavigator,
    PermissionsHelper.PermissionCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        doBinding()
        doInit()
    }

    private fun doBinding() {
        binding = getViewDataBinding()
        viewModel.setNavigator(this)
    }

    private fun doInit() {
        binding.noPermissionsLayout.visibility = View.VISIBLE

        /*This dialog is shown for the first time when user installs the app*/
        if (!dataStorage.getBoolean(Keys.IS_INFO_SHOWN)) {
            InfoDialog(object : InfoDialog.InfoDialogCallback {
                override fun onContinue() {
                    dataStorage.saveBoolean(Keys.IS_INFO_SHOWN, true)
                    checkPermissions()
                }
            }).show(supportFragmentManager, "InfoDialog")
        } else {
            checkPermissions()
        }

    }

    override fun checkPermissions() {
        if (isRunTimePermissionsRequired()) {
            permissionsHelper.initPermissions()
            if (permissionsHelper.isPermissionsAvailable) {
                loadContacts()
            } else {
                askPermission()
            }
        } else {
            loadContacts()
        }
    }

    /*
    * This method load all contacts
    * */
    private fun loadContacts() {
        binding.noPermissionsLayout.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE

        val contactsList = ContactsHelper.getAllContacts(this)
        if (!contactsList.isNullOrEmpty()) {
            val dbList = dbHelper.getAllNumbers()

            /*
            * this loop checks the contacts from phone and sqliteDb and updates selection
            * */
            for (items in contactsList) {
                Log.e("debug", "CONTACT DATA Name ${items.name}  Number ${items.number}")
                for (dbItems in dbList) {
                    Log.e("debug", "DB DATA Name ${dbItems.name}  Number ${dbItems.number}")
                    val spaceRemovedNum: String = items.number.replace(" ", "", true)
                    if (spaceRemovedNum == dbItems.number) {
                        Log.e(
                            "debug",
                            "CONTACT DATA and DB Data matched for spaceRemovedNum $spaceRemovedNum"
                        )
                        items.isSelected = true
                    }
                }
            }

            binding.progress.visibility = View.GONE
            binding.titleTv.visibility = View.VISIBLE
            binding.contactsRv.visibility = View.VISIBLE
            binding.mainParent.showSnackbar(
                "a total of ${contactsList.size} contacts loaded",
                Snackbar.LENGTH_LONG
            )

            /*
            * setting contacts to the adapter
            * */
            val adapter = ContactsListAdapter(
                contactsList,
                object : ContactsListAdapter.ContactsAdapterCallback {
                    override fun onContactClick(items: Contact) {
                        updateDb(items)
                    }
                })
            binding.contactsRv.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.contactsRv.adapter = adapter

            binding.titleTv.visibility = View.VISIBLE
            binding.contactsRv.visibility = View.VISIBLE
        } else {
            /*
            * No contacts available
            * */
            binding.noContactsLayout.visibility = View.GONE
        }
    }

    private fun askPermission() {
        permissionsHelper.requestPermission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (permissionsHelper.isPermissionsAvailable) {
            loadContacts()
        } else {
            permissionsHelper.requestPermission(this)
        }
    }

    private fun updateDb(items: Contact) {
        val values = ContentValues()
        values.put(DbData.NAME, items.name)
        values.put(DbData.NUMBER, DbData.wildcardsViewToDb(items.number))

        val isExists = dbHelper.checkIfRecordExist(DbData._TABLE, DbData.NUMBER, items.number, true)
        if (isExists) {
            dbHelper.deleteRecord(items.number)
            Log.e("debug", "data exists!")
        } else {
            dbHelper.insertRecord(values)
            Log.e("debug", "data not exists!")
        }
    }

    override fun onGranted() {
        loadContacts()
    }

    override fun onDeniedCompletely() {
        binding.noPermissionsLayout.visibility = View.VISIBLE
        permissionsHelper.askAndOpenSettings(
            Permissions.READ_CONTACTS_INFO,
            Permissions.CANNOT_READ_CONTACTS
        )
    }

    override fun onSinglePermissionGranted(grantedPermission: Array<out String>?) {

    }

    override fun onDenied() {
        binding.noPermissionsLayout.visibility = View.VISIBLE
        showToast(getString(R.string.info_contact_permission_required))
    }
}