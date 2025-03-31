package com.tests.findflagbot.Bot;

import com.tests.findflagbot.models.CountryImages;
import com.tests.findflagbot.models.Player;
import com.tests.findflagbot.service.CountryImagesService;
import com.tests.findflagbot.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FindFlagBot extends TelegramLongPollingBot {

    private final PlayerService playerService;
    private final CountryImagesService countryImagesService;
    private String solution;

    public FindFlagBot(@Value("${bot.token}") String botToken, PlayerService playerService, CountryImagesService countryImagesService) {
        super(botToken);
        this.playerService = playerService;
        this.countryImagesService = countryImagesService;
    }

    @Override
    public String getBotUsername() {
        return "findflagbot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();

            String text = message.getText();
            log.info("ChatId: " + message.getChatId() + "    Message: " + text);

            switch (text) {
                case "/start":
                    saveAPlayer(message);
                    sendMessage(message, "" +
                            "Hello, This is FindFlagBot." +
                            "You can test yourslef");
                    break;
                case "/profile":
                    sendMessage(message,
                            "ID : " + playerService.findByChatId(message.getChatId()).getId() + "\n"
                             + "Username : " + playerService.findByChatId(message.getChatId()).getName()  + "\n"
                            +    "Date Of Register : " + playerService.findByChatId(message.getChatId()).getDateOfRegistration() + "\n"
                            +    "Points: " + playerService.findByChatId(message.getChatId()).getPoints()
                    );
                    break;
                 case "/play":
                     sendMessage(message, "Let's go");
                     solution = getCallBackQuery(message);
                     break;
                case "/help":
                    sendMessage(message, "Start the Game: /play" +
                            "\n" + "See a Profile: /profile");
                    break;
                default:
                    sendMessage(message, "This is wrong command, if wanna see commands, then type" +
                            "\n" + "/help");
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            if (callbackQuery.getData().equals(solution)) {
                Player player = playerService.findByChatId(callbackQuery.getMessage().getChatId());
                playerService.addPointToPlayer(player.getId());
                sendMessage(callbackQuery.getMessage(), "Correct, 1+ point");
            } else{
                log.info(solution);
                sendMessage(callbackQuery.getMessage(), "Wrong, no point");
            }

            try {
                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());

                execute(deleteMessage);
            } catch (TelegramApiException e) {
                log.error("Error deleting message: {}", e.getMessage());
            }

        }
    }

    private void saveAPlayer(Message message) {
        if(playerService.findByChatId(message.getChatId()) == null) {
            playerService.save(new Player(message.getChatId(), message.getFrom().getUserName(), 0, LocalDateTime.now()));
        }
    }

    public void sendMessage(Message message, String text){
        try{
            execute(
                    SendMessage.builder()
                            .chatId(message.getChatId())
                            .text(text)
                            .build()
            );
        } catch (TelegramApiException e) {
            log.info(e.getMessage());
        }
    }

    public String getCallBackQuery(Message message) {
        Set<Integer> set = new HashSet<>();

        int randN = (int)(Math.random()*countryImagesService.getAllCountryImages().size());
        set.add(randN);

        CountryImages countryImages = countryImagesService.getCountryImagesById(randN);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        int ran = (int)(Math.random() * 4) + 1;

        List<InlineKeyboardButton> keyboardButtons;
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        String imageName;
        for(int i = 1; i < 5; i++){
            if(ran == i){
                imageName = countryImages.name().replaceFirst(".png", "").replaceFirst("_", "");

                InlineKeyboardButton button1 = new InlineKeyboardButton();
                button1.setText(imageName);
                button1.setCallbackData(""+i);
                keyboardButtons = new ArrayList<>();
                keyboardButtons.add(button1);
            } else {
                int elementId = countryImagesService.getRandomCountryImagesId(set).get();
                set.add(elementId);
                CountryImages element = countryImagesService.getCountryImagesById(elementId);
                imageName = element.name();

                InlineKeyboardButton button1 = new InlineKeyboardButton();
                button1.setText(imageName);
                button1.setCallbackData(""+i);
                keyboardButtons = new ArrayList<>();
                keyboardButtons.add(button1);
            }
            list.add(keyboardButtons);
        }

        keyboardMarkup.setKeyboard(list);

        try {
            execute(SendPhoto.builder()
                    .parseMode("MarkDown")
                    .chatId(message.getChatId())
                    .photo(new InputFile(countryImages.file()))
                    .replyMarkup(keyboardMarkup)
                    .build());
        }catch (TelegramApiException e) {
            log.info(e.getMessage());
        }
        return "" + ran;
    }
}
