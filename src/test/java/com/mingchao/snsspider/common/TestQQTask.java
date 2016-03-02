package com.mingchao.snsspider.common;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.mingchao.snsspider.login.Login;
import com.mingchao.snsspider.qq.resource.Resource;
import com.mingchao.snsspider.qq.util.Agorithm;

public class TestQQTask {
	Resource resource = Resource.getInstance();
	public static void main(String[] args) {
		TestQQTask test = new TestQQTask();
		System.out.println(Agorithm.g_tk_hash("@gU2dlUcO9"));
		try {
			test.test();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	public void test() throws MalformedURLException{
		Login login = new LoginQQCookie();
		try {
			login.login();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String shuoshuo = "http://taotao.qq.com/cgi-bin/emotion_cgi_msglist_v6"
				+ "?uin=1946231687"
				+ "&ftype=0"
				+ "&sort=0"
				+ "&pos=0"
				+ "&num=40"
				+ "&replynum=100"
				+ "&g_tk=@GTK"
				// + "&callback=_preloadCallback"  or + "&callback=Callback" 
				+ "&code_version=1&format=jsonp&need_private_comment=1";
		HttpGet httpget = new HttpGet(shuoshuo.replace("@GTK", String.valueOf(Agorithm.g_tk("http://qzone.qq.com",null))));
		//httpget.setHeaders(resource.getHttpResource().getBaseHeaders());
		httpget.addHeader("Host", "taotao.qq.com");
		httpget.addHeader("Refer","http://ctc.qzs.qq.com/qzone/app/mood_v6/html/index.html");
		httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpget.addHeader("Upgrade-Insecure-Requests", "1");
		CloseableHttpClient httpclient = null;//resource.getHttpResource().getHttpclient();
		ResponseHandler<Boolean> reponseHandler = new ResponseHandler<Boolean>() {
			@Override
			public Boolean handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				System.out.println(response.getStatusLine().getStatusCode());
				HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);
				return true;
			};
		};
		try {
			httpclient.execute(httpget,reponseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
