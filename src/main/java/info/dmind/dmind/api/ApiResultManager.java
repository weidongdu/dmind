package info.dmind.dmind.api;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiResultManager {

    public ApiResult success(){
        ApiResult<Object> result = new ApiResult<>();
        result.setCode(ApiStatus.SUCCESS);
        result.setMsg(ApiStatus.SUCCESS_VALUE);
        result.setData(new Object());
        return result;
    }

    public <T> ApiResult success(T data){
        ApiResult<T> result = new ApiResult<>();
        result.setCode(ApiStatus.SUCCESS);
        result.setMsg(ApiStatus.SUCCESS_VALUE);
        result.setData(data);
        return result;
    }

    public ApiResult failDefault(){
        ApiResult<Object> result = new ApiResult<>();
        result.setCode(ApiStatus.FAIL);
        result.setMsg(ApiStatus.FAIL_VALUE);
        return result;
    }

    @Deprecated
    public <T> ApiResult fail(T data){
        ApiResult<T> result = new ApiResult<>();
        result.setCode(ApiStatus.FAIL);
        result.setMsg(ApiStatus.FAIL_VALUE);
        result.setData(data);
        return result;
    }


    public <T> ApiResult fail(T data,int code,String msg){
        ApiResult<T> result = new ApiResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static void main(String[] args) {
        ApiResultManager apm = new ApiResultManager();
        System.out.println(JSON.toJSONString(apm.success()));
    }
}
