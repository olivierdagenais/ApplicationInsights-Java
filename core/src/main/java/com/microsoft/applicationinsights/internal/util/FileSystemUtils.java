/*
 * ApplicationInsights-Java
 * Copyright (c) Microsoft Corporation
 * All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the ""Software""), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.microsoft.applicationinsights.internal.util;

import java.io.File;

/**
 * Helper methods for dealing with files and folders.
 */
public class FileSystemUtils {

    /**
     * Finds a suitable folder to use for temporary files,
     * while avoiding the risk of collision when multiple users
     * are running applications that make use of Application Insights.
     *
     * See the third paragraph at http://www.chiark.greenend.org.uk/~peterb/uxsup/project/tmp-per-user/
     * for a great explanation of the motivation behind this method.
     *
     * @return a {@link File} representing a folder in which temporary files will be stored
     * for the current user.
     *
     */
    public static File getTempDir() {
        final String tempDirectory = System.getProperty("java.io.tmpdir");
        final String currentUserName = System.getProperty("user.name");

        final File result = getTempDir(tempDirectory, currentUserName);
        if (!result.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            result.mkdirs();
        }
        return result;
    }

    static File getTempDir(final String initialValue, final String userName) {
        String tempDirectory = initialValue;

        // does it look shared?
        // TODO: this only catches the Linux case; I think a few system users on Windows might share c:\Windows\Temp
        if ("/tmp".contentEquals(tempDirectory)) {
            final File candidate = new File(tempDirectory, userName);
            tempDirectory = candidate.getAbsolutePath();
        }

        final File result = new File(tempDirectory);
        return result;
    }
}
