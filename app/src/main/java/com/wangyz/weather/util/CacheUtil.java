package com.wangyz.weather.util;

import android.util.Log;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.CacheMemoryUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wangyz
 * CacheUtil
 */
public class CacheUtil {

    private static final String TAG = CacheUtil.class.getSimpleName();

    /**
     * 最大内存缓存数量
     */
    private static final int MAX_MEMORY_COUNT = 256;

    /**
     * 最大缓存,单位byte
     */
    private static final long MAX_DISK_SIZE = 1024 * 1024 * 10;

    /**
     * 最大缓存数量
     */
    private static final int MAX_DISK_COUNT = 256;

    private static CacheMemoryUtils mCacheMemoryUtils;

    private static CacheDiskUtils mCacheDiskUtils;

    private static CacheUtil mInstance;

    private CacheUtil() {

    }

    public static CacheUtil getInstance() {
        synchronized (CacheUtil.class) {
            if (mInstance == null) {
                synchronized (CacheUtil.class) {
                    mInstance = new CacheUtil();
                    mCacheMemoryUtils = CacheMemoryUtils.getInstance(MAX_MEMORY_COUNT);
                    mCacheDiskUtils = CacheDiskUtils.getInstance(MAX_DISK_SIZE, MAX_DISK_COUNT);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取内存中的缓存
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getMemoryCache(String key) {
        Log.i(TAG, "getMemoryCache");
        try {
            key = getKey(key);
            return mCacheMemoryUtils.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成内存缓存
     *
     * @param key
     * @param value
     */
    public void putMemoryCache(String key, Object value) {
        Log.i(TAG, "putMemoryCache");
        try {
            key = getKey(key);
            Object o = mCacheMemoryUtils.get(key);
            if (o == null) {
                Log.i(TAG, "add cache");
                mCacheMemoryUtils.put(key, value);
            } else if (!value.equals(o)) {
                Log.i(TAG, "update cache");
                mCacheMemoryUtils.put(key, value);
            } else {
                Log.i(TAG, "cache already existed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Disk中的缓存
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getDiskCache(String key) {
        Log.i(TAG, "getDiskCache");
        try {
            key = getKey(key);
            return (T) mCacheDiskUtils.getSerializable(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成Disk缓存
     *
     * @param key
     * @param value
     */
    public void putDiskCache(String key, Object value) {
        Log.i(TAG, "putDiskCache");
        try {
            key = getKey(key);
            Object o = mCacheDiskUtils.getSerializable(key);
            if (o == null) {
                Log.i(TAG, "add cache");
                mCacheDiskUtils.put(key, (Serializable) value);
            } else if (!value.equals(o)) {
                Log.i(TAG, "update cache");
                mCacheDiskUtils.put(key, (Serializable) value);
            } else {
                Log.i(TAG, "cache already existed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将key进行md5加密生成一个字符串
     *
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String getKey(String key) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("md5");
        messageDigest.update(key.getBytes());
        return bytesToString(messageDigest.digest());
    }

    /**
     * byte转string
     *
     * @param bytes
     */
    private String bytesToString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
