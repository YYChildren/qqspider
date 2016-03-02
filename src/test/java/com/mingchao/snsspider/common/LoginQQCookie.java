package com.mingchao.snsspider.common;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mingchao.snsspider.http.CookieSns;
import com.mingchao.snsspider.logging.LogFactory;
import com.mingchao.snsspider.login.Login;
import com.mingchao.snsspider.login.view.LoginCookieView;
import com.mingchao.snsspider.qq.resource.Resource;

public class LoginQQCookie implements Login {
	private static BigDecimal TB = BigDecimal.valueOf(1000000000);
	private Resource resource = Resource.getInstance();

	public LoginQQCookie() {
	}

	public void login() throws IOException {
		if(visitMainPage()){
			LogFactory.getLog(this.getClass()).info("登录成功");
			return;
		}
		LoginCookieView lw = new LoginCookieView();
		lw.submit();
		JSONArray ja = new JSONArray(lw.getCookiesString());
		List<CookieSns> cookies= new ArrayList<CookieSns>();
		for (Iterator<Object> iterator = ja.iterator(); iterator.hasNext();) {
			JSONObject cookiejson = (JSONObject) iterator.next();
			String name = cookiejson.getString("name") ;
			String value = cookiejson.getString("value").replaceAll("\"", "");
			String domain = cookiejson.has("domain") ? domain = cookiejson.getString("domain") : null; 
			String path = cookiejson.has("path") ? cookiejson.getString("path") : null;
			Date expiry = cookiejson.has("expirationDate") ? createExpiry(cookiejson) : null;
			boolean session =  cookiejson.has("session") ? cookiejson.getBoolean("session") : false;
			boolean hostonly = cookiejson.has("hostonly") ? cookiejson.getBoolean("hostonly") : false;
			boolean secure = cookiejson.has("secure") ? cookiejson.getBoolean("secure") : false;
			boolean httponly = cookiejson.has("httponly") ? cookiejson.getBoolean("httponly") : false;
			CookieSns cookie = new CookieSns(name, value, domain, path, expiry, secure, httponly,session,hostonly);
			cookies.add(cookie);
		}
		//resource.addCookies(cookies.toArray(new CookieSns[0]));
		if (visitMainPage()) {
			LogFactory.getLog(this.getClass()).info("登录成功");
		}else{
			LogFactory.getLog(this.getClass()).info("登录失败");
		}
		lw.finish();
	}
	
	private Date createExpiry(JSONObject cookiejson){
		BigDecimal timestamp = cookiejson.getBigDecimal("expirationDate");
		long epochSecond = timestamp.longValue();
		long nanoAdjustment = timestamp.subtract(BigDecimal.valueOf(epochSecond)).multiply(TB).longValue();
		Instant instant = Instant.ofEpochSecond(epochSecond, nanoAdjustment);
		return Date.from(instant);
	}
	

	private Boolean visitMainPage() throws IOException {
		Cookie cookie = new BasicClientCookie("1","2");//= resource.getCookie("uin");
		if(cookie == null){
			return false;
		}
		@SuppressWarnings("unused")
		String qq = cookie.getValue().substring(1);
		HttpGet httpget = new HttpGet(resource.getMainUrl() + qq);
		//httpget.setHeaders(resource.getHttpResource().getBaseHeaders());
		httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpget.addHeader("Upgrade-Insecure-Requests", "1");
		
		CloseableHttpClient httpclient = null;//resource.getHttpResource().getHttpclient();
		final HttpContext httpContext = new BasicHttpContext();
		ResponseHandler<Boolean> reponseHandler = new ResponseHandler<Boolean>() {
			@Override
			public Boolean handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				HttpHost targetHost = (HttpHost)httpContext.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
				HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);
				if(targetHost.getHostName().equalsIgnoreCase(new URL(resource.getMainUrl()).getHost())){
					return true;
				}
				return false;
			};
		};
		Boolean isLogin = httpclient.execute(httpget,reponseHandler,httpContext);
		return isLogin;
	}
}
