/** @file AudioRecordModual.java
 *  @note HangZhou Hikvision System Technology Co., Ltd. All Right Reserved.
 *  @brief 音频模块类定义
 *
 *  @author     Dengshihua
 *  @date       2011-11-09
 *
 *  @note Created By Dengshihua     2011-11-09
 *
 *  @warning
 */

package com.sinosoftyingda.fastclaim.hkvideo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioModual
{
//    private final static String TAG = "MPUSDKDemo";
    
    // 16000hz的音频采样
    private final static int SAMPLE_RATE_16000 = 16000;
    
    // 每40ms一个采样回调
    private final static int AUDIO_BUFFER_SIZE = 1280;
    
    // 静态变量(单态模式)
    private static AudioModual mAudioModual = null;
    
    /*
     * 录音部分
     */
    // 录音线程
    private Thread mAudioRecordThread = null;
    
    // 音频对象
    private AudioRecord mAudioRecord = null;
    
    // 音频缓冲区大小
    private int mAudioBufferSize = 0;
    
    // 音频缓冲区
    private byte[] mAudioBuffer = null;
    
    // 是否在录音
    private boolean mRecording = false;
    
    // 停止录音标志
    private boolean mStopRecord = false;
    
    // 录音回调对象
    private AudioCallBack mAudioCallBack = null;
    
    /*
     * 放音部分
     */
    // 音频播放对象
    private AudioTrack mAudioTrack = null;
    
    /*
     * 音频文件播放部分
     */
    // 文件读取线程
    private Thread mReadFileThread = null;
    
    // 是否正在读取文件
    boolean mReading = false;
    
    // 停止读取文件标志
    boolean mStopRead = false;
    
    // 获取VideoModual实例
    public static AudioModual getInstance()
    {
        if (mAudioModual == null)
        {
            mAudioModual = new AudioModual();
        }
        
        return mAudioModual;
    }
    
    private AudioModual()
    {
        
    }
    
    /** @fn setCallBack
     *  @brief 初始化
     *  @param aAudioCallBack - 音频回调对象
     *  @return 无
     */
    public void setCallBack(AudioCallBack aAudioCallBack)
    {
        // 回调监听对象初始化
        mAudioCallBack = aAudioCallBack;
    }
    
    /** @fn startRecord
     *  @brief 开始录音
     *  @param 无
     *  @return 无
     */
    public void startRecord()
    {
        if (mRecording)
        {
            return;
        }
        
        // 获取音频所需要的缓冲区大小
        mAudioBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_16000, 
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        
        if (mAudioBufferSize <= 0)
        {
            Log.i("MPUSDK", "Audio Buffer Size:" + mAudioBufferSize);
            mAudioBufferSize = AUDIO_BUFFER_SIZE;
        }
        
        mAudioBuffer = new byte[mAudioBufferSize];
        
        // 音频对象初始化(以16000采样\单声道\16bit为参数录制PCM音频)
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
                SAMPLE_RATE_16000, 
                AudioFormat.CHANNEL_CONFIGURATION_MONO, 
                AudioFormat.ENCODING_PCM_16BIT,
                mAudioBufferSize);
        
        // 开启录音线程
        mAudioRecordThread = new Thread(null, new RecordThread(), "RecordThread");
        
        // 开启录音线程
        mStopRecord = false;
        mAudioRecordThread.start();
    }
    
    /** @fn stopRecord
     *  @brief 停止录音
     *  @param 无
     *  @return 无
     */
    public void stopRecord()
    {
        mStopRecord = true;
        try
        {
            mAudioRecordThread.join();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /** @fn startPlay
     *  @brief 开始播放
     *  @param 无
     *  @return 无
     */
    public void startPlay()
    {
        // initialize AudioTrack(minBufSize eques 1486)
        int minBufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE_16000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                SAMPLE_RATE_16000,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufSize,
                AudioTrack.MODE_STREAM);
        
        mAudioTrack.play();
    }
    
    /** @fn stopPlay
     *  @brief 停止播放
     *  @param 无
     *  @return 无
     */
    public void stopPlay()
    {
        if (mAudioTrack != null)
        {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }
    
    /** @fn inputData
     *  @brief 塞入数据
     *  @param data - 音频数据(IN)
     *  @return 无
     */
    public void inputData(byte[] data)
    {
        if (mAudioTrack != null)
        {
            mAudioTrack.write(data, 0, data.length);
        }
    }
    
    /** @fn startReadFile
     *  @brief 开始读文件
     *  @param 无
     *  @return 无
     */
    public void startReadFile()
    {
        if (mReading)
        {
            return;
        }
        
        // 创建线程
        mReadFileThread = new Thread(null, new ReadFileThread(), "ReadFileThread");
        
        // 开启读文件线程
        mReadFileThread.start();
    }

    /** @fn stopReadFile
     *  @brief 停止读文件
     *  @param 无
     *  @return 无
     */
    public void stopReadFile()
    {
        mStopRead = true;
        if (mReadFileThread == null)
        {
            return;
        }
        
        try
        {
            mReadFileThread.join();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    // 录音线程
    class RecordThread extends Thread 
    {  
        @Override
		public void run() 
        {
            if (mAudioRecord == null)
            {
                return;
            }
            
            mRecording = true;
            
            // 开始录制  
            mAudioRecord.startRecording();
            while (!mStopRecord) 
            {  
                // 从MIC保存数据到缓冲区  
                int readResult = mAudioRecord.read(mAudioBuffer, 0, mAudioBufferSize);  
                if (readResult > 0 && mAudioCallBack != null)
                {
                    mAudioCallBack.onPCMData(mAudioBuffer);
                }
            }   
            mAudioRecord.stop();  
            mRecording = false;
        }
    };  
    
    // 读取文件线程
    class ReadFileThread extends Thread 
    {  
        @Override
		public void run() 
        {
            mReading = true;
            
            String fileName = "/mnt/sdcard/test.pcm";
            byte[] readBytes = new byte[1280];
            int byteread = 0;
            InputStream in = null;
            
            mStopRead = false;
            
            // 读取文件, 然后塞数据
            try
            {
                in = new FileInputStream(fileName);
                
                //读入多个字节到字节数组中，byteread为一次读入的字节数 
                while (!mStopRead)
                {
                    byteread = in.read(readBytes);
                    if (byteread == -1)
                    {
                        in.close(); 
                        in = null;
                        in = new FileInputStream(fileName);
                        continue;
                    }
                    
                    mAudioCallBack.onPCMData(readBytes);
                    
                    Thread.sleep(40);
                } 
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            } 
            finally 
            { 
                if (in != null)
                { 
                    try 
                    { 
                        in.close(); 
                        mReading = false;
                    } 
                    catch (IOException e) 
                    { 
                    } 
                } 
            }
        }
    };
    
    /**
     * @fn AudioCallBack
     * @brief 音频数据回调接口
     */
    public interface AudioCallBack
    {
        public void onPCMData(byte[] data);
    }
}