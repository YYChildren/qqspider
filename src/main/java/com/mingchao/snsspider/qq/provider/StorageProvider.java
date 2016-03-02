package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.storage.StorageImpl;
import com.mingchao.snsspider.storage.spi.jdbc.StorageMySQL;

public class StorageProvider {
	public static Storage newMySQLStorage(){
		return new StorageImpl(new StorageMySQL());
	}
}
