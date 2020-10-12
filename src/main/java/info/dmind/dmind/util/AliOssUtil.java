package info.dmind.dmind.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;

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
                    set.add(split_arr[index]);
                }
            }

            baos.close();
            // 关闭OSSClient。
            ossClient.shutdown();
        }

        if (set.size() > 0) {
            return new ArrayList<>(set);
        } else {
            return null;
        }
    }


    private OSS ossClient() {
        // 创建OSSClient实例。
        return new OSSClientBuilder().build(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
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

}
