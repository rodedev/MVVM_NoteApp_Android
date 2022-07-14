package com.example.noteapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(msgId: Int, length: Int = Snackbar.LENGTH_SHORT, actionMessageId: Int) {
    showSnackbar(msgId, length, actionMessageId) {}
}

fun View.showSnackbar(
    msgId: Int,
    length: Int = Snackbar.LENGTH_SHORT,
    actionMessageId: Int,
    action: ((View) -> Unit)? = null
) {
    showSnackbar(context.getString(msgId), length, context.getString(actionMessageId), action)
}

fun View.showSnackbar(
    msg: String,
    length: Int = Snackbar.LENGTH_SHORT,
    actionMessage: CharSequence? = null,
    action: ((View) -> Unit)? = null
) {

    val snackbar = Snackbar.make(this, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action?.invoke(this)
        }
    }
    snackbar.show()
}