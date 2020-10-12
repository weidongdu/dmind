package info.dmind.dmind.domain;

import lombok.Data;

@Data
public class DmindObject {
    private String url;
    private String topicWord;
    private Boolean isTopic;
    private int l1;
    private int l2;
    private int l3;
    private int l4;
    private String mailTo;
    private String baiduLacServer;
    private String stopWords;
    private String stopSymbol;//分隔符，、,/
    private Boolean divided;//xmind 是否分割
}
