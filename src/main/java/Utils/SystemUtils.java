package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yon_b on 1/2/2018.
 */
public class SystemUtils {
    public static long getPID() {
        String processName =
                java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }
    public static void init() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss");
        System.setProperty("current.date", dateFormat.format(new Date()));
    }
}