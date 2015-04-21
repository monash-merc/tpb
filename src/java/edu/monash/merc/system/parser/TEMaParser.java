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

package edu.monash.merc.system.parser;

import au.com.bytecode.opencsv.CSVReader;
import edu.monash.merc.dto.CsvProbeTissueEntryBean;
import edu.monash.merc.dto.CsvTissueExpBean;
import edu.monash.merc.exception.DMParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 2/07/13 4:38 PM
 */
public class TEMaParser {

    public List<CsvProbeTissueEntryBean> parse(InputStream inputStream) {

        CSVReader csvReader = null;

        List<CsvProbeTissueEntryBean> probeTissueEntryBeans = new ArrayList<CsvProbeTissueEntryBean>();
        try {
            csvReader = new CSVReader(new InputStreamReader(inputStream));
            //get the csv header as the column name
            String[] columnsLine = csvReader.readNext();
            //the column values line. each line will be given a list of values based on the header columns
            String[] columnValuesLine;
            //start from the second line, we do a loop to get all column values
            while ((columnValuesLine = csvReader.readNext()) != null) {
                //we only consider the length of column values is equal to the length of the column names, otherwise it will be mis-matched.
                if (columnValuesLine.length == columnsLine.length) {
                    //create a CsvProbeTissueEntryBean
                    CsvProbeTissueEntryBean probeTissueEntryBean = new CsvProbeTissueEntryBean();
                    //set the probe set id value
                    probeTissueEntryBean.setProbeId(columnValuesLine[0]);
                    //get a list of tissue and expression values
                    for (int i = 1; i < columnsLine.length; i++) {
                        String columnName = columnsLine[i];
                        String columnValue = columnValuesLine[i];
                        //Create a CsvTissueExpBean
                        CsvTissueExpBean tissueExpBean = new CsvTissueExpBean();
                        //set the tissue name
                        tissueExpBean.setTissueName(columnName);
                        //set the tissue expression value
                        tissueExpBean.setExpression(Double.valueOf(columnValue).doubleValue());
                        //add it into the CsvProbeTissueEntryBean
                        probeTissueEntryBean.getTissueExpBeans().add(tissueExpBean);
                    }
                    //add it into the list of CsvProbeTissueEntryBean
                    probeTissueEntryBeans.add(probeTissueEntryBean);
                }
            }

            //return the parsed result.
            return probeTissueEntryBeans;
        } catch (Exception ex) {
            throw new DMParserException(ex);
        } finally {
            try {
                if (csvReader != null) {
                    csvReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception fex) {
                //ignore whatever caught
            }
        }
    }


    public static void main(String[] arg) throws Exception {

        String filename = "./testData/abc.ntc.GPL570.csv";

        FileInputStream fileInputStream = new FileInputStream(new File(filename));
        TEMaParser teMaParser = new TEMaParser();

        List<CsvProbeTissueEntryBean> probeTissueEntryBeans = teMaParser.parse(fileInputStream);

        for (CsvProbeTissueEntryBean probeTissueEntryBean : probeTissueEntryBeans) {
            System.out.println("=========== probe id: " + probeTissueEntryBean.getProbeId());
            List<CsvTissueExpBean> csvTissueExpBeans = probeTissueEntryBean.getTissueExpBeans();
            int tissueNo = 1;
            for (CsvTissueExpBean tissueExpBean : csvTissueExpBeans) {
                System.out.println("No: " +  tissueNo + " ============= tissue : " + tissueExpBean.getTissueName() + " - expression : " + tissueExpBean.getExpression());
                tissueNo++;
            }
        }

        System.out.println("============ probe tissue entry beans size : " + probeTissueEntryBeans.size());
    }
}
