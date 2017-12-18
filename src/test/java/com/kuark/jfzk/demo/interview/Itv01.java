package com.kuark.jfzk.demo.interview;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.*;

public class Itv01 {
    public static int computeAge(int n) {
        if (n == 1) {
            return 10;
        } else {
            return computeAge(n - 1) + 2;
        }
    }

    public static int trimGBK(byte[] buf, int n) {
        int num = 0;
        boolean bChineseFirstHalf = false;
        for (int i = 0; i < n; i++) {
            if (buf[i] < 0 && !bChineseFirstHalf) {
                bChineseFirstHalf = true;
            } else {
                num++;
                bChineseFirstHalf = false;
            }
        }
        return num;
    }

    public static void doubleNum(int n) {
        System.out.println(n);
        if (n <= 5000)
            doubleNum(n * 2);
    }

    @Test
    public void tt01() throws Exception {
        File file = new File("");
        file.listFiles((dir, name) -> name.endsWith(".java"));
    }

    @Test
    public void tt02() throws Exception {
        WatchService service = FileSystems.getDefault().newWatchService();
        Paths.get("D:").register(service,
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
        WatchKey key;
        do {
            key = service.take();
            key.pollEvents().forEach(watchEvent -> System.out.println(watchEvent.kind() + "=" + watchEvent.context()));
        } while (key.reset());
    }

    @Test
    public void tt03() throws Exception {
        String str = "我a爱中华abc我爱传智def";
        int num = trimGBK(str.getBytes("GBK"), 4);
        System.out.println(str.substring(0, num));
    }

    @Test
    public void tt04() throws Exception {
        doubleNum(500);
    }

    @Test
    public void tt05() throws Exception {
        System.out.println(computeAge(8));
    }

    @Test
    public void tt06() throws Exception {
        System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
    }

    @Test
    public void tt07() throws Exception {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return null;
                    }
                });
    }

    @Test
    public void tt08() throws Exception {
    }
}
