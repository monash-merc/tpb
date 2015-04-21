<div class="simple_con_div">
    <div class="simple_conditions">
        <@s.form action="trafficlight.jspx" namespace="/tl" method="post">
            Chromosome: <@s.select name ="tlSearchBean.selectedChromType" headerKey="tlSearchBean.selectedChromType" list ="chromTypes" cssClass="select_norm"/> &nbsp;&nbsp;
            &nbsp;Data Sources : <@s.checkbox name="tlSearchBean.nxDbSelected" cssClass="check_box_norm" />neXtProt &nbsp;
            &nbsp; <@s.checkbox name="tlSearchBean.gpmDbSelected" cssClass="check_box_norm" />GPM &nbsp;
            &nbsp; <@s.checkbox name="tlSearchBean.hpaDbSelected" cssClass="check_box_norm" />HPA &nbsp;&nbsp;
            &nbsp; <@s.checkbox name="tlSearchBean.bcDbSelected" cssClass="check_box_norm" />Barcode &nbsp; &nbsp;
            <input type="submit" name="explore" value="Explore" class="input_button">
        </@s.form>
    </div>
</div>
<div style="clear: both;"></div>