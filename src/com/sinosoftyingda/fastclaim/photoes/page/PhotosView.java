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
import com.sinosoftyingda.fastclaim.common.db.CheckTask;
import com.sinosoftyingda.fastclaim.common.db.DBUtils;
import com.sinosoftyingda.fastclaim.common.db.access.CheckTaskAccess;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskQuery;
import com.sinosoftyingda.fastclaim.common.db.dao.TblTaskinfo;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.RedioRequest;
import com.sinosoftyingda.fastclaim.common.model.SynchroClaimRequest;
import com.sinosoftyingda.fastclaim.common.service.CheckSubmitHttpService;
import com.sinosoftyingda.fastclaim.common.service.RedioHttpService;
import com.sinosoftyingda.fastclaim.common.service.SynchroClaimHttpService;
import com.sinosoftyingda.fastclaim.common.utils.AlarmClockManager;
import com.sinosoftyingda.fastclaim.common.utils.DeviceUtils;
import com.sinosoftyingda.fastclaim.common.utils.FileUtils;
import com.sinosoftyingda.fastclaim.common.utils.Toast;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.common.views.TopManager;
import com.sinosoftyingda.fastclaim.common.views.TopManager.IWordSurveyTopButton;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnSumbitSurvery;
import com.sinosoftyingda.fastclaim.common.views.TopManager.TopBtnTongBuSurveryContinue;
import com.sinosoftyingda.fastclaim.common.views.UiManager;
import com.sinosoftyingda.fastclaim.hkvideo.MPUSDKActivity;
import com.sinosoftyingda.fastclaim.maintask.page.SurveyTaskActivity;
import com.sinosoftyingda.fastclaim.photoes.view.MediaCamera;
import com.sinosoftyingda.fastclaim.photoes.view.PhotosAdapter;
import com.sinosoftyingda.fastclaim.sketchmap.standtable.ResourceManager;
import com.sinosoftyingda.fastclaim.survey.config.CheckSurveyValue;
import com.sinosoftyingda.fastclaim.survey.page.SBasicActivity;
import com.sinosoftyingda.fastclaim.survey.page.SSurveyActivity;
import com.sinosoftyingda.fastclaim.survey.utils.UtilIsNotNull;
import com.sinosoftyingda.fastclaim.upload.page.UploadActivity;
import com.sinosoftyingda.fastclaim.upload.util.CreateZipFile;
import com.sinosoftyingda.fastclaim.work.page.WorkflowlogView;

/**
 * 查勘照片分类
 * 
 * @author DengGuang 20130306
 * 
 */
public class PhotosView extends BaseView implements OnItemClickListener, OnClickListener {
	private View			gView;
	private Button			gBtnCamera;
	private Button			gBtnPrev;
	private Button			gBtnNext;
	private TextView		gTvType;
	private Button			gBtnVideo;

	private GridView		gGvPhotos;
	private Button			gBtnNodate;

	private ListView		listView;
	private PopupWindow		pop;
	private SpinnerAdapter	adapter;
	private int				id;
	boolean					isPopShow	= false;
	public String[]			subs;

	public static int		subIndex	= 0;

	public PhotosView(Context context, Bundle bundle) {
		super(context, bundle);
		this.context = context;
	}

	@Override
	public View getView() {

		return gView;
	}

	@Override
	public Integer getType() {

		return ConstantValue.Page_Survey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String	registNo;

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
		} else {
			registNo = "报案号错误";
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
		super.onResume();
		// 顶部四个按钮
		setFourDopBtn();
		// 按钮操作是否
		if (!SystemConfig.isOperate) {
			gBtnCamera.setEnabled(false);
		} else {
			gBtnCamera.setEnabled(true);
		}
		

		TopManager.getInstance().setTopBtnSumbitSurvery(new TopBtnSumbitSurvery() {
			// 判断是否有联系过客户
			boolean isTakephotos = TblTaskinfo.checkIsTakephotos(registNo);
						
			@Override
			public void OnclickTongBu() {
				/**
				 *                              ***********************
				 *  ============================>>>>>> 【查勘同步】  <<<<<<==============
				 *                              ***********************
				 */ 
				if (SystemConfig.isOperate) {
					if(isTakephotos){
						Toast.showToast(context, "请先在现场拍摄一张照片！");
					}else {
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected void onPreExecute() {
								handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_OPEN);
							};
	
							@Override
							protected void onPostExecute(CommonResponse result) {
								if ("YES".equals(result.getResponseCode())) {
									handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_CLOSE);
									Toast.showToastTest(context, "同步理赔成功！");
									CheckTask checkTask = CheckTaskAccess.findByRegistno(registNo);
									checkTask.setSurveytaskstatus("2");
									checkTask.setSynchrostatus("synchroApply");
									List<CheckTask> list = new ArrayList<CheckTask>();
									list.add(checkTask);
									CheckTaskAccess.insertOrUpdate(list, true);
									TblTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);
	
									// 判断是否有上传队列
									if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(checkTask);
									}else{
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(checkTask);
									}
								} else {
									handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_CLOSE);
									Message message = Message.obtain();
									message.obj = result.getResponseMessage();
									message.what = ConstantValue.ERROE;
									handler.sendMessage(message);
								}
							};
	
							@Override
							protected CommonResponse doInBackground(Void... params) {
								DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery.getRegistNo());
								DataConfig.tblTaskQuery.setSubmitType("synch");
								return CheckSubmitHttpService.checkSubmitService(DataConfig.tblTaskQuery, context.getString(R.string.http_url));
							}
						}.execute();
					}
				}else{
					Toast.showToast(context, "不能【查勘同步】，请确定是否“受理任务”或“继续处理”！");
				}
			}

			@Override
			public void OnclickSumbit() {
				/**
				 *                              ***********************
				 *  ============================>>>>>> 【查勘提交】  <<<<<<==============
				 *                              ***********************
				 */ 
				// 交互
				if (SystemConfig.isOperate) {
					// 校验身份证号码是否为空// 校验身份证号码是否为空
					String idCard = DataConfig.tblTaskQuery.getCheckDriver().get(0).getIdentifyNumber();
					if(isTakephotos){
						Toast.showToast(context, "请先在现场拍摄一张照片！");
					}else if (!CheckSurveyValue.idCardIsNull || !CheckSurveyValue.idCard(idCard)) {
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected void onPreExecute() {
								handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_OPEN);
							};

							@Override
							protected void onPostExecute(CommonResponse result) {
								if ("YES".equals(result.getResponseCode())) {
									handler.sendEmptyMessage(ConstantValue.PROGRESSSUMBIT_CLOSE);
									Toast.showToastTest(context, "查勘提交成功！");
									ContentValues values = new ContentValues();
									values.put("surveytaskstatus", "4");
									values.put("completetime", result.getHandletime());
									DBUtils.update("checktask", values, "registno=?", new String[] { registNo });
									TblTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);

									// 判断是否有上传队列
									if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(CheckTaskAccess.findByRegistno(registNo));
										
										// Toast.showToast(context, "开始上传，请稍等...");
									}else{
										// 全部清除缓存
										UiManager.getInstance().clearViewCache();
										UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, false);
										AlarmClockManager.getInstance(context).remove(CheckTaskAccess.findByRegistno(registNo));
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
							protected CommonResponse doInBackground(Void... params) {

								DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery.getRegistNo());
								DataConfig.tblTaskQuery.setSubmitType("submit");
								return CheckSubmitHttpService.checkSubmitService(DataConfig.tblTaskQuery, context.getString(R.string.http_url));

							}
						}.execute();
					} else {
						Toast.showToast(context, "驾驶人信息-身份证号码不能为空！");
					}
				}else{
					Toast.showToast(context, "不能【查勘提交】，请确定是否“受理任务”或“继续处理”！");
				}
			}
		});

		// 理赔同步撤销
		TopManager.getInstance().btnTongBuSurveryContinue(new TopBtnTongBuSurveryContinue() {
			@Override
			public void onClick() {
				synchCancel();
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
							Toast.showToast(context, "任务理赔同步撤销成功");

							// 把状态改变
							CheckTask checkTask = CheckTaskAccess.findByRegistno(registNo);
							checkTask.setSurveytaskstatus("2");
							checkTask.setSynchrostatus("synchroCannel");
							List<CheckTask> list = new ArrayList<CheckTask>();
							list.add(checkTask);
							CheckTaskAccess.insertOrUpdate(list, true);

							// 同步按钮显示
							TopManager.getInstance().TongBuSurveryContinue(true);
							// 控件可操作
							SystemConfig.isOperate = true;
							// 界面刷新
							UiManager.getInstance().changeView(PhotosView.class, null, false);

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
						requestSynchro.setCancleType("Synchback");
						requestSynchro.setNodeType("check");
						requestSynchro.setLossNo("-2");
						return SynchroClaimHttpService.synchroClaimService(requestSynchro, params[0]);
					}

				}.execute(context.getString(R.string.http_url));
			}
		});
		loadPic(SystemConfig.PHOTO_TYPES[subIndex]);
	}

	/**
	 * 加载图片
	 */
	private void loadPic(final String typeDir) {
		// 查找图片分类目录下的照片
		new AsyncTask<Void, Void, Void>() {
			private List<String>	photosPaths;
			private String			picDir;

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
				PhotosAdapter adapter = new PhotosAdapter(context, photosPaths, picDir, typeDir, "0");
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
		private TextView	groupNameTV;

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
			bundle.putInt("type_key", 0);
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

		// 视频协助
		case R.id.photo_btn_video:
			/**
			 *                              #######################
			 *  ============================>>>>>> 【视频协助】  <<<<<<==============
			 *                              #######################
			 */
			if (registNo != null) {
				// start DengGuang 2013-08-15 10:09:33
				CheckTask checkTask = CheckTaskAccess.findByRegistno(registNo);
				// 判断是否接受按钮
				if(checkTask.getSurveytaskstatus().equals("4")){
					Toast.showToast(context, "已完成案件不能点击视频协助！");
				}else if((checkTask.getSurveytaskstatus().equals("1") && checkTask.getApplycannelstatus().equals("apply"))
						|| (checkTask.getSurveytaskstatus().equals("1") && checkTask.getApplycannelstatus().equals("success"))){
					Toast.showToast(context, "改派案件不能点击视频协助！");
				}else if (checkTask != null && 1 == checkTask.getIsaccept() || checkTask.getSurveytaskstatus().equals("2")) {
					//if (UtilIsNotNull.isNotNullContact(DataConfig.tblTaskQuery)) {
						new AsyncTask<Void, Void, CommonResponse>() {
							@Override
							protected CommonResponse doInBackground(Void... arg0) {
								RedioRequest redioRequest = new RedioRequest();
								redioRequest.setIsXeipei("0");
								redioRequest.setLossNo("");
								redioRequest.setNodeType("check");
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
										if (SystemConfig.isOperate) {
											new AsyncTask<Void, Void, CommonResponse>() {
												@Override
												protected void onPreExecute() {
													handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_OPEN);
												};

												@Override
												protected void onPostExecute(CommonResponse result) {
													if ("YES".equals(result.getResponseCode())) {
														handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_CLOSE);
														Toast.showToastTest(context, "同步理赔成功！");
														CheckTask checkTask = CheckTaskAccess.findByRegistno(registNo);
														checkTask.setSurveytaskstatus("2");
														checkTask.setSynchrostatus("synchroApply");
														List<CheckTask> list = new ArrayList<CheckTask>();
														list.add(checkTask);
														CheckTaskAccess.insertOrUpdate(list, true);
														TblTaskQuery.insertOrUpdate(SystemConfig.dbhelp.getWritableDatabase(), DataConfig.tblTaskQuery, true, false, false);

														// 判断是否有上传队列
														if(CreateZipFile.saveZIPMSG(context, SystemConfig.PHOTO_TEMP, SystemConfig.PHOTO_CLAIMNO, SystemConfig.LOSSNO)){
															// 全部清除缓存
															UiManager.getInstance().clearViewCache();
															UiManager.getInstance().changeView(UploadActivity.class, false, null, false);
															AlarmClockManager.getInstance(context).remove(checkTask);
															
															// Toast.showToast(context, "开始上传，请稍等...");
														}else{
															// 全部清除缓存
															UiManager.getInstance().clearViewCache();
															UiManager.getInstance().changeView(SurveyTaskActivity.class, false, null, false);
															AlarmClockManager.getInstance(context).remove(checkTask);
														}
													} else {
														handler.sendEmptyMessage(ConstantValue.PROGRESSTONGBU_CLOSE);
														Message message = Message.obtain();
														message.obj = result.getResponseMessage();
														message.what = ConstantValue.ERROE;
														handler.sendMessage(message);
													}

												};

												@Override
												protected CommonResponse doInBackground(Void... params) {
													DataConfig.tblTaskQuery = TblTaskQuery.getTaskQuery(SystemConfig.dbhelp.getWritableDatabase(),
															DataConfig.tblTaskQuery.getRegistNo());
													DataConfig.tblTaskQuery.setSubmitType("synch");
													return CheckSubmitHttpService.checkSubmitService(DataConfig.tblTaskQuery, context.getString(R.string.http_url));
												}
											}.execute();

										}
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
//					} else {
//						Toast.showToast(context, "请先联系客户，再请求视频协助");
//					}
				}else {
					Toast.showToast(context, "请先在基本信息页面点击【任务受理】");
				}
			} else {
				Toast.showToast(context, "获取报案号失败");
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
		return ConstantValue.DAOHANG_SurveyPhoto;
	}

	/**
	 * chenjianfan
	 */
	private void setFourDopBtn() {
		TopManager.getInstance().surveyPageTopBtn(new IWordSurveyTopButton() {

			@Override
			public void onSurveyWorkFlowlogClick() {
				// 查勘流程
				UiManager.getInstance().changeView(WorkflowlogView.class, bundle, false);
			}

			@Override
			public void onSurveyPhotoClick() {
				// 拍照
				UiManager.getInstance().changeView(PhotosView.class, bundle, false);
			}

			@Override
			public void onSurveyInfoClick() {
				// 查勘信息页面
				UiManager.getInstance().changeView(SSurveyActivity.class, bundle, true);
			}

			@Override
			public void onSurveyBasicClick() {
				// 查勘基本信息
				UiManager.getInstance().changeView(SBasicActivity.class, bundle, true);
			}
		});
	}

	@Override
	public Integer getBackMain() {
		return ConstantValue.Back_Survey;

	}

	@Override
	public void onPause() {
		if (SystemConfig.isAddAccident)
			ResourceManager.release();

		super.onPause();
	}
}
