package com.sinosoftyingda.fastclaim.work.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.model.CertainLoss;
/**
 * 定损 迭代器
 * @author haoyun 20130304
 *
 */
public class CertainLossAdapter extends BaseAdapter {
	private List<CertainLoss> gCertainLossListValues;
	private View gView;
	private Context gContext;
	public CertainLossAdapter(Context context,List<CertainLoss> certainLossListValues)
	{
		gContext=context;
		gCertainLossListValues=certainLossListValues;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gCertainLossListValues.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gCertainLossListValues.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int postion, View arg1, ViewGroup arg2) {
		LayoutInflater inflater =LayoutInflater.from(gContext);
		gView=inflater.inflate(R.layout.procedure_query_certainloss, null);
		TextView itemName,certainUserCode,certainAccepTime,certainHandleTime,certainAmount;
		itemName=(TextView)gView.findViewById(R.id.pqc_tv_item_name);
		certainUserCode=(TextView)gView.findViewById(R.id.pqc_tv_certain_user_code);
		certainAccepTime=(TextView)gView.findViewById(R.id.pqc_tv_certain_accep_time);
		certainHandleTime=(TextView)gView.findViewById(R.id.pqc_tv_certain_handle_time);
		certainAmount=(TextView)gView.findViewById(R.id.pqc_tv_certain_amount);
		
		itemName.setText(gCertainLossListValues.get(postion).getItemName());
		certainUserCode.setText(gCertainLossListValues.get(postion).getCertinUserCode());
		certainAccepTime.setText(gCertainLossListValues.get(postion).getCertinAcceptTime());
		certainHandleTime.setText(gCertainLossListValues.get(postion).getCertinHandleTime());
		certainAmount.setText(gCertainLossListValues.get(postion).getCertainAmount());
//		if(DataConfig.defLossInfoQueryData!=null&&DataConfig.defLossInfoQueryData.getRegist()!=null){
//			VerifyLossSubmitRequest verifyLossSubmitRequest = DeflossSynchroAccess.find(DataConfig.defLossInfoQueryData.getRegist().getRegistNo(), DataConfig.defLossInfoQueryData.getRegist().getLossNo());
//			if(verifyLossSubmitRequest!=null){
//				certainAmount.setText(verifyLossSubmitRequest.getSumcertainloss());
//			}else{
//				certainAmount.setText("0.0");
//			}
//		}else{
//			certainAmount.setText("0.0");
//		}
		
		return gView;
	}

}
