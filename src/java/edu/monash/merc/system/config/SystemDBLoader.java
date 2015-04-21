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

package edu.monash.merc.system.config;

import edu.monash.merc.domain.TLColor;
import edu.monash.merc.domain.TPBDataType;
import edu.monash.merc.dto.DbInitEntry;
import edu.monash.merc.dto.TLColorDbBean;
import edu.monash.merc.dto.TPBDataTypeDbBean;
import edu.monash.merc.exception.DMConfigException;
import edu.monash.merc.service.DMSystemService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * SystemDBLoader class initializes the database for the TPB data types.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 19/04/12 1:20 PM
 */
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Component
public class SystemDBLoader implements ApplicationListener {

    @Autowired
    @Qualifier("dbXmlConfigurator")
    private SystemDBXmlConfigurator xmlConfigurator;

    @Autowired
    private DMSystemService dmSystemService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setXmlConfigurator(SystemDBXmlConfigurator xmlConfigurator) {
        this.xmlConfigurator = xmlConfigurator;
    }

    public void setDmSystemService(DMSystemService dmSystemService) {
        this.dmSystemService = dmSystemService;
    }

    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        logger.info("Start to initialize the database.");
        loadDbFromXml();
        logger.info("Finished to initialize the database.");
    }

    public void loadDbFromXml() {
        //load the db from xml file
        DbInitEntry dbInitEntry = this.xmlConfigurator.loadDbFromXml();

        if (dbInitEntry == null) {
            throw new DMConfigException("Invalid db xml file");
        }

        //sort the data type tree
        List<TPBDataType> firstLevelDatTypes = sortTypeTree(dbInitEntry);

        //start to persist the data type, start fro the top level and go through it's sub level.
        for (TPBDataType firstDataType : firstLevelDatTypes) {
            //try to find the previous persist data type from database
            TPBDataType found1DataType = this.dmSystemService.getTPBDataTypeByTypeName(firstDataType.getDataType());
            if (found1DataType != null) {
                firstDataType.setId(found1DataType.getId());
            }

            //get sub data types(level2)
            List<TPBDataType> secondLevelDataTypes = firstDataType.getSubDataTypes();
            for (TPBDataType secondDataType : secondLevelDataTypes) {
                //try to find the previous persist data type from database
                TPBDataType found2DataType = this.dmSystemService.getTPBDataTypeByTypeName(secondDataType.getDataType());
                if (found2DataType != null) {
                    secondDataType.setId(found2DataType.getId());
                }
                //get sub data types (level3)
                List<TPBDataType> thirdLevelDataTypes = secondDataType.getSubDataTypes();

                for (TPBDataType thirdDataType : thirdLevelDataTypes) {
                    //try to find the previous persist data type from database
                    TPBDataType found3DataType = this.dmSystemService.getTPBDataTypeByTypeName(thirdDataType.getDataType());
                    if (found3DataType != null) {
                        thirdDataType.setId(found3DataType.getId());
                    }
                    //get sub data types (level4)
                    List<TPBDataType> fourthLevelDataTypes = thirdDataType.getSubDataTypes();
                    for (TPBDataType fourthDataType : fourthLevelDataTypes) {
                        //try to find the previous persist data type from database
                        TPBDataType found4DataType = this.dmSystemService.getTPBDataTypeByTypeName(fourthDataType.getDataType());
                        if (found4DataType != null) {
                            fourthDataType.setId(found4DataType.getId());
                        }
                        //set the parent data type for fourth level
                        fourthDataType.setParentDataType(thirdDataType);
                    }
                    //set the parent data type for third level
                    thirdDataType.setParentDataType(secondDataType);
                }
                //set the parent data type for second level
                secondDataType.setParentDataType(firstDataType);
            }
            //save or update the data type
            this.dmSystemService.mergeTPBDataType(firstDataType);
        }

        // persist the Traffic Lights Colors
        List<TLColorDbBean> tlColorDbBeanList = dbInitEntry.getColorDbBeans();
        for (TLColorDbBean colorDbBean : tlColorDbBeanList) {
            String color = colorDbBean.getTlColor();
            int colorLevel = colorDbBean.getColorLevel();
            TLColor tlColor = this.dmSystemService.getTLColorByColor(color);
            if (tlColor == null) {
                tlColor = new TLColor();
                tlColor.setTlColor(color);
                tlColor.setColorLevel(colorLevel);
                this.dmSystemService.saveTLColor(tlColor);
            }
        }
    }

    private List<TPBDataType> sortTypeTree(DbInitEntry dbInitEntry) {

        //all top level data types
        List<TPBDataType> topLevelDataTypes = new ArrayList<TPBDataType>();

        List<TPBDataTypeDbBean> firstDataTypesDbBeans = dbInitEntry.getFirstLevelDataTypeDbBeans();

        List<TPBDataTypeDbBean> secondDataTypesDbBeans = dbInitEntry.getSecondLevelDataTypeDbBeans();

        List<TPBDataTypeDbBean> thirdDataTypesDbBeans = dbInitEntry.getThirdLevelDataTypeDbBeans();

        List<TPBDataTypeDbBean> fourthDataTypesDbBeans = dbInitEntry.getFourthLevelDataTypeDbBeans();

        //to build the data type tree, here we start from the first level
        //for each first level of the data type
        for (TPBDataTypeDbBean fDtDbBean : firstDataTypesDbBeans) {
            TPBDataType fTpbDataType = new TPBDataType();
            String fTypeName = fDtDbBean.getDisplayName();
            String fDataType = fDtDbBean.getDataType();
            int fLevel = fDtDbBean.getTlLevel();
            fTpbDataType.setDisplayName(fTypeName);
            fTpbDataType.setDataType(fDataType);
            fTpbDataType.setTlLevel(fLevel);

            //for each second level data type, we try to build second level tree
            for (TPBDataTypeDbBean sDtDbBean : secondDataTypesDbBeans) {

                String sDataType = sDtDbBean.getDataType();
                String sTypeName = sDtDbBean.getDisplayName();
                int sLevel = sDtDbBean.getTlLevel();
                String sParentId = sDtDbBean.getParentRefId();
                //found the second level children
                if (sParentId.equalsIgnoreCase(fDataType)) {
                    //create a new TPBDataType
                    TPBDataType sTpbDataType = new TPBDataType();
                    sTpbDataType.setDisplayName(sTypeName);
                    sTpbDataType.setDataType(sDataType);
                    sTpbDataType.setTlLevel(sLevel);
                    //for each third level data type, we try to group them
                    for (TPBDataTypeDbBean thDtDbBean : thirdDataTypesDbBeans) {
                        String thDataType = thDtDbBean.getDataType();
                        String thTypeName = thDtDbBean.getDisplayName();
                        int thLevel = thDtDbBean.getTlLevel();

                        String thParentRefId = thDtDbBean.getParentRefId();
                        if (thParentRefId.equalsIgnoreCase(sDataType)) {
                            //create a new TPBDataType
                            TPBDataType thTpbDataType = new TPBDataType();
                            thTpbDataType.setDisplayName(thTypeName);
                            thTpbDataType.setDataType(thDataType);
                            thTpbDataType.setTlLevel(thLevel);
                            //for each fourth level data type, we try to group them
                            for (TPBDataTypeDbBean foDtDbBean : fourthDataTypesDbBeans) {
                                String foDataType = foDtDbBean.getDataType();
                                String foTypeName = foDtDbBean.getDisplayName();
                                int foLevel = foDtDbBean.getTlLevel();
                                String foParentRefId = foDtDbBean.getParentRefId();

                                if (foParentRefId.equalsIgnoreCase(thDataType)) {
                                    //create a new TPBDataType
                                    TPBDataType foTpbDataType = new TPBDataType();
                                    foTpbDataType.setDisplayName(foTypeName);
                                    foTpbDataType.setDataType(foDataType);
                                    foTpbDataType.setTlLevel(foLevel);
                                    //group it as fourth sub-level data types for the third sub-level data types
                                    thTpbDataType.getSubDataTypes().add(foTpbDataType);
                                }
                            }
                            //group it into third sub-level data types for the second sub-level data types
                            sTpbDataType.getSubDataTypes().add(thTpbDataType);
                        }
                    }
                    //group it into second sub-level data types for the first sub-level data types
                    fTpbDataType.getSubDataTypes().add(sTpbDataType);
                }
            }
            //add into top level
            topLevelDataTypes.add(fTpbDataType);
        }
        return topLevelDataTypes;
    }
}
