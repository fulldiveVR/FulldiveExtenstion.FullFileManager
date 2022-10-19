/*
 * Copyright (C) 2014-2020 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
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

package com.full.installer.ui.fragments.preferencefragments

import android.os.Bundle
import androidx.preference.Preference
import com.full.installer.R
import com.fulldive.startapppopups.PopupManager
import com.fulldive.startapppopups.donation.DonationManager

class PrefsFragment : BasePrefsFragment() {
    override val title = R.string.setting

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>("appearance")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                activity.pushFragment(AppearancePrefsFragment())
                true
            }

        findPreference<Preference>("ui")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                activity.pushFragment(UiPrefsFragment())
                true
            }

        findPreference<Preference>("behavior")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                activity.pushFragment(BehaviorPrefsFragment())
                true
            }

        findPreference<Preference>("security")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                activity.pushFragment(SecurityPrefsFragment())
                true
            }

        findPreference<Preference>("supportus")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                DonationManager.purchaseFromSettings(
                    activity,
                    onPurchased = {
                        PopupManager().showDonationSuccess(activity)
                    }
                )

                false
            }
    }
}
