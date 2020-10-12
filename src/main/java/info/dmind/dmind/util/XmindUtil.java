package info.dmind.dmind.util;

import org.xmind.core.*;


public class XmindUtil {


    public static IWorkbook getWorkBook() {
        // 创建思维导图的工作空间
        IWorkbookBuilder workbookBuilder = Core.getWorkbookBuilder();
        return workbookBuilder.createWorkbook();
    }

    public static ITopic getRootTopic(String title, IWorkbook workbook) {
        // 获得默认sheet
        ISheet primarySheet = workbook.getPrimarySheet();
        primarySheet.setTitleText(title);
        // 获得根主题
        ITopic rootTopic = primarySheet.getRootTopic();
        // 设置根主题的标题
        rootTopic.setTitleText(title);
        return rootTopic;
    }

    public static ITopic getRootTopic(String title, IWorkbook workbook,String sheetName) {
        // 获得默认sheet
        ISheet sheetSmart = workbook.createSheet();
        sheetSmart.setTitleText(sheetName);
        workbook.addSheet(sheetSmart);

        // 获得根主题
        ITopic rootTopic = sheetSmart.getRootTopic();

        // 设置根主题的标题
        rootTopic.setTitleText(title);
        return rootTopic;
    }

    //将 item 添加到 topic
    public static ITopic getTopic(String content, IWorkbook workbook) {
        ITopic topic = workbook.createTopic();
        topic.setTitleText(content);
        return topic;
    }

}
