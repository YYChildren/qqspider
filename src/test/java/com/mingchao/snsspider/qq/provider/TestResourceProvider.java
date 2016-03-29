/**
 * 
 */
package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.qq.resource.Resource;

/**
 * @author Administrator
 *
 */
public class TestResourceProvider {
	public static void main(String[] args) {
		Resource resource = ResourceProvider.INSTANCE.getResource();
		System.out.println(resource);
		System.out.println(ResourceProvider.INSTANCE.getApplicationContext());
		System.out.println(ResourceProvider.INSTANCE.getParaments());
		resource.close();
	}
}
