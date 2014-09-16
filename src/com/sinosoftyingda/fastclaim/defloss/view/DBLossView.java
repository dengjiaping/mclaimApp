package com.sinosoftyingda.fastclaim.defloss.view;

import java.util.ArrayList;
import java.util.List;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.model.CarLossInfo;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.common.utils.ScrollListViewUtils;
import com.sinosoftyingda.fastclaim.defloss.adapter.DBLossThreeCarAdapter;
import com.sinosoftyingda.fastclaim.defloss.bean.ThreeCarBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/****
 * 定损基本信息中 案件涉损字段界面
 * 
 * @author chenjianfan
 * 
 */
public class DBLossView {
	private Context context;
	private LayoutInflater inflater;
	private View gView;
	// 标的车车牌
	private TextView tvInsureCarCarNO;
	private TextView tvInsureCarDeflossName;
	private TextView tvInsureCarDeflossPrice;
	// 三者车
	private ListView lvThreeCar;
	private DBLossThreeCarAdapter lossThreeCarAdapter;
	private List<ThreeCarBean> threeCarBeans;
	private ThreeCarBean threeCarBean;
	private int i = 1;

	public DBLossView(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	public View gView() {
		return gView;
	}

	private void init() {
		gView = inflater.inflate(R.layout.defloss_basic_loss, null);
		findView();
		setView();
	}

	private void findView() {
		tvInsureCarCarNO = (TextView) gView.findViewById(R.id.defloss_basic_loss_report_no);
		tvInsureCarDeflossName = (TextView) gView.findViewById(R.id.defloss_basic_loss_deflossname);
		tvInsureCarDeflossPrice = (TextView) gView.findViewById(R.id.defloss_basic_loss_deflossprice);
		lvThreeCar = (ListView) gView.findViewById(R.id.defloss_basic_loss_lv_threecar);
	}

	private void setView() {

		// 涉损车辆
		threeCarBeans = new ArrayList<ThreeCarBean>();
		if (DataConfig.defLossInfoQueryData != null) {

			if (DataConfig.defLossInfoQueryData.getCarLossInfos() != null && !DataConfig.defLossInfoQueryData.getCarLossInfos().isEmpty()) {
				for (CarLossInfo carlossinfo : DataConfig.defLossInfoQueryData.getCarLossInfos()) {
					if ("1".equals(carlossinfo.getInsureCarFlag())) {
						// 标的车
						tvInsureCarCarNO.setText(carlossinfo.getLicenseNo());
						tvInsureCarDeflossName.setText(carlossinfo.getDefineLossPerson());
						tvInsureCarDeflossPrice.setText(carlossinfo.getDefineLossAmount());

					}
					if ("0".equals(carlossinfo.getInsureCarFlag())) {
						threeCarBean = new ThreeCarBean();
						threeCarBean.setThreeCarTitle("三者车" + (i++));
						threeCarBean.setThreeCarCarNo(carlossinfo.getLicenseNo());
						threeCarBean.setDeflossName(carlossinfo.getDefineLossPerson());
						threeCarBean.setDeflossPrice(carlossinfo.getDefineLossAmount());
						threeCarBeans.add(threeCarBean);
					} else {
						// 服务端数据为空
						Log.i(DBLossView.class.getSimpleName(), "服务端数据为空");
					}

				}

			}

		}
		if (lossThreeCarAdapter == null) {
			lossThreeCarAdapter = new DBLossThreeCarAdapter(context, threeCarBeans);
			lvThreeCar.setAdapter(lossThreeCarAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvThreeCar);
		} else {
			lvThreeCar.setAdapter(lossThreeCarAdapter);
			new ScrollListViewUtils().setListViewHeightBasedOnChildren(lvThreeCar);
		}

	}
}
