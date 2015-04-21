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
import edu.monash.merc.system.parser.XMLStack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: simonyu
 * Date: 16/12/2013
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class HPAHander extends DefaultHandler {

    private List<HPAEntryBean> hpaEntryBeans = new ArrayList<HPAEntryBean>();

    private HPAEntryBean hpaEntryBean;

    private XMLStack pathStack = new XMLStack();

    private StringBuilder stringBuilder = new StringBuilder();


    public List<HPAEntryBean> getHpaEntryBeans() {
        return hpaEntryBeans;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        pathStack.push(qName);
        //Reset the StringBuilder
        stringBuilder.setLength(0);

        //get the current from the stack
        String currentPath = pathStack.currentPath();
        if(currentPath.equals(HPAFields.PATH_ENTRY_IDENTIFIER)){
            System.out.println("====== entry- identifier : " + currentPath);
            String id = attributes.getValue(HPAFields.ATT_IDENTIFIER_ID);
            String db = attributes.getValue(HPAFields.ATT_IDENTIFIER_DB);
            String version = attributes.getValue(HPAFields.ATT_IDENTIFIER_VERSION);
            System.out.println("========= ENSG : " + id + "  db: " + db + " version: " + version);
        }

        if (currentPath.equals(HPAFields.PATH_ENTRY_TISSUE_EXPRESSION)) {
            System.out.println("====== start tissue expression - path : " + currentPath);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        //super the super-class
        super.characters(ch, start, length);
        //using a stringbuilder to get all characters during startElement and endElement processing
        stringBuilder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //get current path from the stack
        String currentPath = pathStack.currentPath();

        if (currentPath.equals(HPAFields.PATH_ENTRY_TISSUE_EXPRESSION)) {
            System.out.println("====== end path : " + currentPath);
        }
        //finally pop the current path as it's end of current element.
        pathStack.pop();

    }
}
