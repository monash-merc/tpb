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

package edu.monash.merc.domain;

import edu.monash.merc.common.name.DataType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * TrafficLight Domain class
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 8/03/12 1:05 PM
 */
@Entity
@Table(name = "traffic_light")
public class TrafficLight extends Domain {

    @Id
    @GeneratedValue(generator = "pk_generator")
    @GenericGenerator(name = "pk_generator", strategy = "org.hibernate.id.enhanced.TableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "table_name", value = "pk_gen_tab"),
                    @org.hibernate.annotations.Parameter(name = "value_column_name ", value = "pk_next_val"),
                    @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "pk_sequence"),
                    @org.hibernate.annotations.Parameter(name = "segment_value", value = "traffic_light_id"),
                    @org.hibernate.annotations.Parameter(name = "increment_size  ", value = "20"),
                    @org.hibernate.annotations.Parameter(name = "optimizer ", value = "hilo")
            })
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne(targetEntity = TLGene.class)
    @JoinColumn(name = "tl_gene_id", referencedColumnName = "id")
    private TLGene tlGene;

    @ManyToOne(targetEntity = TPBVersion.class)
    @JoinColumn(name = "version_id", referencedColumnName = "id", nullable = false)
    private TPBVersion tpbVersion;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_color_id", referencedColumnName = "id")
    private TLColor tlPEColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_ms_color_id", referencedColumnName = "id")
    private TLColor tlPEMSColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_anti_color_id", referencedColumnName = "id")
    private TLColor tlPEANTIColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_oth_color_id", referencedColumnName = "id")
    private TLColor tlPEOTHColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_ms_ann_color_id", referencedColumnName = "id")
    private TLColor tlPEMSANNColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_ms_prob_color_id", referencedColumnName = "id")
    private TLColor tlPEMSPROBColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_ms_sam_color_id", referencedColumnName = "id")
    private TLColor tlPEMSSAMColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_anti_ann_color_id", referencedColumnName = "id")
    private TLColor tlPEANTIANNColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_anti_ihc_color_id", referencedColumnName = "id")
    private TLColor tlPEANTIIHCColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_anti_ihc_norm_color_id", referencedColumnName = "id")
    private TLColor tlPEANTIIHCNORMColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "pe_oth_cur_color_id", referencedColumnName = "id")
    private TLColor tlPEOTHCURColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "ptm_color_id", referencedColumnName = "id")
    private TLColor tlPTMColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "ptm_phs_color_id", referencedColumnName = "id")
    private TLColor tlPTMPHSColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "ptm_phs_ser_color_id", referencedColumnName = "id")
    private TLColor tlPTMPHSSERColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "ptm_phs_thr_color_id", referencedColumnName = "id")
    private TLColor tlPTMPHSTHRColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "ptm_phs_tyr_color_id", referencedColumnName = "id")
    private TLColor tlPTMPHSTYRColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "ptm_ace_color_id", referencedColumnName = "id")
    private TLColor tlPTMACEColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "ptm_ace_lys_color_id", referencedColumnName = "id")
    private TLColor tlPTMACELYSColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "ptm_ace_nta_color_id", referencedColumnName = "id")
    private TLColor tlPTMACENTAColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "te_color_id", referencedColumnName = "id")
    private TLColor tlTEColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "te_ma_color_id", referencedColumnName = "id")
    private TLColor tlTEMAColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "te_ma_prop_color_id", referencedColumnName = "id")
    private TLColor tlTEMAPROPColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "te_oth_color_id", referencedColumnName = "id")
    private TLColor tlTEOTHColor;

    @ManyToOne(targetEntity = TLColor.class)
    @JoinColumn(name = "te_oth_cur_color_id", referencedColumnName = "id")
    private TLColor tlTEOTHCURColor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TLGene getTlGene() {
        return tlGene;
    }

    public void setTlGene(TLGene tlGene) {
        this.tlGene = tlGene;
    }

    public TPBVersion getTpbVersion() {
        return tpbVersion;
    }

    public void setTpbVersion(TPBVersion tpbVersion) {
        this.tpbVersion = tpbVersion;
    }

    public TLColor getTlPEColor() {
        return tlPEColor;
    }

    public void setTlPEColor(TLColor peTlColor) {
        this.tlPEColor = peTlColor;
    }

    public TLColor getTlPEMSColor() {
        return tlPEMSColor;
    }

    public void setTlPEMSColor(TLColor peMSTlColor) {
        this.tlPEMSColor = peMSTlColor;
    }

    public TLColor getTlPEANTIColor() {
        return tlPEANTIColor;
    }

    public void setTlPEANTIColor(TLColor tlPEANTIColor) {
        this.tlPEANTIColor = tlPEANTIColor;
    }

    public TLColor getTlPEOTHColor() {
        return tlPEOTHColor;
    }

    public void setTlPEOTHColor(TLColor tlPEOTHColor) {
        this.tlPEOTHColor = tlPEOTHColor;
    }

    public TLColor getTlPEMSANNColor() {
        return tlPEMSANNColor;
    }

    public void setTlPEMSANNColor(TLColor tlPEMSANNColor) {
        this.tlPEMSANNColor = tlPEMSANNColor;
    }

    public TLColor getTlPEMSPROBColor() {
        return tlPEMSPROBColor;
    }

    public void setTlPEMSPROBColor(TLColor tlPEMSPROBColor) {
        this.tlPEMSPROBColor = tlPEMSPROBColor;
    }

    public TLColor getTlPEMSSAMColor() {
        return tlPEMSSAMColor;
    }

    public void setTlPEMSSAMColor(TLColor tlPEMSSAMColor) {
        this.tlPEMSSAMColor = tlPEMSSAMColor;
    }

    public TLColor getTlPEANTIANNColor() {
        return tlPEANTIANNColor;
    }

    public void setTlPEANTIANNColor(TLColor tlPEANTIANNColor) {
        this.tlPEANTIANNColor = tlPEANTIANNColor;
    }

    public TLColor getTlPEANTIIHCColor() {
        return tlPEANTIIHCColor;
    }

    public void setTlPEANTIIHCColor(TLColor tlPEANTIIHCColor) {
        this.tlPEANTIIHCColor = tlPEANTIIHCColor;
    }

    public TLColor getTlPEANTIIHCNORMColor() {
        return tlPEANTIIHCNORMColor;
    }

    public void setTlPEANTIIHCNORMColor(TLColor tlPEANTIIHCNORMColor) {
        this.tlPEANTIIHCNORMColor = tlPEANTIIHCNORMColor;
    }

    public TLColor getTlPEOTHCURColor() {
        return tlPEOTHCURColor;
    }

    public void setTlPEOTHCURColor(TLColor tlPEOTHCURColor) {
        this.tlPEOTHCURColor = tlPEOTHCURColor;
    }

    public TLColor getTlPTMColor() {
        return tlPTMColor;
    }

    public void setTlPTMColor(TLColor tlPTMColor) {
        this.tlPTMColor = tlPTMColor;
    }

    public TLColor getTlPTMPHSColor() {
        return tlPTMPHSColor;
    }

    public void setTlPTMPHSColor(TLColor tlPTMPHSColor) {
        this.tlPTMPHSColor = tlPTMPHSColor;
    }

    public TLColor getTlPTMPHSSERColor() {
        return tlPTMPHSSERColor;
    }

    public void setTlPTMPHSSERColor(TLColor tlPTMPHSSERColor) {
        this.tlPTMPHSSERColor = tlPTMPHSSERColor;
    }

    public TLColor getTlPTMPHSTHRColor() {
        return tlPTMPHSTHRColor;
    }

    public void setTlPTMPHSTHRColor(TLColor tlPTMPHSTHRColor) {
        this.tlPTMPHSTHRColor = tlPTMPHSTHRColor;
    }

    public TLColor getTlPTMPHSTYRColor() {
        return tlPTMPHSTYRColor;
    }

    public void setTlPTMPHSTYRColor(TLColor tlPTMPHSTYRColor) {
        this.tlPTMPHSTYRColor = tlPTMPHSTYRColor;
    }

    public TLColor getTlPTMACEColor() {
        return tlPTMACEColor;
    }

    public void setTlPTMACEColor(TLColor tlPTMACEColor) {
        this.tlPTMACEColor = tlPTMACEColor;
    }

    public TLColor getTlPTMACELYSColor() {
        return tlPTMACELYSColor;
    }

    public void setTlPTMACELYSColor(TLColor tlPTMACELYSColor) {
        this.tlPTMACELYSColor = tlPTMACELYSColor;
    }

    public TLColor getTlPTMACENTAColor() {
        return tlPTMACENTAColor;
    }

    public void setTlPTMACENTAColor(TLColor tlPTMACENTAColor) {
        this.tlPTMACENTAColor = tlPTMACENTAColor;
    }

    public TLColor getTlTEColor() {
        return tlTEColor;
    }

    public void setTlTEColor(TLColor tlTEColor) {
        this.tlTEColor = tlTEColor;
    }

    public TLColor getTlTEMAColor() {
        return tlTEMAColor;
    }

    public void setTlTEMAColor(TLColor tlTEMAColor) {
        this.tlTEMAColor = tlTEMAColor;
    }

    public TLColor getTlTEMAPROPColor() {
        return tlTEMAPROPColor;
    }

    public void setTlTEMAPROPColor(TLColor tlTEMAPROPColor) {
        this.tlTEMAPROPColor = tlTEMAPROPColor;
    }

    public TLColor getTlTEOTHColor() {
        return tlTEOTHColor;
    }

    public void setTlTEOTHColor(TLColor tlTEOTHColor) {
        this.tlTEOTHColor = tlTEOTHColor;
    }

    public TLColor getTlTEOTHCURColor() {
        return tlTEOTHCURColor;
    }

    public void setTlTEOTHCURColor(TLColor tlTEOTHCURColor) {
        this.tlTEOTHCURColor = tlTEOTHCURColor;
    }

    public TLColor getTLColorByDataType(DataType dataType) {
        if (dataType.equals(DataType.PE)) {
            return this.tlPEColor;
        } else if (dataType.equals(DataType.PE_MS)) {
            return this.tlPEMSColor;
        } else if (dataType.equals(DataType.PE_MS_PROB)) {
            return this.tlPEMSPROBColor;
        } else if (dataType.equals(DataType.PE_MS_SAM)) {
            return this.tlPEMSSAMColor;
        } else if (dataType.equals(DataType.PE_MS_ANN)) {
            return this.tlPEMSANNColor;
        } else if (dataType.equals(DataType.PE_ANTI)) {
            return this.tlPEANTIColor;
        } else if (dataType.equals(DataType.PE_ANTI_ANN)) {
            return this.tlPEANTIANNColor;
        } else if (dataType.equals(DataType.PE_ANTI_IHC)) {
            return this.tlPEANTIIHCColor;
        } else if (dataType.equals(DataType.PE_ANTI_IHC_NORM)) {
            return this.tlPEANTIIHCNORMColor;
        } else if (dataType.equals(DataType.PE_OTH)) {
            return this.tlPEOTHColor;
        } else if (dataType.equals(DataType.PE_OTH_CUR)) {
            return this.tlPEOTHCURColor;
        } else if (dataType.equals(DataType.PTM)) {
            return this.tlPTMColor;
        } else if (dataType.equals(DataType.PTM_PHS)) {
            return this.tlPTMPHSColor;
        } else if (dataType.equals(DataType.PTM_PHS_SER)) {
            return this.tlPTMPHSSERColor;
        } else if (dataType.equals(DataType.PTM_PHS_THR)) {
            return this.tlPTMPHSTHRColor;
        } else if (dataType.equals(DataType.PTM_PHS_TYR)) {
            return this.tlPTMPHSTYRColor;
        } else if (dataType.equals(DataType.PTM_ACE)) {
            return this.tlPTMACEColor;
        } else if (dataType.equals(DataType.PTM_ACE_LYS)) {
            return this.tlPTMACELYSColor;
        } else if (dataType.equals(DataType.PTM_ACE_NTA)) {
            return this.tlPTMACENTAColor;
        } else if (dataType.equals(DataType.TE)) {
            return this.tlTEColor;
        } else if (dataType.equals(DataType.TE_MA)) {
            return this.tlTEMAColor;
        } else if (dataType.equals(DataType.TE_MA_PROP)) {
            return this.tlTEMAPROPColor;
        } else if (dataType.equals(DataType.TE_OTH)) {
            return this.tlTEOTHColor;
        } else if (dataType.equals(DataType.TE_OTH_CUR)) {
            return this.tlTEOTHCURColor;
        } else {
            return null;
        }
    }
}
