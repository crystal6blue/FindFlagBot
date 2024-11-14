package com.tests.findflagbot;

import com.tests.findflagbot.Bot.FindFlagBot;
import com.tests.findflagbot.models.CountryImages;
import com.tests.findflagbot.repository.CountryImagesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
public class FindFlagBotApplication {

    /*private final CountryImagesRepository countryImagesRepository;

    public FindFlagBotApplication(CountryImagesRepository countryImagesRepository) {
        this.countryImagesRepository = countryImagesRepository;
    }*/

    public static void main(String[] args) throws TelegramApiException {
        ApplicationContext context = SpringApplication.run(FindFlagBotApplication.class, args);

        // Retrieve FindFlagBot bean from the context
        FindFlagBot findFlagBot = context.getBean(FindFlagBot.class);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(findFlagBot);
    }

    /*@Bean
    public CommandLineRunner runner(@Autowired ResourceLoader resourceLoader) {
        return args -> {
            try {
                // Load the resource path using ResourceLoader
                Resource resource = resourceLoader.getResource("classpath:CountryImages");

                if (resource.exists()) {
                    File[] files = resource.getFile().listFiles();

                    if (files != null) {
                        for (File file : files) {
                            countryImagesRepository.save(new CountryImages(file.getName(), file.getAbsolutePath()));
                        }
                    }
                } else {
                    System.err.println("Directory not found: CountryImages");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }*/

}
