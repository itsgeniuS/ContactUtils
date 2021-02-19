/*
 * Copyright © Ricki Hirner (bitfire web engineering).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 */
package com.genius.contactutils.models

import android.content.ContentValues

/**
 * Created by geniuS on 19/02/2021.
 */
class DbData(var name: String, var number: String) {

    companion object {
        const val _TABLE = "numbers"
        const val NUMBER = "number"
        const val NAME = "name"

        fun fromValues(values: ContentValues): DbData {
            val number = DbData("", "")
            number.number = values.getAsString(NUMBER)
            number.name = values.getAsString(NAME)
            return number
        }

        fun wildcardsDbToView(number: String): String {
            return number
                .replace('%', '*')
                .replace('_', '#')
        }

        fun wildcardsViewToDb(number: String): String {
            return number
                .replace("[^+#*%_0-9]".toRegex(), "")
                .replace('*', '%')
                .replace('#', '_')
        }
    }
}