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

package edu.monash.merc.wsclient.biomart;

import edu.monash.merc.domain.TPBGene;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * CSVGeneCreator class which provides the function to create a gene from the CSV file
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 19/06/12 1:18 PM
 */
public class CSVGeneCreator {
    /**
     * a list of CSVColumn objects.
     */
    private List<CSVColumn> columns = new ArrayList<CSVColumn>();

    /**
     * get a list of CSVColumn objects
     *
     * @return
     */
    public List<CSVColumn> getColumns() {
        return columns;
    }

    /**
     * set a list of CSVColumn objects
     *
     * @param columns
     */
    public void setColumns(List<CSVColumn> columns) {
        this.columns = columns;
    }

    /**
     * Create a gene method based on a list of CSVColumn objects.
     *
     * @return a TPBGene object
     */
    public TPBGene createGene() {
        TPBGene tpbGene = new TPBGene();
        for (CSVColumn csvColumn : columns) {
            String columnName = csvColumn.getColumnName();
            String columnValue = csvColumn.getColumnValue();
            if (StringUtils.equalsIgnoreCase(columnName, GeneConsts.ENSG_ACCESSION)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    tpbGene.setEnsgAccession(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneConsts.DESCRIPTION)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    tpbGene.setDescription(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneConsts.CHROMOSOME)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    tpbGene.setChromosome(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneConsts.START_POSITION)) {
                tpbGene.setStartPosition(Long.valueOf(columnValue).longValue());
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneConsts.END_POSITION)) {
                tpbGene.setEndPosition(Long.valueOf(columnValue).longValue());
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneConsts.STRAND)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    tpbGene.setStrand(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneConsts.BAND)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    tpbGene.setBand(columnValue);
                }
            }
            if (StringUtils.equalsIgnoreCase(columnName, GeneConsts.GENE_NAME)) {
                if (StringUtils.isNotBlank(columnValue) && !StringUtils.equals("\t", columnValue)) {
                    tpbGene.setGeneName(columnValue);
                }
            }
        }
        return tpbGene;
    }
}
