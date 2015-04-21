/*
 * Copyright (c) 2011-2013, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package edu.monash.merc.util.file;

import edu.monash.merc.exception.DMFileException;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DMFileUtils class is a file utility class.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 27/02/12 4:30 PM
 */
public class DMFileUtils {

    private static Logger logger = Logger.getLogger(DMFileUtils.class.getName());

    public static boolean checkWritePermission(String pathName) {
        if (pathName == null) {
            throw new DMFileException("directory name must not be null");
        }
        try {
            File dir = new File(pathName);
            return dir.canWrite();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public static void deleteDirectory(String dirName) {
        if (dirName == null) {
            throw new DMFileException("directory name must not be null");
        }

        try {
            FileUtils.deleteDirectory(new File(dirName));
        } catch (Exception e) {
            throw new DMFileException(e);
        }
    }

    public static void moveDirectory(String olderDirName, String newDirName) {
        if (olderDirName == null) {
            throw new DMFileException("old directory name must not be null");
        }
        if (newDirName == null) {
            throw new DMFileException("new directory name must not be null");
        }
        try {
            FileUtils.moveDirectory(new File(olderDirName), new File(newDirName));
        } catch (Exception e) {
            throw new DMFileException(e);
        }
    }

    public static void createDirectory(String dirName) {
        if (dirName == null) {
            throw new DMFileException("directory name must not be null");
        }
        try {
            FileUtils.forceMkdir(new File(dirName));
        } catch (Exception e) {
            throw new DMFileException(e);
        }
    }

    public static void creatFileFromSrc(String srcFileName, String destFileName) {
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        copyFile(srcFile, destFile, false);
    }

    public static void copyFile(String srcFileName, String destFileName, boolean preserveFileDate) {

        if (srcFileName == null) {
            throw new DMFileException("Source must not be null");
        }
        if (destFileName == null) {
            throw new DMFileException("Destination must not be null");
        }
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);

        if (!srcFile.exists()) {
            throw new DMFileException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new DMFileException("Source '" + srcFile + "' is a directory");
        }

        if (destFile.isDirectory()) {
            throw new DMFileException("Destination '" + destFile + "' is a directory");
        }
        try {
            FileUtils.copyFile(srcFile, destFile, preserveFileDate);
        } catch (Exception e) {
            throw new DMFileException(e);
        }
    }

    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) {
        try {
            FileUtils.copyFile(srcFile, destFile, preserveFileDate);
        } catch (Exception e) {
            throw new DMFileException(e);
        }
    }

    public static void deleteFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                FileUtils.forceDelete(new File(fileName));
            }
        } catch (Exception e) {
            throw new DMFileException(e);
        }
    }

    public static byte[] readFileToByteArray(String fileName) {
        try {
            return FileUtils.readFileToByteArray(new File(fileName));
        } catch (Exception e) {
            throw new DMFileException(e);
        }
    }

    public static InputStream readFileToInputStream(String fileName) {
        if (fileName == null) {
            throw new DMFileException("file name must not be null");
        }
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(fileName));
        } catch (Exception e) {
            throw new DMFileException(e);
        }
        return in;
    }

    public static List<String> discoverFileNames(String stagePath, FilenameFilter filter) {

        if (stagePath == null) {
            throw new DMFileException("directory must not be null");
        }
        File scannedDir = new File(stagePath);

        if (scannedDir.exists() && scannedDir.isFile()) {
            throw new DMFileException("Destination '" + stagePath + "' exists but is a file");
        }

        if (filter == null) {
            filter = new ScanFileFilter();
        }
        String[] fileNames = scannedDir.list(filter);
        List<String> scannedFiles = new ArrayList<String>();

        for (int i = 0; i < fileNames.length; i++) {
            File file = new File(stagePath + File.separator + fileNames[i]);
            if (file.isFile()) {
                scannedFiles.add(file.getName());
            }
        }
        return scannedFiles;
    }

    public static void moveFile(String srcFileName, String destFileName, boolean override) {
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        moveFile(srcFile, destFile, override);
    }

    public static void moveFile(File srcFile, File destFile, boolean override) {
        try {
            if (srcFile == null) {
                throw new NullPointerException("Source file must not be null");
            }

            if (destFile == null) {
                throw new NullPointerException("Destination file must not be null");
            }

            if (!srcFile.exists()) {
                throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
            }
            if (srcFile.isDirectory()) {
                throw new IOException("Source '" + srcFile + "' is a directory");
            }
            if (destFile.exists() && !override) {
                throw new FileExistsException("Destination '" + destFile + "' already exists");
            }
            if (destFile.isDirectory()) {
                throw new IOException("Destination '" + destFile + "' is a directory");
            }
            boolean rename = srcFile.renameTo(destFile);
            if (!rename) {
                copyFile(srcFile, destFile, true);
                if (!srcFile.delete()) {
                    FileUtils.deleteQuietly(destFile);
                    throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
                }
            }
        } catch (Exception e) {
            throw new DMFileException(e);
        }
    }

    public static void moveFile(File srcFile, String destFileName, boolean override) {
        File destFile = new File(destFileName);
        moveFile(srcFile, destFile, override);
    }

    public static List<String> readLines(File file) {
        List<String> lines = new ArrayList<String>();
        try {
            lines = FileUtils.readLines(file, "utf-8");
        } catch (Exception ex) {
            throw new DMFileException(ex);
        }
        return lines;
    }
}
