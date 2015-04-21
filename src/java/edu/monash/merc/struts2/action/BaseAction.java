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

import com.opensymphony.xwork2.ActionSupport;
import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.dto.tl.TLSearchBean;
import edu.monash.merc.service.DMSystemService;
import edu.monash.merc.system.config.SystemPropConts;
import edu.monash.merc.system.config.SystemPropSettings;
import edu.monash.merc.util.DMUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseAction Action class is a base Action class, the other action classed must extend this action
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 7/02/12 12:43 PM
 */
public class BaseAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

    protected Map<String, Object> session;

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected Map<String, String> chromTypes = new LinkedHashMap<String, String>();

    protected HashMap<String, String> tpbVersions = new LinkedHashMap<String, String>();

    protected TLSearchBean tlSearchBean;

    protected static final String DS_SELECTED = "1";

    protected static final String NONE_DS_SELECTED = "0";

    protected static final String CHROMOSOME = "chromosome ";

    public static final String JSON = "json";

    public static final String REDIRECT = "redirect";

    public static final String CHAIN = "chain";

    public static final String FILE_NOT_FOUND = "file_not_found";

    private String successActMsg;

    @Autowired
    protected SystemPropSettings systemPropSettings;

    @Autowired
    protected DMSystemService dmSystemService;

    public void setSystemPropSettings(SystemPropSettings systemPropSettings) {
        this.systemPropSettings = systemPropSettings;
    }

    public void setDmSystemService(DMSystemService dmSystemService) {
        this.dmSystemService = dmSystemService;
    }

    @PostConstruct
    public void init() {

        //init the chromosome types
        initChromosomeTypes();

        //get the default datasource combination from the configuration file
        String defaultCombinatedToken = this.systemPropSettings.getPropValue(SystemPropConts.TRAFFIC_LIGHT_COMBINATION_DEFAILT);
        initDefaultCombinatedChromTypes(defaultCombinatedToken);
    }

    private void initChromosomeTypes() {

        List<String> chroms = ChromType.allChmTypes();
        for (String chmType : chroms) {
            chromTypes.put(chmType, (CHROMOSOME + chmType));
        }
        //init traffic light search bean
        tlSearchBean = new TLSearchBean();
        tlSearchBean.setSelectedChromType(ChromType.CHM7.chm());
        //gene value type
        tlSearchBean.setSelectedGeneValueType("symbol");
    }

    private void initDefaultCombinatedChromTypes(String defaultToken) {
        if (StringUtils.isNotBlank(defaultToken)) {
            if (matchTokenPattern(defaultToken)) {
                String[] tokens = DMUtil.split(defaultToken);
                for (int i = 0; i < tokens.length; i++) {
                    String token = tokens[i];
                    if (StringUtils.equals(token, DS_SELECTED)) {
                        if (i == 0) {
                            tlSearchBean.setNxDbSelected(true);
                        }
                        if (i == 1) {
                            tlSearchBean.setGpmDbSelected(true);
                        }
                        if (i == 2) {
                            tlSearchBean.setHpaDbSelected(true);
                        }
                        if (i == 3) {
                            tlSearchBean.setBcDbSelected(true);
                        }
                    }
                }
            } else { //not matching the token pattern
                tlSearchBean.setNxDbSelected(true);
            }
        } else { //the default is not set
            tlSearchBean.setNxDbSelected(true);
        }
    }


    private boolean matchTokenPattern(String defaultToken) {
        char[] validchars = new char[]{'1', '0'};
        return StringUtils.containsOnly(defaultToken, validchars);
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getServletResponse() {
        return response;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getServletRequest() {
        return request;
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public void storeInSession(String key, Object obj) {
        this.session.put(key, obj);
    }

    public void removeFromSession(String sessionKey) {
        this.session.remove(sessionKey);
    }

    public Object findFromSession(String key) {
        return this.session.get(key);
    }

    public String getAppRealPath(String path) {
        return ServletActionContext.getServletContext().getRealPath(path);
    }

    public String getAppRoot() {
        return getAppRealPath("/");
    }

    public String getAppContextPath() {
        return ServletActionContext.getRequest().getContextPath();
    }

    public int getServerPort() {
        return ServletActionContext.getRequest().getServerPort();
    }

    public String getAppHostName() {
        return ServletActionContext.getRequest().getServerName();
    }

    public String getServerQName() {
        String scheme = request.getScheme();
        String hostName = request.getServerName();
        int port = request.getServerPort();

        StringBuffer buf = new StringBuffer();
        if (scheme.equals(ActionConts.HTTP_SCHEME)) {
            buf.append(ActionConts.HTTP_SCHEME).append(ActionConts.HTTP_SCHEME_DELIM);
        } else {
            buf.append(ActionConts.HTTPS_SCHEME).append(ActionConts.HTTP_SCHEME_DELIM);
        }
        buf.append(hostName);
        if (port == 80 || port == 443) {
            return new String(buf);
        }
        buf.append(ActionConts.COLON_DEIM).append(port);
        return new String(buf);
    }

    protected boolean isSecurityCodeError(String securityCode) {
        String code = (String) findFromSession(ActionConts.SESS_SECURITY_CODE);
        if (StringUtils.isBlank(code)) {
            return true;
        } else if (StringUtils.equalsIgnoreCase(securityCode, code)) {
            return false;
        } else {
            return true;
        }
    }

    public TLSearchBean getTlSearchBean() {
        return tlSearchBean;
    }

    public void setTlSearchBean(TLSearchBean tlSearchBean) {
        this.tlSearchBean = tlSearchBean;
    }

    public Map<String, String> getChromTypes() {
        return chromTypes;
    }

    public void setChromTypes(Map<String, String> chromTypes) {
        this.chromTypes = chromTypes;
    }

    public HashMap<String, String> getTpbVersions() {
        return tpbVersions;
    }

    public void setTpbVersions(HashMap<String, String> tpbVersions) {
        this.tpbVersions = tpbVersions;
    }

    public String getSuccessActMsg() {
        return successActMsg;
    }

    public void setSuccessActMsg(String successActMsg) {
        this.successActMsg = successActMsg;
    }
}
