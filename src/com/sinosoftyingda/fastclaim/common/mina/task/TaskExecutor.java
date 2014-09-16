package com.sinosoftyingda.fastclaim.common.mina.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskExecutor {

	public static final String Type_SingleThread = "Single Thread";
	
	private static byte[] lock = new byte[0];
	
	private static TaskExecutor taskExecutor;
	
	private ExecutorService service;
	
	private List<Runnable> taskList = new ArrayList<Runnable>();
	
	private boolean running;
		
	private TaskExecutor(String type){
		if(type.equals(Type_SingleThread)){
			service = Executors.newSingleThreadExecutor();
		}else{
			service = Executors.newSingleThreadExecutor();
		}
		running = false;
	}
	
	public synchronized static TaskExecutor getInstance(){
		if(taskExecutor==null){
			taskExecutor = new TaskExecutor(Type_SingleThread);
		}
		return taskExecutor;
	}
	
	/**
	 * ����������У��ö��еĸ�������֮�䲻���й�ֵĹ�����<br>
	 * �磺ֻ��ע��ɹ�(��Ҫ�ȴ���Ӧ)����ֻ�ܵ�½�������ܽ�����������ڼ�����ִ�д˷���
	 * @param taskList
	 */
	public void runTask(List<Runnable> taskList){
		synchronized (lock) {
			if(taskList!=null&&taskList.size()>=1){
				for(Runnable task:taskList){
					service.submit(task);
				}
			}

		}
	}
	
	/**
	 * ���һ���������������û������������ִ�и�����<br>
	 * �˷�����Ҫ�Ǵ��?Ӧ�������ӡ�ע�ᡢ��¼�������������
	 * @param task
	 */
	public void submitTask(Runnable task){
		service.submit(task);
	}
	
	public void shutdown(){
		if(!service.isShutdown()){
			service.shutdown();
		}
		running = false;
	}
	
	public void shutdownNow(){
		if(!service.isShutdown()){
			service.shutdownNow();
		}
		running = false;
	}
	
	public void addTask(List<Runnable> list){
		if(list.size()>=1){
			for(Runnable task:list){
				taskList.add(task);
			}
		}
	}
	
	public void submitTask(){
		synchronized (lock) {
			if(taskList.size()>=1){
				Runnable task = taskList.get(0);
				service.submit(task);
				taskList.remove(0);
			}
		}
	}
	
	public void addTask(Runnable task){
		synchronized (lock) {
			if(taskList.size()==0&&!running){
				running = true;
				service.submit(task);
			}else{
				taskList.add(task);
			}
		}
	}


	/**************����set��get*****************/
	
	public List<Runnable> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Runnable> taskList) {
		this.taskList = taskList;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	
	
}
