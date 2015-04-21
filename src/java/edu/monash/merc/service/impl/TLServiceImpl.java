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

package edu.monash.merc.service.impl;

import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.dao.TrafficLightDAO;
import edu.monash.merc.domain.TPBVersion;
import edu.monash.merc.domain.TrafficLight;
import edu.monash.merc.dto.TLSumReporter;
import edu.monash.merc.dto.tl.TLSearchBean;
import edu.monash.merc.service.TLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TLServiceImpl implements the TLService interface which provides the service operations for TrafficLight.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 28/05/12 3:26 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Service
@Transactional
public class TLServiceImpl implements TLService {

    @Autowired
    private TrafficLightDAO trafficLightDao;

    /**
     * set a TrafficLightDAO
     *
     * @param trafficLightDao a TrafficLightDAO object
     */
    public void setTrafficLightDao(TrafficLightDAO trafficLightDao) {
        this.trafficLightDao = trafficLightDao;
    }

    /**
     * {@inheritDoc}
     */
    public void saveTrafficLight(TrafficLight trafficLight) {
        this.trafficLightDao.add(trafficLight);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeTrafficLight(TrafficLight trafficLight) {
        this.trafficLightDao.merge(trafficLight);
    }

    /**
     * {@inheritDoc}
     */
    public void updateTrafficLight(TrafficLight trafficLight) {
        this.trafficLightDao.update(trafficLight);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTrafficLight(TrafficLight trafficLight) {
        this.trafficLightDao.remove(trafficLight);
    }

    /**
     * {@inheritDoc}
     */
    public TrafficLight getTrafficLightById(long id) {
        return this.trafficLightDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTLById(long id) {
        this.trafficLightDao.deleteTLById(id);
    }

    /**
     * {@inheritDoc}
     */
    public TrafficLight getTLByChromEnsemblAcVersionToken(ChromType chromType, String ensgAc, long versionId, int trackToken) {
        return this.trafficLightDao.getTLByChromEnsemblAcVersionToken(chromType, ensgAc, versionId, trackToken);
    }

    /**
     * {@inheritDoc}
     */
    public Pagination<TrafficLight> getVersionTrafficLight(ChromType chromType, int trackToken, long versionId, int startPage, int recordsPerPage, OrderBy[] orderConds) {
        return this.trafficLightDao.getVersionTrafficLight(chromType, trackToken, versionId, startPage, recordsPerPage, orderConds);
    }

    /**
     * {@inheritDoc}
     */
    public Pagination<TrafficLight> getVersionTrafficLight(TLSearchBean tlSearchBean, int startPage, int recordsPerPage, OrderBy[] orderConds) {
        return this.trafficLightDao.getVersionTrafficLight(tlSearchBean, startPage, recordsPerPage, orderConds);
    }

    /**
     * {@inheritDoc}
     */
    public TLSumReporter getTLSumReporter(TLSearchBean tlSearchBean) {
        return this.trafficLightDao.getTLSumReporter(tlSearchBean);
    }

    /**
     * {@inheritDoc}
     */
    public List<TrafficLight> getTrafficLightsByChromAndTPBVersion(ChromType chromType, TPBVersion tpbVersion) {
        return this.trafficLightDao.getTrafficLightsByChromAndTPBVersion(chromType, tpbVersion);
    }
}
