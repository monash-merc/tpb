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

package edu.monash.merc.struts2.action;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.domain.TPBVersion;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RssFeedAction action class which handles the rss feed action request
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 4/12/12 2:10 PM
 */
@Scope("prototype")
@Controller("rss.rssFeedAction")
public class RssFeedAction extends BaseAction {

    private SyndFeed tpbFeed;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String rssFeed() {
        try {
            tpbFeed = createRSSFeed();
        } catch (Exception ex) {
            logger.error(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    private SyndFeed createRSSFeed() {
        //get all chromosome types
        List<String> chroms = ChromType.allChmTypes();

        String serverName = this.getServerQName();
        String appContext = this.getAppContextPath();
        SyndFeed feed = new SyndFeedImpl();

        String title = getText("tl.rss.feed.title");
        String link = serverName + appContext + "/rss/rss";
        String description = getText("tl.rss.feed.desc");

        feed.setTitle(title);
        feed.setLink(link);
        feed.setDescription(description);
        feed.setAuthor(serverName);

        List<SyndEntry> feedEntries = new ArrayList<SyndEntry>();

        for (String chm : chroms) {

            ChromType chromType = ChromType.fromType(chm);
            //nextprot
            TPBVersion nxTlVersion = this.dmSystemService.getCurrentTPBVersionByChromTypeTrackToken(chromType, 1);
            SyndEntry feedEntryNx = createEntry(nxTlVersion, serverName, appContext);
            if (feedEntryNx != null) {
                feedEntries.add(feedEntryNx);
            }
            //gpm
            TPBVersion gpmTlVersion = this.dmSystemService.getCurrentTPBVersionByChromTypeTrackToken(chromType, 10);
            SyndEntry feedEntryGpm = createEntry(gpmTlVersion, serverName, appContext);
            if (feedEntryGpm != null) {
                feedEntries.add(feedEntryGpm);
            }
            //hpa
            TPBVersion hpaTlVersion = this.dmSystemService.getCurrentTPBVersionByChromTypeTrackToken(chromType, 100);
            SyndEntry feedEntryHpa = createEntry(hpaTlVersion, serverName, appContext);
            if (feedEntryHpa != null) {
                feedEntries.add(feedEntryHpa);
            }

            //barcode
            TPBVersion bcTlVersion = this.dmSystemService.getCurrentTPBVersionByChromTypeTrackToken(chromType, 1000);
            SyndEntry feedEntryBc = createEntry(bcTlVersion, serverName, appContext);
            if (feedEntryBc != null) {
                feedEntries.add(feedEntryBc);
            }
            //combinated
            TPBVersion combinatedTLVersion = this.dmSystemService.getCurrentTPBVersionByChromTypeTrackToken(chromType, 1111);
            SyndEntry feedEntryComb = createEntry(combinatedTLVersion, serverName, appContext);
            if (feedEntryComb != null) {
                feedEntries.add(feedEntryComb);
            }
        }

        feed.setEntries(feedEntries);
        return feed;
    }

    private SyndEntry createEntry(TPBVersion tpbVersion, String serverName, String appRoot) {
        if (tpbVersion != null) {
            SyndEntry syndEntry = new SyndEntryImpl();
            String chrom = tpbVersion.getChromosome();
            long versionId = tpbVersion.getId();
            int versionNo = tpbVersion.getVersionNo();
            int token = tpbVersion.getTrackToken();

            Date createdDate = tpbVersion.getCreatedTime();
            syndEntry.setTitle(getText("tl.rss.entry.title", new String[]{chrom, String.valueOf(versionNo)}));
            syndEntry.setPublishedDate(createdDate);
            syndEntry.setAuthor(serverName);
            String link = generateTlUrl(serverName + appRoot, chrom, token, versionId);
            syndEntry.setLink(link);

            SyndContent entryDesc = new SyndContentImpl();
            entryDesc.setType("text/html");
            String dbsource = "";
            if (token == 1) {
                dbsource = DbAcType.NextProt.type();
            }
            if (token == 10) {
                dbsource = DbAcType.GPM.type();
            }
            if (token == 100) {
                dbsource = DbAcType.HPA.type();
            }

            if (token == 1000) {
                dbsource = DbAcType.BarCode.type();
            }

            if (token == 1111) {
                dbsource = DbAcType.NextProt.type() + ", " + DbAcType.GPM.type() + ", " + DbAcType.HPA.type() + " and " + DbAcType.BarCode.type();
            }

            entryDesc.setValue(getText("tl.rss.entry.desc", new String[]{chrom, dbsource}));
            syndEntry.setDescription(entryDesc);
            return syndEntry;
        }
        return null;
    }

    private String generateTlUrl(String baseUrl, String chromosome, int trackToken, long tpbversionId) {
        StringBuffer url = new StringBuffer();
        url.append(baseUrl).append("/tl/trafficlight.jspx?");
        url.append("chm=").append(chromosome);
        url.append("&tt=").append(trackToken);
        url.append("&vid=").append(tpbversionId);
        return url.toString();
    }

    public SyndFeed getTpbFeed() {
        return tpbFeed;
    }

    public void setTpbFeed(SyndFeed tpbFeed) {
        this.tpbFeed = tpbFeed;
    }
}
