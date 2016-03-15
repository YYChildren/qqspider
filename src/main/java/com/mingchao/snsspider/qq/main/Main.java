package com.mingchao.snsspider.qq.main;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.main.SpiderServiceCtl;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.common.ParamentsProvider;
import com.mingchao.snsspider.util.Crawlable;

public class Main {
	private static Log log = LogFactory.getLog(Main.class);
	private static final int PORT=6666;
	
	@SuppressWarnings("unused")
	private static Paraments para = ParamentsProvider.getInstance(); 
	public static void main(String[] args) {
		int status = 0;
		String command;
		int port = PORT;
		try {
			switch(args.length){
			case 2:
				port = Integer.parseInt(args[1]);
			case 1:
				command = args[0];
				Main mainObject = new Main(port);
				switch (command) {
				case "start":
					mainObject.start();
					mainObject.stop();
					break;
				case "stop":
					mainObject.close();
					break;
				}
				break;
			default:
				System.err.println("Argument: [start/stop] [port]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.fatal(e, e);
			status = 1;
		}
		System.exit(status);
	}
	
	private int port;
	private Registry reg;
	
	public Main(){
		this(PORT);
	}
	
	public Main(int port){
		this.port = port;
	}
	
	public void start() throws RemoteException, MalformedURLException, NotBoundException{
		Crawlable spider = new Spider(this);
		spider.crawl();
		reg = SpiderServiceCtl.startServer(spider, port);
	}
	
	public synchronized void notifyStop(){
		notify();
	}
	
	public synchronized void stop() throws AccessException, RemoteException, NotBoundException, InterruptedException{
		wait();
		SpiderServiceCtl.stopServer(reg);
	}

	// 另起客户端调用
	public void close() throws MalformedURLException, RemoteException, NotBoundException{
		SpiderServiceCtl.close(port);
	}
}
