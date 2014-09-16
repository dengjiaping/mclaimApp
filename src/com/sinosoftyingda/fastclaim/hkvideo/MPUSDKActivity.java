package com.sinosoftyingda.fastclaim.hkvideo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.hikvision.mpusdk.LOGIN_INFO;
import com.hikvision.mpusdk.MPUSDK;
import com.hikvision.mpusdk.MediaDataCallback;
import com.hikvision.mpusdk.MessageCallback;
import com.hikvision.mpusdk.VideoEncodeParam;
import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.config.SystemConfig;
import com.sinosoftyingda.fastclaim.hkvideo.VideoModual.VideoCallBack;

/**
 * 海康视频
 * 
 * @author DengGuang
 * 
 */
public class MPUSDKActivity extends Activity implements
		MediaDataCallback, AudioModual.AudioCallBack, VideoCallBack, MessageCallback, OnClickListener {
	private final String TAG = "MPUSDKDemo";

	// 视频部分参数定义,VideoModual主要完成视频采集功能,SurfaceView
	// 是视频图像显示的对象,在res/layout/main.xml中定义资源ID, 然后
	// 通过findViewById引入
	private VideoModual mVideoModual = null;
	private SurfaceView mSurfaceView = null;

	private Button mBtnConnect = null;
	private Button mBtnOffline = null;
	
	// 音频模块,在该对象中实现了音频采集,音频播放功能
	private AudioModual mAudioModual = null;

	// MPUSDK对象定义,在MPUSDK中实现了视频编码,音频编解码,网络通信功能
	private MPUSDK mMPUSDK = null;

	// 播放标识,防止重复播放问题
	private boolean mAudioPlaying = false;
	private boolean mVideoPlaying = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置不显示标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 高亮
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// 设置应用不休眠
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// 初始化
		setContentView(R.layout.hkvideo);
		init();
	}

	// 全局消息处理函数,处理MPUSDK回调的消息
	private Handler mGloableHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MPUSDK.MSG_REGISTER_OK: // 登录成功
				Toast.makeText(getApplicationContext(), R.string.sdk_message_login_success, Toast.LENGTH_LONG).show();
				mBtnConnect.setBackgroundDrawable(getResources().getDrawable(R.drawable.hkvideo_connect1_style));
				break;
			case MPUSDK.MSG_REGISTER_FAIL: // 登录失败
				Toast.makeText(getApplicationContext(), R.string.sdk_message_login_fail, Toast.LENGTH_LONG).show();
				mBtnConnect.setBackgroundDrawable(getResources().getDrawable(R.drawable.hkvideo_connect2_style));
				break;
			case MPUSDK.MSG_KEEPALIVE_FAIL: // 保活失败
				Toast.makeText(getApplicationContext(), R.string.sdk_message_keepalive_fail, Toast.LENGTH_LONG).show();
				break;
			case MPUSDK.MSG_STARTVOICETALK_REQ: // 开始语音对讲
				Toast.makeText(getApplicationContext(), R.string.sdk_message_start_voicetalk, Toast.LENGTH_LONG).show();
				if (!mAudioPlaying) {
					mMPUSDK.onStartVoiceTalkRequest();
					mAudioModual.startRecord();
					mAudioModual.startPlay();
					mAudioPlaying = true;
				}
				break;
			case MPUSDK.MSG_STOPVOICETALK_REQ: // 停止语音对讲
				Toast.makeText(getApplicationContext(), R.string.sdk_message_stop_voicetalk, Toast.LENGTH_LONG).show();
				if (mAudioPlaying) {
					mMPUSDK.onStopVoiceTalkRequest();
					mAudioModual.stopRecord();
					mAudioModual.stopPlay();
					mAudioPlaying = false;
				}
				break;
			case MPUSDK.MSG_STARTPLAY_REQ: // 开始视频上传
				Toast.makeText(getApplicationContext(), R.string.sdk_message_start_realplay, Toast.LENGTH_LONG).show();
				if (!mVideoPlaying) {
					mMPUSDK.onStartRealPlayRequest();
					mVideoPlaying = true;
				}
				break;
			case MPUSDK.MSG_STOPPLAY_REQ: // 停止视频上传
				Toast.makeText(getApplicationContext(), R.string.sdk_message_stop_realplay, Toast.LENGTH_LONG).show();
				mBtnConnect.setBackgroundDrawable(getResources().getDrawable(R.drawable.hkvideo_connect2_style));
				logout();
				if (mVideoPlaying) {
					mMPUSDK.onStopRealPlayRequest();
					mVideoPlaying = false;
				}
				break;
			default:
				break;
			}
		}
	};

	private void init() {
		// 视频部分
		mVideoModual = VideoModual.getInstance();
		mVideoModual.setCallBack(this);
		mSurfaceView = (SurfaceView) this.findViewById(R.id.cameraView);
		mBtnConnect = (Button)this.findViewById(R.id.hkvideo_btn_connect);
		mBtnOffline = (Button)this.findViewById(R.id.hkvideo_btn_offline);
		
		if (mSurfaceView != null) {
			mVideoModual.startPreview(mSurfaceView);
		}

		// 音频部分
		mAudioModual = AudioModual.getInstance();
		mAudioModual.setCallBack(this);

		// SDK部分,目前只支持UDP传输
		mMPUSDK = MPUSDK.getInstance();
		MPUSDK.init(MPUSDK.PROTOCOL_UDP);
		mMPUSDK.setMessageCallback(this);
		mMPUSDK.setMediaDataCallback(this);
		
		mSurfaceView.setOnClickListener(this);
		mBtnConnect.setOnClickListener(this);
	}

	private void fini() {
		mVideoModual.stopPreview();
		MPUSDK.fini();
		mMPUSDK = null;

		mAudioModual = null;
		mVideoModual = null;

		this.finish();
//		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "请求协助");
		menu.add(0, 1, 0, "断开连接");
//		menu.add(0, 2, 0, "获取参数");
//		menu.add(0, 3, 0, "设置参数");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case 0:
			login();
			break;
		case 1:
			logout();
			fini();
			break;
		case 2:
			getParam();
			break;
		case 3:
			setParam();
			break;
		default:
			break;
		}
		return true;
	}

	private void login() {
		// 登录平台
		LOGIN_INFO loginInfo = new LOGIN_INFO();
		loginInfo.servIP = SystemConfig.loginResponse.getHikserViceIp().getBytes();
		loginInfo.servPort = Integer.parseInt(SystemConfig.loginResponse.getHikserVicePort());
		loginInfo.deviceID = SystemConfig.loginResponse.getDeviceId().getBytes();
		loginInfo.password = SystemConfig.loginResponse.getDeviceId().getBytes();
		
		if (!mMPUSDK.login(loginInfo)) {
			Message aMessage = new Message();
			aMessage.what = MPUSDK.MSG_REGISTER_FAIL;
			mGloableHandler.sendMessage(aMessage);
		}
	}

	private void logout() {
		if(mMPUSDK != null){
			// 登出平台
			mMPUSDK.logout();
		}
	}

	private void getParam() {
		// 获取视频编码参数
		VideoEncodeParam param = new VideoEncodeParam();
		mMPUSDK.getVideoEncodeParam(param);

		Log.i("HKTAG", "bitrate:" + param.bitrate);
		Log.i("HKTAG", "frameRate:" + param.frameRate);
		Log.i("HKTAG", "iFramePeriod:" + param.iFramePeriod);
	}

	private void setParam() {
		// 获取视频编码参数
		VideoEncodeParam param = new VideoEncodeParam();
		mMPUSDK.getVideoEncodeParam(param);
		param.bitrate += 512000;
		param.frameRate -= 2;
		param.iFramePeriod -= 10;

		// 设置视频编码参数
		mMPUSDK.setVideoEncodeParam(param);
	}

	// MPUSDK消息回调函数(重写MessageCallback类中的方法)
	@Override
	public void onMessage(int msgCode, byte[] msgText) {
		// 发送消息,统一处理
		Message aMessage = new Message();
		aMessage.what = msgCode;
		mGloableHandler.sendMessage(aMessage);
	}

	// MPUSDK多媒体数据回调函数(重写MediaDataCallback类中的方法)
	@Override
	public void onAudioData(byte[] data) {
		// input到音频模块,播放声音数据
		mAudioModual.inputData(data);
	}

	// MPUSDK多媒体数据回调函数(重写MediaDataCallback类中的方法,暂不可用)
	@Override
	public void onPictureData(byte[] data) {
	}

	// MPUSDK多媒体数据回调函数(重写MediaDataCallback类中的方法,暂不可用)
	@Override
	public void onTextData(byte[] data) {
	}

	// MPUSDK多媒体数据回调函数(重写MediaDataCallback类中的方法,暂不可用)
	@Override
	public void onVideoData(byte[] data) {
	}

	// MPUSDK多媒体数据回调函数(重写MediaDataCallback类中的方法,暂不可用)
	@Override
	public void onPCMData(byte[] data) {
		try {
			mMPUSDK.inputVoiceData(data, data.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMsg(int msg, String msgText) {
		
	}

	@Override
	public void onPictureData(byte[] data, int dataType) {
		
	}

	// 视频数据回调函数(重写VideoCallBack类中的方法)
	@Override
	public void onVideoData(byte[] data, int dataType) {
		try {
			if (mVideoPlaying) {
				mMPUSDK.inputVideoData(data, data.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 自动对焦
		case R.id.cameraView:
			mVideoModual.aotoFocus();
			break;
			
		// 连接
		case R.id.hkvideo_btn_connect:
			login();
			break;

		// 断开连接
		case R.id.hkvideo_btn_offline:
			logout();
			break;
		}
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			logout();
			fini();
			break;
		default:
			break;
		}

		return false;
	}

}