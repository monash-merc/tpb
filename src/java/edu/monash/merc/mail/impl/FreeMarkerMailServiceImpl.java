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

package edu.monash.merc.mail.impl;

import edu.monash.merc.exception.MailException;
import edu.monash.merc.mail.MailService;
import edu.monash.merc.util.mail.MailSenderThread;
import freemarker.template.Template;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Map;

/**
 * FreeMarkerMailServiceImpl class implements the MailService interface which provides the mail service.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 10/12/12 12:06 PM
 */
@Scope("prototype")
@Service
public class FreeMarkerMailServiceImpl implements MailService {

    @Autowired
    @Qualifier("mailSender")
    private JavaMailSender mailSender;

    @Autowired
    @Qualifier("mailFreeMarker")
    private FreeMarkerConfigurer mailFreeMarker;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public FreeMarkerConfigurer getMailFreeMarker() {
        return mailFreeMarker;
    }

    public void setMailFreeMarker(FreeMarkerConfigurer mailFreeMarker) {
        this.mailFreeMarker = mailFreeMarker;
    }

    public void sendMail(String emailFrom, String emailTo, String emailSubject, String emailBody, boolean isHtml) {
        try {
            MailSenderThread sendThread = new MailSenderThread(mailSender, emailFrom, emailTo, emailSubject, emailBody, isHtml);
            sendThread.startSendMail();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new MailException(e);
        }
    }

    public void sendMail(String emailFrom, String emailTo, String emailSubject, Map<String, String> templateValues, String templateFile, boolean isHtml) {
        try {
            String emailBody = createMailBody(templateValues, templateFile);
            MailSenderThread sendThread = new MailSenderThread(mailSender, emailFrom, emailTo, emailSubject, emailBody, isHtml);
            sendThread.startSendMail();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new MailException(e);
        }
    }

    /**
     * Create a mail body base on the mail template file and template values.
     *
     * @param templateValueMap
     * @param templateFile
     * @return
     * @throws Exception
     */
    private String createMailBody(Map<String, String> templateValueMap, String templateFile) throws Exception {
        String htmlText = "";
        Template tpl = mailFreeMarker.getConfiguration().getTemplate(templateFile);
        htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, templateValueMap);
        return htmlText;
    }
}
