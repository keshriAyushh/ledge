package com.ayush.common.utils

import android.content.Context
import android.widget.Toast

fun String?.toast(
    context: Context,
    length: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(context, this, length).show()
}