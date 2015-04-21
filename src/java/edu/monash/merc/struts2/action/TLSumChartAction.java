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

package edu.monash.merc.struts2.action;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.List;

/**
 * TLSumChartAction action class which handles the traffic light's pie chart of summary report request
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 10/01/13 11:24 AM
 */
@Scope("prototype")
@Controller("tl.sumChartAction")
public class TLSumChartAction extends BaseAction {

    private String dt;

    private int l4;

    private int l3;

    private int l2;

    private int l1;

    private JFreeChart chart;

    @SuppressWarnings("unchecked")
    public String piechart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Green", l4);
        dataset.setValue("Yellow", l3);
        dataset.setValue("Red", l2);
        dataset.setValue("Black", l1);

        chart = ChartFactory.createPieChart(dt, dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setBackgroundPaint(Color.WHITE);
        //no border
        plot.setOutlineStroke(null);
        //no label
        plot.setLabelGenerator(null);

        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}={2}", NumberFormat.getNumberInstance(), percentFormat));
        //set specific colors  green, yellow, red and black
        Color[] colors = {new Color(0, 140, 0), new Color(254, 172, 0), new Color(226, 0, 40), new Color(0, 0, 0)};
        setColor(plot, dataset, colors);

        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(chart.getTitle().getFont().deriveFont(11.0f));
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    private void setColor(PiePlot plot, DefaultPieDataset dataset, Color[] colors) {
        List<Comparable> keys = dataset.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            plot.setSectionPaint(keys.get(i), colors[i]);
        }
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public int getL4() {
        return l4;
    }

    public void setL4(int l4) {
        this.l4 = l4;
    }

    public int getL3() {
        return l3;
    }

    public void setL3(int l3) {
        this.l3 = l3;
    }

    public int getL2() {
        return l2;
    }

    public void setL2(int l2) {
        this.l2 = l2;
    }

    public int getL1() {
        return l1;
    }

    public void setL1(int l1) {
        this.l1 = l1;
    }

    public JFreeChart getChart() {
        return chart;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }
}
