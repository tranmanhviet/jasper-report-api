package vn.com.itechcorp.jasper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import vn.com.itechcorp.jasper.config.JarComponentScanner;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class,
        DataSourceAutoConfiguration.class})
public class ReportApplication {

    @Value("${jar.path}")
    private String jarDirectory;

    @Autowired
    private JarComponentScanner jarComponentScanner;

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }

    @PostConstruct
    public void loadJars() {
        jarComponentScanner.scan(jarDirectory);
    }

}
