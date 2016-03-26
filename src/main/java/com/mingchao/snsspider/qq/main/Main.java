package com.mingchao.snsspider.qq.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

import org.apache.commons.codec.Charsets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.io.Files;
import com.mingchao.snsspider.main.SpiderJmxLuncher;
import com.mingchao.snsspider.main.SpiderLuncher;
import com.mingchao.snsspider.qq.common.Paraments;
import com.mingchao.snsspider.qq.provider.ResourceProvider;
import com.mingchao.snsspider.util.Crawlable;

public class Main {
	private static Log log = LogFactory.getLog(Main.class);
	private static Paraments para = ResourceProvider.INSTANCE.getParaments();

	public static void main(String[] args) {
		int status = 0;
		String command;
		try {
			switch (args.length) {
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

	private static void savePid() throws IOException {
		String path = para.getPidFile();
		File file = new File(path);
		if (file.exists()) {
			String oldPid = readPid();
			InputStream in = Runtime.getRuntime().exec("jps").getInputStream();
			BufferedReader b = new BufferedReader(new InputStreamReader(in));
			String line = null;
			boolean processExists = false;
			while ((line = b.readLine()) != null) {
				if(oldPid.equals(line.split("\\s+")[0])){
					processExists = true;
					break;
				}
			}
			in.close();
			if (processExists) {
				throw new IOException("qqspider is alive! Pid is " + oldPid );
			}
		}else{
			Files.createParentDirs(file);
		}
		String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		Files.write(pid, file, Charsets.UTF_8);
	}

	private static String readPid() throws IOException {
		String path = para.getPidFile();
		File file = new File(path);
		return Files.readFirstLine(file, Charsets.UTF_8);
	}

	private static void deletePidFile() throws IOException {
		String path = para.getPidFile();
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
}

class SpiderServer implements NotifyStopAble {
	private Crawlable spider;
	private SpiderLuncher spiderLuncher;

	SpiderServer() {
		spider = new Spider(this);
		spiderLuncher = new SpiderJmxLuncher(spider);
	}

	void start() {
		spiderLuncher.startServer();
		spider.crawl();
	}

	synchronized void stop() throws InterruptedException {
		wait();
		spiderLuncher.stopServer();
	}

	@Override
	public synchronized void notifyStop() {
		notify();
	}

}

class SpiderCloseClient {
	private SpiderLuncher spiderLuncher;

	SpiderCloseClient(String pid) {
		spiderLuncher = new SpiderJmxLuncher(pid);
	}

	void close() {
		spiderLuncher.close();
	}
}
