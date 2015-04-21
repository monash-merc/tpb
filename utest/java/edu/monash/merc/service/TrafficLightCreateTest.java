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
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.domain.DSVersion;
import edu.monash.merc.system.version.DSVCombination;
import edu.monash.merc.system.version.TLVersionTrack;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
 *        Date: 24/06/13 12:15 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
@Transactional
public class TrafficLightCreateTest {

    @Autowired
    private DMSystemService dmSystemService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setDmSystemService(DMSystemService dmSystemService) {
        this.dmSystemService = dmSystemService;
    }

    @Test
    public void createTrafficLights() throws Exception {
        //start to generate the traffic Lights
        List<ChromType> requiredChromTypes = new ArrayList<ChromType>();
        requiredChromTypes.add(ChromType.CHM7);
        requiredChromTypes.add(ChromType.CHMOTHER);
        Date importedTime = GregorianCalendar.getInstance().getTime();

        try {
            for (ChromType chromType : requiredChromTypes) {
                // createTL(chromType, importedTime);
                //try to pause 2 seconds between two chromosome type

            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private void createTL(ChromType chromType, Date importedTime) {

        DSVersion nxDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.NextProt, chromType);
        DSVersion gpmDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.GPM, chromType);
        DSVersion gpmPstyDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.GPMPSYT, chromType);
        DSVersion gpmLysDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.GPMLYS, chromType);
        DSVersion gpmNtaDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.GPMNTA, chromType);
        DSVersion hpaDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.HPA, chromType);
        DSVersion bcHgu133aDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.BarcodeHgu133a, chromType);
        DSVersion bcHgu133p2DsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.BarcodeHgu133plus2, chromType);


        List<TLVersionTrack> tlVersionTracks = DSVCombination.createTLVersionTracks(nxDsVersion, gpmDsVersion, gpmPstyDsVersion, gpmLysDsVersion, gpmNtaDsVersion, hpaDsVersion, bcHgu133aDsVersion, bcHgu133p2DsVersion);
        if (tlVersionTracks != null) {
            System.out.println("Starting to create a total of " + +tlVersionTracks.size() + "  traffic lights");
            for (TLVersionTrack tlvTrack : tlVersionTracks) {
                try {

                    System.out.println("<<================ Starting to create the traffic lights for " + chromType.chm() + ", track token: " + tlvTrack.getTrackToken());
                    DSVersion gpmPstyDsV = tlvTrack.getGpmPstyDsVersion();
                    boolean gpmDsIncluded = tlvTrack.isGpmDsIncluded();
                    if (gpmDsIncluded && gpmPstyDsV != null) {
                        System.out.println("<<================  GPM PSTY Db source: " + gpmPstyDsV.getDbSource() + ", version: " + gpmPstyDsV.getVersionNo() + ", chromosome: " + gpmPstyDsV.getChromosome());
                    } else {
                        System.out.println("<<================  GPM PSTY Not Included");
                    }

                    boolean succeed = this.dmSystemService.createVersionTLByChrom(chromType, tlvTrack, importedTime);


                    if (succeed) {
                        System.out.println("<<================ Finished to create the traffic lights for " + chromType.chm() + ", track token: " + tlvTrack.getTrackToken());
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
