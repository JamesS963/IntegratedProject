package main;

import main.dao.DocumentDao;
import main.dao.UserDao;
import main.storage.StorageProperties;
import main.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService, DocumentDao documentDao, UserDao userDao) {
        return (args) -> {
            /* Uncomment these to wipe database and/or filesystem*/
            documentDao.deleteAll(); // document database
            storageService.deleteAll(); // files in filesystem
            //userDao.deleteAll(); // user database

            storageService.init(); // don't comment out
        };
    }


}