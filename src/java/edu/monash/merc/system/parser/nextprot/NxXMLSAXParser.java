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

package edu.monash.merc.system.parser.nextprot;

import edu.monash.merc.dto.*;
import edu.monash.merc.exception.DMXMLParserException;
import org.apache.log4j.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * NxXMLParser class which parses the nextprot xml file
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 24/02/12 1:03 PM
 */
public class NxXMLSAXParser {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public List<NXEntryBean> parseNextProtXML(InputStream xmlStream) {
        try {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setValidating(true);
            //create a new SAX parser
            SAXParser saxParser = saxParserFactory.newSAXParser();

            //parser handler
            NXHandler nxHandler = new NXHandler();

            //parse the xml file
            saxParser.parse(xmlStream, nxHandler);

            //get the parsed results
            return nxHandler.getNxEntryBeans();

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


    public static void main(String[] args) throws Exception {
        String filename = "./testData/nextprot_chromosome_7.xml";
        long startTime = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream(new File(filename));
        NxXMLSAXParser parser = new NxXMLSAXParser();
        List<NXEntryBean> nxEntryBeans = parser.parseNextProtXML(fileInputStream);
        long endTime = System.currentTimeMillis();


        for (NXEntryBean nxEntryBean : nxEntryBeans) {
            System.out.println("\n======== NextProt AC: " + nxEntryBean.getIdentifiedAccessionBean().getAccession() + " - desc : " + nxEntryBean.getIdentifiedAccessionBean().getDescription());
            GeneBean geneBean = nxEntryBean.getGeneBean();
            System.out.println("================ gene symbol : " + geneBean.getDisplayName() + " -- chromsome : " + geneBean.getChromosome() + " - ensg ac : " + geneBean.getEnsgAccession());
            List<DbSourceAcEntryBean> dbSourceAcEntryBeans = nxEntryBean.getDbSourceAcEntryBeans();
//            for (DbSourceAcEntryBean dbSourceAcEntryBean : dbSourceAcEntryBeans) {
//                AccessionBean accessionBean = dbSourceAcEntryBean.getAccessionBean();
//                DBSourceBean dbSourceBean = dbSourceAcEntryBean.getDbSourceBean();
//                System.out.println("   === ac type : " + accessionBean.getAcType() + " - ac : " + accessionBean.getAccession() + " -- dbsource : " + dbSourceBean.getDbName());
//            }
            System.out.println("================ identifiers size : " + dbSourceAcEntryBeans.size());

            NXPeTeOthEntryBean nxPeTeOthEntryBean = nxEntryBean.getNxPeTeOthEntryBean();

            PEEvidenceBean peOthCurEvi = nxPeTeOthEntryBean.getNxPeOthEvidenceBean();

            System.out.println("========= PE oth cur evidence : " + "color level: " + peOthCurEvi.getColorLevel() + " - evidence: " + peOthCurEvi.getEvidenceValue() + " -- type : " + peOthCurEvi.getTpbDataTypeBean().getDataType() + " --- url : " + peOthCurEvi.getHyperlink());

            TEEvidenceBean teOthCurEvi = nxPeTeOthEntryBean.getNxTeOthEvidenceBean();

            System.out.println("========= TE oth cur evidence : " + "color level: " + teOthCurEvi.getColorLevel() + " - evidence: " + teOthCurEvi.getEvidenceValue() + " -- type : " + teOthCurEvi.getTpbDataTypeBean().getDataType() + " --- url : " + teOthCurEvi.getHyperlink());

            NXPeMsAntiEntryBean nxPeMsAntiEntryBean = nxEntryBean.getNxPeMsAntiEntryBean();

            List<PEEvidenceBean> nxPeMsAnnBeans = nxPeMsAntiEntryBean.getPeMsAnnEvidenceBeans();
            for(PEEvidenceBean peEvidenceBean: nxPeMsAnnBeans){
                System.out.println("========= PE MS Ann evidence : " + "color level: " + peEvidenceBean.getColorLevel() + " - evidence: " + peEvidenceBean.getEvidenceValue() + " -- type : " + peEvidenceBean.getTpbDataTypeBean().getDataType() + " --- url : " + peEvidenceBean.getHyperlink());
            }
            PEEvidenceBean nxPeAntiAnnBean = nxPeMsAntiEntryBean.getPeAntiAnnEvidenceBean();
            if(nxPeAntiAnnBean != null){
                System.out.println("========= PE Anit Ann evidence : " + "color level: " + nxPeAntiAnnBean.getColorLevel() + " - evidence: " + nxPeAntiAnnBean.getEvidenceValue() + " -- type : " + nxPeAntiAnnBean.getTpbDataTypeBean().getDataType() + " --- url : " + nxPeAntiAnnBean.getHyperlink());
            }

        }

        System.out.println("======== total size of nextprot entry: " + nxEntryBeans.size());
        System.out.println("============= total processing time : " + (endTime - startTime) / 1000 + " seconds.");
    }


}
