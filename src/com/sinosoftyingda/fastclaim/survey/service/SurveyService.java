package com.sinosoftyingda.fastclaim.survey.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinosoftyingda.fastclaim.common.model.SpinnerBean;
import com.sinosoftyingda.fastclaim.survey.bean.SurveryBean;
import com.sinosoftyingda.fastclaim.survey.model.SPointsViewParam;
import com.sinosoftyingda.fastclaim.survey.page.three.model.GuaranteelipBeam;
import com.sinosoftyingda.fastclaim.survey.page.three.model.HistoryClaimBean;

public class SurveyService {

	/*****
	 * 获取查勘报案信息
	 * 
	 * @return
	 */
	public static List<SurveryBean> getSurveryBeansData() {
		List<SurveryBean> surveryBeans = new ArrayList<SurveryBean>();

		SurveryBean surveryBean = new SurveryBean();
		surveryBean.setReportNo("R23131344312312341");
		surveryBean.setAssured("周小云");
		surveryBean.setCarNo("京A2290");
		surveryBean.setFactory("GR381");
		surveryBean.setReason("2013-01-12 10:10:29");
		surveryBean.setTime("2013-01-12 10:10:29");
		surveryBean.setAddress("北京市朝阳区某某大街122号2红路灯路口处");
		surveryBean.setDriver("周小云");
		surveryBean.setStarttime("2012-01-12 10:10:29");
		surveryBean.setClaimNumber("近10天3次出险");

		surveryBeans.add(surveryBean);
		return surveryBeans;
	}

	/****
	 * 获取查勘信息中 查勘记录的数据
	 * 
	 * @return
	 */
	public static List<SPointsViewParam> getPointsViewParamsData() {

		List<SPointsViewParam> sPointsViewParams = new ArrayList<SPointsViewParam>();

		SPointsViewParam param = new SPointsViewParam();
		param.setIsBaijing(true);

		sPointsViewParams.add(param);
		return sPointsViewParams;
	}

	/****
	 * 获取基本信息的保单数据
	 * 
	 * @return
	 */
	public static List<GuaranteelipBeam> getGuaranteelipBeamsData() {

		List<GuaranteelipBeam> beams = new ArrayList<GuaranteelipBeam>();
		GuaranteelipBeam beam = new GuaranteelipBeam();
		beam.setAssured("张三");
		beam.setCarNo("京BF9368");
	
		beams.add(beam);
		return beams;

	}

	/****
	 * 获取查勘历史赔案的数据
	 * 
	 * @return
	 */
	public static Map<String, Object> getHistoryClaimBeansData() {

		Map<String, Object> data = new HashMap<String, Object>();

		List<HistoryClaimBean> beans = new ArrayList<HistoryClaimBean>();
		HistoryClaimBean historyClaimBean = new HistoryClaimBean();
		historyClaimBean.setClaimNo("R088981224344324");
		historyClaimBean.setGoTime("2012-12-12");
		historyClaimBean.setReportTime("2012-12-12");
		historyClaimBean.setGoAddress("上海市松江区玉树路汇路交叉口");
		historyClaimBean.setClaimState("未结案");
		beans.add(historyClaimBean);
		HistoryClaimBean historyClaimBean1 = new HistoryClaimBean();
		historyClaimBean1.setClaimNo("R198122434443131");
		historyClaimBean1.setGoTime("2012-12-18");
		historyClaimBean1.setReportTime("2012-12-18");
		historyClaimBean1.setGoAddress("上海市松江区玉树路汇路交叉口");
		historyClaimBean1.setClaimState("已结案");
		beans.add(historyClaimBean1);
		data.put("historyclaimbean", beans);
		return data;

	}

	/****
	 * 获取查勘历史赔案的数据
	 * 
	 * @return
	 */
	public static List<HistoryClaimBean> getHistoryClaimBeansData1() {
		List<HistoryClaimBean> beans = new ArrayList<HistoryClaimBean>();
		HistoryClaimBean historyClaimBean = new HistoryClaimBean();
		historyClaimBean.setClaimNo("R088981224344324");
		historyClaimBean.setGoTime("2012-12-12");
		historyClaimBean.setReportTime("2012-12-12");
		historyClaimBean.setGoAddress("上海市松江区玉树路汇路交叉口");
		historyClaimBean.setClaimState("未结案");
		beans.add(historyClaimBean);
		HistoryClaimBean historyClaimBean1 = new HistoryClaimBean();
		historyClaimBean1.setClaimNo("R198122434443131");
		historyClaimBean1.setGoTime("2012-12-18");
		historyClaimBean1.setReportTime("2012-12-18");
		historyClaimBean1.setGoAddress("上海市松江区玉树路汇路交叉口");
		historyClaimBean1.setClaimState("已结案");
		beans.add(historyClaimBean1);

		return beans;
	}

	/****
	 * 下拉列表的数据
	 * 
	 * @return
	 */
	public static List<SpinnerBean> getSpinnerData() {
		List<SpinnerBean> spinnerBeans = new ArrayList<SpinnerBean>();
		SpinnerBean bean = new SpinnerBean();
		bean.setValue("是");
		SpinnerBean bean1 = new SpinnerBean();
		bean1.setValue("否");
		SpinnerBean bean2 = new SpinnerBean();
		bean2.setValue("待定");
		spinnerBeans.add(bean);
		spinnerBeans.add(bean1);
		spinnerBeans.add(bean2);
		return spinnerBeans;
	}
}
