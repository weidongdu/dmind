package info.dmind.dmind.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@Component
public class AliOssUtil {

    @Value("${aliyun.oss.endpoint}")
    private String END_POINT;
    @Value("${aliyun.oss.bucketName}")
    private String BUCKET_NAME;
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    @Value("${aliyun.oss.accessKeyId}")
    private String ACCESS_KEY_ID;
    @Value("${aliyun.oss.accessKeySecret}")
    private String ACCESS_KEY_SECRET;

    private static final String CSV_SEP = ",";
    private static final String CHARSET_UTF8 = "UTF-8";

    public ArrayList<String> readCsv(String ossObjectName, int col) throws IOException {
        // <yourObjectName>从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。

        OSS ossClient = ossClient();
        OSSObject ossObject = ossClient.getObject(BUCKET_NAME, ossObjectName);
        InputStream content = ossObject.getObjectContent();
        HashSet<String> set = new HashSet<>();
        if (content != null) {
            readColumn(col, content, set);
            // 关闭OSSClient。
            ossClient.shutdown();
        }

        if (set.size() > 0) {
            return new ArrayList<>(set);
        } else {
            return null;
        }
    }


    /**
     * 返回本地 文件 abs path
     * 返回绝对路径
     *
     * @param ossObjectName
     * @return
     */
    public String downloadToFile(String ossObjectName) throws IOException {
        String filePath = C.PROJECT_PATH + C.PATH_SYMBOL + C.DOWNLOAD_DIR + C.PATH_SYMBOL + ossObjectName;
        boolean check = FileUtil.check(filePath);
        if (check) {
            log.info("File Exist [{}]", filePath);
            return filePath;
        }

        FileUtil.create(filePath);

        OSS oss = ossClient();
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        oss.getObject(new GetObjectRequest(BUCKET_NAME, ossObjectName), new File(filePath));
        // 关闭OSSClient。
        oss.shutdown();
        return filePath;
    }


    public ArrayList<String> readCsvFromLocal(String filePath, int col) throws IOException {
        // <yourObjectName>从OSS下载文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        InputStream content = new FileInputStream(filePath);
        HashSet<String> set = new HashSet<>();
        readColumn(col, content, set);

        if (set.size() > 0) {
            return new ArrayList<>(set);
        } else {
            return null;
        }
    }

    private void readColumn(int col, InputStream content, HashSet<String> set) throws IOException {
        ByteArrayOutputStream baos = copyIS(content);
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        content.close();

        int length = Math.min(baos.size(), 1000);
        String encoding = new CpDetectorUtil().getInputStreamEncoding(new ByteArrayInputStream(baos.toByteArray()), length);

        if (encoding == null) {
            encoding = CHARSET_UTF8;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), Charset.forName(encoding)));

        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            String[] split_arr = line.split(CSV_SEP);
            int index = Math.max(col - 1, 0);
            if (split_arr.length > index) {
                if (StringUtils.isEmpty(split_arr[index])) {

                } else {
                    set.add(split_arr[index]);
                }
            }
        }

        baos.close();
    }


    private OSS ossClient() {
        // 创建OSSClient实例。
        return new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    public List<OssFile> fileList(String KeyPrefix) {
        OSS ossClient = ossClient();
        ObjectListing objectListing = ossClient.listObjects(BUCKET_NAME, KeyPrefix);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        // 关闭OSSClient。
        ossClient.shutdown();
        return toOssFile(sums);
    }

    public List<OssFile> fileList() {
        OSS ossClient = ossClient();
        ObjectListing objectListing = ossClient.listObjects(BUCKET_NAME);
        List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
        // 关闭OSSClient。
        ossClient.shutdown();
        return toOssFile(sums);
    }

    //default sort by date desc
    public List<OssFile> toOssFile(List<OSSObjectSummary> list) {
        if (list != null && list.size() > 0) {
            ArrayList<OssFile> ossFiles = new ArrayList<>(list.size());
            for (OSSObjectSummary obj : list) {
                if(obj.getSize() == 0)continue;
                OssFile ossFile = new OssFile();
                ossFile.setKey(obj.getKey());
                ossFile.setSize(toHumanRead(obj.getSize()));
                ossFile.setLastModified(obj.getLastModified());
                ossFiles.add(ossFile);
            }
            ossFiles.sort(Comparator.comparing(OssFile::getLastModified).reversed());
            return ossFiles;
        } else {
            return null;
        }
    }

    private ByteArrayOutputStream copyIS(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > -1) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        return baos;
    }

    @Data
    public class OssFile {
        private String key;
        ;//"key": "abc/rank.xlsx",
        private String size;
        ;//"size": 220067,
        private Date lastModified;
        ;//"lastModified": "2020-10-16T06:23:28.000+00:00",
    }

    private String toHumanRead(long size) {
        int k = 1024;
        if (size < k) {
            return size + "b";
        }

        if (size < k * k) {
            return (size / k) + "kb";
        }

        if (size < k * k * k) {
            return (size / (k * k)) + "mb";
        }


        if (size < k * k * k * k) {
            return (size / (k * k * k)) + "gb";
        }

        return size + "b";
    }
}
