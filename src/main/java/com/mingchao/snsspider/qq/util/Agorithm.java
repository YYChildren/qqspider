package com.mingchao.snsspider.qq.util;

import java.net.MalformedURLException;
import java.net.URL;

import com.mingchao.snsspider.http.HttpResource;

public class Agorithm {
	
	public static long g_tk(String urlStr,HttpResource resource) throws MalformedURLException {
	    URL url = new URL(urlStr);
	    String skey = null;
	    if (url != null){
	    	if (url.getHost() != null && url.getHost().indexOf("qzone.qq.com") > 0) {
	        	skey = resource.getCookie("p_skey").getValue();
	        } else if (url.getHost() != null && url.getHost().indexOf("qq.com") > 0) {
	            skey =  resource.getCookie("skey").getValue();
	         }
	    }
	    if (skey == null) {
	    	if(resource.getCookie("p_skey") != null){
	    		skey = resource.getCookie("p_skey").getValue();
	    	}else if(resource.getCookie("skey") != null){
	    		skey = resource.getCookie("skey").getValue();
	    	}else if(resource.getCookie("rv2") != null){
	    		skey = resource.getCookie("rv2").getValue();
	    	}else{
	    		skey="";
	    	}
	    }
	    return g_tk_hash(skey);
	}
	public static  long g_tk_hash(String str) {
	    long hash = 5381;
	    for (int i = 0, len = str.length(); i < len; ++i) {
	    	hash += (hash << 5) + str.charAt(i);
	    }
	    return hash & 2147483647;
	}
	
}
