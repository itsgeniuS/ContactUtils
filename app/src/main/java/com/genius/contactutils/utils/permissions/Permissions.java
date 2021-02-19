package com.genius.contactutils.utils.permissions;

import android.Manifest;

/**
 * Created by geniuS on 7/10/2019.
 */
public class Permissions {
    private static final String appName = "Contact Utils";

    public static final String READ_CONTACTS_INFO = appName + " needs to use phone and calls permission to access contacts, allow permission in settings.";

    public static final String CANNOT_READ_CONTACTS = "Sorry, cannot open contacts without permission!";

    public static String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static String CALL_PHONE = Manifest.permission.CALL_PHONE;
}
