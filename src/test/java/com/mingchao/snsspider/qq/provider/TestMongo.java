package com.mingchao.snsspider.qq.provider;

import com.mingchao.snsspider.qq.model.UserKey;
import com.mingchao.snsspider.qq.provider.ResourceProvider;
import com.mingchao.snsspider.storage.db.StorageDB;

public class TestMongo {
	public static void main(String[] args) {
		StorageDB storage = ResourceProvider.INSTANCE.getMongoStorage();
		UserKey uk = new UserKey();
		uk.setId(1L);
		uk.setQq(199934315L);
		uk.setVisitable(false);
		storage.insertDuplicate(uk);
		storage.close();
	}
}
