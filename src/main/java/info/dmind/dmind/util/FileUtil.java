package info.dmind.dmind.util;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {


    /**
     * check file is exists
     * @param filePath
     * @return
     */
    public static boolean check(String filePath) {
        File f = new File(filePath);
        return f.exists();
    }

    /**
     * create file
     * @param filePath
     * @throws IOException
     */
    public static void create(String filePath) throws IOException {
        File f = new File(filePath);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        if (!f.exists()) {
            f.createNewFile();
        }
    }

    public static void writeToFileJSON(String filePath, Object obj) throws IOException {

        create(filePath);

        try {
            Files.write(Paths.get(filePath), JSON.toJSONString(obj).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
