package com.genius.contactutils.activity.main.adapter

import androidx.databinding.ObservableField
import com.genius.contactutils.models.Contact

/**
 * Created by geniuS on 19/02/2021.
 */
class ContactsItemVH(
    private val items: Contact,
    private val callback: ContactsCallback
) {

    val contactName = ObservableField<String>()
    val contactNumber = ObservableField<String>()

    init {
        contactName.set(items.name)
        contactNumber.set(items.number)
    }

    fun onItemClick() {
        callback.onContactsClick(items)
    }

    interface ContactsCallback {
        fun onContactsClick(items: Contact)
    }
}