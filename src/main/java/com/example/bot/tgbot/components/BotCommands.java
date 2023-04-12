package com.example.bot.tgbot.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "start bot"),
            new BotCommand("/help", "bot info")
    );

    String HELP_TEXT = "Когда-нибудь я смогу тебе помочь, но пока что я сам ничего не умею\n" +
            "Тебе доступны следующие команды:\n\n" +
            "/start - start the bot\n" +
            "/help - help menu";

    String INFO_TEXT = "Введи название города в ковычках:\n\n" +
            "\"Воронеж\"\n\n" +
            "и я попробую разведать какая там обстановка";
}
