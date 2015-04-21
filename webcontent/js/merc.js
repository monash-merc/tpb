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

/**remove the message pane*/
$(document).ready(function () {
    $(".message_pane .delete").click(function () {
        $(".success_act_msg").remove();
    });
});


function loadRssFeeds (feedDiv) {
    if (feedDiv != null) {
        var numberItems = 5;
        $.ajax({
            type:'GET',
            url:'rss/rss',
            dataType:'xml',
            error:function (xhr) {
                var html = rssFeedFailure(xhr);
                feedDiv.html(html);
            },
            success:function (xml) {
                var html = parseRSS(xml, numberItems);
                feedDiv.attr('class', "rssFeed");
                feedDiv.html(html);
            }
        });
    }
}

function rssFeedFailure(xhr) {
    var html = '';
    html += "<div class='rssHeader'>";
    html += "TPB Traffic Light RSS Feeds <img class='rssImg' src='images/rss.png' />";
    html += "</div>";
    html += "<div class='rssBody'>";
    html += "<div class='rssError'> Unable to load the RSS Feed.</div>";
    html += "</div>";
    html += "<div style='clear: both;'/>";
    return html;
}

function parseRSS(rss, num) {
    var html = '';
    var cTitle = $('title', rss).eq(0).text();
    var cLink = $('link', rss).eq(0).text();
    var cDesc = $('description', rss).eq(0).text();

    html += "<div class='rssHeader'>";
    html += "<a href='" + cLink + "' target='_blank'>" + cTitle + " <img class='rssImg' src='images/rss.png' /></a>";
    html += "</div>";

    html += '<div class="rssBody"><ul>';
    var i = 0;
    $('item', rss).each(function (index) {
        if (index < num) {
            var iTitle = $(this).find('title').eq(0).text();
            var iLink = $(this).find('link').eq(0).text();
            var iDesc = $(this).find('description').eq(0).text();
            var iPubDate = $(this).find('pubDate').eq(0).text();
            html += "<li class='rssRow'>";
            html += "<h4>";
            html += "<a href='" + iLink + "' target='_self'>" + iTitle + "</a>";
            html += "</h4>";
            html += "<p>" + iDesc + "</p>";
            html += "<div>" + iPubDate + "</div>";
            html += "</li>";
        }
    });

    html += "</u></div>";
    html += "<div style='clear: both;'/>";
    return html;
}
