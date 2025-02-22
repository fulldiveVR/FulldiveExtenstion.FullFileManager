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

package com.full.installer.filesystem.ssh

import net.schmizz.sshj.connection.channel.direct.Session
import java.io.IOException

abstract class SshClientSessionTemplate<T>
/**
 * Constructor.
 *
 * @param url SSH connection URL, in the form of `
 * ssh://<username>:<password>@<host>:<port>` or `
 * ssh://<username>@<host>:<port>`
 */
(@JvmField val url: String) {

    /**
     * Implement logic here.
     *
     * @param sshClientSession [Session] instance, with connection opened and authenticated
     * @param <T> Requested return type
     * @return Result of the execution of the type requested
     */
    @Throws(IOException::class)
    abstract fun execute(sshClientSession: Session): T
}
