package com.epam.healthmonitor.android

import android.support.design.widget.Snackbar
import android.view.View

fun showSnackbar(view: View, message: String, length: Int = Snackbar.LENGTH_LONG) =
        Snackbar
            .make(view, message, length)
            .show()