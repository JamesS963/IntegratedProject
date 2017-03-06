package main;

import main.dao.DocumentDao;
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
    CommandLineRunner init(StorageService storageService, DocumentDao documentDao) {
        return (args) -> {
            //documentDao.deleteAll();
            //storageService.deleteAll();
            storageService.init();
        };
    }


}