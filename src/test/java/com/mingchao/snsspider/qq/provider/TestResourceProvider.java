/**
 * 
 */
package com.mingchao.snsspider.qq.provider;

import org.junit.Test;

import com.mingchao.snsspider.qq.resource.Resource;

/**
 * @author Administrator
 *
 */
public class TestResourceProvider {
	public static void main(String[] args) {
		Resource resource = ResourceProvider.INSTANCE.getResource();
		System.out.println(resource);
		resource.close();
	}
	

	@Test
	public void testGetApplicationContext() {
		System.out.println(ResourceProvider.INSTANCE.getApplicationContext());
	}
	
	@Test
	public void testGetParaments() {
		System.out.println(ResourceProvider.INSTANCE.getParaments());
	}

}
