package com.sinosoftyingda.fastclaim.photoes.page;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.comdata.DataConfig;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.db.CertainLossTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossInfoAccess;
import com.sinosoftyingda.fastclaim.common.db.access.CertainLossTaskAccess;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.RedioRequest;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.service.RedioHttpService;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.service.VerifyLossSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.DeviceUtils;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.TopManager.IWordSurveyTopButton;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitDefloss;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnTongBuDeflossContinue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossBasicActivity;
import com.sinosoftyingda.fastclaim.defloss.page.DeflossInfoActivity;
import com.sinosoftyingda.fastclaim.defloss.service.DeflossinfoUploadToClaimSystem;
import com.sinosoftyingda.fastclaim.defloss.util.UtilIsNotNull;
import com.sinosoftyingda.fastclaim.hkvideo.MPUSDKActivity;
import com.sinosoftyingda.fastclaim.maintask.page.DelossTaskActivity;
import com.sinosoftyingda.fastclaim.photoes.view.MediaCamera;
import com.sinosoftyingda.fastclaim.photoes.view.PhotosAdapter;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.ResourceManager;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;
import com.sinosoftyingda.fastclaim.work.page.DeflossWorkflowlogView;

/**
 * 照片分类
 * 
 * @author DengGuang 20130306 copy chenjianfan
 */
public class DeflossPhotosView extends BaseView implements OnItemClickListener, OnClickListener {
	private TopBtnSumbitDefloss gTopBtnSumbitDefloss;
	private View gView;
	private Button gBtnCamera;
	private Button gBtnPrev;
	private Button gBtnNext;
	private TextView gTvType;
	private Button gBtnVideo;

	private GridView gGvPhotos;
	private Button gBtnNodate;

	private ListView listView;
	private PopupWindow pop;
	private SpinnerAdapter adapter;
	private int id;
	boolean isPopShow = false;
	public String[] subs;
	private String registNo;
	private String lossNo;
	public static int subIndex = 0;

	public DeflossPhotosView(Context context, Bundle bundle) {
		super(context, bundle);
		this.context = context;
	}

	@Override
	public View getView() {

		return gView;
	}

	@Override
	public Integer getType() {

		return ConstantValue.Page_Defloss;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	protected void init() {
		// 创建该案件的根目录 /mnt/sdcard/cClaim/1001010010
		SystemConfig.PHOTO_CLAIM_DIR = SystemConfig.PHOTO_DIR + SystemConfig.PHOTO_CLAIMNO;
		FileUtils.mkDir(new File(SystemConfig.PHOTO_CLAIM_DIR));

		SystemConfig.PHOTO_CLAIM_TEMP = SystemConfig.PHOTO_TEMP + SystemConfig.PHOTO_CLAIMNO;
		FileUtils.mkDir(new File(SystemConfig.PHOTO_CLAIM_TEMP));

		subs = context.getResources().getStringArray(R.array.photos_type);
		if (bundle != null) {
			registNo = bundle.getString("registNo");
			lossNo = bundle.getString("itemNo");
		} else {
			registNo = "报案号出错";
			lossNo = "-3";
		}

		TopManager.getInstance().setHeadTitle(registNo, 16, Typeface.defaultFromStyle(Typeface.NORMAL));

		/* 初始化组件 */
		gView = inflater.inflate(R.layout.photos, null);
		gBtnCamera = (Button) gView.findViewById(R.id.photo_btn_camera);
		gBtnPrev = (Button) gView.findViewById(R.id.photo_btn_prev);
		gBtnNext = (Button) gView.findViewById(R.id.photo_btn_next);
		gTvType = (TextView) gView.findViewById(R.id.photo_tv_type);
		gBtnVideo = (Button) gView.findViewById(R.id.photo_btn_video);
		gGvPhotos = (GridView) gView.findViewById(R.id.photo_gv_photos);
		gGvPhotos.setFastScrollEnabled(true);
		gBtnNodate = (Button) gView.findViewById(R.id.photo_btn_nodate);

		// 图片分类
		if (bundle != null) {
			String pType = bundle.getString("PType");
			if (pType != null)
				gTvType.setText(pType);
			else
				gTvType.setText(subs[subIndex]);
		} else {
			gTvType.setText(subs[subIndex]);
		}

		TopManager.getInstance().getBtnSurveyPhoto().setBackgroundResource(R.drawable.tasks_phototab_click);
		TopManager.getInstance().getBtnDeflossPhoto().setBackgroundResource(R.drawable.tasks_phototab_click);

		loadPic(SystemConfig.PHOTO_TYPES[subIndex]);

	}

	@Override
	protected void setListener() {
		gBtnCamera.setOnClickListener(this);
		gBtnPrev.setOnClickListener(this);
		gBtnNext.setOnClickListener(this);
		gTvType.setOnClickListener(this);
		gBtnVideo.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// 顶部四个按钮
		setFourDopBtn();
		// 按钮操作是否
		if (!SystemConfig.isOperate) {
			gBtnCamera.setEnabled(false);
		} else {
			gBtnCamera.setEnabled(true);
		}
		
		// 理赔同步撤销
		TopManager.getInstance().btnTongBuDeflossContinue(new TopBtnTongBuDeflossContinue() {
			@Override
			public void onClick() {
				synchCancel();
			}
		});

		// 与理赔同步和提交按钮
		gTopBtnSumbitDefloss = new TopBtnSumbitDefloss() {
			TopBtnSumbitDefloss topBtnSumbitDefloss;
			@Override
			public void OnclickTongBu() {
				topBtnSumbitDefloss = this;
				//VerifyLossSubmitRequest verifyLossSubmitRequest = DeflossSynchroAccess.find(registNo, lossNo);

				if (SystemConfig.isOperate) {
					/**
					 *                              ***********************
					 *  ============================>>>>>> 【定损同步】  <<<<<<==============
					 *                              ***********************
					 */  
					new AsyncTask<String, Void, CommonResponse>() {
						protected void onPreExecute() {
							//handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
						};
						protected void onPostExecute(CommonResponse result) {
							if("YES".equals(result.getResponseCode())){
								DeflossinfoUploadToClaimSystem uploadToClaimSystem = new DeflossinfoUploadToClaimSystem(context);
								boolean isSynch = uploadToClaimSystem.synch(registNo, lossNo, topBtnSumbitDefloss);
								if(isSynch){
									synchOrSubmit("synch");
								}
							}else{
								handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
								// 提示错误信息
								Message message = Message.obtain();
								if (result != null) {
									message.obj = result.getResponseMessage();
								} else {
									message.obj = "提交失败!";
								}
								message.what = ConstantValue.ERROE;
								handler.sendMessage(message);
							}
						};
						protected CommonResponse doInBackground(String... params) {
							return VerifyLossSubmitHttpService.lossApplyService(registNo,lossNo,SystemConfig.USERLOGINNAME,context.getString(R.string.http_url));
						}
					}.execute("");
				}else{
					Toast.showToast(context, "不能【定损同步】，请确定是否“受理任务”或“继续处理”！");
				}
			}

			/**
			 * 同步或提交方法
			 */
			@Override
			public void synchOrSubmit(final String type) {
				// 提交理赔 add by jingtuo start
				new AsyncTask<String, Void, CommonResponse>() {
					@Override
					protected void onPreExecute() {
						handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
					};

					@Override
					protected void onPostExecute(CommonResponse result) {
						Toast.showToast(context, "同步/提交理赔");
						if ("YES".equals(result.getResponseCode())) {
							handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_CLOSE);
							if (type.equals("synch")) {
								CertainLossTask certainLossTask = CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo));
								certainLossTask.setSurveytaskstatus("2");
								certainLossTask.setSynchrostatus("synchroApply");
								List<CertainLossTask> list = new ArrayList<CertainLossTask>();
								list.add(certainLossTask);
								CertainLossTaskAccess.insertOrUpdate(list, true);
							} else if (type.equals("submit")) {
								ContentValues values = new ContentValues();
								values.put("surveytaskstatus", "4");
								values.put("verifypassflag", "1");
								values.put("completetime", result.getHandletime());
								DBUtils.update("certainlosstask", values, "registno=? and itemno=?", new String[] { registNo, lossNo });
							}
							CertainLossInfoAccess.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.defLossInfoQueryData, false);
							
							// 判断是否有上传队列
							if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
								// 全部清除缓存
								UiManager.getInstance().clearViewCache();
								UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
								AlarmClockManager.getInstance(context).remove(CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo)));
							}else{
								// 全部清除缓存
								UiManager.getInstance().clearViewCache();
								UiManager.getInstance().changeView(DelossTaskActivity.class, false, null, false);
								AlarmClockManager.getInstance(context).remove(CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo)));
							}
						} else {
							handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_CLOSE);
							Message message = Message.obtain();
							message.obj = result.getResponseMessage();
							message.what = ConstantValue.ERROE;
							handler.sendMessage(message);
						}
					};

					@Override
					protected CommonResponse doInBackground(String... params) {
						DataConfig.defLossInfoQueryData.setSubmitType(params[0]);
						return VerifyLossSubmitHttpService.lossSubmitService(DataConfig.defLossInfoQueryData, context.getString(R.string.http_url));
					}
				}.execute(type);
				// 提交理赔 add by jingtuo end
			}

			@Override
			public void OnclickSumbit() {
				topBtnSumbitDefloss = this;
				// 提交理赔 add by jingtuo start
				if (SystemConfig.isOperate) {
					/**
					 *                              ***********************
					 *  ============================>>>>>> 【定损提交】  <<<<<<==============
					 *                              ***********************
					 */  
					new AsyncTask<String, Void, CommonResponse>() {
						protected void onPreExecute() {
							//handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
						};
						protected void onPostExecute(CommonResponse result) {
							if("YES".equals(result.getResponseCode())){
								DeflossinfoUploadToClaimSystem uploadToClaimSystem = new DeflossinfoUploadToClaimSystem(context);
								boolean isSynch = uploadToClaimSystem.submit(registNo, lossNo, topBtnSumbitDefloss);
								if(isSynch){
									synchOrSubmit("submit");
								}
							}else{
								handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
								// 提示错误信息
								Message message = Message.obtain();
								if (result != null) {
									message.obj = result.getResponseMessage();
								} else {
									message.obj = "提交失败!";
								}
								message.what = ConstantValue.ERROE;
								handler.sendMessage(message);
							}
						};
						protected CommonResponse doInBackground(String... params) {
							return VerifyLossSubmitHttpService.lossApplyService(registNo,lossNo,SystemConfig.USERLOGINNAME,context.getString(R.string.http_url));
						}
					}.execute("");
				}else{
					Toast.showToast(context, "不能【定损提交】，请确定是否“受理任务”或“继续处理”！");
				}
				// 提交理赔 add by jingtuo end
			}
		};
		TopManager.getInstance().setTopBtnSumbitDefloss(gTopBtnSumbitDefloss);
		loadPic(SystemConfig.PHOTO_TYPES[subIndex]);
		super.onResume();
	}

	/**
	 * 加载图片
	 */
	private void loadPic(final String typeDir) {
		// 查找图片分类目录下的照片
		new AsyncTask<Void, Void, Void>() {
			private List<String> photosPaths;
			private String picDir;
			@Override
			protected void onPreExecute() {

			};

			@Override
			protected Void doInBackground(Void... params) {
				photosPaths = new ArrayList<String>();
				FileUtils fileUtils = new FileUtils();
				picDir = SystemConfig.PHOTO_CLAIM_DIR + typeDir;
				photosPaths = fileUtils.getFileName(new File(picDir), picDir);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// 当有数据，背景图片文字置控
				if (photosPaths.size() != 0) {
					gBtnNodate.setBackgroundDrawable(null);
				} else {
					gBtnNodate.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.photo_nodate));
				}
				// 重新加载图片
				PhotosAdapter adapter = new PhotosAdapter(context, photosPaths, picDir, typeDir, "1");
				gGvPhotos.setAdapter(adapter);
				gGvPhotos.setOnItemClickListener(adapter); // 单击查看单张图片
			};

		}.execute();
	}

	/**
	 * 选择照片分类
	 */
	private void choiceType() {
		if (pop == null) {
			// isPopShow=true;
			listView = new ListView(context);
			adapter = new SpinnerAdapter();
			listView.setAdapter(adapter);
			// listview上下滑动时，颜色不改变
			listView.setCacheColorHint(Color.WHITE);
			// listView背景圆角样式
			listView.setBackgroundResource(R.drawable.list_corner_round);

			// listView 分割线进行改造
			listView.setDivider(null);
			listView.setVerticalScrollBarEnabled(false);
			listView.setOnItemClickListener(this);
			pop = new PopupWindow(listView, gTvType.getWidth(), LayoutParams.WRAP_CONTENT, true);

			// 设置popupwindow背景色 功能点击popupwindow之外的部分 可以将popupwindow隐藏
			pop.setBackgroundDrawable(new ColorDrawable(0x00000000));
			pop.showAsDropDown(gTvType);
		}
		if (isPopShow) {
			pop.dismiss();
		} else {
			pop.showAsDropDown(gTvType);
		}
	}

	class SpinnerAdapter extends BaseAdapter {
		private TextView groupNameTV;

		@Override
		public int getCount() {
			return subs.length;
		}

		@Override
		public Object getItem(int position) {
			return subs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.common_spinner_item, null);
			}
			groupNameTV = (TextView) convertView.findViewById(R.id.report_spinner_item_tv);
			groupNameTV.setText(subs[position]);

			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();

		switch (v.getId()) {
		// 拍照
		case R.id.photo_btn_camera:
			// 将图片目录
			Bundle bundle = new Bundle();
			bundle.putInt("typeindex_key", subIndex);
			bundle.putInt("type_key", 1);
			intent.putExtras(bundle);
			intent.setClass(context, MediaCamera.class);
			context.startActivity(intent);
			break;

		// 上一类
		case R.id.photo_btn_prev:
			subIndex = subIndex - 1;
			if (subIndex < 0) {
				subIndex = subs.length + subIndex;
			}
			// 显示图片
			loadPic(SystemConfig.PHOTO_TYPES[subIndex]);
			gTvType.setText(subs[subIndex]);
			break;

		// 下一类
		case R.id.photo_btn_next:
			subIndex = subIndex + 1;
			if (subIndex >= subs.length) {
				subIndex = subIndex - subs.length;
			}
			// 显示图片
			loadPic(SystemConfig.PHOTO_TYPES[subIndex]);
			gTvType.setText(subs[subIndex]);
			break;

		// 选择类别
		case R.id.photo_tv_type:
			choiceType();
			break;

		/**
		 *                              #######################
		 *  ============================>>>>>> 【视频协助】  <<<<<<==============
		 *                              #######################
		 */
		case R.id.photo_btn_video:
			// start DengGuang 2013-08-15 10:09:33
			CertainLossTask certainLossTask = CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo));
			if(certainLossTask.getSurveytaskstatus().equals("4")){					// 状态为已完成
				Toast.showToast(context, "已完成案件不能点击视频协助！");
			}else if((certainLossTask.getSurveytaskstatus().equals("1") && certainLossTask.getApplycannelstatus().equals("apply"))
					|| (certainLossTask.getSurveytaskstatus().equals("1") && certainLossTask.getApplycannelstatus().equals("success"))){
				Toast.showToast(context, "改派案件不能点击视频协助！");
			}else if (certainLossTask != null && 1 == certainLossTask.getIsaccept() // 判断是否接受按钮
					|| certainLossTask.getSurveytaskstatus().equals("2") 			// 状态为正在处理
					|| certainLossTask.getSurveytaskstatus().equals("5")) {			// 状态为核损打回
				if (registNo != null && lossNo != null) {
					new AsyncTask<Void, Void, CommonResponse>() {
						@Override
						protected CommonResponse doInBackground(Void... arg0) {
							RedioRequest redioRequest = new RedioRequest();
							redioRequest.setIsXeipei("0");
							redioRequest.setLossNo(lossNo);
							redioRequest.setNodeType("certainLoss");
							redioRequest.setRegistNo(registNo);
							redioRequest.setUserCode(SystemConfig.USERLOGINNAME);
							redioRequest.setIMEI(DeviceUtils.getDeviceId(context));
							return RedioHttpService.redioService(redioRequest, context.getString(R.string.http_url));
						}

						@Override
						protected void onPreExecute() {
							super.onPreExecute();
							handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
						}

						@Override
						protected void onPostExecute(CommonResponse result) {
							if (result != null && "YES".equals(result.getResponseCode())) {
								handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
								Intent hkVideoIntent = new Intent();
								hkVideoIntent.setClass(context, MPUSDKActivity.class);
								context.startActivity(hkVideoIntent);
								if (result.getResponseMessage().equals("synchroYes"))
									gTopBtnSumbitDefloss.OnclickTongBu();
							} else {
								handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
								// 提示错误信息
								Message message = Message.obtain();
								if (result != null) {
									message.obj = result.getResponseMessage();
								} else {
									message.obj = "请求协助失败";
								}
								message.what = ConstantValue.ERROE;
								handler.sendMessage(message);

							}
						};
					}.execute();
				} else {
					Toast.showToast(context, "请求视频响应失败");
				}
			}else{
				Toast.showToast(context, "请先在基本信息页面点击【任务受理】");
			}
			break;
		default:
			break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		id = position;
		gTvType.setText(subs[position]);
		pop.dismiss();

		// 将序号重新赋值给下标subIndex
		subIndex = position;
		loadPic(SystemConfig.PHOTO_TYPES[subIndex]);
	}

	@Override
	public Integer getExit() {
		return ConstantValue.DAOHANG_DeflossPhoto;
	}

	/**
	 * chenjianfan
	 */
	private void setFourDopBtn() {
		TopManager.getInstance().surveyPageTopBtn(new IWordSurveyTopButton() {

			@Override
			public void onSurveyWorkFlowlogClick() {
				// 定损流程

				UiManager.getInstance().changeView(DeflossWorkflowlogView.class, bundle, false);

			}

			@Override
			public void onSurveyPhotoClick() {
				// 定损拍照

				UiManager.getInstance().changeView(DeflossPhotosView.class, bundle, false);

			}

			@Override
			public void onSurveyInfoClick() {
				// 定损信息页面
				UiManager.getInstance().changeView(DeflossInfoActivity.class, bundle, true);

			}

			@Override
			public void onSurveyBasicClick() {
				// 定损基本信息
				UiManager.getInstance().changeView(DeflossBasicActivity.class, bundle, true);

			}
		});
	}

	@Override
	public Integer getBackMain() {

		return ConstantValue.Back_Defloss;
	}

	private void synchCancel() {
		new AsyncTask<String, Void, CommonResponse>() {

			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(CommonResponse result) {
				if ("YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					// 控件可以操作
					SystemConfig.isOperate = true;
					Toast.showToast(context, "任务理赔同步撤销成功");

					CertainLossTask certainLossTask = CertainLossTaskAccess.find(registNo, Integer.parseInt(lossNo));
					certainLossTask.setSurveytaskstatus("2");
					certainLossTask.setSynchrostatus("synchroCancel");
					List<CertainLossTask> list = new ArrayList<CertainLossTask>();
					list.add(certainLossTask);
					CertainLossTaskAccess.insertOrUpdate(list, true);

					// 显示同步按钮
					TopManager.getInstance().TongBuDeflossContinue(true);
					// 控件可操作
					SystemConfig.isOperate = true;
					// 界面刷新
					UiManager.getInstance().changeView(DeflossPhotosView.class, null, false);

				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					message.obj = result.getResponseMessage();
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}

			};

			@Override
			protected CommonResponse doInBackground(String... params) {
				SynchroClaimRequest requestSynchro = new SynchroClaimRequest();
				requestSynchro.setRegistNo(registNo);
				requestSynchro.setLossNo(lossNo);
				requestSynchro.setCancleType("Synchback");
				requestSynchro.setNodeType("certainLoss");
				return SynchroClaimHttpService.synchroClaimService(requestSynchro, params[0]);
			}
		}.execute(context.getString(R.string.http_url));
	}

	@Override
	public void onPause() {
		if (SystemConfig.isAddAccident)
			ResourceManager.release();
		super.onPause();
	}
}