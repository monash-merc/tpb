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
import org.apache.log4j.Logger;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * DMFileGZipper class which provides the gz archive files unzipping functionalities.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 27/02/12 4:30 PM
 */
public class DMFileGZipper {

    /**
     * Logger object
     */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Default buffer size
     */
    private static int BUFFER_SIZE = 1240 * 15;

    /**
     * unzip the gz archive file
     *
     * @param zipFileName  a gz archive file
     * @param destFileName a unzipped the file
     */
    public void unzipFile(String zipFileName, String destFileName) {

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(zipFileName));
            unzipFile(fileInputStream, destFileName);
        } catch (IOException e) {
            throw new DMFileException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception fex) {
                    logger.warn(fex.getMessage());
                }
            }
        }
    }

    /**
     * unzip the archive file based on the gzip file input stream.
     *
     * @param gzipFileInputStream a gzip file input stream
     * @param destFileName        a unzip  file name
     */
    public void unzipFile(InputStream gzipFileInputStream, String destFileName) {
        GZIPInputStream gzipInputStream = null;
        OutputStream out = null;
        try {
            gzipInputStream = new GZIPInputStream(gzipFileInputStream);
            out = new FileOutputStream(destFileName);
            byte[] buf = new byte[BUFFER_SIZE];
            //size can be changed according to programmer 's need.
            int len;
            while ((len = gzipInputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            throw new DMFileException(e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (gzipInputStream != null) {
                    gzipInputStream.close();
                }
                if (gzipFileInputStream != null) {
                    gzipFileInputStream.close();
                }
            } catch (Exception fex) {
                logger.warn(fex.getMessage());
            }
        }
    }
}
