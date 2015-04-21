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

import edu.monash.merc.exception.DMException;
import edu.monash.merc.system.config.SystemPropConts;
import edu.monash.merc.util.DMUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * ContactUsAction action class which handles the contact us request
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 7/08/12 12:43 PM
 */
@Scope("prototype")
@Controller("site.contactUsAction")
public class ContactUsAction extends BaseAction {

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private String subject;

    private String message;

    private String securityCode;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public String contactinfo() {
        return SUCCESS;
    }

    public String contactus() {
        try {
            if (hasInputErrors()) {
                return INPUT;
            }
            //send the contact details to admin
            sendContactUsMailToAdmin();
            //set the action success message
            setSuccessActMsg(getText("tpb.site.submit.contactus.success.msg", new String[]{contactName}));
            //reset the inputs
            resetInputs();
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("tpb.site.submit.contactus.failed"));
            return ERROR;
        }
        return SUCCESS;
    }

    private void sendContactUsMailToAdmin() {
        // site name
        String serverQName = getServerQName();

        //mail template
        String activateEmailTemplateFile = "contactUsMailTemplate.ftl";
        //application name
        String appName = systemPropSettings.getPropValue(SystemPropConts.APPLICATION_NAME);
        // prepare to send email.
        String adminEmail = systemPropSettings.getPropValue(SystemPropConts.SYSTEM_SERVICE_EMAIL);

        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("Subject", subject);
        templateMap.put("UserName", contactName);
        templateMap.put("UserEmail", contactEmail);
        if (StringUtils.isBlank(contactPhone)) {
            contactPhone = " ";
        }
        templateMap.put("UserPhone", contactPhone);
        templateMap.put("Message", message);

        templateMap.put("SiteName", serverQName);
        templateMap.put("AppName", appName);
        this.dmSystemService.sendMail(contactEmail, adminEmail, subject, templateMap, activateEmailTemplateFile, true);
    }

    private void resetInputs() {
        this.contactName = null;
        this.contactEmail = null;
        this.contactPhone = null;
        this.subject = null;
        this.message = null;
        this.securityCode = null;
    }

    private boolean hasInputErrors() {
        boolean hasError = false;
        if (StringUtils.isBlank(contactName)) {
            addFieldError("contactName", getText("tpb.contact.us.name.required"));
            hasError = true;
        }

        if (StringUtils.isBlank(contactEmail)) {
            addFieldError("contactEmail", getText("tpb.contact.us.email.required"));
            hasError = true;
        }

        if (!DMUtil.validateEmail(contactEmail)) {
            addFieldError("contactEmail", getText("tpb.contact.us.email.invalid"));
            hasError = true;
        }

        if (StringUtils.isBlank(subject)) {
            addFieldError("subject", getText("tpb.contact.us.subject.required"));
            hasError = true;
        }

        if (StringUtils.isBlank(message)) {
            addFieldError("message", getText("tpb.contact.us.message.required"));
            hasError = true;
        }

        if (isSecurityCodeError(securityCode)) {
            addFieldError("securityCode", getText("tpb.contact.us.security.code.invalid"));
            hasError = true;
        }
        return hasError;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
