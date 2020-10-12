package info.dmind.dmind.util;

import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.HashMap;

@Component
public class MailUtil {

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    @PostConstruct
    public void init() {
        OhMyEmail.config(OhMyEmail.SMTP_QQ(false), username, password);
    }

    public void sendFile(HashMap<String, String> map, String subject, String mailTo) throws SendMailException {
        OhMyEmail email = OhMyEmail.subject(subject)
                .from(username)
                .to(mailTo)
                .html("<h1>详情如下</h1>");
        for (String filename : map.keySet()) {
            email.attach(new File(map.get(filename)), filename);
        }
        email.send();
    }

}
