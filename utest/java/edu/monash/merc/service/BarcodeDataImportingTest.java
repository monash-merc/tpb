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

package edu.monash.merc.service;

import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.dto.CsvProbeTissueEntryBean;
import edu.monash.merc.dto.barcode.BarcodeDataBean;
import edu.monash.merc.system.parser.TEMaParser;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 9/07/13 2:58 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TransactionConfiguration(defaultRollback = false, transactionManager = "transactionManager")
@Transactional
public class BarcodeDataImportingTest {

    @Autowired
    private DMSystemService dmSystemService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setDmSystemService(DMSystemService dmSystemService) {
        this.dmSystemService = dmSystemService;
    }

    @Test
    public void importBarcodeData() throws Exception {

        List<ChromType> requiredChromTypes = new ArrayList<ChromType>();
        requiredChromTypes.add(ChromType.CHM7);
        requiredChromTypes.add(ChromType.CHMOTHER);
        Date importedTime = GregorianCalendar.getInstance().getTime();
        long start = System.currentTimeMillis();
        String filename1 = "../../testData/abc.ntc.GPL570.csv";

        FileInputStream fileInputStream = new FileInputStream(new File(filename1));
        TEMaParser teMaParser = new TEMaParser();

        System.out.println("=============== start to parse TE MA data for GPL570.csv file");
        List<CsvProbeTissueEntryBean> probeTissueEntryBeans = teMaParser.parse(fileInputStream);
        long endFile1 = System.currentTimeMillis();

        System.out.println("=============== finished to parse TE MA data for GPL570.csv file");
        System.out.println("========================> Total parsing time for GPL 570.csv file : " + (endFile1 - start) / 1000);
        System.out.println("========================> total entry size of GPL 570.csv is : " + probeTissueEntryBeans.size());

        BarcodeDataBean barcodeDataBean = new BarcodeDataBean();
        barcodeDataBean.add(probeTissueEntryBeans);
        this.dmSystemService.saveTEMAEntry(requiredChromTypes, barcodeDataBean, "abc.ntc.GPL570.csv", importedTime, "2013=10-23 12:35:22");
        long endSaveTime1 = System.currentTimeMillis();
        System.out.println("========================> Total time for GPL 570.csv file : " + (endSaveTime1 - start) / 1000);

        String filename2 = "../../testData/abc.ntc.GPL96.csv";
        FileInputStream fileInputStream2 = new FileInputStream(new File(filename2));
        TEMaParser teMaParser2 = new TEMaParser();

        System.out.println("=============== start to parse TE MA data for GPL96.csv file");
        List<CsvProbeTissueEntryBean> probeTissueEntryBeans2 = teMaParser2.parse(fileInputStream2);
        long endFile2 = System.currentTimeMillis();
        System.out.println("=============== finished to parse TE MA data for GPL96.csv file");
        System.out.println("========================> Total parsing time for GPL 96.csv file : " + (endFile2 - endFile1) / 1000);
        System.out.println("========================> total entry size of GPL 96.csv is : " + probeTissueEntryBeans2.size());
        BarcodeDataBean barcodeDataBean2 = new BarcodeDataBean();

        barcodeDataBean2.add(probeTissueEntryBeans2);
        this.dmSystemService.saveTEMAEntry(requiredChromTypes, barcodeDataBean2, filename2, importedTime, "2013=10-23 12:35:22");
        long endSaveTime2 = System.currentTimeMillis();
        System.out.println("========================> Total time for GPL 96.csv file : " + (endSaveTime2 - start) / 1000);

    }
}
