package com.genius.contactutils.models

/**
 * Created by geniuS on 19/02/2021.
 */
data class Contact(
    var number: String,
    var name: String,
    var id: Int,
    var contactId: Int,
    var isSelected: Boolean
)