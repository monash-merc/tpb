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

import edu.monash.merc.common.name.*;
import edu.monash.merc.dto.*;
import edu.monash.merc.dto.gpm.GPMDbBean;
import edu.monash.merc.dto.gpm.GPMDbEntryBean;
import edu.monash.merc.exception.DMFileException;
import edu.monash.merc.exception.DMParserException;
import edu.monash.merc.util.DMUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 11/06/13 11:47 AM
 */
public class GPMDbParser {

    private static String RELEASE_DATE = "date:";

    private static String NOMINAL_MASS = "nominal_mass:";

    private static String SEQUNCE_ASEMBLY = "sequnce_assembly:";

    private static String SEQUENCE_ASEMBLY = "sequence_assembly:";

    private static String SEQUENCE_SOURCE = "sequence_source:";

    private static String MAXIMUM_LOG_E = "maximum_log_e:";

    private static String PROTEIN = "protein:";

    private static String GENE = "gene:";

    private static String TRANSCRIPT = "transcript:";

    private static String CHROMOSOME = "chromosome:";

    private static String CHROM_START = "start:";

    private static String CHROM_END = "end:";

    private static String CHROM_STRAND = "strand:";

    private static String DES_ENSG_ENSP = "desc:";

    private static String MODIFIED_PEPTIDE_OBS = "modified_peptide_obs:";

    private static String POS = "pos:";

    private static String RES = "res:";

    private static String OBS = "obs:";

    private static String COLON_DELIM = ":";

    private static String GPMDB_BASE_QUERY = "http://psyt.thegpm.org/thegpm-cgi/dblist_pep_modmass.pl";

    private static String PARAM_LABEL = "?label=";

    private static String PARAM_MODMASS = "&modmass=";

    private static String PARAM_AND = "@";

    private static String PARAM_MAXEXPECT = "&maxexpect=";

    private static final Logger logger = Logger.getLogger(GPMDbParser.class.getName());

    public GPMDbBean parse(InputStream ins, String encoding, GPMDbType gpmDbType) {
        try {
            String defaultEncoding = "UTF-8";
            if (StringUtils.isNotBlank(encoding)) {
                defaultEncoding = encoding;
            }
            InputStreamReader insReader = new InputStreamReader(ins, Charset.forName(defaultEncoding));
            BufferedReader reader = new BufferedReader(insReader);

            //create a GPMDbBean
            GPMDbBean gpmDbBean = new GPMDbBean();
            //set the GPMDbType
            gpmDbBean.setGpmDbType(gpmDbType);

            List<GPMDbEntryBean> gpmDbEntryBeanList = new ArrayList<GPMDbEntryBean>();
            GPMDbEntryBean gpmDbEntryBean = null;

            String releaseDate = null;
            String nominalMass = null;
            String sequenceAssembly = null;
            String sequenceSource = null;
            String maximumLoge = null;
            String enspAccession = null;
            String ensgAccession = null;
            String enstAccession = null;
            String chromName = null;
            int chromStart = 0;
            int chromEnd = 0;
            String chromStrand = null;
            int modifiedPeptideObs = 0;
            int pos = 0;
            String res = null;
            int obs = 0;
            String line = null;
            int counterIndex = 0;
            while ((line = reader.readLine()) != null) {

                if (StringUtils.isNotBlank(line) && !StringUtils.startsWith(line, "#")) {
                    //start to parse the head of psyt file from gpm
                    if (StringUtils.startsWith(line, RELEASE_DATE)) {
                        String[] dateLineFields = DMUtil.splitByDelims(line, COLON_DELIM);
                        if (dateLineFields.length != 2) {
                            throw new DMFileException("Invalid gpm psyt file,  the release date is not specified");
                        }
                        //There are a total of 26 release dates, just add the first release date as a primary release date
                        if (StringUtils.isBlank(releaseDate)) {
                            releaseDate = dateLineFields[1];
                            gpmDbBean.setReleaseToken(releaseDate);
                        }
                    }

                    if (StringUtils.startsWith(line, NOMINAL_MASS)) {
                        String[] nominalMassLineFields = DMUtil.splitByDelims(line, COLON_DELIM);
                        if (nominalMassLineFields.length != 2) {
                            throw new DMFileException("Invalid gpm psyt file,  the nominal mass number is not specified");
                        }
                        if (StringUtils.isBlank(nominalMass)) {
                            nominalMass = nominalMassLineFields[1];
                            gpmDbBean.setNominalMass(nominalMass);
                        }
                    }

                    if (StringUtils.startsWith(line, SEQUENCE_ASEMBLY) || StringUtils.startsWith(line, SEQUENCE_ASEMBLY)) {
                        String[] sequenceAssemblyLineFields = DMUtil.splitByDelims(line, COLON_DELIM);
                        if (sequenceAssemblyLineFields.length != 2) {
                            throw new DMFileException("Invalid gpm psyt file,  the sequence assembly version is not specified");
                        }
                        if (StringUtils.isBlank(sequenceAssembly)) {
                            sequenceAssembly = sequenceAssemblyLineFields[1];
                            gpmDbBean.setSequenceAssembly(sequenceAssembly);
                        }
                    }

                    if (StringUtils.startsWith(line, SEQUENCE_SOURCE)) {
                        String[] sequenceSourceLineFields = DMUtil.splitByDelims(line, COLON_DELIM);
                        if (sequenceSourceLineFields.length != 2) {
                            throw new DMFileException("Invalid gpm psyt file,  the sequence source number is not specified");
                        }
                        if (StringUtils.isBlank(sequenceSource)) {
                            sequenceSource = sequenceSourceLineFields[1];
                            gpmDbBean.setSequenceSource(sequenceSource);
                        }
                    }

                    if (StringUtils.startsWith(line, MAXIMUM_LOG_E)) {
                        String[] maxLogELineFields = DMUtil.splitByDelims(line, COLON_DELIM);
                        if (maxLogELineFields.length != 2) {
                            throw new DMFileException("Invalid gpm psyt file,  the maximum log(e) number is not specified");
                        }
                        if (StringUtils.isBlank(maximumLoge)) {
                            maximumLoge = maxLogELineFields[1];
                            gpmDbBean.setMaximumLoge(maximumLoge);
                        }
                    }

                    if (StringUtils.startsWith(line, PROTEIN)) {
                        gpmDbEntryBean = new GPMDbEntryBean();
                        //create the primary dbsource bean;
                        DBSourceBean gpmDbSourceBean = new DBSourceBean();
                        //all genes come from the gpm psty file as a datasource
                        if(gpmDbType.equals(GPMDbType.GPMDB_PSYT)){
                            gpmDbSourceBean.setDbName(DbAcType.GPMPSYT.type());
                        }
                        if(gpmDbType.equals(GPMDbType.GPMDB_LYS)){
                            gpmDbSourceBean.setDbName(DbAcType.GPMLYS.type());
                        }
                        if(gpmDbType.equals(GPMDbType.GPMDB_NTA)){
                            gpmDbSourceBean.setDbName(DbAcType.GPMNTA.type());
                        }
                        gpmDbSourceBean.setPrimaryEvidences(true);
                        gpmDbEntryBean.setPrimaryDbSourceBean(gpmDbSourceBean);

                        //parse the protein accession
                        enspAccession = DMUtil.splitStrByDelim(line, COLON_DELIM)[1];
                        if (StringUtils.isBlank(enspAccession)) {
                            throw new DMFileException("The protein accession number not found");
                        } else {
                            //create an identified accession bean
                            AccessionBean identAccessionBean = createAcBean(enspAccession, DbAcType.Protein.type());
                            gpmDbEntryBean.setIdentifiedAccessionBean(identAccessionBean);
                        }
                    }

                    //parse gene accession
                    if (StringUtils.startsWith(line, GENE)) {
                        ensgAccession = DMUtil.splitStrByDelim(line, COLON_DELIM)[1];
                    }

                    //parse transcript accession
                    if (StringUtils.startsWith(line, TRANSCRIPT)) {
                        enstAccession = DMUtil.splitStrByDelim(line, COLON_DELIM)[1];
                    }

                    //chromosome fields
                    if (StringUtils.startsWith(line, CHROMOSOME)) {
                        String[] chromLineFields = DMUtil.splitByDelims(line, "\t", "\r", "\n");
                        for (String chromField : chromLineFields) {
                            if (StringUtils.startsWith(chromField, CHROMOSOME)) {
                                String[] chromNameFileds = DMUtil.splitStrByDelim(chromField, COLON_DELIM);
                                if (chromNameFileds.length == 2) {
                                    chromName = chromNameFileds[1].trim();
                                } else {
                                    chromName = NameType.UNKNOWN.cn();
                                }
                            }
                            if (StringUtils.startsWith(chromField, CHROM_START)) {
                                String[] chromStartFields = DMUtil.splitStrByDelim(chromField, COLON_DELIM);
                                if (chromStartFields.length == 2) {
                                    chromStart = Integer.valueOf(chromStartFields[1].trim());
                                }
                            }
                            if (StringUtils.startsWith(chromField, CHROM_END)) {
                                String[] chromEndFields = DMUtil.splitStrByDelim(chromField, COLON_DELIM);
                                if (chromEndFields.length == 2) {
                                    chromEnd = Integer.valueOf(chromEndFields[1].trim());
                                }
                            }
                            if (StringUtils.startsWith(chromField, CHROM_STRAND)) {
                                String[] chromStrandFields = DMUtil.splitStrByDelim(chromField, COLON_DELIM);
                                if (chromStrandFields.length == 2) {
                                    chromStrand = chromStrandFields[1].trim();
                                }
                            }
                        }
                        //create GeneBean
                        GeneBean geneBean = new GeneBean();
                        geneBean.setEnsgAccession(ensgAccession);
                        geneBean.setChromosome(chromName);
                        geneBean.setStartPosition(chromStart);
                        geneBean.setEndPosition(chromEnd);
                        geneBean.setStrand(chromStrand);
                        gpmDbEntryBean.setGeneBean(geneBean);
                    }

                    //Gene or Protein Desc
                    if (StringUtils.startsWith(line, DES_ENSG_ENSP)) {
                        String[] desLineFields = DMUtil.splitByDelims(line, COLON_DELIM);
                        if (desLineFields.length == 2) {
                            String descValue = desLineFields[1];
                            String[] descValueFields = DMUtil.splitByDelims(descValue, ",", "\t");
                            String desc = "";
                            if (descValueFields.length >= 2) {
                                gpmDbEntryBean.getGeneBean().setDisplayName(descValueFields[0]);
                                for (int i = 1; i < descValueFields.length; i++) {
                                    desc += descValueFields[i];
                                }
                            } else {
                                desc = descValueFields[0];
                            }
                            gpmDbEntryBean.getGeneBean().setDescription(StringUtils.trim(desc));
                        } else {
                            gpmDbEntryBean.getGeneBean().setDisplayName(NameType.UNKNOWN.cn());
                        }

                    }

                    //we add non evidence bean first once we meet the tag : modified_peptide_obs
                    if (StringUtils.startsWith(line, MODIFIED_PEPTIDE_OBS)) {
                        //create dbsource and accession entry bean
                        List<DbSourceAcEntryBean> dbSourceAcEntryBeanList = parseDBSourceAcEntryBeans(enspAccession, ensgAccession, enstAccession);
                        gpmDbEntryBean.setDbSourceAcEntryBeans(dbSourceAcEntryBeanList);

                        if (gpmDbType.equals(GPMDbType.GPMDB_PSYT)) {
                            //create a non phs s evidence bean
                            PTMEvidenceBean nonPhsSEvidenceBean = createNonEvidenceBean(enspAccession, nominalMass, maximumLoge, GPMPTMSubType.PHS_S, gpmDbType);
                            gpmDbEntryBean.setPtmEvidenceBean(nonPhsSEvidenceBean, GPMPTMSubType.NON_PHS_S);
                            //create a non phs t evidence bean
                            PTMEvidenceBean nonPhsTEvidenceBean = createNonEvidenceBean(enspAccession, nominalMass, maximumLoge, GPMPTMSubType.PHS_T, gpmDbType);
                            gpmDbEntryBean.setPtmEvidenceBean(nonPhsTEvidenceBean, GPMPTMSubType.NON_PHS_T);
                            //create a non phs y evidence bean
                            PTMEvidenceBean nonPhsYEvidenceBean = createNonEvidenceBean(enspAccession, nominalMass, maximumLoge, GPMPTMSubType.PHS_Y, gpmDbType);
                            gpmDbEntryBean.setPtmEvidenceBean(nonPhsYEvidenceBean, GPMPTMSubType.NON_PHS_Y);

                        } else if (gpmDbType.equals(GPMDbType.GPMDB_LYS)) {
                            // Create Non LYS evidence bean first
                            PTMEvidenceBean nonLysEvidenceBean = createNonEvidenceBean(enspAccession, nominalMass, maximumLoge, GPMPTMSubType.LYS, gpmDbType);
                            gpmDbEntryBean.setPtmEvidenceBean(nonLysEvidenceBean, GPMPTMSubType.NON_LYS);

                        } else if (gpmDbType.equals(GPMDbType.GPMDB_NTA)) {
                            //create non nta evidence bean first
                            PTMEvidenceBean nonNtaEvidenceBean = createNonEvidenceBean(enspAccession, nominalMass, maximumLoge, GPMPTMSubType.NTA, gpmDbType);
                            gpmDbEntryBean.setPtmEvidenceBean(nonNtaEvidenceBean, GPMPTMSubType.NON_NTA);
                        }

                        gpmDbEntryBeanList.add(gpmDbEntryBean);
                        counterIndex = gpmDbEntryBeanList.size() - 1;
                    }

                    //parse the RES: S, T, Y or Others and POS and OBS
                    if (StringUtils.startsWith(line, POS)) {
                        String[] posLineFields = DMUtil.splitByDelims(line, "\t", "\r", "\n");
                        for (String posLineField : posLineFields) {
                            if (StringUtils.startsWith(posLineField, POS)) {
                                String[] posFileds = DMUtil.splitByDelims(posLineField, COLON_DELIM);
                                if (posFileds.length == 2) {
                                    pos = Integer.valueOf(posFileds[1]);
                                } else {
                                    throw new DMFileException("The position value not found.");
                                }
                            }
                            if (StringUtils.startsWith(posLineField, RES)) {
                                String[] resFileds = DMUtil.splitByDelims(posLineField, COLON_DELIM);
                                if (resFileds.length == 2) {
                                    res = resFileds[1];
                                } else {
                                    throw new DMFileException("The res value not found.");
                                }
                            }
                            if (StringUtils.startsWith(posLineField, OBS)) {
                                String[] obsFileds = DMUtil.splitByDelims(posLineField, COLON_DELIM);
                                if (obsFileds.length == 2) {
                                    obs = Integer.valueOf(obsFileds[1].trim());
                                } else {
                                    throw new DMFileException("The obs value not found.");
                                }
                            }
                        }

                        PTMEvidenceBean ptmEvidenceBean = createPTMEvidenceBean(nominalMass, pos, res, obs, enspAccession, maximumLoge, gpmDbType);

                        //Identify the type
                        GPMPTMSubType ptmSubType = GPMPTMSubType.PHS_S;

                        if (gpmDbType.equals(GPMDbType.GPMDB_PSYT)) {
                            ptmSubType = GPMPTMSubType.fromType(res);
                        } else if (gpmDbType.equals(GPMDbType.GPMDB_LYS)) {
                            ptmSubType = GPMPTMSubType.LYS;
                        } else if (gpmDbType.equals(GPMDbType.GPMDB_NTA)) {
                            ptmSubType = GPMPTMSubType.NTA;
                        }
                        //add the ptm evidence bean
                        gpmDbEntryBeanList.get(counterIndex).setPtmEvidenceBean(ptmEvidenceBean, ptmSubType);
                    }
                }
            }
            logger.info("The total entry size of the " + gpmDbType.type()  + " is :" + gpmDbEntryBeanList.size());
            gpmDbBean.setPgmDbEntryBeans(gpmDbEntryBeanList);
            return gpmDbBean;
        } catch (Exception ex) {
            logger.error(ex);
            throw new DMParserException(ex);
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (Exception e) {
                    //ignore whatever caught.
                }
            }
        }
    }

    private PTMEvidenceBean createPTMEvidenceBean(String nominalMass, int pos, String res, int obs, String enspAccession, String maxexpect, GPMDbType gpmDbType) {

        PTMEvidenceBean ptmEvidenceBean = new PTMEvidenceBean();
        ptmEvidenceBean.setPos(pos);
        ptmEvidenceBean.setEvidenceValue(String.valueOf(obs));

        //evidence hyper link
        String hyperLink = GPMDB_BASE_QUERY + PARAM_LABEL + enspAccession + PARAM_MODMASS + nominalMass + PARAM_AND + res + PARAM_MAXEXPECT + maxexpect;
        //set the evidence hyperlink
        ptmEvidenceBean.setHyperlink(hyperLink);

        TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
        //set the data type level to 3
        tpbDataTypeBean.setLevel(TLLevel.TL3.level());

        //tpb data type
        if (gpmDbType.equals(GPMDbType.GPMDB_PSYT)) {
            if (StringUtils.equals(res, GPMPTMSubType.PHS_S.type())) {
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PTM_PHS_SER.type());
            }

            if (StringUtils.equals(res, GPMPTMSubType.PHS_Y.type())) {
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PTM_PHS_TYR.type());
            }

            if (StringUtils.equals(res, GPMPTMSubType.PHS_T.type())) {
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PTM_PHS_THR.type());
            }
        } else if (gpmDbType.equals(GPMDbType.GPMDB_LYS)) {
            tpbDataTypeBean.setDataType(DataType.PTM_ACE_LYS.type());
        } else if (gpmDbType.equals(GPMDbType.GPMDB_NTA)) {
            tpbDataTypeBean.setDataType(DataType.PTM_ACE_NTA.type());
        }
        //set the tpb data type
        ptmEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);

        int colorLevel = ColorType.BLACK.color();
        //evidence color level
        if (obs >= 50) {
            colorLevel = ColorType.GREEN.color();
        } else if (obs >= 20 && obs < 50) {
            colorLevel = ColorType.YELLOW.color();
        } else if (obs >= 5 && obs < 20) {
            colorLevel = ColorType.RED.color();
        }
        //set the color level
        ptmEvidenceBean.setColorLevel(colorLevel);
        return ptmEvidenceBean;
    }

    private PTMEvidenceBean createNonEvidenceBean(String enspAccession, String nominalMass, String maxexpect, GPMPTMSubType gpmptmSubType, GPMDbType gpmDbType) {
        if (StringUtils.isBlank(enspAccession)) {
            return null;
        }
        PTMEvidenceBean ptmEvidenceBean = new PTMEvidenceBean();
        ptmEvidenceBean.setPos(0);
        ptmEvidenceBean.setEvidenceValue("0");
        ptmEvidenceBean.setColorLevel(ColorType.BLACK.color());

        TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
        //set the data type level to 3
        tpbDataTypeBean.setLevel(TLLevel.TL3.level());

        if (gpmDbType.equals(GPMDbType.GPMDB_PSYT)) {
            if (gpmptmSubType.equals(GPMPTMSubType.PHS_S)) {
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PTM_PHS_SER.type());
                ptmEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);
                String hyperLink = GPMDB_BASE_QUERY + PARAM_LABEL + enspAccession + PARAM_MODMASS + nominalMass + PARAM_AND + gpmptmSubType.type() + PARAM_MAXEXPECT + maxexpect;
                ptmEvidenceBean.setHyperlink(hyperLink);
                return ptmEvidenceBean;
            } else if (gpmptmSubType.equals(GPMPTMSubType.PHS_T)) {
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PTM_PHS_THR.type());
                ptmEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);

                String hyperLink = GPMDB_BASE_QUERY + PARAM_LABEL + enspAccession + PARAM_MODMASS + nominalMass + PARAM_AND + gpmptmSubType.type() + PARAM_MAXEXPECT + maxexpect;
                ptmEvidenceBean.setHyperlink(hyperLink);
                return ptmEvidenceBean;
            } else if (gpmptmSubType.equals(GPMPTMSubType.PHS_Y)) {
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PTM_PHS_TYR.type());
                ptmEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);

                String hyperLink = GPMDB_BASE_QUERY + PARAM_LABEL + enspAccession + PARAM_MODMASS + nominalMass + PARAM_AND + gpmptmSubType.type() + PARAM_MAXEXPECT + maxexpect;
                ptmEvidenceBean.setHyperlink(hyperLink);
                return ptmEvidenceBean;
            }
        } else if (gpmDbType.equals(GPMDbType.GPMDB_LYS)) {
            //set the data type
            tpbDataTypeBean.setDataType(DataType.PTM_ACE_LYS.type());
            ptmEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);
            String hyperLink = GPMDB_BASE_QUERY + PARAM_LABEL + enspAccession + PARAM_MODMASS + nominalMass + PARAM_MAXEXPECT + maxexpect;
            ptmEvidenceBean.setHyperlink(hyperLink);
            return ptmEvidenceBean;

        } else if (gpmDbType.equals(GPMDbType.GPMDB_NTA)) {
            //set the data type
            tpbDataTypeBean.setDataType(DataType.PTM_ACE_NTA.type());
            ptmEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);
            String hyperLink = GPMDB_BASE_QUERY + PARAM_LABEL + enspAccession + PARAM_MODMASS + nominalMass + PARAM_MAXEXPECT + maxexpect;
            ptmEvidenceBean.setHyperlink(hyperLink);
            return ptmEvidenceBean;
        }
        return null;
    }

    private AccessionBean createAcBean(String accession, String accessionType) {
        if (StringUtils.isNotBlank(accession)) {
            AccessionBean accessionBean = new AccessionBean();
            accessionBean.setAccession(accession);
            accessionBean.setAcType(accessionType);
            return accessionBean;
        }
        return null;
    }

    private List<DbSourceAcEntryBean> parseDBSourceAcEntryBeans(String enspAc, String ensgAc, String enstAc) {
        List<DbSourceAcEntryBean> dbSourceAcEntryBeans = new ArrayList<DbSourceAcEntryBean>();
        DbSourceAcEntryBean dbSourceAcEntryBeanEnsp = generateDbSourceAcEntry(enspAc, DbAcType.Protein);
        if (dbSourceAcEntryBeanEnsp != null) {
            dbSourceAcEntryBeans.add(dbSourceAcEntryBeanEnsp);
        }
        DbSourceAcEntryBean dbSourceAcEntryBeanEnsg = generateDbSourceAcEntry(ensgAc, DbAcType.Gene);
        if (dbSourceAcEntryBeanEnsg != null) {
            dbSourceAcEntryBeans.add(dbSourceAcEntryBeanEnsg);
        }
        DbSourceAcEntryBean dbSourceAcEntryBeanEnst = generateDbSourceAcEntry(enstAc, DbAcType.Transcript);
        if (dbSourceAcEntryBeanEnsg != null) {
            dbSourceAcEntryBeans.add(dbSourceAcEntryBeanEnst);
        }
        return dbSourceAcEntryBeans;
    }

    private DbSourceAcEntryBean generateDbSourceAcEntry(String accession, DbAcType dbAcType) {
        if (StringUtils.isNotBlank(accession)) {
            DbSourceAcEntryBean dbSourceAcEntryBean = new DbSourceAcEntryBean();
            AccessionBean accessionBean = new AccessionBean();
            accessionBean.setAccession(accession);
            if (dbAcType.equals(DbAcType.Gene)) {
                accessionBean.setAcType(DbAcType.Gene.type());
            }
            if (dbAcType.equals(DbAcType.Protein)) {
                accessionBean.setAcType(DbAcType.Protein.type());
            }

            if (dbAcType.equals(DbAcType.Transcript)) {
                accessionBean.setAcType(DbAcType.Transcript.type());
            }
            DBSourceBean dbSourceBean = new DBSourceBean();
            dbSourceBean.setDbName(DbAcType.Ensembl.type());

            dbSourceAcEntryBean.setAccessionBean(accessionBean);
            dbSourceAcEntryBean.setDbSourceBean(dbSourceBean);
            return dbSourceAcEntryBean;
        }
        return null;
    }

    public static void main(String[] arg) throws Exception {

        String filename = "./testData/human_all_chr.mod.txt";
        //String filename = "./testData/nta_hs_all_chr.mod.txt";
        //String filename = "./testData/lys_hs_all_chr.mod.txt";


        FileInputStream fileInputStream = new FileInputStream(new File(filename));

        GPMDbParser parser = new GPMDbParser();
        GPMDbBean gpmDbBean = parser.parse(fileInputStream, "utf-8", GPMDbType.GPMDB_LYS);
        List<GPMDbEntryBean> gpmDbEntryBeans = gpmDbBean.getPgmDbEntryBeans();


        for (GPMDbEntryBean gpmDbEntryBean : gpmDbEntryBeans) {
            AccessionBean identifiedAcBean = gpmDbEntryBean.getIdentifiedAccessionBean();
            System.out.println("============== ensp : " + identifiedAcBean.getAccession());
            GeneBean geneBean = gpmDbEntryBean.getGeneBean();

            System.out.println("================= gene symbol : " + geneBean.getDisplayName() + " desc: " + geneBean.getDescription());

            List<DbSourceAcEntryBean> dbSourceAcEntryBeans = gpmDbEntryBean.getDbSourceAcEntryBeans();

            for (DbSourceAcEntryBean dbSourceAcEntryBean : dbSourceAcEntryBeans) {
                AccessionBean accessionBean = dbSourceAcEntryBean.getAccessionBean();
                System.out.println("============ accession: " + accessionBean.getAccession() + " type: " + accessionBean.getAcType());
                DBSourceBean dbSourceBean = dbSourceAcEntryBean.getDbSourceBean();
                System.out.println("============ dbsource: " + dbSourceBean.getDbName());
            }

            List<PTMEvidenceBean> ptmEvidenceBeans = gpmDbEntryBean.getPtmEvidenceBeans();
            System.out.println("========== ptm evidence bean size: " + ptmEvidenceBeans.size());
            for (PTMEvidenceBean ptmEvidenceBean : ptmEvidenceBeans) {
                TPBDataTypeBean tpbDataTypeBean = ptmEvidenceBean.getTpbDataTypeBean();
                System.out.println("============ tpb data type: " + tpbDataTypeBean.getDataType());
                System.out.println("============ pos: " + ptmEvidenceBean.getPos() + " obs: " + ptmEvidenceBean.getEvidenceValue());
                System.out.println("============ color : " + ptmEvidenceBean.getColorLevel());
                System.out.println("============ hyper link: " + ptmEvidenceBean.getHyperlink());
            }
            if (ptmEvidenceBeans.size() == 0) {
                System.out.println("============= No peptides found for " + identifiedAcBean.getAccession());
            }
            System.out.println("\n");
        }

        System.out.println("================= total size: " + gpmDbEntryBeans.size());

    }
}
