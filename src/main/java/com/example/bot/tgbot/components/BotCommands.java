package com.example.bot.tgbot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot")
    );

    String INFO_TEXT = "Бот создан пользователем https://t.me/Boris320\n\n" +
            "boris86msk@yandex.ru\n\n" +
            "приятного общения!";

    String WEATHER_TEXT = "Введи название города в ковычках\n\n" +
            "например: \"Воронеж\"\n\n" +
            "и я попробую разведать какая там обстановка";
}
