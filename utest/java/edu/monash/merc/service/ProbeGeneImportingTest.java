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

import edu.monash.merc.dto.ProbeGeneBean;
import edu.monash.merc.system.config.SystemPropConts;
import edu.monash.merc.wsclient.biomart.BioMartClient;
import edu.monash.merc.wsclient.biomart.GeneConsts;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 4/07/13 12:26 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TransactionConfiguration(defaultRollback = false, transactionManager = "transactionManager")
@Transactional
public class ProbeGeneImportingTest {
    @Autowired
    private DMSystemService dmSystemService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setDmSystemService(DMSystemService dmSystemService) {
        this.dmSystemService = dmSystemService;
    }

    @Test
    public void importProbes() {
        List<String> requiredPlatForms = new ArrayList<String>();
        requiredPlatForms.add("affy_hg_u133_plus_2");
        requiredPlatForms.add("affy_hg_u133a");

        for (String platform : requiredPlatForms) {
           // importGeneProbes(GeneConsts.HUMAN, platform);
        }

    }

    private void importGeneProbes(String species, String platform) {
        BioMartClient client = new BioMartClient();
        try {
            System.out.println("TPB is starting to import the probes for platform - " + platform);
            String wsURL = "http://www.biomart.org/biomart/martservice/result?query=";
            client.configProbe(wsURL, species);
            List<ProbeGeneBean> probeGeneBeans = client.importProbes(platform);
            System.out.println("The total size of the gene probes: " + probeGeneBeans.size());
            this.dmSystemService.importProbes(probeGeneBeans);
            System.out.println("Successfully imported the gene probes into database.");
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                client.release();
            } catch (Exception cex) {
                //ignore whatever
            }
        }
    }


}
