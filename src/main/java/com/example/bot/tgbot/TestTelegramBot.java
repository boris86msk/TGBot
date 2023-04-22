package com.example.bot.tgbot;

import com.example.bot.tgbot.components.Buttons;
import com.example.bot.tgbot.config.BotConfig;
import com.example.bot.tgbot.dto.ResponseDto;
import com.example.bot.tgbot.service.WeatherRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.regex.Pattern;

import static com.example.bot.tgbot.components.BotCommands.*;

@Slf4j
@Service
public class TestTelegramBot extends TelegramLongPollingBot {
    @Autowired
    private WeatherRestTemplate weatherRestTemplate;
    private final BotConfig botConfig;

    public TestTelegramBot(BotConfig config) {
        this.botConfig = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        long chatId = 0;
        long userId = 0; //это нам понадобится позже
        String userName = null;
        String receivedMessage;

        //если получено сообщение текстом
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName);
            }

            //если нажата одна из кнопок бота
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();
            log.info("Пользователь " + userId + " " +
                    "nickname = " + update.getCallbackQuery().getFrom().getFirstName());

            botAnswerUtils(receivedMessage, chatId, userName);
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        String regex = "\"[а-яА-Я]+\"";
        if (Pattern.matches(regex, receivedMessage)) {
            sendWeatherInCity(receivedMessage, chatId);
        } else if(receivedMessage.equals("/start")) {
            startBot(chatId, userName);
        } else if(receivedMessage.equals("/help")) {
            sendHelpText(chatId, HELP_TEXT);
        } else if (receivedMessage.equals("/info")) {
            sendInfoText(chatId, INFO_TEXT);
        } else if (receivedMessage.equals("/java")) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(new File("C:\\projects\\tgBot\\src\\main\\java\\com\\example\\bot\\tgbot\\service\\java.jpg")));

            try {
                execute(sendPhoto);
                log.info("neeeeeeeeed");
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        } else {
            sendDefaultMessage(chatId, userName, receivedMessage);
            log.info(receivedMessage);
        }

    }

    private void sendDefaultMessage(long chatId, String userName, String receivedMessage) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(userName + ", скорее всего вы ввели неверные данные" +
                " или не в правильном формате, проверьте " + "<<" + receivedMessage +
                        ">>" + " и повторите попытку");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, " + userName + "! это учебный бот для проекта WeatherAPI.");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            log.info("Start bot");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendHelpText(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            log.info("need halp");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendInfoText(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            log.info("send info");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendWeatherInCity(String param, long chatId) {
        ResponseDto weatherInfo = null;
        String str = "сервис недоступен";
        try {
            weatherInfo = weatherRestTemplate.getWeatherInfo(param);
        } catch (ResourceAccessException e) {
            log.error(e.getMessage());
        }

        if(weatherInfo != null) {
            str = weatherRestTemplate.transformResponseDto(weatherInfo);
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(str);
        try {
            execute(message);
            log.info("id:" + chatId + " asked for the weather in " + param);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    //Отправка сообщения по id "в лоб"
    public void testmethod(String mess) {
        SendMessage message = new SendMessage();
        message.setChatId("5184901392");  //Юля=5099445382   1468098809  Вера=5184901392
        message.setText("Сообщение от Бориса : " + mess);

        try {
            execute(message);
            log.info("message in lob");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
