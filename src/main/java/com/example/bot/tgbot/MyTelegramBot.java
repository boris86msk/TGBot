package com.example.bot.tgbot;

import com.example.bot.tgbot.components.Buttons;
import com.example.bot.tgbot.config.BotConfig;
import com.example.bot.tgbot.dto.ResponseDto;
//import com.example.bot.tgbot.entity.NewUserEntity;
import com.example.bot.tgbot.repository.NewUserRepository;
import com.example.bot.tgbot.service.WeatherRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.time.LocalDate;
import java.util.regex.Pattern;

import static com.example.bot.tgbot.components.BotCommands.*;

@Slf4j
@Service
@EnableScheduling
public class MyTelegramBot extends TelegramLongPollingBot {
    //@Autowired
    //private NewUserRepository newUserRepository;

    @Autowired
    private WeatherRestTemplate weatherRestTemplate;
    private final BotConfig botConfig;

    public MyTelegramBot(BotConfig config) {
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

    /**
     * Метод вызывается при регистрации действий в чате с ботом
     * полученное сообщение текстом обрабатывает блок if (update.hasMessage())
     * нажатие кнопок обрабатывает else if (update.hasCallbackQuery())
     * в обоих случаях вызывается метод botAnswerUtils()
     */
    @Override
    public void onUpdateReceived(@NonNull Update update) {
        long chatId = 0;
        long userId = 0;
        String userName = null;
        String receivedMessage;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();
            //findUser(userId, userName);

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName);
            }

        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            //findUser(userId, userName);
            receivedMessage = update.getCallbackQuery().getData();
            log.info("Пользователь " + userId + " " +
                    "nickname = " + update.getCallbackQuery().getFrom().getFirstName());

            botAnswerUtils(receivedMessage, chatId, userName);
        }
    }

    /**
     *  Добавление нового юзера в БД
     *  ПОМЕТКА: в разработке
    //   private void findUser(long userId, String userName) {
//        if (newUserRepository.findByTgId(String.valueOf(userId)) == null) {
//            NewUserEntity newUserEntity = new NewUserEntity();
//            newUserEntity.setTgId(String.valueOf(userId));
//            newUserEntity.setUserName(userName);
//            newUserEntity.setRegistrationDate(LocalDate.now().toString());
//            newUserRepository.save(newUserEntity);
//        }
  //  }
     */

    /**
     * Метод обработчик команд, поступающих через
     * @param receivedMessage, команда может быть произвольным сообщением
     * а при нажатии кнопки это будет ее маркер setCallbackData()
     */
    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {
        String regex = "\"[а-яА-Я]+-*[а-яА-Я]*\"";
        if (Pattern.matches(regex, receivedMessage)) {
            sendWeatherInCity(receivedMessage, chatId);
        } else if(receivedMessage.equals("/start")) {
            startBot(chatId, userName);
        } else if(receivedMessage.equals("/info")) {
            sendInfoText(chatId, INFO_TEXT);
        } else if(receivedMessage.equals("/weather")) {
            sendInfoText(chatId, WEATHER_TEXT);
        } else if (receivedMessage.equals("/id")) {
            findOutId(chatId, userName);
        } else if (receivedMessage.equals("/java")) {
            entryToJava(chatId);
        } else {
            sendDefaultMessage(chatId, userName, receivedMessage);
            log.info(receivedMessage);
        }

    }

    private void runExecute(SendMessage message, String logInfo) {
        try {
            execute(message);
            if (logInfo != null) {
                log.info(logInfo);
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendDefaultMessage(long chatId, String userName, String receivedMessage) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setReplyMarkup(Buttons.inlineMarkup());
        message.setText(userName + ", скорее всего вы ввели неверные данные/команду" +
                " или не в правильном формате, проверьте " + "<<" + receivedMessage + ">>");

        runExecute(message, null);
    }

    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, " + userName + "! тебе доступны следующие команды:");
        message.setReplyMarkup(Buttons.inlineMarkup());

        runExecute(message, "Start bot");
    }

    private void sendInfoText(long chatId, String info) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(info);

        runExecute(message, null);
    }

    private void findOutId(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Ваши персональные данные в telegram\n" +
                "id: " + chatId + "\n" + "user name: " + userName);

        runExecute(message, null);
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
        message.setReplyMarkup(Buttons.inlineMarkup());
        String logInfo = "id:" + chatId + " asked for the weather in " + param;

        runExecute(message, logInfo);
    }

    /**
     *  Возможность отправлять сообщения по расписанию через шедулер
     *  ПОМЕТКА: в разработке
     */
    @Scheduled(cron = "0 30 10 * * ?")  //fixedDelay = 100 000 = 1:40
    public void sendMessage() {
        SendMessage message = new SendMessage();
        message.setChatId("1468098809");  //Юля=5099445382   1468098809  Вера=5184901392  Александр=948173068
        message.setText("");

        runExecute(message, "message from Schedule");
    }


    /**
     * метод примера добавления мультимедиа в сообщения
     * ПОМЕТКА: в разработке
     */
    private void  entryToJava(long chatId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(new File("C:\\projects\\tgBot\\src\\main\\java\\com\\example\\bot\\tgbot\\service\\java.jpg")));

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

}
