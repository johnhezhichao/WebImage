package com.example.webimage.image;

import java.io.File;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * ͼƬ�ڴ滺��
 * 
 * @author Li Bin
 */
public class ImageCache {
	private LruCache<String, Bitmap> mCache;

	// ��ǰӦ�ó���Ψһʵ��
	private static ImageCache mInstance;

	private static Object mLock = new Object();

	private ImageCache() {
		int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;
		mCache = new LruCache<String, Bitmap>(maxSize);
	}

	public static ImageCache getInstance() {
		synchronized (mLock) {
			if (mInstance == null) {
				mInstance = new ImageCache();
			}
			return mInstance;
		}
	}

	public boolean isCachedInLocal(String key) {
		String fileName = String.valueOf(key.hashCode());
		File file = new File(ImageWorker.APP_DIR, fileName);
		return file.exists();
	}

	/**
	 * ��ָ����Bitmap�����ڴ滺��
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Bitmap put(String key, Bitmap value) {
		synchronized (mCache) {
			return mCache.put(key, value);
		}
	}

	public Bitmap get(String key) {
		synchronized (mCache) {
			return mCache.get(key);
		}
	}

	public Bitmap remove(String key) {
		synchronized (mCache) {
			return mCache.remove(key);
		}
	}
}

