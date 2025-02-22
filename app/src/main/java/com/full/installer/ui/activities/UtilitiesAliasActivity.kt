/*
 * Copyright (C) 2014-2022 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
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

package com.full.installer.ui.activities

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.full.installer.R
import com.full.installer.application.AppConfig
import com.full.installer.databinding.UtilitiesAliasLayoutBinding
import com.full.installer.ui.updateAUAlias
import com.full.installer.utils.PackageUtils
import com.full.installer.utils.Utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UtilitiesAliasActivity : AppCompatActivity() {
    private val log: Logger = LoggerFactory.getLogger(UtilitiesAliasActivity::class.java)

    private val _binding by lazy(LazyThreadSafetyMode.NONE) {
        UtilitiesAliasLayoutBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        _binding.downloadButton.setOnClickListener {
            Utils.openURL(
                AboutActivity.URL_AMAZE_UTILS,
                this
            )
        }
        _binding.cancelButton.setOnClickListener {
            finish()
        }
        val isAUInstalled = PackageUtils.appInstalledOrNot(
            AboutActivity.PACKAGE_AMAZE_UTILS,
            packageManager
        )
        if (isAUInstalled) {
            AppConfig.toast(this, R.string.amaze_utils_installed_alias)
            val intent = packageManager.getLaunchIntentForPackage(
                AboutActivity.PACKAGE_AMAZE_UTILS
            )
            try {
                if (intent != null) {
                    this.updateAUAlias(false)
                    startActivity(intent)
                    finish()
                }
            } catch (e: ActivityNotFoundException) {
                log.warn("Amaze utils not installed", e)
            }
        }
    }
}
