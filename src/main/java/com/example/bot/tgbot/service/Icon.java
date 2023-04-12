package com.example.bot.tgbot.service;

import com.vdurmont.emoji.EmojiParser;

import java.util.List;

public enum Icon {
    CHECK(":white_check_mark:"),
    NOT(":x:"),
    CLOUD(":cloud:"),
    TEMP(":thermometer:"),
    WIND(":wind_face:"),
    RAIN(":cloud with rain:"),
    QUEST(":question:");

    private String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

    Icon(String value) {
        this.value = value;
    }
}
