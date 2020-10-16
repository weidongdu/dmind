package info.dmind.dmind.api;

import lombok.Data;

@Data
public class ApiResult<T> {
    private Integer code;//状态码
    private String msg;//状态描述
    private T data;//详细数据
}
