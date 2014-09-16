package com.sinosoftyingda.fastclaim.zhike;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class PrintUtils {
	public static String ErrorMessage = "No_Error_Message";
	public static boolean TextPosWinStyle = false;
	// private static final UUID SPP_UUID =
	// UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static OutputStream myOutStream = null;
	private static InputStream myInStream = null;
	private static BluetoothSocket mySocket = null;
	private static BluetoothAdapter myBluetoothAdapter;
	private static BluetoothDevice myDevice;

	public static boolean open(BluetoothAdapter myBluetoothAdapter,
			BluetoothDevice btDevice) {
		if (SPPOpen(myBluetoothAdapter, btDevice) == false)
			return false;
		return true;
	}

	/**************************************************************/
	public static void close() {
		SPPClose();
	}

	public static boolean OpenPrinter(String BDAddr) {
		if (BDAddr.equals("")) {
			ErrorMessage = "没有可用的打印机";
			return false;
		}
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (myBluetoothAdapter == null) {
			ErrorMessage = "蓝牙系统错误";
			return false;
		}
		myDevice = myBluetoothAdapter.getRemoteDevice(BDAddr);
		if (myDevice == null) {
			ErrorMessage = "读取蓝牙设备错误";
			return false;
		}
		if (!SPPOpen(myBluetoothAdapter, myDevice)) {
			return false;
		}
		return true;
	}

	private static boolean SPPOpen(BluetoothAdapter BluetoothAdapter,
			BluetoothDevice btDevice) {
		boolean error = false;
		myBluetoothAdapter = BluetoothAdapter;
		myDevice = btDevice;

		if (!myBluetoothAdapter.isEnabled()) {
			ErrorMessage = "蓝牙适配器没有打开";
			return false;
		}
		// myBluetoothAdapter.cancelDiscovery();

		try {
			// mySocket = myDevice.createRfcommSocketToServiceRecord(SPP_UUID);
			Method m = myDevice.getClass().getMethod("createRfcommSocket",
					new Class[] { int.class });
			mySocket = (BluetoothSocket) m.invoke(myDevice, 1);
		} catch (SecurityException e) {
			mySocket = null;
			ErrorMessage = "蓝牙端口错误";
			return false;
		} catch (NoSuchMethodException e) {
			mySocket = null;
			ErrorMessage = "蓝牙端口错误";
			return false;
		} catch (IllegalArgumentException e) {
			mySocket = null;
			ErrorMessage = "蓝牙端口错误";
			return false;
		} catch (IllegalAccessException e) {
			mySocket = null;
			ErrorMessage = "蓝牙端口错误";
			return false;
		} catch (InvocationTargetException e) {
			mySocket = null;
			ErrorMessage = "蓝牙端口错误";
			return false;
		}

		try {
			mySocket.connect();
		} catch (IOException e2) {
			ErrorMessage = e2.getLocalizedMessage();// "无法连接蓝牙打印机";
			mySocket = null;
			return false;
		}

		try {
			myOutStream = mySocket.getOutputStream();
		} catch (IOException e3) {
			myOutStream = null;
			error = true;
		}

		try {
			myInStream = mySocket.getInputStream();
		} catch (IOException e3) {
			myInStream = null;
			error = true;
		}

		if (error) {
			SPPClose();
			return false;
		}

		return true;
	}

	private static boolean SPPClose() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		if (myOutStream != null) {
			try {
				myOutStream.flush();
			} catch (IOException e1) {
			}
			try {
				myOutStream.close();
			} catch (IOException e) {
			}
			myOutStream = null;
		}
		if (myInStream != null) {
			try {
				myInStream.close();
			} catch (IOException e) {
			}
			myInStream = null;
		}

		if (mySocket != null) {
			try {
				mySocket.close();
			} catch (IOException e) {
			}
			mySocket = null;
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		return true;
	}

	public static boolean SPPWrite(byte[] Data, int DataLen) {
		try {
			myOutStream.write(Data, 0, DataLen);
		} catch (IOException e) {
			ErrorMessage = "发送蓝牙数据失败";
			return false;
		}
		return true;
	}

	public static boolean SPPWrite(byte[] buffer) {
		try {
			myOutStream.write(buffer);
		} catch (IOException e) {
			ErrorMessage = "发送蓝牙数据失败";
			return false;
		}
		return true;
	}

	public static boolean SPPReadTimeout(byte[] Data, int DataLen, int Timeout) {
		int i;
		for (i = 0; i < (Timeout / 50); i++) {
			try {
				if (myInStream.available() >= DataLen) {
					try {
						myInStream.read(Data, 0, DataLen);
						return true;
					} catch (IOException e) {
						ErrorMessage = "读取蓝牙数据失败";
						return false;
					}
				}
			} catch (IOException e) {
				ErrorMessage = "读取蓝牙数据失败";
				return false;
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				ErrorMessage = "读取蓝牙数据失败";
				return false;
			}
		}
		ErrorMessage = "蓝牙读数据超时";
		return false;
	}

	private static void SPPFlush() {
		int i = 0, DataLen = 0;
		try {
			DataLen = myInStream.available();
		} catch (IOException e1) {
		}
		for (i = 0; i < DataLen; i++) {
			try {
				myInStream.read();
			} catch (IOException e) {
			}
		}
	}

	public static boolean zp_open(BluetoothAdapter myBluetoothAdapter,
			BluetoothDevice btDevice) {
		if (SPPOpen(myBluetoothAdapter, btDevice) == false)
			return false;
		return true;
	}
}
