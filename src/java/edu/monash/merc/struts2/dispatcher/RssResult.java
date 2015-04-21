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

package edu.monash.merc.struts2.dispatcher;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

/**
 * A simple {@link com.opensymphony.xwork2.Result} to output a <a href="https://rome.dev.java.net/">Rome</a> {@link com.sun.syndication.feed.synd.SyndFeed} object into a newsfeed.
 * <p/>
 * Has 4 parameters that can be set on the {@link com.opensymphony.xwork2.Result}.
 * <ul>
 * <li><b>feedName</b> (required): the expression to find the {@link com.sun.syndication.feed.synd.SyndFeed} on the value stack (eg. 'feed' will result in a call to 'getFeed()' on your Action.</li>
 * <li><b>mimeType</b> (optional, defaults to 'text/xml'): the preferred mime type.</li>
 * <li><b>encoding</b> (optional, defaults to the {@link com.sun.syndication.feed.synd.SyndFeed}'s encoding or falls back on the system): the preferred encoding (eg. UFT-8)
 * <li><b>feedType</b> (optional): the feed type.
 * <p>
 * Accepted feedType values are:
 * <ul>
 * <li>atom_0.3</li>
 * <li>atom_1.0</li>
 * <li>rss_0.90</li>
 * <li>rss_0.91N (RSS 0.91 Netscape)</li>
 * <li>rss_0.91U (RSS 0.91 Userland)</li>
 * <li>rss_0.92</li>
 * <li>rss_0.93</li>
 * <li>rss_0.94</li>
 * <li>rss_1.0</li>
 * <li>rss_2.0</li>
 * </ul>
 * </p>
 * </li>
 * </ul>
 * <p/>
 * <p/>
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 4/12/12 10:48 AM
 */
public class RssResult implements Result {

    private static final String DEFAULT_MIME_TYPE = "application/xml";

    //feedName must be set by the parameter
    private String feedName;

    //see the javadoc for a list of the supported values
    private String feedType;

    //the mime type for response
    private String mimeType;

    //the encoding for response and feed
    private String encoding;

    private static Logger logger = Logger.getLogger(RssResult.class.getName());

    public void execute(ActionInvocation actionInvocation) throws Exception {
        if (StringUtils.isBlank(feedName)) {
            String msg = ("Required parameter with the name [" + feedName + "] not found. " + "Make sure you have the param tag set.");
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }

        //set the content type for response
        HttpServletResponse response = (HttpServletResponse) actionInvocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
        if (StringUtils.isBlank(mimeType)) {
            mimeType = DEFAULT_MIME_TYPE;
        }
        response.setContentType(mimeType);

        SyndFeed feed = (SyndFeed) actionInvocation.getStack().findValue(feedName);
        if (feed != null) {
            if (StringUtils.isBlank(encoding)) {
                encoding = feed.getEncoding();
            }
            //set the response encoding
            if (StringUtils.isNotBlank(encoding)) {
                response.setCharacterEncoding(encoding);
            }
            //set the feed type
            if (StringUtils.isNotBlank(feedType)) {
                feed.setFeedType(feedType);
            }
            SyndFeedOutput feedOutput = new SyndFeedOutput();
            //we will writer to write out to the outpustream
            Writer out = null;
            try {
                out = response.getWriter();
                feedOutput.output(feed, out);
            } catch (Exception ex) {
                logger.error("Could not write the feed: " + ex.getMessage());
                throw new RuntimeException(ex);
                // throw new RuntimeException("Could not write the feed: " + ex.getMessage());
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } else {
            String errormsg = "Did not find the object on the stack with name '" + feedName + "'";
            logger.error(errormsg);
            throw new RuntimeException(errormsg);
        }
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
