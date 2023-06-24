package com.apw.ql.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SortDialogItem(val id:Int, val label: String, isChecked: Boolean = false) {
    var checked by mutableStateOf(isChecked)
}