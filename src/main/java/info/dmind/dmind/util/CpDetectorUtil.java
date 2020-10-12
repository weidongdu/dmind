package info.dmind.dmind.util;

import info.monitorenter.cpdetector.io.*;

import java.io.InputStream;

public class CpDetectorUtil {

    private CodepageDetectorProxy detector = CodepageDetectorProxy
            .getInstance();
    private ParsingDetector parseDector = new ParsingDetector(false);
    private JChardetFacade jChardetFacade = JChardetFacade.getInstance();

    public CpDetectorUtil() {
        detector.add(parseDector);
        detector.add(jChardetFacade);// 用到antlr.jar、chardet.jar
        detector.add(ASCIIDetector.getInstance());
        // UnicodeDetector用于Unicode家族编码的测定
        detector.add(UnicodeDetector.getInstance());
    }



    public String getInputStreamEncoding(InputStream is, int length) {
        java.nio.charset.Charset charset = null;
        try {
            charset = detector.detectCodepage(is, length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (charset != null)
            return charset.name();
        else
            return null;
    }

}
