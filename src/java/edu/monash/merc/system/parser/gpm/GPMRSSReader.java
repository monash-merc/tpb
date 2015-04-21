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

package edu.monash.merc.system.parser.gpm;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import edu.monash.merc.exception.DMException;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * GPMRSSReader class which reads the rss feed from the GPM
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 2/08/12 11:42 AM
 */
public class GPMRSSReader {

    private static String PATH_SEPARATOR = "/";

    private static String FTP_PROTOCOL = "ftp://";

    private HttpURLConnection httpcon;

    @SuppressWarnings("unchecked")
    public GPMSyndEntry readRSS(String url) {
        try {
            URL feedUrl = new URL(url);
            httpcon = (HttpURLConnection) feedUrl.openConnection();
            // Reading the feed
            SyndFeedInput feedInput = new SyndFeedInput();
            SyndFeed feed = feedInput.build(new XmlReader(httpcon));
            List<SyndEntry> entries = feed.getEntries();
            String ftpLink = null;
            Date releasedTime = null;
            for (SyndEntry entry : entries) {
                String link = entry.getLink();
                Date publishedDate = entry.getPublishedDate();
                if (StringUtils.isNotBlank(link)) {
                    ftpLink = link;
                }
                if (publishedDate != null) {
                    releasedTime = publishedDate;
                }
            }
            GPMSyndEntry gpmSyndEntry = createGPMSyndEntry(ftpLink, releasedTime);
            if (gpmSyndEntry == null) {
                throw new DMException("Can't get the GPM RSS feeds");
            }
            return gpmSyndEntry;
        } catch (Exception ex) {
            throw new DMException(ex);
        } finally {
            if (httpcon != null) {
                httpcon.disconnect();
            }
        }
    }

    public void release() {
        if (httpcon != null) {
            httpcon.disconnect();
        }
    }

    private GPMSyndEntry createGPMSyndEntry(String ftpLink, Date publishedDate) {
        if (publishedDate == null) {
            return null;
        }

        if (StringUtils.isBlank(ftpLink)) {
            return null;
        }

        GPMSyndEntry gpmSyndEntry = new GPMSyndEntry();
        gpmSyndEntry.setReleasedTime(publishedDate);

        String tmpFtpDir = StringUtils.substringBeforeLast(ftpLink, PATH_SEPARATOR);
        String ftpPath = StringUtils.substringAfter(tmpFtpDir, FTP_PROTOCOL);
        String ftpServerName = StringUtils.substringBefore(ftpPath, PATH_SEPARATOR);
        String workdir = StringUtils.substringAfter(ftpPath, PATH_SEPARATOR);
        String fileName = StringUtils.substringAfterLast(ftpLink, PATH_SEPARATOR);
        gpmSyndEntry.setGmpFtpServer(ftpServerName);
        gpmSyndEntry.setTpbWorkDir(PATH_SEPARATOR + workdir);
        gpmSyndEntry.setReleasedTpbFileName(fileName);
        return gpmSyndEntry;
    }


    public static void main(String[] args) throws Exception {
        String url = "http://gpmdb.thegpm.org/tpb/current.xml";
        GPMRSSReader gpmrssReader = new GPMRSSReader();
        GPMSyndEntry gpmSyndEntry = gpmrssReader.readRSS(url);
        System.out.println("=== server name: " + gpmSyndEntry.getGmpFtpServer() + ", working dir: " + gpmSyndEntry.getTpbWorkDir() +
                ", file name: " + gpmSyndEntry.getReleasedTpbFileName() + " released time: " + gpmSyndEntry.getReleasedTime());
    }
}
