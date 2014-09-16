package com.sinosoftyingda.fastclaim.defloss.page;

import java.util.Date;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.common.model.CommonResponse;
import com.sinosoftyingda.fastclaim.common.model.PrintAccessorles;
import com.sinosoftyingda.fastclaim.common.model.PrintRepair;
import com.sinosoftyingda.fastclaim.common.model.PrintRequest;
import com.sinosoftyingda.fastclaim.common.model.PrintResponse;
import com.sinosoftyingda.fastclaim.common.service.PrintHttpService;
import com.sinosoftyingda.fastclaim.common.service.PrintORMsgHttpService;
import com.sinosoftyingda.fastclaim.common.utils.DateTimeUtils;
import com.sinosoftyingda.fastclaim.common.views.BaseView;
import com.sinosoftyingda.fastclaim.common.views.ConstantValue;
import com.sinosoftyingda.fastclaim.zhike.PageUtils;
import com.sinosoftyingda.fastclaim.zhike.PrintUtils;
import com.sinosoftyingda.fastclaim.zhike.StatusBox;

public class DeflossPrintActivity extends BaseView implements OnClickListener{

	private View layout;
	
	private Button btnOk;
	private CheckBox cbBluetoothprint,cbSmsnotice;
	
	private static final int replacenum = 5;
	private static final int repairnum = 8;
	
	private BluetoothAdapter bluetoothAdapter;
	
	public static final int PAGE_WIDTH = 600;
	public static final int DEFAULT_FONTSIZE = 0;
	
	private PrintResponse printResponse;
	
	private TextView tvDeflossinstitution, tvDeflossstaff, tvReportno, tvPolicynoCtaliLabel, tvPolicynoBusinessLabel,
		tvPolicynoCtali, tvPolicynoBusiness,
		tvInsurant, tvDeflosstarget, tvBrandmodel, tvVincode, tvReplaceTotal,tv_repair_total,
		tv_scrap_value,tv_deflosstotalmoney,tv_bigletter, tv_printdate;
	
	private String currentDate = null;
	
	private TableLayout tlReplace, tlRepair;
	
	private String registno;
	
	private int itemno;
	
	private int printCount;
	
	public DeflossPrintActivity(Context context, Bundle bundle) {
		super(context, bundle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return layout;
	}

	@Override
	public Integer getType() {
		// TODO Auto-generated method stub
		return ConstantValue.Page_third;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		printCount = 0;
		layout = View.inflate(context, R.layout.deflossprint, null);
		tvDeflossinstitution = (TextView)layout.findViewById(R.id.deflossprint_tv_deflossinstitution);
		tvDeflossstaff = (TextView)layout.findViewById(R.id.deflossprint_tv_deflossstaff);
		tvReportno = (TextView)layout.findViewById(R.id.deflossprint_tv_reportno);
		tvPolicynoCtali = (TextView)layout.findViewById(R.id.deflossprint_tv_policyno_ctali);
		tvPolicynoBusiness = (TextView)layout.findViewById(R.id.deflossprint_tv_policyno_business);
		tvPolicynoCtaliLabel = (TextView)layout.findViewById(R.id.deflossprint_tv_policyno_ctali_label);
		tvPolicynoBusinessLabel = (TextView)layout.findViewById(R.id.deflossprint_tv_policyno_business_label);
		tvInsurant = (TextView)layout.findViewById(R.id.deflossprint_tv_insurant);
		tvDeflosstarget = (TextView)layout.findViewById(R.id.deflossprint_tv_deflosstarget);
		tvBrandmodel = (TextView)layout.findViewById(R.id.deflossprint_tv_brandmodel);
		tvVincode = (TextView)layout.findViewById(R.id.deflossprint_tv_vincode);
		tvReplaceTotal = (TextView)layout.findViewById(R.id.deflossprint_tv_replace_total);
		tv_repair_total = (TextView)layout.findViewById(R.id.deflossprint_tv_repair_total);
		tv_scrap_value = (TextView)layout.findViewById(R.id.deflossprint_tv_scrap_value);
		tv_deflosstotalmoney = (TextView)layout.findViewById(R.id.deflossprint_tv_deflosstotalmoney);
		tv_bigletter = (TextView)layout.findViewById(R.id.deflossprint_tv_bigletter);
		tv_printdate = (TextView)layout.findViewById(R.id.deflossprint_tv_printdate);
		tlReplace = (TableLayout)layout.findViewById(R.id.defloss_tl_replace);
		tlRepair = (TableLayout)layout.findViewById(R.id.defloss_tl_repair);
		currentDate = DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
		tv_printdate.setText(currentDate);
		
		cbBluetoothprint = (CheckBox)layout.findViewById(R.id.deflossprint_cb_bluetoothprint);
		cbBluetoothprint.setChecked(false);
		cbBluetoothprint.setEnabled(false);
		cbBluetoothprint.setButtonDrawable(android.R.drawable.checkbox_off_background);
		cbBluetoothprint.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		
		cbSmsnotice = (CheckBox)layout.findViewById(R.id.deflossprint_cb_smsnotice);
		cbSmsnotice.setButtonDrawable(android.R.drawable.checkbox_off_background);
		cbSmsnotice.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		
		btnOk = (Button)layout.findViewById(R.id.deflossprint_btn_ok);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		registno = bundle.getString("registno");
		itemno = bundle.getInt("itemno");
		getPrintInfo(registno, itemno + "");
//		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//		layout.setLayoutParams(params);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		cbBluetoothprint.setOnClickListener(this);
		cbSmsnotice.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	@Override
	public Integer getExit() {
		return null;
	}

	@Override
	public Integer getBackMain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.deflossprint_cb_bluetoothprint:
			
			break;
		case R.id.deflossprint_cb_smsnotice:
			
			break;
		case R.id.deflossprint_btn_ok:
			if(!cbBluetoothprint.isChecked()&&!cbSmsnotice.isChecked()){
				Toast.makeText(context, "蓝牙打印和短信通知至少勾选一个", Toast.LENGTH_SHORT).show();
				return;
			}
			if(printResponse==null){
				Toast.makeText(context, "没有定损单信息", Toast.LENGTH_SHORT).show();
				return;
			}
			if(cbSmsnotice.isChecked()&&!cbBluetoothprint.isChecked()){//只是短信通知
				printOrMsg(registno, itemno + "");
			}else{//蓝牙打印
				if(bluetoothAdapter==null||!bluetoothAdapter.isEnabled()){
					Intent intent = new Intent();
					intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					((Activity)context).startActivityForResult(intent, 2);
				}else{
					Set<BluetoothDevice> set = bluetoothAdapter.getBondedDevices();
					bluetoothDevices = set.toArray(new BluetoothDevice[set.size()]);
					show();
				}
			}
			break;
			
		case R.id.bluetooth_btn_ok:
			
			if(bluetoothDevice!=null){
				bluetooth.dismiss();
				statusBox.Show("正在打印...");
				if(printCount<=0){
					printOrMsg(registno, itemno + "");
				}else{
					new Thread(){
						@Override
						public void run() {
							Message msg = new Message();
							try {
								if(!PrintUtils.OpenPrinter(bluetoothDevice.getAddress())){
									PrintUtils.close();
									msg.what = CONNECT_ERROR;
									return;
								}
								PageUtils.SelectPage(0);
								PageUtils.ClearPage();
//							PageUtils.SelectPage(1);
//							PageUtils.ClearPage();
								int height = 1500;
								if(printResponse.getPrintAccessorles().size()>=1){
									height +=90;
									height += replacenum*80;
								}
								if(printResponse.getPrintRepair().size()>=1){
									height +=90;
									height += repairnum*80;
								}
								height = height/2;
								PageUtils.SetPageSize(PAGE_WIDTH, height);
								PageUtils.ErrorConfig(true);
								drawContent(1);// content
								PageUtils.PrintPage(0x04, 10, false);
//							PageUtils.PrintPageSpike(0x04, 150, 1, 0x00);
								PageUtils.ClearPage();
								PrintUtils.close();
								msg.what = PRINT_SUCCESS;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								msg.what = PRINT_ERROR;
							}
							gHandler.sendMessage(msg);
						};
					}.start();
				}
			}else{
				Toast.makeText(context, "至少选择一个蓝牙设备", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bluetooth_btn_cancel:
			bluetooth.dismiss();
			break;
		default:
			break;
		}
	}
	
	private Dialog bluetooth;
	private ListView lvBluetoothDevices;
	private BluetoothDevice[] bluetoothDevices;
	private BluetoothDevice bluetoothDevice;
	private StatusBox statusBox;
	private void show(){
		if(bluetooth==null){
			bluetooth = new Dialog(context);
			bluetooth.setTitle(R.string.title_bluetoothdevice);
			bluetooth.setContentView(R.layout.bluetooth);
			lvBluetoothDevices = (ListView)bluetooth.findViewById(R.id.bluetooth_lv);
			Button btnOk = (Button)bluetooth.findViewById(R.id.bluetooth_btn_ok);
			Button btnCancel = (Button)bluetooth.findViewById(R.id.bluetooth_btn_cancel);
			btnOk.setOnClickListener(this);
			btnCancel.setOnClickListener(this);
			if(statusBox==null){
				statusBox = new StatusBox(context, btnOk);
			}
			lvBluetoothDevices.setAdapter(new BluetoothDeviceAdapter());
			bluetooth.show();
		}else{
			if(!bluetooth.isShowing()){
				lvBluetoothDevices.setAdapter(new BluetoothDeviceAdapter());
				bluetooth.show();
			}
		}
	}
	
	private static final int CONNECT_ERROR = 0;
	
	private static final int PRINT_ERROR = 1;
	
	private static final int PRINT_SUCCESS = 2;
	
	private Handler gHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			statusBox.Close();
			switch (msg.what) {
			case CONNECT_ERROR:
				Toast.makeText(context, "连接蓝牙设备失败", Toast.LENGTH_SHORT).show();
				break;
			case PRINT_ERROR:
				Toast.makeText(context, "打印失败", Toast.LENGTH_SHORT).show();
				break;
			case PRINT_SUCCESS:
				Toast.makeText(context, "打印成功", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		};
	};
	
	private int drawContent(int num) {
		currentDate = DateTimeUtils.parseDateToString(new Date(System.currentTimeMillis()), DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
		int left = 0;
		int lineSpace = 30;
		int marginTop = 0;
		String startline = "**********************************";
		
		for (int i = 0; i < num; i++) {
			
		PageUtils.DrawText(left, marginTop, "定损确认信息", 0x00, 1);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left, marginTop, startline, 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);

		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "定损机构", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		PageUtils.DrawText(left + 150, marginTop, printResponse.getVerifyComCode() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "定损人员", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 150, marginTop, printResponse.getVerifyUserCode() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 15, marginTop, "报案号", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 150, marginTop, printResponse.getRegistNo() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		if(!TextUtils.isEmpty(printResponse.getQsPolicyNo())){
			marginTop += lineSpace;
			PageUtils.DrawText(left + 10, marginTop, "保险单号", 0x00, DEFAULT_FONTSIZE);
			zp_realtime_status(1000);
			marginTop += 25;
			PageUtils.DrawText(left + 10, marginTop, "(交强险)", 0x00, DEFAULT_FONTSIZE);
			zp_realtime_status(1000);
			PageUtils.DrawText(left + 150, marginTop - 15, printResponse.getQsPolicyNo() + " ", 0x00, DEFAULT_FONTSIZE);
			zp_realtime_status(1000);
		}
		
		if(!TextUtils.isEmpty(printResponse.getBusInessPolicyNo())){
			marginTop += lineSpace;
			PageUtils.DrawText(left + 10, marginTop, "保单号码", 0x00, DEFAULT_FONTSIZE);
			zp_realtime_status(1000);
			marginTop += 25;
			PageUtils.DrawText(left + 10, marginTop, "(商业险)", 0x00, DEFAULT_FONTSIZE);
			zp_realtime_status(1000);
			PageUtils.DrawText(left + 150, marginTop - 15, printResponse.getBusInessPolicyNo() + " ", 0x00, DEFAULT_FONTSIZE);
			zp_realtime_status(1000);
		}
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "被保险人", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 150, marginTop, printResponse.getInsureName() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "定损对象", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 150, marginTop, printResponse.getLossName() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "厂牌型号", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 150, marginTop, printResponse.getBrandName() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "VIN码", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 150, marginTop, printResponse.getVinNo() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		if(printResponse.getPrintAccessorles().size()>=1){
			marginTop += 60;
			PageUtils.DrawText(left, marginTop, "换件明细", 0x00, 1);
			zp_realtime_status(1000);
			
			marginTop += 30;
			PageUtils.DrawText(left, marginTop, startline, 0x00, DEFAULT_FONTSIZE);
			zp_realtime_status(1000);

			for (int j = 0; j < printResponse.getPrintAccessorles().size(); j++) {
				marginTop += lineSpace;
				PageUtils.DrawText(left + 10, marginTop, printResponse.getPrintAccessorles().get(j).getPartName() + " ", 0x00, DEFAULT_FONTSIZE);
				zp_realtime_status(1000);
				
				PageUtils.DrawText(left + 200, marginTop, "X " +  printResponse.getPrintAccessorles().get(j).getQuanTity() + " ", 0x00, DEFAULT_FONTSIZE);
				zp_realtime_status(1000);
				
				PageUtils.DrawText(left + 280, marginTop,  printResponse.getPrintAccessorles().get(j).getSumDefLoss() + " ", 0x00, DEFAULT_FONTSIZE);
				zp_realtime_status(1000);
			}
		}

		if(printResponse.getPrintRepair().size()>=1){
			marginTop += 60;
			PageUtils.DrawText(left, marginTop, "维修明细", 0x00, 1);
			zp_realtime_status(1000);
			
			marginTop += 30;
			PageUtils.DrawText(left, marginTop, startline, 0x00, DEFAULT_FONTSIZE);
			zp_realtime_status(1000);
			
			for (int j = 0; j < printResponse.getPrintRepair().size(); j++) {
				marginTop += lineSpace;
				PageUtils.DrawText(left + 10, marginTop, printResponse.getPrintRepair().get(j).getRepairName() + " ", 0x00, DEFAULT_FONTSIZE);
				zp_realtime_status(1000);
				
				PageUtils.DrawText(left + 200, marginTop, printResponse.getPrintRepair().get(j).getRepairPro() + " ", 0x00, DEFAULT_FONTSIZE);
				zp_realtime_status(1000);
				
				PageUtils.DrawText(left + 310, marginTop, printResponse.getPrintRepair().get(j).getRepairPrice() + " ", 0x00, DEFAULT_FONTSIZE);
				zp_realtime_status(1000);
			}
		}
		
		marginTop += 60;
		PageUtils.DrawText(left, marginTop, "定损金额", 0x00, 1);
		zp_realtime_status(1000);
		
		marginTop += 30;
		PageUtils.DrawText(left, marginTop, startline, 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "配件金额合计", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 180, marginTop, printResponse.getComponentSumFee() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "维修合计金额", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 180, marginTop, printResponse.getRepairfeesSumFee() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "残值合计", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 180, marginTop, printResponse.getResetFee() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "定损金额合计", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		marginTop += 25;
		PageUtils.DrawText(left + 10, marginTop, "( 小写 )", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 180, marginTop, printResponse.getVerifyLossFee() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "( 大写 )", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		PageUtils.DrawText(left + 180, marginTop, printResponse.getVertfyLossfee1() + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "打印单生成时间", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		marginTop += lineSpace;
		PageUtils.DrawText(left + 150, marginTop, currentDate + " ", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		
		marginTop += lineSpace;
		PageUtils.DrawText(left + 10, marginTop, "客户签名：", 0x00, DEFAULT_FONTSIZE);
		zp_realtime_status(1000);
		marginTop += lineSpace;
		}
		System.out.println("Print Height:" + marginTop);
		return marginTop;
	}

	public static int zp_realtime_status(int timeout) {
		byte[] status = new byte[8];
		byte[] buf = new byte[11];
		buf[0] = 0x1f;
		buf[1] = 0x00;
		buf[2] = 0x06;
		buf[3] = 0x00;
		buf[4] = 0x07;
		buf[5] = 0x14;
		buf[6] = 0x18;
		buf[7] = 0x23;
		buf[8] = 0x25;
		buf[9] = 0x32;
		buf[10] = 0x00;
		PrintUtils.SPPWrite(buf, 10);
		if (PrintUtils.SPPReadTimeout(status, 1, timeout) == false) {
			return -1;
		}
		return status[0];
	}
	 
	 
	/**
	 * @author JingTuo
	 *
	 */
	class BluetoothDeviceAdapter extends BaseAdapter{

		private RadioButton radioButton;
		
		@Override
		public int getCount() {
			return bluetoothDevices.length;
		}

		@Override
		public Object getItem(int position) {
			return bluetoothDevices[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup group) {
			final BluetoothDevice device = bluetoothDevices[position];
			if(view==null){
				view = View.inflate(context, R.layout.bluetooth_lv, null);
			}
			TextView name = (TextView)view.findViewById(R.id.bluetooth_lv_tv_name);
			TextView address = (TextView)view.findViewById(R.id.bluetooth_lv_tv_address);
			name.setText(device.getName());
			address.setText(device.getAddress());
			RadioButton rb = (RadioButton)view.findViewById(R.id.bluetooth_lv_rb);
			rb.setClickable(false);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					RadioButton rb = (RadioButton)view.findViewById(R.id.bluetooth_lv_rb);
					if (rb.isChecked()) {
						rb.setChecked(false);
						radioButton = null;
						bluetoothDevice = null;
					} else {
						rb.setChecked(true);
						if (radioButton != null && radioButton.isChecked()) {
							radioButton.setChecked(false);
						}
						radioButton = rb;
						bluetoothDevice = device;
					}
				}
			});
			return view;
		}
	}
	
	/**
	 * @param registNo
	 */
	private void printOrMsg(final String registno, final String itemno) {
		new AsyncTask<String, Void, CommonResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};
			@Override
			protected void onPostExecute(CommonResponse result) {
				if (result!=null&&"YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Toast.makeText(context, "交互成功!", Toast.LENGTH_SHORT).show();
					if(cbBluetoothprint.isChecked()){
						new Thread(){
							@Override
							public void run() {
								Message msg = new Message();
								try {
									if(!PrintUtils.OpenPrinter(bluetoothDevice.getAddress())){
										PrintUtils.close();
										msg.what = CONNECT_ERROR;
										return;
									}
									PageUtils.SelectPage(0);
									PageUtils.ClearPage();
//								PageUtils.SelectPage(1);
//								PageUtils.ClearPage();
									int height = 1500;
									if(printResponse.getPrintAccessorles().size()>=1){
										height +=90;
										height += replacenum*80;
									}
									if(printResponse.getPrintRepair().size()>=1){
										height +=90;
										height += repairnum*80;
									}
									height = height/2;
									PageUtils.SetPageSize(PAGE_WIDTH, height);
									PageUtils.ErrorConfig(true);
									drawContent(1);// content
									PageUtils.PrintPage(0x04, 10, false);
//								PageUtils.PrintPageSpike(0x04, 150, 1, 0x00);
									PageUtils.ClearPage();
									PrintUtils.close();
									msg.what = PRINT_SUCCESS;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									msg.what = PRINT_ERROR;
								}
								gHandler.sendMessage(msg);
							};
						}.start();
					}
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					if(result!=null){
						message.obj = result.getResponseMessage();
					}else{
						message.obj = "交互失败!";
					}
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}
			}

			@Override
			protected CommonResponse doInBackground(String... params) {
				PrintRequest printRequest = new PrintRequest();
				printRequest.setRegistNo(registno);
				printRequest.setLossNo(itemno);
				printRequest.setUserCode(SystemConfig.USERLOGINNAME);
				if(cbBluetoothprint.isChecked()){
					printRequest.setIsPrint("yes");
				}else{
					printRequest.setIsPrint("no");
				}
				if(cbSmsnotice.isChecked()){
					printRequest.setIsSendMessage("yes");
				}else{
					printRequest.setIsSendMessage("no");
				}
				return PrintORMsgHttpService.printORMsgService(printRequest, context.getString(R.string.http_url));
			}
		}.execute();
	}

	
	/**
	 * @param registNo
	 */
	private void getPrintInfo(final String registno, final String itemno) {
		new AsyncTask<String, Void, CommonResponse>() {
			@Override
			protected void onPreExecute() {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			};

			@Override
			protected void onPostExecute(CommonResponse result) {
				if (result!=null&&"YES".equals(result.getResponseCode())) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Toast.makeText(context, "打印信息获取成功!", Toast.LENGTH_SHORT).show();
					printResponse = (PrintResponse)result;
					tvDeflossinstitution.setText(printResponse.getVerifyComCode());
					tvDeflossstaff.setText(printResponse.getVerifyUserCode());
					tvReportno.setText(printResponse.getRegistNo());
					if(TextUtils.isEmpty(printResponse.getQsPolicyNo())){
						tvPolicynoCtaliLabel.setVisibility(View.GONE);
						tvPolicynoCtali.setVisibility(View.GONE);
					}else{
						tvPolicynoCtali.setText(printResponse.getQsPolicyNo());
					}
					if(TextUtils.isEmpty(printResponse.getBusInessPolicyNo())){
						tvPolicynoBusinessLabel.setVisibility(View.GONE);
						tvPolicynoBusiness.setVisibility(View.GONE);
					}else{
						tvPolicynoBusiness.setText(printResponse.getBusInessPolicyNo());
					}
					tvInsurant.setText(printResponse.getInsureName());
					tvDeflosstarget.setText(printResponse.getLossName());
					tvBrandmodel.setText(printResponse.getBrandName());
					tvVincode.setText(printResponse.getVinNo());
					//换件
					List<PrintAccessorles> printAccessorles =  printResponse.getPrintAccessorles();
					if(printAccessorles!=null&&printAccessorles.size()>=1){
						for (int i = 0; i < printAccessorles.size(); i++) {
							PrintAccessorles pa = printAccessorles.get(i);
							TableRow row = (TableRow)LayoutInflater.from(context).inflate(R.layout.deflossprint_tr, null);
							TextView one = (TextView)row.findViewById(R.id.deflossprint_tr_tv_one);
							TextView two = (TextView)row.findViewById(R.id.deflossprint_tr_tv_two);
							TextView three = (TextView)row.findViewById(R.id.deflossprint_tr_tv_three);
							one.setText(pa.getPartName());
							two.setText("X " + pa.getQuanTity());
							three.setText(pa.getSumDefLoss());
							tlReplace.addView(row);
						}
					}
					
					//维修
					List<PrintRepair> printRepairs = printResponse.getPrintRepair();
					if(printRepairs!=null&&printRepairs.size()>=1){
						for (int i = 0; i < printRepairs.size(); i++) {
							PrintRepair pr = printRepairs.get(i);
							TableRow row = (TableRow)LayoutInflater.from(context).inflate(R.layout.deflossprint_tr, null);
							TextView one = (TextView)row.findViewById(R.id.deflossprint_tr_tv_one);
							TextView two = (TextView)row.findViewById(R.id.deflossprint_tr_tv_two);
							TextView three = (TextView)row.findViewById(R.id.deflossprint_tr_tv_three);
							one.setText(pr.getRepairName());
							two.setText(pr.getRepairPro());
							three.setText(pr.getRepairPrice());
							tlRepair.addView(row);
						}
					}
			
					tvReplaceTotal.setText(printResponse.getComponentSumFee());
					tv_repair_total.setText(printResponse.getRepairfeesSumFee());
					tv_scrap_value.setText(printResponse.getResetFee());
					tv_deflosstotalmoney.setText(printResponse.getVerifyLossFee());
					tv_bigletter.setText(printResponse.getVertfyLossfee1());
				} else {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					Message message = Message.obtain();
					if(result!=null){
						message.obj = result.getResponseMessage();
					}else{
						message.obj = "打印信息获取失败!";
					}
					message.what = ConstantValue.ERROE;
					handler.sendMessage(message);
				}

			}

			@Override
			protected CommonResponse doInBackground(String... params) {
				PrintRequest printRequest = new PrintRequest();
				printRequest.setRegistNo(registno);
				printRequest.setLossNo(itemno);
				printRequest.setUserCode(SystemConfig.USERLOGINNAME);
				return PrintHttpService.printService(printRequest, context.getString(R.string.http_url));
			}
		}.execute();
	}
	
	
	class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton button, boolean flag) {
			// TODO Auto-generated method stub
			if(button.isChecked()){
				button.setButtonDrawable(android.R.drawable.checkbox_on_background);
			}else{
				button.setButtonDrawable(android.R.drawable.checkbox_off_background);
			}
			
		}
		
	}
}
