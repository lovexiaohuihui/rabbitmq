package com.bochao.util;

import java.util.concurrent.TimeUnit;

/**
 * 睡眠工具类
 */
public class SleepUtils {

    /**
     * 睡眠多少秒
     * */
    public static void sleep(int second) throws Exception {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void threadSleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
