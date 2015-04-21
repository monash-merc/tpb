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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.List;

/**
 * TPBGene Domain class
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 19/06/12 10:20 AM
 */
@Entity
@Table(name = "tpb_gene")
@org.hibernate.annotations.Table(appliesTo = "tpb_gene",
        indexes = {@Index(name = "idx_tpbg_display_name", columnNames = {"display_name"}),
                @Index(name = "idx_tpbg_chromosome", columnNames = {"chromosome"}),
                @Index(name = "idx_tpbg_ensg_accession", columnNames = {"ensg_accession"}),
                @Index(name = "idx_tpbg_band", columnNames = {"band"}),
                @Index(name = "idx_tpbg_strand", columnNames = {"strand"})
        })
public class TPBGene {
    @Id
    @GeneratedValue(generator = "pk_generator")
    @GenericGenerator(name = "pk_generator", strategy = "org.hibernate.id.enhanced.TableGenerator",
            parameters = {
                    @Parameter(name = "table_name", value = "pk_gen_tab"),
                    @Parameter(name = "value_column_name ", value = "pk_next_val"),
                    @Parameter(name = "segment_column_name", value = "pk_sequence"),
                    @Parameter(name = "segment_value", value = "tpb_gene_id"),
                    @Parameter(name = "increment_size  ", value = "20"),
                    @Parameter(name = "optimizer ", value = "hilo")
            })
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "display_name")
    private String geneName;

    @Basic
    @Column(name = "ensg_accession")
    private String ensgAccession;

    @Basic
    @Column(name = "description", length = 2000)
    private String description;

    @Basic
    @Column(name = "chromosome")
    private String chromosome;

    @Basic
    @Column(name = "start_position")
    private long startPosition;

    @Basic
    @Column(name = "end_position")
    private long endPosition;

    @Basic
    @Column(name = "strand")
    private String strand;

    @Basic
    @Column(name = "band")
    private String band;

    @ManyToMany(targetEntity = Probe.class, mappedBy = "tpbGenes")
    private List<Probe> probes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnsgAccession() {
        return ensgAccession;
    }

    public void setEnsgAccession(String ensgAccession) {
        this.ensgAccession = ensgAccession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public List<Probe> getProbes() {
        return probes;
    }

    public void setProbes(List<Probe> probes) {
        this.probes = probes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TPBGene)) {
            return false;
        }
        TPBGene tpbGene = (TPBGene) o;
        if (id != tpbGene.id) {
            return false;
        }
        if (ensgAccession != null ? !ensgAccession.equals(tpbGene.ensgAccession) : tpbGene.ensgAccession != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (ensgAccession != null ? ensgAccession.hashCode() : 0);
        return result;
    }
}

