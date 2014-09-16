package com.sinosoftyingda.fastclaim.defloss.adapter;



import com.sinosoftyingda.fastclaim.defloss.view.SSAccidentView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/****
 * 查勘要点 事故信息字段
 * 
 * @author chenjianfan
 * 
 */
public class AccidentAdapter extends BaseAdapter {

	private SSAccidentView ssAccidentView;

	public AccidentAdapter(SSAccidentView ssAccidentView) {
		this.ssAccidentView = ssAccidentView;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return ssAccidentView.getView();
	}

}
