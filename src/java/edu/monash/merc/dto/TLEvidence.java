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

package edu.monash.merc.dto;

import java.io.Serializable;

/**
 * TLEvidence class which is a DTO class populates the TLEvidence object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/05/12 11:47 AM
 */
public class TLEvidence implements Serializable {

    private int colorLevel = 1;

    private long geneId;

    private String geneName;

    private String ensemblId;

    private long evidenceId;

    private int pos;

    private String evidence;

    private String hyperLink;

    private String identifiedAccession;

    private String dbSource;

    private String tpbDataType;

    private String typeShortName;

    private String typeDisplayName;

    private boolean lastLevel;

    private int tlLevel;

    private String tissue;

    private double expression;

    private boolean uniqueGene;

    public TLEvidence() {

    }

    public int getColorLevel() {
        return colorLevel;
    }

    public void setColorLevel(int colorLevel) {
        this.colorLevel = colorLevel;
    }

    public long getGeneId() {
        return geneId;
    }

    public void setGeneId(long geneId) {
        this.geneId = geneId;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getEnsemblId() {
        return ensemblId;
    }

    public void setEnsemblId(String ensemblId) {
        this.ensemblId = ensemblId;
    }

    public long getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(long evidenceId) {
        this.evidenceId = evidenceId;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getHyperLink() {
        return hyperLink;
    }

    public void setHyperLink(String hyperLink) {
        this.hyperLink = hyperLink;
    }

    public String getIdentifiedAccession() {
        return identifiedAccession;
    }

    public void setIdentifiedAccession(String identifiedAccession) {
        this.identifiedAccession = identifiedAccession;
    }

    public String getDbSource() {
        return dbSource;
    }

    public void setDbSource(String dbSource) {
        this.dbSource = dbSource;
    }

    public String getTpbDataType() {
        return tpbDataType;
    }

    public void setTpbDataType(String tpbDataType) {
        this.tpbDataType = tpbDataType;
    }

    public String getTypeShortName() {
        return typeShortName;
    }

    public void setTypeShortName(String typeShortName) {
        this.typeShortName = typeShortName;
    }

    public String getTypeDisplayName() {
        return typeDisplayName;
    }

    public void setTypeDisplayName(String typeDisplayName) {
        this.typeDisplayName = typeDisplayName;
    }

    public boolean isLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(boolean lastLevel) {
        this.lastLevel = lastLevel;
    }

    public int getTlLevel() {
        return tlLevel;
    }

    public void setTlLevel(int tlLevel) {
        this.tlLevel = tlLevel;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public double getExpression() {
        return expression;
    }

    public void setExpression(double expression) {
        this.expression = expression;
    }

    public boolean isUniqueGene() {
        return uniqueGene;
    }

    public void setUniqueGene(boolean uniqueGene) {
        this.uniqueGene = uniqueGene;
    }
}

