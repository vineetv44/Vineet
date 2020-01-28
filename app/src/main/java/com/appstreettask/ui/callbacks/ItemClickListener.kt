package com.appstreettask.ui.callbacks

import android.view.View
import com.appstreettask.ui.model.DataDetailsItem

@FunctionalInterface
interface ItemClickListener {
    fun onItemClick(item: DataDetailsItem, view: View)
}