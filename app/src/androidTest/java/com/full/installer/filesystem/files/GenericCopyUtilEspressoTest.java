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

package com.full.installer.filesystem.files;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.full.installer.asynchronous.management.ServiceWatcherUtil;
import com.full.installer.test.DummyFileGenerator;
import com.full.installer.utils.ProgressHandler;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

@RunWith(AndroidJUnit4.class)
public class GenericCopyUtilEspressoTest {

  private GenericCopyUtil copyUtil;

  private File file1, file2;

  @Before
  public void setUp() throws IOException {
    copyUtil =
        new GenericCopyUtil(
            InstrumentationRegistry.getInstrumentation().getTargetContext(), new ProgressHandler());
    file1 = File.createTempFile("test", "bin");
    file2 = File.createTempFile("test", "bin");
    file1.deleteOnExit();
    file2.deleteOnExit();
  }

  @Test
  public void testCopyFile1() throws IOException, NoSuchAlgorithmException {
    doTestCopyFile1(512);
    doTestCopyFile1(10 * 1024 * 1024);
  }

  @Test
  public void testCopyFile2() throws IOException, NoSuchAlgorithmException {
    doTestCopyFile2(512);
    doTestCopyFile2(10 * 1024 * 1024);
  }

  @Test
  public void testCopyFile3() throws IOException, NoSuchAlgorithmException {
    doTestCopyFile3(512);
    doTestCopyFile3(10 * 1024 * 1024);
  }

  // doCopy(ReadableByteChannel in, WritableByteChannel out)
  private void doTestCopyFile1(int size) throws IOException, NoSuchAlgorithmException {
    byte[] checksum = DummyFileGenerator.createFile(file1, size);
    copyUtil.doCopy(
        new FileInputStream(file1).getChannel(),
        Channels.newChannel(new FileOutputStream(file2)),
        ServiceWatcherUtil.UPDATE_POSITION);
    assertEquals(file1.length(), file2.length());
    assertSha1Equals(checksum, file2);
  }

  // copy(FileChannel in, FileChannel out)
  private void doTestCopyFile2(int size) throws IOException, NoSuchAlgorithmException {
    byte[] checksum = DummyFileGenerator.createFile(file1, size);
    copyUtil.copyFile(
        new FileInputStream(file1).getChannel(),
        new FileOutputStream(file2).getChannel(),
        ServiceWatcherUtil.UPDATE_POSITION);
    assertEquals(file1.length(), file2.length());
    assertSha1Equals(checksum, file2);
  }

  // copy(BufferedInputStream in, BufferedOutputStream out)
  private void doTestCopyFile3(int size) throws IOException, NoSuchAlgorithmException {
    byte[] checksum = DummyFileGenerator.createFile(file1, size);
    copyUtil.copyFile(
        new BufferedInputStream(new FileInputStream(file1)),
        new BufferedOutputStream(new FileOutputStream(file2)),
        ServiceWatcherUtil.UPDATE_POSITION);
    assertEquals(file1.length(), file2.length());
    assertSha1Equals(checksum, file2);
  }

  private void assertSha1Equals(byte[] expected, File file)
      throws NoSuchAlgorithmException, IOException {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    DigestInputStream in = new DigestInputStream(new FileInputStream(file), md);
    byte[] buffer = new byte[GenericCopyUtil.DEFAULT_BUFFER_SIZE];
    while (in.read(buffer) > -1) {}
    in.close();
    assertArrayEquals(expected, md.digest());
  }
}
