/** @file VideoModual.java
 *  @note HangZhou Hikvision System Technology Co., Ltd. All Right Reserved.
 *  @brief 视频模块类定义
 *
 *  @author     Dengshihua
 *  @date       2011-11-11
 *
 *  @note Created By Dengshihua     2011-11-11
 *
 *  @warning
 */

package com.sinosoftyingda.fastclaim.hkvideo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

/**
 * @fn VideoModual
 * @brief 视频模块
 */
public class VideoModual implements PreviewCallback, ErrorCallback, 
AutoFocusCallback, PictureCallback, Callback
{
    // 图片格式
    public static final int PICTURE_FORMAT_JPEG = 1;
    public static final int PICTURE_FORMAT_BMP = 2;
    
    // 日志标签
    private static final String TAG = "MPUSDKDemo";
    
    // 抓拍触发消息
    private static final int MSG_TAKEN_PICTURE = 1;
    
    // CIF分辨率
    private Resolution CIF = new Resolution(352, 288);
    
    // 静态变量(单态模式)
    private static VideoModual mVideoModual = null;
    
    // 视频回调对象
    private VideoCallBack mVideoCallBack = null;
    
    // Camera实例
    private Camera mCamera = null;
    
    // SurfaceHolder实例
    private SurfaceHolder mSurfaceHolder = null;
    
    // 表示是否正在预览
    private boolean mPreview = false;
    
    // Camera支持的参数
    private List<Resolution> mSurVideoResolution = null;
    private List<Resolution> mSurPictureResolution = null;
    
    // 视频分辨率和图片分辨率
    private Resolution mCurVideoResolution = null;
    private Resolution mCurPictureResolution = null;
    
    // 视频格式和图片格式
    private int mCurVideoFormat = ImageFormat.NV21;
    private int mCurPictureFormat = ImageFormat.JPEG;
    
    // 抓图参数(目前支持JPEG格式)
    private int mPictureSize = 0;
    private byte[] mPictureData = null;
    
    // 消息对象
    private Handler mHandler = null;
    
    // 获取VideoModual实例
    public static VideoModual getInstance()
    {
        if (mVideoModual == null)
        {
            mVideoModual = new VideoModual();
        }
        
        return mVideoModual;
    }
    
    /** @fn setCallBack
     *  @brief 设置回调函数
     *  @param videoCallBack - 视频回调对象(OUT)
     *  @return 无
     */
    public void setCallBack(VideoCallBack videoCallBack)
    {
        mVideoCallBack = videoCallBack;
    }
    
    /** @fn startPreview
     *  @brief 开始预览
     *  @param surfaceView - Surface View(IN)
     *  @return true - 成功
     *          false - 失败
     */
    public boolean startPreview(SurfaceView surfaceView)
    {
        if (mPreview)
        {
            Log.e(TAG, "previewing");
            return true;
        }
        
        if (surfaceView == null)
        {
            Log.e(TAG, "surfaceView eques null");
            return false;
        }
        
        // 获取surface holder
        mSurfaceHolder = surfaceView.getHolder();
        if (mSurfaceHolder == null)
        {
            Log.e(TAG, "surfaceHolder eques null");
            return false;
        }
        
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); 
        
        // 创建消息接收对象
        mHandler = new Handler()
        {
            @Override
			public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                case MSG_TAKEN_PICTURE:
                    if (mVideoCallBack != null)
                    {
                        mVideoCallBack.onPictureData(mPictureData, PICTURE_FORMAT_JPEG);
                    }
                    break;
                default:
                    break;
                }
            }
        };
        
        
        
        return true;
    }
    
    /** @fn stopPreview
     *  @brief 停止预览
     *  @param 无
     *  @return 无
     */
    public void stopPreview()
    {
        if (!mPreview || mCamera == null)
        {
            return;
        }
        
//        mCamera.stopPreview();
//        mCamera.release();
//        mCamera = null;
        
        mPreview = false;
    }
    
    /** @fn takenPicture
     *  @brief 拍照
     *  @param 无
     *  @return true - 成功
     *          false - 失败
     */
    public boolean takenPicture()
    {
        if (!mPreview || mCamera == null)
        {
            return false;
        }
        
        mCamera.takePicture(null, null, this);
        
        return true;
    }
    
    /** @fn setVideoSize
     *  @brief 设置视频尺寸
     *  @param width - 视频宽
     *  @param height - 视频高
     *  @return 无
     */
    public void setVideoSize(int width, int height)
    {
        
    }
    
    /** @fn setPictureSize
     *  @brief 设置图片尺寸
     *  @param width - 图片宽
     *  @param height - 图片高
     *  @return 无
     */
    public void setPictureSize(int width, int height)
    {
        
    }
    
    /** @fn aotoFocus
     *  @brief 自动对焦
     *  @param 无
     *  @return 无
     */
    public void aotoFocus()
    {
        if (mCamera != null)
        {
            mCamera.autoFocus(null);
        }
    }
    
    // 私有构造函数
    private VideoModual()
    {
    }
    
    /** @fn getParamFromApplication
     *  @brief 从配置文件中获取视频参数和图像参数
     *  @param 无
     *  @return true - 获取成功
     *          false - 获取失败
     */
    private boolean getParamFromApplication()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /** @fn setParamToApplication
     *  @brief 将视频参数和图像参数写入配置文件中
     *  @param 无
     *  @return true - 写入成功
     *          false - 写入失败
     */
    private boolean setParamToApplication()
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    private void preview()
    {
     // 开启摄像头
        mCamera = Camera.open();
        if (mCamera == null)
        {
            Log.e(TAG, "camera open fail!");
            return;
        }
        
        try
        {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        }
        catch (IOException e)
        {
            mCamera.release();
            e.printStackTrace();
            return;
        }
        
        // 设置视频数据回调函数
        mCamera.setPreviewCallback(this);
        
        // 设置出错回调函数
        mCamera.setErrorCallback(this);
        
        // 获取摄像头参数
        Camera.Parameters param = mCamera.getParameters();
        
        // 获取摄像头支持的预览分辨率
        List<Size> preSizes = param.getSupportedPreviewSizes();
        if (preSizes.size() > 0)
        {
            mSurVideoResolution = new ArrayList<Resolution>();
            for (Size size : preSizes)
            {
                Log.i(TAG, "videoSize:" + size.width + "*" + size.height);
                Resolution resulution = new Resolution();
                resulution.width = size.width;
                resulution.height = size.height;
                mSurVideoResolution.add(resulution);
            }
        }
        
        // 获取摄像头支持的图片分辨率
        List<Size> picSizes = param.getSupportedPictureSizes();
        if (picSizes.size() > 0)
        {
            mSurPictureResolution = new ArrayList<Resolution>();
            for (Size size : picSizes)
            {
                Resolution resulution = new Resolution();
                resulution.width = size.width;
                resulution.height = size.height;
                mSurPictureResolution.add(resulution);
            }
        }
        
        // 从配置文件中获取摄像头视频参数和图片参数
        boolean ret = getParamFromApplication();
        if (ret)
        {
            // 使用配置文件中的参数设置摄像头,使摄像头的数据与配置文件中的一致
            //param.setPreviewSize(mCurVideoResolution.height, mCurVideoResolution.width);
            param.setPictureSize(mCurPictureResolution.width, mCurPictureResolution.width);
            //param.setPreviewFormat(mCurVideoFormat);
            param.setPictureFormat(mCurPictureFormat);
        }
        else
        {
            // 使用当前摄像头参数作为默认参数,并设置到配置文件中
            //Size preSize = param.getPictureSize();
            //mCurVideoResolution.height = preSize.width;
            //mCurVideoResolution.width = preSize.height;
            
            //Size picSize = param.getPictureSize();
            //mCurPictureResolution.width = picSize.width;
            //mCurPictureResolution.width = picSize.height;
            
            // 设置视频图片的格式(模拟器不支持)
            //param.setPreviewFormat(mCurVideoFormat);
            //param.setPictureFormat(mCurPictureFormat);
            
            //param.setPreviewSize(352, 288);
            //param.setPreviewFormat(ImageFormat.NV21);
        }
        
        param.setPreviewSize(352, 288);
        param.setPreviewFormat(ImageFormat.NV21);
        
        Size preSize = param.getPreviewSize();
        Log.i(TAG, "preview size:" + preSize.width + "*" + preSize.height);
        
        // 将参数设置到配置文件中
        setParamToApplication();
        
        // 设置摄像头参数
        mCamera.setParameters(param);
        
        // 开始预览
        mCamera.startPreview();
        
        mPreview = true;
    }
    
    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        // TODO Auto-generated method stub
        if (mVideoCallBack != null)
        {
            mVideoCallBack.onVideoData(data, mCurVideoFormat);
        }
    }
    
    @Override
    public void onPictureTaken(byte[] data, Camera camera)
    {
        // 加上这个判断,避免mPictureData内存频繁分配
        int pictrueSize = data.length;
        if (mPictureData == null)
        {
            mPictureData = new byte[pictrueSize];
        }
        else if (pictrueSize != mPictureSize)
        {
            mPictureData = null;
            mPictureData = new byte[pictrueSize];
            mPictureSize = pictrueSize;
        }
        
        // 拷贝JPEG数据
        System.arraycopy(data, 0, mPictureData, 0, pictrueSize);
        
        // 发送消息
        if (mHandler != null)
        {
            Message msg = new Message();
            msg.what = MSG_TAKEN_PICTURE;
            mHandler.sendMessage(msg);
        }
        
        // 重新开始预览
        camera.startPreview();
    }
    
    @Override
    public void onAutoFocus(boolean arg0, Camera arg1)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onError(int error, Camera camera)
    {
        // TODO Auto-generated method stub
        if (mVideoCallBack != null)
        {
            mVideoCallBack.onMsg(error, "");
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
        // TODO Auto-generated method stub
        // 开始预览
        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
        preview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
    
    /**
     * @fn Resolution
     * @brief 分辨率定义
     */
    public class Resolution
    {
        public Resolution()
        {
            // TODO Auto-generated constructor stub
        }
        
        public Resolution(int w, int h)
        {
            this.width = w;
            this.height = h;
        }

        public int width = 0;
        public int height = 0;
    }
    
    /**
     * @fn VideoCallBack
     * @brief 视频数据回调接口
     */
    public interface VideoCallBack
    {
        /** @fn onVideoData
         *  @brief 视频数据回调
         *  @param data - 视频数据(OUT)
         *  @param dataType - 视频类型(OUT)
         *  @return 无
         */
        public void onVideoData(byte[] data, int dataType);
        
        /** @fn onPictureData
         *  @brief 图片数据回调
         *  @param data - 图片数据(OUT)
         *  @param dataType - 图片类型(OUT)
         *  @return 无
         */
        public void onPictureData(byte[] data, int dataType);
        
        /** @fn onMsg
         *  @brief 消息回调
         *  @param msg - 消息类型(OUT)
         *  @param msgText - 消息文字(OUT)
         *  @return 无
         */
        public void onMsg(int msg, String msgText);
    }
}
