package com.sinosoftyingda.fastclaim.common.mina;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.sinosoftyingda.fastclaim.common.mina.task.Offline;
import com.sinosoftyingda.fastclaim.common.mina.task.TaskExecutor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 终端推送机制 用于处理终端与服务端创建长连接 I/O 处理器用来执行具体的业务逻辑。对接收到的消息执行特定的处理。 <br>
 * I/O 处理器需要:<br>
 * 实现 org.apache.mina.core.service.IoHandler接口<br>
 * 或者<br>
 * 继承自 org.apache.mina.core.service.IoHandlerAdapter<br>
 * 
 * @author JingTuo
 * 
 */
public class MinaClientIOHandler implements IoHandler {

	private Context context;


	public MinaClientIOHandler(Context context) {
		this.context = context;
	}

	@Override
	public void sessionCreated(IoSession ioSession) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void sessionOpened(IoSession ioSession) throws Exception {
		// TODO Auto-generated method stub
		ioSession.resumeRead();
	}

	/**
	 * 可以处理捕获到的异常
	 */
	@Override
	public void exceptionCaught(IoSession ioSession, Throwable cause)
			throws Exception {
		Log.e("Mina", "--------》Exception: " + cause);
		if (!(cause.getMessage().contains("IOException"))) {
			Log.e("Mina", "--------》IOException: " + cause);
		} else {
			Log.e("Mina", "--------》OtherException: " + cause);
		}
	}

	@Override
	public void messageReceived(IoSession ioSession, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		String msg = message.toString().trim();
		System.out.println("Received:" + msg);
		MinaClientXmlParse.handle(ioSession, context, msg);
	}

	@Override
	public void messageSent(IoSession ioSession, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Send:" + message);
	}

	/**
	 * 连接断开时调用此方法
	 */
	@Override
	public void sessionClosed(IoSession ioSession) throws Exception {
		// add by haoyun 20130502
		System.out.println("开始");
		Intent intent = new Intent();
		intent.setAction("android.net.DISCONNECT");
       
		context.sendBroadcast(intent);
		// end by haoyun 20130502
		
		close(ioSession);
	}

	@Override
	public void sessionIdle(IoSession ioSession, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		// System.out.println("sessionIdle...");//如果设置空闲时间为10，则每隔10秒调用一次方法
	}

	private void close(IoSession ioSession) {
		if (ioSession != null) {
			CloseFuture future = ioSession.close(true);
			if (future != null) {
				future.awaitUninterruptibly();
			}
		}

		Log.e("Mina", "--------》网络断开，关闭长连接 ");
		// 断开长连接
		TaskExecutor.getInstance().submitTask(new Offline(context, ConnectionManager.getMinaClient(context)));
		
	}
}
