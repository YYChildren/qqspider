package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.storage.Storage;
import com.mingchao.snsspider.storage.StorageJdbcLocal;

public class StorageProvider {
	public static Storage newMySQLStorage(){
		return StorageJdbcLocal.MySQL.getInstance();
	}
}
