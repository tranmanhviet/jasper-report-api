package vn.com.itechcorp.jasper.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
@Component
public class JarComponentScanner {

    @Autowired
    private ApplicationContext applicationContext;

    public void scan(String directory) {
        log.info("Scanning bean from jar folder : " + directory);

        File[] jars = new File(directory).listFiles((dir, name) -> name.endsWith(".jar"));
        if (jars == null) {return;}

        for (File jar : jars) {
            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{new URL("jar:file:" + jar + "!/")}, Thread.currentThread().getContextClassLoader());
                 JarFile jarFile = new JarFile(jar);) {

                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName().replaceAll("/", ".").replaceAll(".class", "");
                        Class<?> clazz = classLoader.loadClass(className);
                        if (clazz.isAnnotationPresent(Component.class)) {
                            Object bean = clazz.newInstance();
                            String beanName = clazz.getSimpleName().substring(0, 1).toLowerCase()
                                    + clazz.getSimpleName().substring(1);

                            // Register the bean with the application context
                            log.info("Creating bean {}", clazz);
                            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                            beanFactory.registerSingleton(beanName, bean);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }
}