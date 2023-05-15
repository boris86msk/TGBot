package com.example.bot.tgbot.components;

import com.example.bot.tgbot.service.Icon;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton INFO_BUTTON = new InlineKeyboardButton("INFO");
    private static final InlineKeyboardButton WEATHER_BUTTON = new InlineKeyboardButton("Погода" + Icon.QUEST.get());

    private static final InlineKeyboardButton ID_BUTTON = new InlineKeyboardButton("узнать tg id");

    private static final InlineKeyboardButton JAVA_BUTTON = new InlineKeyboardButton("JAVA");

    public static InlineKeyboardMarkup inlineMarkup() {
        INFO_BUTTON.setCallbackData("/info");
        WEATHER_BUTTON.setCallbackData("/weather");
        ID_BUTTON.setCallbackData("/id");
        JAVA_BUTTON.setCallbackData("/java");

        List<InlineKeyboardButton> rowInline = List.of(INFO_BUTTON, WEATHER_BUTTON);
        List<InlineKeyboardButton> rowInline2 = List.of(ID_BUTTON, JAVA_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline, rowInline2);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
