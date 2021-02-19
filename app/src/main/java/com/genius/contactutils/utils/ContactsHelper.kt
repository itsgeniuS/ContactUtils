package com.genius.contactutils.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.genius.contactutils.models.Contact


/**
 * Created by geniuS on 19/02/2021.
 */

class ContactsHelper {

    companion object {
        var uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        var selection = ContactsContract.Contacts.HAS_PHONE_NUMBER

        /*
        * We list all contacts from phone using in this method
        * */
        fun getAllContacts(context: Context): ArrayList<Contact> {
            val contactsList = ArrayList<Contact>()
            val cursor: Cursor? = context.contentResolver.query(
                uri,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone._ID,
                    ContactsContract.Contacts._ID
                ),
                selection,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )

            cursor?.let {
                it.moveToFirst()
                while (!it.isAfterLast) {
                    val contactNumber =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    val contactName =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    val phoneContactID =
                        it.getInt(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                    val contactID = it.getInt(it.getColumnIndex(ContactsContract.Contacts._ID));

                    contactsList.add(Contact(contactNumber, contactName, phoneContactID, contactID, false))

                    Log.e("Contacts", contactName + "\t" + contactNumber)

                    it.moveToNext();
                }
                it.close();
            }
            return contactsList
        }
    }
}