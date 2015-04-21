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

package edu.monash.merc.system.remote;

import edu.monash.merc.dto.hpa.HPAEntryBean;
import edu.monash.merc.exception.DMRemoteException;
import edu.monash.merc.system.parser.xml.HPAWSXmlParser;
import edu.monash.merc.system.parser.xml.WSXmlInputFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * HttpHpaFileGetter class which downloads the file from the HPA site
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 9/10/12 3:49 PM
 */
public class HttpHpaFileGetter {

    private static int BUFFER_SIZE = 10 * 1024;

    private static String HPA_FILE_NAME = "proteinatlas.xml";

    public boolean importHPAXML(String remoteFile, String localFile) {
        //use httpclient to get the remote file
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(remoteFile);

        ZipInputStream zis = null;
        FileOutputStream fos = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream in = entity.getContent();
                    zis = new ZipInputStream(in);
                    ZipEntry zipEntry = zis.getNextEntry();
                    while (zipEntry != null) {
                        String fileName = zipEntry.getName();
                        if (StringUtils.contains(fileName, HPA_FILE_NAME)) {
                            System.out.println("======= found file.");
                            File aFile = new File(localFile);
                            fos = new FileOutputStream(aFile);
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                            fos.flush();
                            break;
                        }
                    }
                }
            } else {
                throw new DMRemoteException("can't get the file from " + remoteFile);
            }
        } catch (Exception ex) {
            throw new DMRemoteException(ex);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (zis != null) {
                    zis.closeEntry();
                    zis.close();
                }
                httpClient.getConnectionManager().shutdown();
            } catch (Exception e) {
                //ignore whatever caught
            }
        }
        return true;
    }

    public boolean importHPAXMLBZ2(String remoteFile, String localFile) {
        //use httpclient to get the remote file
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(remoteFile);
        BZip2CompressorInputStream bzIn = null;
        FileOutputStream fos = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream in = entity.getContent();
                    bzIn = new BZip2CompressorInputStream(in);
                    File aFile = new File(localFile);
                    fos = new FileOutputStream(aFile);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int len = 0;
                    while ((len = bzIn.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                }
            } else {
                throw new DMRemoteException("can't get the file from " + remoteFile);
            }
        } catch (Exception ex) {
            throw new DMRemoteException(ex);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (bzIn != null) {

                    bzIn.close();
                }
                httpClient.getConnectionManager().shutdown();
            } catch (Exception e) {
                //ignore whatever caught
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String fileLocation = "http://www.proteinatlas.org/download/proteinatlas.xml.bz2";
        HttpHpaFileGetter fileGetter = new HttpHpaFileGetter();
        long startTime = System.currentTimeMillis();
        fileGetter.importHPAXMLBZ2(fileLocation, "/opt/tpb/db_download/hpatest.xml");

        HPAWSXmlParser parser = new HPAWSXmlParser();

        List<HPAEntryBean> hpaEntryBeans = parser.parseHPAXml("/opt/tpb/db_download/hpatest.xml", WSXmlInputFactory.getInputFactoryConfiguredForXmlConformance());
        long endTime = System.currentTimeMillis();

        System.out.println("===== total entry size : " + hpaEntryBeans.size());
        System.out.println("===== total processing time : " + (endTime - startTime) / 1000 + " seconds.");
    }
}
