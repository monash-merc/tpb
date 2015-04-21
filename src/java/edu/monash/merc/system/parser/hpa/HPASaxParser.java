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

package edu.monash.merc.system.parser.hpa;

import edu.monash.merc.dto.hpa.HPAEntryBean;
import edu.monash.merc.exception.DMXMLParserException;
import org.apache.log4j.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: simonyu
 * Date: 16/12/2013
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class HPASaxParser {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public List<HPAEntryBean> parseHPAXML(InputStream xmlStream) {

        try {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setValidating(true);
            //create a new SAX parser
            SAXParser saxParser = saxParserFactory.newSAXParser();

            HPAHander hpaHander = new HPAHander();

            saxParser.parse(xmlStream, hpaHander);

            return hpaHander.getHpaEntryBeans();
        } catch (Exception ex) {
            logger.error(ex);
            throw new DMXMLParserException(ex);
        } finally {
            try {
                if (xmlStream != null) {
                    xmlStream.close();
                }
            } catch (Exception fex) {
                //ignore
            }
        }
    }

    public static void main(String[] art) throws Exception {
        String hpaFileName = "/opt/tpb/db_download/hpatest.xml";
        long startTime = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream(new File(hpaFileName));
        HPASaxParser hpaSaxParser = new HPASaxParser();
        hpaSaxParser.parseHPAXML(fileInputStream);

        long endTime = System.currentTimeMillis();

        System.out.println("============= total processing time : " + (endTime - startTime) / 1000 + " seconds.");
    }
}
