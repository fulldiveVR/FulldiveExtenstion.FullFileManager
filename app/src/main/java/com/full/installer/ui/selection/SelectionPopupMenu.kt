/*
 * Copyright (C) 2014-2021 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
 * Emmanuel Messulam<emmanuelbendavid@gmail.com>, Raymond Lai <airwave209gt at gmail.com> and Contributors.
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.full.installer.ui.selection

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.full.installer.R
import com.full.installer.adapters.RecyclerAdapter
import com.full.installer.ui.activities.MainActivity
import com.full.installer.ui.theme.AppTheme
import java.util.*

class SelectionPopupMenu(
    private val recyclerAdapter: RecyclerAdapter,
    private val actionModeView: View,
    private val currentPath: String,
    context: Context
) :
    PopupMenu(context, actionModeView), PopupMenu.OnMenuItemClickListener {

    companion object {

        private const val SIMILARITY_THRESHOLD = 500
        const val FUZZYNESS_FACTOR = 4

        fun invokeSelectionDropdown(
            recyclerAdapter: RecyclerAdapter,
            actionModeView: View,
            currentPath: String,
            mainActivity: MainActivity?
        ) {
            mainActivity?.also {
                var currentContext: Context = mainActivity.applicationContext
                if (mainActivity.appTheme.getSimpleTheme(currentContext) == AppTheme.BLACK) {
                    currentContext = ContextThemeWrapper(
                        mainActivity.applicationContext,
                        R.style.overflow_black
                    )
                }
                val popupMenu = SelectionPopupMenu(
                    recyclerAdapter,
                    actionModeView,
                    currentPath,
                    currentContext
                )
                popupMenu.inflate(R.menu.selection_criteria)
                recyclerAdapter.itemsDigested?.let {
                        itemsDigested ->
                    if (itemsDigested.size > SIMILARITY_THRESHOLD) {
                        popupMenu.menu.findItem(R.id.select_similar).isVisible = false
                    }
                }
                popupMenu.setOnMenuItemClickListener(popupMenu)
                popupMenu.show()
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.select_all -> {
                // select_all
                recyclerAdapter.toggleChecked(
                    !recyclerAdapter
                        .areAllChecked(currentPath),
                    currentPath
                )
            }
            R.id.select_inverse -> {
                recyclerAdapter.toggleInverse(currentPath)
            }
            R.id.select_by_type -> {
                recyclerAdapter.toggleSameTypes()
            }
            R.id.select_by_date -> {
                recyclerAdapter.toggleSameDates()
            }
            R.id.select_similar -> {
                recyclerAdapter.toggleSimilarNames()
            }
        }
        actionModeView.invalidate()
        actionModeView.findViewById<TextView>(R.id.item_count).text = recyclerAdapter
            .checkedItems.size.toString()
        return true
    }
}
