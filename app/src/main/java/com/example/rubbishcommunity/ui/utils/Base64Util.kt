package com.example.rubbishcommunity.ui.utils

import android.util.Base64

typealias Base64String = String

fun ByteArray.toBase64String(): Base64String = Base64.encodeToString(this, Base64.NO_WRAP)

fun Base64String.toBase64Data(): ByteArray = Base64.decode(this, Base64.NO_WRAP)