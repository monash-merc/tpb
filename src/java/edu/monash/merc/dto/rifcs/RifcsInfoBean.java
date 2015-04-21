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

package edu.monash.merc.dto.rifcs;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * RifcsInfoBean class which is a DTO class populates the RifcsInfoBean object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 28/11/12 2:43 PM
 */
public class RifcsInfoBean implements Serializable {

    private boolean republishRequired;

    private String rifcsStoreLocation;

    private String tpbGroupName;

    private String serverName;

    private String appRootRelPath;

    private String rifcsKeyPrefix;

    private String rifcsTemplate;

    private Date publishedDate;

    public boolean isRepublishRequired() {
        return republishRequired;
    }

    public void setRepublishRequired(boolean republishRequired) {
        this.republishRequired = republishRequired;
    }

    public String getRifcsStoreLocation() {
        return rifcsStoreLocation;
    }

    public void setRifcsStoreLocation(String rifcsStoreLocation) {
        this.rifcsStoreLocation = rifcsStoreLocation;
    }

    public String getTpbGroupName() {
        return tpbGroupName;
    }

    public void setTpbGroupName(String tpbGroupName) {
        this.tpbGroupName = tpbGroupName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getAppRootRelPath() {
        return appRootRelPath;
    }

    public void setAppRootRelPath(String appRootRelPath) {
        this.appRootRelPath = appRootRelPath;
    }

    public String getRifcsKeyPrefix() {
        return rifcsKeyPrefix;
    }

    public void setRifcsKeyPrefix(String rifcsKeyPrefix) {
        this.rifcsKeyPrefix = rifcsKeyPrefix;
    }

    public String getRifcsTemplate() {
        return rifcsTemplate;
    }

    public void setRifcsTemplate(String rifcsTemplate) {
        this.rifcsTemplate = rifcsTemplate;
    }


    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public boolean valid() {

        if (StringUtils.isBlank(this.rifcsStoreLocation)) {
            return false;
        }
        if (StringUtils.isBlank(this.tpbGroupName)) {
            return false;
        }
        if (StringUtils.isBlank(this.serverName)) {
            return false;
        }
        if (StringUtils.isBlank(this.appRootRelPath)) {
            return false;
        }
        if (StringUtils.isBlank(this.rifcsKeyPrefix)) {
            return false;
        }
        if (StringUtils.isBlank(this.rifcsTemplate)) {
            return false;
        }
        return true;
    }
}
