package com.mingchao.snsspider.qq.main;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.main.SpiderJmxLuncher;
import com.mingchao.snsspider.main.SpiderLuncher;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.common.ParamentsProvider;
import com.mingchao.snsspider.util.Crawlable;

public class Main {
	private static Log log = LogFactory.getLog(Main.class);
	private static Paraments para = ParamentsProvider.getInstance(); 
	public static void main(String[] args) {
		int status = 0;
		String command;
		try {
			switch(args.length){
			case 1:
				command = args[0];
				switch (command) {
				case "start":
					savePid();
					SpiderServer server = new SpiderServer();
					server.start();
					server.stop();
					break;
				case "stop":
					String pid = readPid();
					SpiderCloseClient closer = new SpiderCloseClient(pid);
					closer.close();
					deletePidFile();
					break;
				}
				break;
			default:
				System.err.println("Argument: [start/stop]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.fatal(e, e);
			status = 1;
		}
		System.exit(status);
	}
	
	private static void savePid() throws IOException{
		String pidPath = ensuredPidPath();
		String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		Files.write(Paths.get(pidPath), pid.getBytes());
	}
	
	private static String readPid() throws IOException{
		String pidPath = ensuredPidPath();
		String pid = new String(Files.readAllBytes(Paths.get(pidPath)));
		return pid;
	}
	
	private static void deletePidFile() throws IOException{
		String pidPath = ensuredPidPath();
		Files.delete(Paths.get(pidPath));
	}
	
	private static String ensuredPidPath() throws IOException{
		String path = para.getProjectPath();
		Files.createDirectories(Paths.get(path));
		String project = para.getProjectName();
		String pidPath = path + File.separator + project + ".pid";
		return pidPath;
	}
}

class SpiderServer implements NotifyStopAble{
	private Crawlable spider;
	private SpiderLuncher spiderLuncher;
	
	SpiderServer(){
		spider = new Spider(this);
		spiderLuncher = new SpiderJmxLuncher(spider);
	}
	
	void start(){
		spiderLuncher.startServer();
		spider.crawl();
	}

	synchronized void stop() throws InterruptedException{
		wait();
		spiderLuncher.stopServer();
	}
	
	@Override
	public synchronized void notifyStop(){
		notify();
	}
	
}

class SpiderCloseClient{
	private SpiderLuncher spiderLuncher;
	SpiderCloseClient(String pid){
		spiderLuncher = new SpiderJmxLuncher(pid);
	}
	void close() {
		spiderLuncher.close();
	}
}

