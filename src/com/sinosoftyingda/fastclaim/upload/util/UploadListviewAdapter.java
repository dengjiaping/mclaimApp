package com.sinosoftyingda.fastclaim.upload.util;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.db.dao.TblPicAddress;
import com.sinosoftyingda.fastclaim.common.db.dao.TblUploadInfo;
import com.sinosoftyingda.fastclaim.common.db.dao.UploadInfo;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager;
import com.sinosoftyingda.fastclaim.common.utils.PromptManager.ShowDialogPositiveButton;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.upload.config.HomeConfig;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;

/**
 * 文件上传适配器
 * 
 * @author haoyun
 */
public class UploadListviewAdapter extends BaseAdapter {

	private LayoutInflater		gInflater;
	private Context				gContext;
	private List<UploadInfo>	gData;
	private ListView 			gLvUploadlist;

	public UploadListviewAdapter(Context gContext, List<UploadInfo> gData, ListView gLvUploadlist) {
		super();
		gInflater = (LayoutInflater) gContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.gContext = gContext;
		this.gData = gData;
		this.gLvUploadlist = gLvUploadlist;

		for (int i = 0; i < gData.size(); i++) {
			FTPBp breakPointResume = new FTPBp(gData.get(i), gContext);
			HomeConfig.ftpBPRList.add(breakPointResume);
		}
	}

	@Override
	public int getCount() {
		return gData.size();
	}

	@Override
	public Object getItem(int position) {
		return gData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Item 组件
	 * 
	 * @author DengGuang
	 */
	class UploadListitem {

		TextView	tvPolicyNo;

		ProgressBar	proBar;
		TextView	tvParcent;

		Button		btnAction;
		Button		btnDel;
		int			position;
		String		fileUrl;
	}

	@Override
	public View getView(int position, View cview, ViewGroup viewGroup) {
		cview = gInflater.inflate(R.layout.upload_content, null);

		UploadInfo info = gData.get(position);
		final UploadListitem listitem = new UploadListitem();

		listitem.tvPolicyNo = (TextView) cview.findViewById(R.id.upload_tv_policyno);
		listitem.proBar = (ProgressBar) cview.findViewById(R.id.upload_pro_bar);
		listitem.tvParcent = (TextView) cview.findViewById(R.id.upload_tv_parcent);
		listitem.btnAction = (Button) cview.findViewById(R.id.upload_btn_action);
		listitem.btnDel = (Button) cview.findViewById(R.id.upload_btn_del);
		listitem.proBar.setProgress(Integer.parseInt(info.getParcent()));
		listitem.tvPolicyNo.setText(info.getPolicyNo());
		listitem.tvParcent.setText(info.getParcent() + "%");
		listitem.fileUrl = info.getFileUrl();
		listitem.position = position;

		if (position == 0 && HomeConfig.ftpBPRList.size() > 0) {
			HomeConfig.ftpBPRList.get(listitem.position)
					.init(gContext.getString(R.string.remote_path),
							listitem.fileUrl, listitem.proBar, listitem.tvParcent, listitem.btnAction, info.getId(), gData);
			HomeConfig.ftpBPRList.get(listitem.position).isStart = true;
			HomeConfig.isStart = true;
			listitem.proBar.getProgress();

			new Thread() {
				@Override
				public void run() {
					HomeConfig.ftpBPRList.get(listitem.position).run();
				}
			}.start();

			listitem.btnAction.setBackgroundResource(R.drawable.update_stop);
		}

		listitem.btnAction.setOnClickListener(new ActionOnclick(listitem, info));
		listitem.btnDel.setOnClickListener(new DelOnclick(listitem, info));
		return cview;
	}
	
	/**
	 * 删除单个上传队列操作
	 * 
	 * @author DengGuang
	 */
	class DelOnclick implements OnClickListener {

		private UploadListitem	listitem;
		private String			state;
		private UploadInfo		info;

		DelOnclick(UploadListitem listitem, UploadInfo info) {
			super();
			this.listitem = listitem;
			this.info = info;
		}
		
		@Override
		public void onClick(View v) {
			if (listitem.position != 0) { 
				Toast.makeText(gContext, "请先删除第一个上传队列！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			delUploadItem(info);
		}
		
	}

	/**
	 * 上传操作
	 * 
	 * @author DengGuang
	 */
	class ActionOnclick implements OnClickListener {

		private UploadListitem	listitem;
		private String			state;
		private UploadInfo		gInfo;

		ActionOnclick(UploadListitem listitem, UploadInfo info) {
			super();
			this.listitem = listitem;
			this.gInfo = info;
		}

		@Override
		public void onClick(View v) { 
			if (listitem.position != 0) {
				Toast.makeText(gContext, "请从第一个案件开始上传", 1).show();
				return;
			}
			TextView tv = (TextView) v;
			if (!HomeConfig.isStart) {
				listitem.proBar.getProgress();
				new Thread() {
					@Override
					public void run() {
						HomeConfig.ftpBPRList.get(listitem.position).run();
					}
				}.start();

				tv.setBackgroundResource(R.drawable.update_stop);
				HomeConfig.ftpBPRList.get(listitem.position).isStart = true;
				HomeConfig.isStart = true;
			} else {
				tv.setBackgroundResource(R.drawable.update_start);
				HomeConfig.ftpBPRList.get(listitem.position).isStart = false;
				HomeConfig.isStart = false;
				gInfo.setParcent(listitem.proBar.getProgress() + "");
			}
		}
	}
	
	
	/**
	 * 删除上传队列
	 * @param gInfo 
	 * @param claimNo
	 */
	public void delUploadItem(UploadInfo gInfo){
		HomeConfig.gDelUploadInfo = gInfo;
		
		PromptManager promptManager = new PromptManager();
		promptManager.showDialog(gContext, "删除队列后，只能通过电脑上传案件【"+gInfo.getPolicyNo()+"】照片，\n\n存放目录为：“sdcard0-cClaim-"+gInfo.getPolicyNo()+"”", 
				R.string.yes, R.string.no, new ShowDialogPositiveButton() {
			@Override
			public void setPositiveButton() {
				// 删除上传记录
				TblUploadInfo.delUploadInfoItem(HomeConfig.gDelUploadInfo.getAction());
				TblPicAddress.delPicAddress(HomeConfig.gDelUploadInfo.getPolicyNo(), HomeConfig.gDelUploadInfo.getPlateNo());
				FileUtils.deleteFile(HomeConfig.gDelUploadInfo.getFileUrl());
				
				// 移除上传队列
				HomeConfig.ftpBPRList.remove(0);
				HomeConfig.uploadListViewAdapter.notifyDataSetChanged();
				
				// 重新加载listview
				List<UploadInfo> uploadInfoList = TblUploadInfo.queryUploadInfolist();
				UploadListviewAdapter uploadAdapter = new UploadListviewAdapter(gContext, uploadInfoList, gLvUploadlist);
				HomeConfig.uploadListViewAdapter = uploadAdapter;
				gLvUploadlist.setAdapter(uploadAdapter);
			}

			@Override
			public void setNegativeButton() {

			}
		});
	}
}
