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

import edu.monash.merc.domain.DSVersion;
import org.apache.commons.lang.StringUtils;

/**
 * TLVersionTrack class is an utility class which track the traffic light of the different datasources
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/07/12 11:31 AM
 */
public class TLVersionTrack {

    private DSVersion nxDsVersion;

    private DSVersion gpmDsVersion;

    private DSVersion gpmPstyDsVersion;

    private DSVersion gpmLysDsVersion;

    private DSVersion gpmNtaDsVersion;

    private DSVersion hpaDsVersion;

    private DSVersion bcHgu133aDsVersion;

    private DSVersion bcHgu133p2DsVersion;

    private boolean nxDsIncluded;

    private boolean gpmDsIncluded;

    private boolean hpaDsIncluded;

    private boolean bcDsIncluded;

    private int trackToken;

    public DSVersion getNxDsVersion() {
        return nxDsVersion;
    }

    public void setNxDsVersion(DSVersion nxDsVersion) {
        this.nxDsVersion = nxDsVersion;
    }

    public DSVersion getGpmDsVersion() {
        return gpmDsVersion;
    }

    public void setGpmDsVersion(DSVersion gpmDsVersion) {
        this.gpmDsVersion = gpmDsVersion;
    }

    public DSVersion getGpmPstyDsVersion() {
        return gpmPstyDsVersion;
    }

    public void setGpmPstyDsVersion(DSVersion gpmPstyDsVersion) {
        this.gpmPstyDsVersion = gpmPstyDsVersion;
    }

    public DSVersion getGpmLysDsVersion() {
        return gpmLysDsVersion;
    }

    public void setGpmLysDsVersion(DSVersion gpmLysDsVersion) {
        this.gpmLysDsVersion = gpmLysDsVersion;
    }

    public DSVersion getGpmNtaDsVersion() {
        return gpmNtaDsVersion;
    }

    public void setGpmNtaDsVersion(DSVersion gpmNtaDsVersion) {
        this.gpmNtaDsVersion = gpmNtaDsVersion;
    }

    public DSVersion getHpaDsVersion() {
        return hpaDsVersion;
    }

    public void setHpaDsVersion(DSVersion hpaDsVersion) {
        this.hpaDsVersion = hpaDsVersion;
    }

    public DSVersion getBcHgu133aDsVersion() {
        return bcHgu133aDsVersion;
    }

    public void setBcHgu133aDsVersion(DSVersion bcHgu133aDsVersion) {
        this.bcHgu133aDsVersion = bcHgu133aDsVersion;
    }

    public DSVersion getBcHgu133p2DsVersion() {
        return bcHgu133p2DsVersion;
    }

    public void setBcHgu133p2DsVersion(DSVersion bcHgu133p2DsVersion) {
        this.bcHgu133p2DsVersion = bcHgu133p2DsVersion;
    }

    public int getTrackToken() {
        return trackToken;
    }

    public void setTrackToken(int trackToken) {
        this.trackToken = trackToken;
    }

    public boolean isNxDsIncluded() {
        return nxDsIncluded;
    }

    public void setNxDsIncluded(boolean nxDsIncluded) {
        this.nxDsIncluded = nxDsIncluded;
    }

    public boolean isGpmDsIncluded() {
        return gpmDsIncluded;
    }

    public void setGpmDsIncluded(boolean gpmDsIncluded) {
        this.gpmDsIncluded = gpmDsIncluded;
    }

    public boolean isHpaDsIncluded() {
        return hpaDsIncluded;
    }

    public void setHpaDsIncluded(boolean hpaDsIncluded) {
        this.hpaDsIncluded = hpaDsIncluded;
    }

    public boolean isBcDsIncluded() {
        return bcDsIncluded;
    }

    public void setBcDsIncluded(boolean bcDsIncluded) {
        this.bcDsIncluded = bcDsIncluded;
    }

    public boolean isSingleDbs() {
        if (this.trackToken == 0) {
            return false;
        }
        String tokenStr = String.valueOf(this.trackToken);
        int matches = StringUtils.countMatches(tokenStr, "1");
        if (matches == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int[] separateTokenAsTwo() {
        String tokenStr = String.valueOf(this.trackToken);
        int last1Index = StringUtils.lastIndexOf(tokenStr, "1");
        int last1Gap = tokenStr.length() - last1Index;
        if (last1Gap == tokenStr.length()) {
            return new int[]{this.trackToken};
        } else {
            int secondToken = createSecondToken(last1Gap);
            int firstToken = this.trackToken - secondToken;
            return new int[]{firstToken, secondToken};
        }
    }

    private int createSecondToken(int indexgap) {
        String secondTStr = "1";
        for (int i = 0; i < indexgap - 1; i++) {
            secondTStr += "0";
        }
        return Integer.valueOf(secondTStr).intValue();
    }

    public static void main(String[] args) {

        TLVersionTrack tlVersionTrack = new TLVersionTrack();
        tlVersionTrack.setTrackToken(1101);

        System.out.println("========== is single dbs: " + tlVersionTrack.isSingleDbs());

        int[] twoTokens = tlVersionTrack.separateTokenAsTwo();
        for (int token : twoTokens) {
            System.out.println("========== token : " + token);
        }
    }

}
