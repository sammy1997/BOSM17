package com.bitspilanidvm.bosm2017.ClickListeners

import java.util.*

interface StarClickListener{
    fun onStarClicked(key: String, isChecked: Boolean, title: String, text: String, imgRes: Int, date: Date)
}