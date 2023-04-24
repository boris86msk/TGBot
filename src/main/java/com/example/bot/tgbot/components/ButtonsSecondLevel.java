package com.example.bot.tgbot.components;

import com.example.bot.tgbot.service.Icon;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class ButtonsSecondLevel {
    private static final InlineKeyboardButton BUTTON_1 = new InlineKeyboardButton("Что такое пул строк?");
    private static final InlineKeyboardButton BUTTON_2 = new InlineKeyboardButton("Кнопа 2");
    private static final InlineKeyboardButton BUTTON_3 = new InlineKeyboardButton("Кнопа 3");
    private static final InlineKeyboardButton BUTTON_4 = new InlineKeyboardButton("Кнопа 4");
    private static final InlineKeyboardButton BUTTON_5 = new InlineKeyboardButton("Кнопа 5");
    private static final InlineKeyboardButton BUTTON_6 = new InlineKeyboardButton("Кнопа 6");
    private static final InlineKeyboardButton BUTTON_7 = new InlineKeyboardButton("Кнопа 7");
    private static final InlineKeyboardButton BUTTON_8 = new InlineKeyboardButton("Назад");

    public static InlineKeyboardMarkup inlineMarkup() {
        BUTTON_1.setCallbackData("/button_1");
        BUTTON_2.setCallbackData("/button_2");
        BUTTON_3.setCallbackData("/button_3");
        BUTTON_4.setCallbackData("/button_4");
        BUTTON_5.setCallbackData("/button_5");
        BUTTON_6.setCallbackData("/button_6");
        BUTTON_7.setCallbackData("/button_7");
        BUTTON_8.setCallbackData("/back");

        List<InlineKeyboardButton> rowInline = List.of(BUTTON_1, BUTTON_2);
        List<InlineKeyboardButton> rowInline2 = List.of(BUTTON_3, BUTTON_4);
        List<InlineKeyboardButton> rowInline3 = List.of(BUTTON_5, BUTTON_6);
        List<InlineKeyboardButton> rowInline4 = List.of(BUTTON_7, BUTTON_8);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline, rowInline2, rowInline3, rowInline4);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
