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

package edu.monash.merc.util.mail;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

/**
 * The MailSenderThread class which sends an email in an asynchronous way.
 *
 * @author Simon Yu
 * @email xiaoming.yu@monash.edu
 * @date: 10/12/12 1:00 PM
 * @version: 1.0
 */
public class MailSenderThread implements Runnable {
    /**
     * mail sender thread *
     */
    private Thread senderThread;

    /**
     * JavaMailSender object
     */
    private JavaMailSender sender;

    /**
     * Mail From
     */
    private String emailFrom;

    /**
     * Mail To
     */
    private String emailTo;

    /**
     * Mail Subject
     */
    private String emailSubject;

    /**
     * Mail Body
     */
    private String emailBody;

    /**
     * Mail in HTML format or not
     */
    private boolean isHtml;

    /**
     * Logger class
     */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Default Constructor
     */
    public MailSenderThread() {
        this.senderThread = new Thread(this);
    }

    /**
     * Constructor
     *
     * @param sender       a JavaMailSender
     * @param emailFrom    an email from
     * @param emailTo      an email to
     * @param emailSubject an email subject
     * @param emailBody    an email body
     * @param isHtml       if it's a html format
     */
    public MailSenderThread(JavaMailSender sender, String emailFrom, String emailTo, String emailSubject, String emailBody, boolean isHtml) {
        this.senderThread = new Thread(this);
        this.sender = sender;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        this.isHtml = isHtml;
    }

    /**
     * Send mail start method
     */
    public void startSendMail() {
        if (this.senderThread == null) {
            this.senderThread = new Thread(this);
        }
        this.senderThread.start();
    }

    /**
     * Thread run method
     */
    public void run() {
        Thread runThread = Thread.currentThread();
        if ((senderThread != null) && (senderThread == runThread)) {
            try {
                MimeMessage mmsg = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mmsg, false, "utf8");
                helper.setFrom(emailFrom);
                helper.setTo(emailTo);
                helper.setSubject(emailSubject);
                helper.setText(emailBody, isHtml);
                sender.send(mmsg);
                logger.info("Finished to send an email.");
            } catch (Exception e) {
                logger.error("Failed to send an email, " + e.getMessage());
            }
        }
    }

    public Thread getSenderThread() {
        return senderThread;
    }

    public void setSenderThread(Thread senderThread) {
        this.senderThread = senderThread;
    }

    public JavaMailSender getSender() {
        return sender;
    }

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }
}
