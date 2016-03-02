package com.mingchao.snsspider.qq.common;

import java.util.Map;

public class BaseParaments implements Paraments {

	//TODO 改为配置文件输入
	public static final String HADOOP_BASEPATH = "hadoop.basepath";//文件存储路径
	
	public static final String EXECUTOR_POOL_SIZE = "executor.pool.size";//线程池数量
	public static final String WEBDRIVER_POOL_SIZE = "webdriver.pool.size";//webDriver 池数量
	public static final String COOKIE_POOL_SIZE = "cookie.pool.size";//Cookie池大小
	
	public static final String BLOOM_EXPECTED = "bloom.expected";//布隆过滤器预期接收用户
	public static final String BLOOM_FPP = "bloom.fpp";//布隆过滤器所容忍的错误率
	
	public static final String APP_PATH = "app.path";//当前应用的文件存储子路径
	public static final String ACOUNT_USER = "acount.user";//当前应用用于登录的账号
	public static final String ACOUNT_PASSWORD = "acount.password";//当前应用用于登录的账号
	
	private Map<String,Object> paraments;
	public BaseParaments() {
	}
	public Map<String,Object> getParaments() {
		return paraments;
	}
	public void setParaments(Map<String,Object> paraments) {
		this.paraments = paraments;
	}
	
	@Override
	public String getHadoopPath() {
		// TODO Auto-generated method stub
		return "c:\\snsspider";
	}

	@Override
	public int getExecutorPoolSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getWebDriverPoolSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getCookiePoolSize() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getBloomExected() {
		// TODO Auto-generated method stub
		return 50000000;
	}

	@Override
	public double getBloomFpp() {
		// TODO Auto-generated method stub
		return 0.0001;
	}

	@Override
	public String getAppPath() {
		// TODO Auto-generated method stub
		return "qq";
	}

	@Override
	public String getAccountUser() {
		// TODO Auto-generated method stub
		return "2463328396";
	}

	@Override
	public String getAccountPassword() {
		// TODO Auto-generated method stub
		return "li27liguanhui";
	}
}
