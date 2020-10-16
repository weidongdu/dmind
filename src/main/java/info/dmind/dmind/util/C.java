package info.dmind.dmind.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.FileSystemResource;

public class C {
    public static final int DEFAULT_COL = 1;
    public static final int DEFAULT_BATCH_SIZE = 1000;
    public static final String LEVEL_WORD_KEY = "levelWord";
    public static final String LEVEL_WORD_LIST_KEY = "levelWordList";
    public static final String WORD_LINK_SYMBOL = "_";
    public static final String OUTPUT_DIR = "output";
    public static final String DOWNLOAD_DIR = "download";
    public static final String PATH_SYMBOL = "/";
    public static final String TYPE_XMIND = ".xmind";
    public static final String SEG_MAP = "seg_map_";
    public static final String PROCESS = "process";
    public static final String PROJECT_PATH = new FileSystemResource("").getFile().getAbsolutePath();// Users/abc
    public static final JSONObject processJSONObject = new JSONObject();
}
