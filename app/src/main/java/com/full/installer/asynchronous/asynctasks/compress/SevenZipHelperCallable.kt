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

package com.full.installer.asynchronous.asynctasks.compress

import android.util.Log
import com.full.installer.adapters.data.CompressedObjectParcelable
import com.full.installer.fileoperations.filesystem.compressed.ArchivePasswordCache
import com.full.installer.filesystem.compressed.CompressedHelper
import com.full.installer.filesystem.compressed.sevenz.SevenZFile
import org.apache.commons.compress.PasswordRequiredException
import org.apache.commons.compress.archivers.ArchiveException
import java.io.File
import java.io.IOException
import java.lang.UnsupportedOperationException

class SevenZipHelperCallable(
    private val filePath: String,
    private val relativePath: String,
    goBack: Boolean
) :
    CompressedHelperCallable(goBack) {

    @Throws(ArchiveException::class)
    @Suppress("Detekt.RethrowCaughtException")
    override fun addElements(elements: ArrayList<CompressedObjectParcelable>) {
        try {
            val sevenzFile = if (ArchivePasswordCache.getInstance().containsKey(filePath)) {
                SevenZFile(
                    File(filePath),
                    ArchivePasswordCache.getInstance()[filePath]!!.toCharArray()
                )
            } else {
                SevenZFile(File(filePath))
            }
            for (entry in sevenzFile.entries) {
                val name = entry.name
                val isInBaseDir = (
                    relativePath == "" &&
                        !name.contains(CompressedHelper.SEPARATOR)
                    )
                val isInRelativeDir = (
                    name.contains(CompressedHelper.SEPARATOR) &&
                        name.substring(0, name.lastIndexOf(CompressedHelper.SEPARATOR))
                        == relativePath
                    )
                if (isInBaseDir || isInRelativeDir) {
                    elements.add(
                        CompressedObjectParcelable(
                            entry.name,
                            try {
                                entry.lastModifiedDate.time
                            } catch (e: UnsupportedOperationException) {
                                Log.w(
                                    javaClass.simpleName,
                                    "Unable to get modified date for 7zip file"
                                )
                                0L
                            },
                            entry.size,
                            entry.isDirectory
                        )
                    )
                }
            }
        } catch (e: PasswordRequiredException) {
            // this is so that the caller can use onError to ask the user for the password
            throw e
        } catch (e: IOException) {
            throw ArchiveException(String.format("7zip archive %s is corrupt", filePath))
        }
    }
}
