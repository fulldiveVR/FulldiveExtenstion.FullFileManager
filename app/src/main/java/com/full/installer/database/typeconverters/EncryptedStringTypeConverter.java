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

package com.full.installer.database.typeconverters;

import com.full.installer.application.AppConfig;
import com.full.installer.database.models.StringWrapper;
import com.full.installer.utils.PasswordUtil;

import android.content.Context;

import androidx.room.TypeConverter;

/**
 * {@link TypeConverter} for password strings encrypted by {@link PasswordUtil}.
 *
 * @see StringWrapper
 * @see PasswordUtil#encryptPassword(Context, String)
 * @see PasswordUtil#decryptPassword(Context, String)
 */
public class EncryptedStringTypeConverter {

  private static final String TAG = EncryptedStringTypeConverter.class.getSimpleName();

  @TypeConverter
  public static StringWrapper toPassword(String encryptedStringEntryInDb) {
    try {
      return new StringWrapper(
          PasswordUtil.INSTANCE.decryptPassword(AppConfig.getInstance(), encryptedStringEntryInDb));
    } catch (Exception e) {
      android.util.Log.e(TAG, "Error decrypting password", e);
      return new StringWrapper(encryptedStringEntryInDb);
    }
  }

  @TypeConverter
  public static String fromPassword(StringWrapper unencryptedPasswordString) {
    try {
      return PasswordUtil.INSTANCE.encryptPassword(
          AppConfig.getInstance(), unencryptedPasswordString.value);
    } catch (Exception e) {
      android.util.Log.e(TAG, "Error encrypting password", e);
      return unencryptedPasswordString.value;
    }
  }
}
