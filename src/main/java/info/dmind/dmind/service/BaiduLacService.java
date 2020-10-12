package info.dmind.dmind.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import info.dmind.dmind.domain.DmindObject;
import info.dmind.dmind.util.*;
import io.github.biezhi.ome.SendMailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.xmind.core.CoreException;
import org.xmind.core.ITopic;
import org.xmind.core.IWorkbook;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BaiduLacService {

    private static final int DEFAULT_COL = 1;
    private static final int DEFAULT_BATCH_SIZE = 1000;
    private static final String LEVEL_WORD_KEY = "levelWord";
    private static final String LEVEL_WORD_LIST_KEY = "levelWordList";
    private static final String WORD_LINK_SYMBOL = "_";
    private static final String OUTPUT_DIR = "output";
    private static final String PATH_SYMBOL = "/";
    private static final String TYPE_XMIND = ".xmind";
    private static final String PROJECT_PATH = new FileSystemResource("").getFile().getAbsolutePath();


    @Resource
    private HttpRequestUtil httpRequestUtil;
    @Resource
    private MailUtil mailUtil;
    @Resource
    private AliOssUtil aliOssUtil;

    /**
     * 去 指定 url 读取文件
     *
     * @param dmind
     */
    @Async()
    public void parse(DmindObject dmind) throws IOException, CoreException, SendMailException {
        ArrayList<String> keywordList = aliOssUtil.readCsv(dmind.getUrl(), DEFAULT_COL);
        Map<String, ArrayList<String>> segMap = seg(keywordList, dmind);
        LinkedHashMap<String, Integer> mapTopN = sortByTopN(segMap);
        AtomicInteger total = new AtomicInteger();
        mapTopN.forEach((k, v) -> {
            total.addAndGet(v);
        });

        HashSet<String> levelWordSet = new HashSet<>();
        if (dmind.getIsTopic()) {
            levelWordSet.add(dmind.getTopicWord());
        }

        log.info("seg map level 1 start");
        ArrayList<HashMap<String, ArrayList<String>>> mapArrayListLevel1 = levelMap(segMap, mapTopN, dmind, 1, levelWordSet);
        IWorkbook workBook = XmindUtil.getWorkBook();
        ITopic rootTopicDefault = XmindUtil.getRootTopic(dmind.getTopicWord(), workBook);

        if (dmind.getDivided()) {
            ArrayList<IWorkbook> xmindList = new ArrayList<>();
            xmindList.add(workBook);

            for (HashMap<String, ArrayList<String>> segMapLevel1 : mapArrayListLevel1) {
                String keywordLevel1 = segMapLevel1.get(LEVEL_WORD_KEY).get(0);
                rootTopicDefault.add(XmindUtil.getTopic(keywordLevel1, workBook));

                //生成新的workBook
                IWorkbook workBookLevel = XmindUtil.getWorkBook();
                xmindLevel(dmind, mapTopN, total, segMapLevel1, keywordLevel1, workBookLevel);
                xmindList.add(workBookLevel);
            }

            outputMulti(dmind, xmindList);

        } else {
            for (HashMap<String, ArrayList<String>> segMapLevel1 : mapArrayListLevel1) {
                String keywordLevel1 = segMapLevel1.get(LEVEL_WORD_KEY).get(0);
                rootTopicDefault.add(XmindUtil.getTopic(keywordLevel1, workBook));
                xmindLevel(dmind, mapTopN, total, segMapLevel1, keywordLevel1, workBook);
            }
            outputSingle(dmind, workBook);
        }

    }

    private void xmindLevel(DmindObject dmind, LinkedHashMap<String, Integer> mapTopN, AtomicInteger total, HashMap<String, ArrayList<String>> segMapLevel1, String keywordLevel1, IWorkbook workBookLevel) {
        log.info("start new sheer from level 1");

        ITopic rootTopicLevel1;

        if (dmind.getDivided()) {
            rootTopicLevel1 = XmindUtil.getRootTopic(calcPct(keywordLevel1, mapTopN, total.get()), workBookLevel);
        } else {
            rootTopicLevel1 = XmindUtil.getRootTopic(calcPct(keywordLevel1, mapTopN, total.get()), workBookLevel, calcPct(keywordLevel1, mapTopN, total.get()));
        }

        log.debug("seg map level 2 start keyword={}", keywordLevel1);
        ArrayList<HashMap<String, ArrayList<String>>> mapArrayListLevel2 = levelMap(segMapLevel1, sortByTopN(segMapLevel1), dmind, 2, new HashSet<>(segMapLevel1.get(LEVEL_WORD_LIST_KEY)));

        for (HashMap<String, ArrayList<String>> segMapLevel2 : mapArrayListLevel2) {
            String keywordLevel2 = segMapLevel2.get(LEVEL_WORD_KEY).get(0);
            ITopic topicLevel2 = XmindUtil.getTopic(calcPct(keywordLevel2, mapTopN, total.get()), workBookLevel);
            rootTopicLevel1.add(topicLevel2);

            log.debug("seg map level 3 start keyword={}", keywordLevel2);
            ArrayList<HashMap<String, ArrayList<String>>> mapArrayListLevel3 = levelMap(segMapLevel2, sortByTopN(segMapLevel2), dmind, 3, new HashSet<>(segMapLevel2.get(LEVEL_WORD_LIST_KEY)));

            for (HashMap<String, ArrayList<String>> segMapLevel3 : mapArrayListLevel3) {
                String keywordLevel3 = segMapLevel3.get(LEVEL_WORD_KEY).get(0);
                ITopic topicLevel3 = XmindUtil.getTopic(calcPct(keywordLevel3, mapTopN, total.get()), workBookLevel);
                topicLevel2.add(topicLevel3);

                log.debug("seg map level 4 start keyword={}", keywordLevel3);
                ArrayList<HashMap<String, ArrayList<String>>> mapArrayListLevel4 = levelMap(segMapLevel3, sortByTopN(segMapLevel3), dmind, 3, new HashSet<>(segMapLevel3.get(LEVEL_WORD_LIST_KEY)));

                for (HashMap<String, ArrayList<String>> segMapLevel4 : mapArrayListLevel4) {
                    for (String key : segMapLevel4.keySet()) {
                        if (LEVEL_WORD_KEY.equalsIgnoreCase(key) || LEVEL_WORD_LIST_KEY.equalsIgnoreCase(key)) {

                        } else {
                            log.debug("final keyword {}", key);
                            topicLevel3.add(XmindUtil.getTopic(key, workBookLevel));
                        }
                    }

                }
            }
        }
    }


    private void outputSingle(DmindObject dmind, IWorkbook workBook) throws IOException, CoreException, SendMailException {
        String xmindName = dmind.getTopicWord()  + WORD_LINK_SYMBOL+ new Date().getTime()+ TYPE_XMIND;
        String outputDir = PROJECT_PATH + PATH_SYMBOL + OUTPUT_DIR;
        String xmindFile = outputDir + PATH_SYMBOL + dmind.getTopicWord() + TYPE_XMIND;

        File dir = new File(outputDir);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdir();
        }

        workBook.save(xmindFile);

        //发送邮件
        HashMap<String, String> attachMap = new HashMap<>();
        attachMap.put(xmindName, new File(xmindFile).getAbsolutePath());
        mailUtil.sendFile(attachMap, xmindName, dmind.getMailTo());
        log.info("mail to [{}] , send success {}", dmind.getMailTo(), xmindFile);
    }

    private void outputMulti(DmindObject dmind, List<IWorkbook> list) throws IOException, CoreException, SendMailException {

        ArrayList<String> xmindFileList = new ArrayList<>();
        int index = 0;
        long now = new Date().getTime();
        for (IWorkbook workbook : list) {
            String xmindName = index + WORD_LINK_SYMBOL + workbook.getPrimarySheet().getTitleText() + WORD_LINK_SYMBOL+ now + TYPE_XMIND;
            String outputDir = PROJECT_PATH + PATH_SYMBOL + OUTPUT_DIR;
            String xmindFile = outputDir + PATH_SYMBOL + xmindName;

            File dir = new File(outputDir);
            if (!dir.exists()) {// 判断目录是否存在
                dir.mkdir();
            }
            workbook.save(xmindFile);
            xmindFileList.add(xmindFile);
            index++;
        }

        String zipName = dmind.getTopicWord() + WORD_LINK_SYMBOL+ now + ".zip";
        String zipFile = PROJECT_PATH + PATH_SYMBOL + OUTPUT_DIR + PATH_SYMBOL + zipName;

        int count = ZipUtil.compress(xmindFileList, zipFile, false);
        log.info("zip {} files", count);

        //发送邮件
        HashMap<String, String> attachMap = new HashMap<>();
        if (count > 0) {
            for (String absPath : xmindFileList) {
                deleteFile(absPath);
            }
            attachMap.put(zipName, zipFile);
        }

        mailUtil.sendFile(attachMap, zipName, dmind.getMailTo());
        log.info("mail to [{}] , send success {}", dmind.getMailTo(), zipName);
    }

    private void deleteFile(String absPath) {
        File file = new File(absPath);
        if (file.delete()) {
            log.info(file.getName() + " delete success");
        } else {
            log.error(file.getName() + " delete fail");
        }
    }

    String calcPct(String keyword, Map<String, Integer> mapTopN, int total) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.0000");//格式化设置
        int count = mapTopN.get(keyword);
        String pct = decimalFormat.format((double) count / total);

        return keyword + WORD_LINK_SYMBOL + count + WORD_LINK_SYMBOL + pct;
    }

    /**
     * 根据 分词map qu
     *
     * @param segMap
     * @param mapTopN
     * @param dmind
     */
    ArrayList<HashMap<String, ArrayList<String>>> levelMap(Map<String, ArrayList<String>> segMap, LinkedHashMap<String, Integer> mapTopN, DmindObject dmind, int level, HashSet<String> upLevelWordSet) {
        ArrayList<HashMap<String, ArrayList<String>>> mapArrayList = new ArrayList<>();
        int count = 0;
        switch (level) {
            case 1:
                count = dmind.getL1();
                break;
            case 2:
                count = dmind.getL2();
                break;
            case 3:
                count = dmind.getL3();
                break;
            case 4:
                count = dmind.getL4();
                break;
            default:
                break;
        }


        for (String word : upLevelWordSet) {
            mapTopN.remove(word);
        }

        int index = 0;
        ArrayList<String> levelWordList = new ArrayList<>();
        for (String keyword : mapTopN.keySet()) {
            index++;
            if (index > count) break;

            levelWordList.add(keyword);
        }


        for (String levelWord : levelWordList) {
            HashMap<String, ArrayList<String>> levelWordSegMap = new HashMap<>();
            //当前 level 关键词
            ArrayList<String> levelWordArrayList = new ArrayList<>();
            levelWordArrayList.add(levelWord);
            levelWordSegMap.put(LEVEL_WORD_KEY, levelWordArrayList);

            //此时 所有 level 关键词
            ArrayList<String> levelWordArrayListChain = new ArrayList<>();
            levelWordArrayListChain.add(levelWord);
            levelWordArrayListChain.addAll(upLevelWordSet);
            levelWordSegMap.put(LEVEL_WORD_LIST_KEY, levelWordArrayListChain);

            segMap.forEach((k, v) -> {
                if (v.contains(levelWord)) {
                    levelWordSegMap.put(k, v);
                }
            });
            mapArrayList.add(levelWordSegMap);
        }

        return mapArrayList;
    }

    private LinkedHashMap<String, Integer> sortByTopN(Map<String, ArrayList<String>> segMap) {
        HashMap<String, Integer> countMap = calcKeywordCount(segMap);
        final LinkedHashMap<String, Integer> map = countMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return map;
    }

    private HashMap<String, Integer> calcKeywordCount(Map<String, ArrayList<String>> map) {
        HashMap<String, Integer> countMap = new HashMap<>();
        //词频
        map.forEach((k, value) -> {
            if (value != null && value.size() > 0) {
                for (String word : value) {
                    if (countMap.containsKey(word)) {
                        countMap.put(word, countMap.get(word) + 1);
                    } else {
                        countMap.put(word, 1);
                    }
                }
            }
        });
        return countMap;
    }

    private Map<String, ArrayList<String>> seg(ArrayList<String> keywordList, DmindObject dmind) throws IOException {
        if (keywordList != null && keywordList.size() > 0) {
            HashMap<String, ArrayList<String>> segMap = new HashMap<>(keywordList.size());
            JSONObject jsonObject = new JSONObject();
            int round = keywordList.size() / BaiduLacService.DEFAULT_BATCH_SIZE;
            int mod = keywordList.size() % BaiduLacService.DEFAULT_BATCH_SIZE;

            if (round > 0) {
                for (int i = 0; i < round; i++) {
                    List<String> word = keywordList.subList(i * BaiduLacService.DEFAULT_BATCH_SIZE, (i + 1) * BaiduLacService.DEFAULT_BATCH_SIZE);
                    jsonObject.put("word", word);
                    segByServer(dmind, segMap, jsonObject, word);
                    log.info("keyword parse {} / {}", i * BaiduLacService.DEFAULT_BATCH_SIZE, keywordList.size());
                }
            }
            if (mod > 0) {
                List<String> word = keywordList.subList(round * BaiduLacService.DEFAULT_BATCH_SIZE, keywordList.size());
                jsonObject.put("word", word);
                segByServer(dmind, segMap, jsonObject, word);
            }

            //排除 stop word
            if (segMap.keySet().size() > 0) {
                ArrayList<String> stopList = new ArrayList<>(Stopwords.STOP_WORD_LIST);
                if (!StringUtils.isEmpty(dmind.getStopWords()) && !StringUtils.isEmpty(dmind.getStopSymbol())) {
                    stopList.addAll(new ArrayList<>(Arrays.asList(dmind.getStopWords().split(dmind.getStopSymbol()))));
                }
                segMap.forEach((k, v) -> {
                    v.removeIf(stopList::contains);
                });
            }
            return segMap;
        }
        return null;
    }

    private void segByServer(DmindObject dmind, HashMap<String, ArrayList<String>> segMap, JSONObject jsonObject, List<String> word) throws IOException {
        byte[] bytes = httpRequestUtil.postJSON(dmind.getBaiduLacServer(), null, jsonObject.toJSONString());
        String body = new String(bytes, Charset.defaultCharset());
        List<ArrayList> segList = JSONArray.parseArray(body, ArrayList.class);

        for (int j = 0; j < word.size(); j++) {
            //stop words remove
            segMap.put(word.get(j), segList.get(j));
        }
    }


}
