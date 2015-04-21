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

package edu.monash.merc.system.version;

import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.domain.DSVersion;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DSVCombination class is an utility class which track the combination of the different datasources
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/07/12 11:29 AM
 */
public class DSVCombination {

    //private static String ALL_EMPTY_TOKEN = "00000";

    private static String DS_TRACK_TOKEN_AVAILABLE = "1";

    private static String DS_TRACK_TOKEN_UNAVAILABLE = "0";

    private static List<DSVersionTrack> createDSVersionTracks(DSVersion dsVersion) {
        List<DSVersionTrack> dsVersionTracks = new ArrayList<DSVersionTrack>();

        DSVersionTrack dsVersionTrackNone = new DSVersionTrack();
        dsVersionTrackNone.setTrackToken(DS_TRACK_TOKEN_UNAVAILABLE);

        //add dsVersionTrackNone first
        dsVersionTracks.add(dsVersionTrackNone);
        if (dsVersion != null) {
            DSVersionTrack dsVersionTrack = new DSVersionTrack();
            dsVersionTrack.setTrackToken(DS_TRACK_TOKEN_AVAILABLE);
            dsVersionTrack.setDsVersion(dsVersion);
            dsVersionTracks.add(dsVersionTrack);
        }
        return dsVersionTracks;
    }

    private static List<DSVersionTrack> createGPMDSVersionTracks(DSVersion gpmDsVersion, DSVersion gpmPstyDsVersion, DSVersion gpmLysDsVersion, DSVersion gpmNtaDsVersion) {
        List<DSVersionTrack> dsVersionTracks = new ArrayList<DSVersionTrack>();

        DSVersionTrack dsVersionTrackNone = new DSVersionTrack();
        dsVersionTrackNone.setTrackToken(DS_TRACK_TOKEN_UNAVAILABLE);
        //add dsVersionTrackNone first
        dsVersionTracks.add(dsVersionTrackNone);

        if (gpmDsVersion != null || gpmPstyDsVersion != null || gpmLysDsVersion != null || gpmNtaDsVersion != null) {
            DSVersionTrack dsVersionTrack = new DSVersionTrack();
            dsVersionTrack.setTrackToken(DS_TRACK_TOKEN_AVAILABLE);
            dsVersionTrack.setDsVersion(gpmDsVersion);
            dsVersionTracks.add(dsVersionTrack);
        }
        return dsVersionTracks;
    }

    private static List<DSVersionTrack> createBarcodeDSVersionTrack(DSVersion bcHgu133aDsVersion, DSVersion bcHgu133p2DsVersion){
        List<DSVersionTrack> dsVersionTracks = new ArrayList<DSVersionTrack>();

        DSVersionTrack dsVersionTrackNone = new DSVersionTrack();
        dsVersionTrackNone.setTrackToken(DS_TRACK_TOKEN_UNAVAILABLE);
        //add dsVersionTrackNone first
        dsVersionTracks.add(dsVersionTrackNone);

        if (bcHgu133aDsVersion != null || bcHgu133p2DsVersion != null) {
            DSVersionTrack dsVersionTrack = new DSVersionTrack();
            dsVersionTrack.setTrackToken(DS_TRACK_TOKEN_AVAILABLE);
            dsVersionTracks.add(dsVersionTrack);
        }
        return dsVersionTracks;
    }

    public static List<TLVersionTrack> createTLVersionTracks(DSVersion nxDsVersion, DSVersion gpmDsVersion, DSVersion gpmPstyDsVersion, DSVersion gpmLysDsVersion, DSVersion gpmNtaDsVersion, DSVersion hpaDsVersion, DSVersion bcHgu133aDsVersion, DSVersion bcHgu133p2DsVersion) {
        List<DSVersionTrack> nxVersionTracks = createDSVersionTracks(nxDsVersion);
        List<DSVersionTrack> gpmVersionTracks = createGPMDSVersionTracks(gpmDsVersion, gpmPstyDsVersion, gpmLysDsVersion, gpmNtaDsVersion);
        List<DSVersionTrack> hpaVersionTracks = createDSVersionTracks(hpaDsVersion);
        List<DSVersionTrack> bcVersionTracks = createBarcodeDSVersionTrack(bcHgu133aDsVersion, bcHgu133p2DsVersion);
        return createTLVersionTracks(nxVersionTracks, gpmVersionTracks, gpmPstyDsVersion, gpmLysDsVersion, gpmNtaDsVersion, hpaVersionTracks, bcVersionTracks, bcHgu133aDsVersion, bcHgu133p2DsVersion);
    }

    public static List<TLVersionTrack> createTLVersionTracks(List<DSVersionTrack> nxVersionTracks, List<DSVersionTrack> gpmVersionTracks, DSVersion gpmPstyDsVersion, DSVersion gpmLysDsVersion, DSVersion gpmNtaDsVersion, List<DSVersionTrack> hpaVersionTracks, List<DSVersionTrack> bcVersionTracks, DSVersion bcHgu133aDsVersion, DSVersion bcHgu133p2DsVersion) {
        List<TLVersionTrack> tlVersionTracks = new ArrayList<TLVersionTrack>();
        for (DSVersionTrack nxvtrack : nxVersionTracks) {
            DSVersion nxDsVersion = nxvtrack.getDsVersion();
            String nxTrackToken = nxvtrack.getTrackToken();

            for (DSVersionTrack gpmtrack : gpmVersionTracks) {
                DSVersion gpmDsVersion = gpmtrack.getDsVersion();
                String gpmTrackToken = gpmtrack.getTrackToken();

                for (DSVersionTrack hpatrack : hpaVersionTracks) {
                    DSVersion hpaDsVersion = hpatrack.getDsVersion();
                    String hpaTrackToken = hpatrack.getTrackToken();

                    for (DSVersionTrack bctrack : bcVersionTracks) {

                        String bcTrackToken = bctrack.getTrackToken();
                        String combinatedToken = nxTrackToken + gpmTrackToken + hpaTrackToken + bcTrackToken;
                        int tokenNum = Integer.valueOf(StringUtils.reverse(combinatedToken));
                        if (tokenNum != 0) {
                            //create a new TLVersionTrack
                            TLVersionTrack tlVersionTrack = new TLVersionTrack();
                            //set the DSVersions for this TLVersionTrack
                            if (nxTrackToken.equals(DS_TRACK_TOKEN_AVAILABLE)) {
                                tlVersionTrack.setNxDsVersion(nxDsVersion);
                                tlVersionTrack.setNxDsIncluded(true);
                            }
                            if (gpmTrackToken.equals(DS_TRACK_TOKEN_AVAILABLE)) {
                                tlVersionTrack.setGpmDsVersion(gpmDsVersion);
                                tlVersionTrack.setGpmPstyDsVersion(gpmPstyDsVersion);
                                tlVersionTrack.setGpmLysDsVersion(gpmLysDsVersion);
                                tlVersionTrack.setGpmNtaDsVersion(gpmNtaDsVersion);
                                tlVersionTrack.setGpmDsIncluded(true);
                            }
                            if (hpaTrackToken.equals(DS_TRACK_TOKEN_AVAILABLE)) {
                                tlVersionTrack.setHpaDsVersion(hpaDsVersion);
                                tlVersionTrack.setHpaDsIncluded(true);
                            }
                            if (bcTrackToken.equals(DS_TRACK_TOKEN_AVAILABLE)) {
                                tlVersionTrack.setBcHgu133aDsVersion(bcHgu133aDsVersion);
                                tlVersionTrack.setBcHgu133p2DsVersion(bcHgu133p2DsVersion);
                                tlVersionTrack.setBcDsIncluded(true);
                            }
                            tlVersionTrack.setTrackToken(tokenNum);
                            //add this TLVersionTrack into the list
                            tlVersionTracks.add(tlVersionTrack);
                        }
                    }
                }

            }
        }
        //sort the TLVersionTrack in ascend order
        Collections.sort(tlVersionTracks, new TLVComparator());
        return tlVersionTracks;
    }


    public static void main(String[] args) {
        //testing
        List<DSVersionTrack> nxVersionTracks = new ArrayList<DSVersionTrack>();
        DSVersionTrack nxDsVersionTrack1 = new DSVersionTrack();
        nxDsVersionTrack1.setTrackToken("1");
        DSVersionTrack nxDsVersionTrack2 = new DSVersionTrack();
        nxDsVersionTrack2.setTrackToken("0");
        nxVersionTracks.add(nxDsVersionTrack1);
        nxVersionTracks.add(nxDsVersionTrack2);

        List<DSVersionTrack> gpmVersionTracks = new ArrayList<DSVersionTrack>();
        DSVersionTrack gpmDsVersionTrack1 = new DSVersionTrack();
        gpmDsVersionTrack1.setTrackToken("1");
        DSVersionTrack gpmDsVersionTrack2 = new DSVersionTrack();
        gpmDsVersionTrack2.setTrackToken("0");
        gpmVersionTracks.add(gpmDsVersionTrack1);
        gpmVersionTracks.add(gpmDsVersionTrack2);

        DSVersion gpmPstyDsVersion = new DSVersion();
        gpmPstyDsVersion.setDbSource(DbAcType.GPMPSYT.type());
        gpmPstyDsVersion.setVersionNo(1);

        DSVersion gpmLysDsVersion = new DSVersion();
        gpmLysDsVersion.setDbSource(DbAcType.GPMLYS.type());
        gpmLysDsVersion.setVersionNo(3);

        DSVersion gpmNtaDsVersion = new DSVersion();
        gpmNtaDsVersion.setDbSource(DbAcType.GPMNTA.type());
        gpmNtaDsVersion.setVersionNo(6);

        List<DSVersionTrack> hpaVersionTracks = new ArrayList<DSVersionTrack>();
        DSVersionTrack hpaDsVersionTrack2 = new DSVersionTrack();
        hpaDsVersionTrack2.setTrackToken("1");
        hpaVersionTracks.add(hpaDsVersionTrack2);
        DSVersionTrack hpaDsVersionTrack1 = new DSVersionTrack();
        hpaDsVersionTrack1.setTrackToken("0");
        hpaVersionTracks.add(hpaDsVersionTrack1);


        List<DSVersionTrack> bcVersionTracks = new ArrayList<DSVersionTrack>();
        DSVersionTrack bcDsVersionTrack2 = new DSVersionTrack();
        bcDsVersionTrack2.setTrackToken("1");
        bcVersionTracks.add(bcDsVersionTrack2);
        DSVersionTrack bcDsVersionTrack1 = new DSVersionTrack();
        bcDsVersionTrack1.setTrackToken("0");
        bcVersionTracks.add(bcDsVersionTrack1);
        DSVersion bcHgu133aDsVersion = new DSVersion();
        bcHgu133aDsVersion.setDbSource(DbAcType.BarcodeHgu133a.type());
        bcHgu133aDsVersion.setVersionNo(1);

        DSVersion bcHgu133p2DsVersion = new DSVersion();
        bcHgu133p2DsVersion.setDbSource(DbAcType.BarcodeHgu133plus2.type());
        bcHgu133p2DsVersion.setVersionNo(10);


        List<TLVersionTrack> tlVersionTracks = DSVCombination.createTLVersionTracks(nxVersionTracks, gpmVersionTracks, gpmPstyDsVersion, gpmLysDsVersion, gpmNtaDsVersion, hpaVersionTracks, bcVersionTracks, bcHgu133aDsVersion, bcHgu133p2DsVersion);

        System.out.println("=========> total combination: " + tlVersionTracks.size());
        if (tlVersionTracks != null) {
            for (TLVersionTrack tlVTrack : tlVersionTracks) {
                int trackToken = tlVTrack.getTrackToken();

                System.out.println("======> DSVersion combinated token:  " + trackToken);

                System.out.println("=========== > nx included? : " + tlVTrack.isNxDsIncluded());
                System.out.println("=========== > barcode included? : " + tlVTrack.isBcDsIncluded());
            }
        }

        System.out.println("==============> Another testing case:");
        DSVersion nxDsVersion = new DSVersion();
        DSVersion gpmDsVersion = null;

        DSVersion hpaDsVersion = null;

       bcHgu133aDsVersion = null;
       bcHgu133p2DsVersion = null;
        List<TLVersionTrack> tlVersionTracks2 = DSVCombination.createTLVersionTracks(nxDsVersion, gpmDsVersion, gpmPstyDsVersion, gpmLysDsVersion, gpmNtaDsVersion, hpaDsVersion, bcHgu133aDsVersion, bcHgu133p2DsVersion);
        System.out.println("=========> a total combination: " + tlVersionTracks2.size());
        if (tlVersionTracks2 != null) {
            for (TLVersionTrack tlVTrack : tlVersionTracks2) {
                int trackToken = tlVTrack.getTrackToken();
                System.out.println("======> DSVersion combinated token:  " + trackToken);
                DSVersion gpmpstyV = tlVTrack.getGpmPstyDsVersion();
                if (tlVTrack.isGpmDsIncluded() && gpmpstyV != null) {
                    System.out.println("======> GpmPsty dbsource: " + tlVTrack.getGpmPstyDsVersion().getDbSource() + ", version: " + gpmpstyV.getVersionNo());
                }
                DSVersion gpmLysV = tlVTrack.getGpmLysDsVersion();
                if (tlVTrack.isGpmDsIncluded() && gpmLysV != null) {
                    System.out.println("======> Gpm Lys dbsource: " + tlVTrack.getGpmLysDsVersion().getDbSource() + ", version: " + gpmLysV.getVersionNo());
                }
                DSVersion gpmNtaV = tlVTrack.getGpmNtaDsVersion();
                if (tlVTrack.isGpmDsIncluded() && gpmNtaV != null) {
                    System.out.println("======> Gpm Nta dbsource: " + tlVTrack.getGpmNtaDsVersion().getDbSource() + ", version: " + gpmNtaV.getVersionNo());
                }

                boolean bcDsIncluded = tlVTrack.isBcDsIncluded();
                if(bcDsIncluded){
                    DSVersion bcHgu133aV = tlVTrack.getBcHgu133aDsVersion();
                    if(bcHgu133aV != null){
                        System.out.println("======> Barcode HGU 133a dbsource: " + bcHgu133aV.getDbSource() + ", version: " + bcHgu133aV.getVersionNo());
                    }
                    DSVersion bcHgu133p2V = tlVTrack.getBcHgu133p2DsVersion();
                    if(bcHgu133p2V != null){
                        System.out.println("======> Barcode HGU 133plus2 dbsource: " + bcHgu133p2V.getDbSource() + ", version: " + bcHgu133p2V.getVersionNo());
                    }
                }
            }
        }

    }
}
