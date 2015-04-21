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

package edu.monash.merc.struts2.intercepter;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import edu.monash.merc.struts2.action.ActionConts;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * AuthenticationInterceptor class extends AbstractInterceptor which provides the Interceptor of request for authentication
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 21/12/11 1:27 PM
 */
public class AuthenticationInterceptor extends AbstractInterceptor {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        Map<String, Object> session = ActionContext.getContext().getSession();
        String authenticated = (String) session.get(ActionConts.SESS_AUTHENTICATION_FLAG);
        // check authentication flag. if not authenticated, then force to login page.
        if (StringUtils.isBlank(authenticated) || !authenticated.equals(ActionConts.SESS_AUTHENCATED)) {
            setRedirectURL(invocation);
            return ActionSupport.LOGIN;
        }
        return invocation.invoke();
    }

    private void setRedirectURL(ActionInvocation invocation) {

        String actionName = invocation.getProxy().getActionName();
        String nsp = invocation.getProxy().getNamespace();
        String namespace = StringUtils.substringAfter(nsp, "/");
        String requestURL = null;
        if (StringUtils.isNotBlank(namespace)) {
            requestURL = namespace;
        }
        if (StringUtils.isNotBlank(actionName)) {
            requestURL = requestURL + "/" + actionName + ".jspx?";
        }

        ActionContext ac = invocation.getInvocationContext();
        Map<String, Object> parameters = ac.getParameters();
        if (parameters != null) {
            for (String paramKey : parameters.keySet()) {
                Object obj = parameters.get(paramKey);
                if (obj instanceof Object[]) {
                    Object[] objArray = (Object[]) obj;
                    if (objArray.length > 0) {
                        for (int index = 0; index < objArray.length; index++) {
                            Object valueAtIndex = objArray[index];
                            logger.debug("parameters:  " + paramKey + "=" + String.valueOf(valueAtIndex));
                            requestURL = requestURL + paramKey + "=" + String.valueOf(valueAtIndex) + "&";
                        }
                    }
                } else {
                    logger.debug("parameters:  " + paramKey + "=" + String.valueOf(obj));
                    requestURL = requestURL + paramKey + "=" + String.valueOf(obj) + "&";
                }
            }
        }
        logger.debug("final request url: " + requestURL);
        Map<String, Object> session = ActionContext.getContext().getSession();
        session.put(ActionConts.REQUEST_URL, requestURL);
    }
}
