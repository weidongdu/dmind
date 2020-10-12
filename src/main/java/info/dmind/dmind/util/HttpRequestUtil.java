package info.dmind.dmind.util;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class HttpRequestUtil {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient DEFAULT_CLIENT = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();
    /**
     * 构造request for RequestBody
     *
     * @param url
     * @param headerMap addHeader() 保留存在key , 添加新的 header
     *                  header() 移除存在key , 添加新的 header
     * @param body      请求体
     * @return
     */
    private Request buildRequest(String url, Map<String, String> headerMap, RequestBody body) {
        Request.Builder requestBuilder = new Request.Builder();
        if (headerMap != null && !headerMap.keySet().isEmpty()) {
            headerMap.forEach(requestBuilder::header);
        }
        requestBuilder.url(url);
        requestBuilder.post(body);
        Request request = requestBuilder.build();
        return request;
    }

    public byte[] postJSON(String url, Map<String, String> headerMap, String json) throws IOException {
        RequestBody jsonBody = RequestBody.create(json, JSON);
        Request request = buildRequest(url, headerMap, jsonBody);
        try (Response response = DEFAULT_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("OKHttp Unexpected code " + response);
            } else {
                return response.body().bytes();
            }
        }
    }

}
