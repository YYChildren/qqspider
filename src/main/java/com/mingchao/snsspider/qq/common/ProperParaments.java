package com.mingchao.snsspider.qq.common;

import java.io.InputStream;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Properties;

import com.mingchao.snsspider.logging.Log;
import com.mingchao.snsspider.logging.LogFactory;

public class ProperParaments extends BaseParaments {
	protected Log log = LogFactory.getLog(this.getClass());

	public static final String PROJECT_NAME = "project.name";
	public static final String PROJECT_PATH = "project.path";
	public static final String HADOOP_BASEPATH = "hadoop.basepath";// 文件存储路径

	public static final String EXECUTOR_POOL_SIZE = "executor.pool.size";// 线程池数量
	public static final String WEBDRIVER_POOL_SIZE = "webdriver.pool.size";// webDriver
																			// 池数量
	public static final String COOKIE_POOL_SIZE = "cookie.pool.size";// Cookie池大小

	public static final String BLOOM_EXPECTED = "bloom.expected";// 布隆过滤器预期接收用户
	public static final String BLOOM_FPP = "bloom.fpp";// 布隆过滤器所容忍的错误率
	public static final String BLOOM_PATH = "bloom.path";// 布隆过滤器缓存路径

	public static final String ACOUNT_USER = "acount.user";// 当前应用用于登录的账号
	public static final String ACOUNT_PASSWORD = "acount.password";// 当前应用用于登录的账号

	public static final String PROPERTIES_NAME = "paraments.properties";// paraments文件名

	public ProperParaments() {
		init();
	}

	private void init() {
		InputStream input = null;
		try {
			Properties properties = new Properties();
			URL url  = Thread.currentThread().getContextClassLoader().getResource(PROPERTIES_NAME);
			input = url.openStream();
			properties.load(input);
			for (Entry<Object, Object> element : properties.entrySet()) {
				setParaments((String)element.getKey(),(String)element.getValue());
			}
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException(e);
		} finally {
			try {
				if(input!=null){
					input.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setParaments(String key, String value) {
		switch (key) {
		case PROJECT_NAME:
			this.projectName = value;
			break;
		case PROJECT_PATH:
			this.projectPath = value;
			break;
		case HADOOP_BASEPATH:
			this.hadoopPath = value;
			break;
		case EXECUTOR_POOL_SIZE:
			this.executorPoolSize = Integer.parseInt(value);
			break;
		case WEBDRIVER_POOL_SIZE:
			this.webDriverPoolSize = Integer.parseInt(value);
			break;
		case COOKIE_POOL_SIZE:
			this.cookiePoolSize = Integer.parseInt(value);
			break;
		case BLOOM_EXPECTED:
			this.bloomExected = Long.parseLong(value);
			break;
		case BLOOM_FPP:
			this.bloomFpp = Double.parseDouble(value);
			break;
		case BLOOM_PATH:
			this.bloomPath = value;
			break;
		case ACOUNT_USER:
			this.accountUser = value;
			break;
		case ACOUNT_PASSWORD:
			this.accountPassword = value;
			break;
		default:
			break;
		}
	}
}
