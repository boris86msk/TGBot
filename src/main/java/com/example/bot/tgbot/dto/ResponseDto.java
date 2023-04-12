package com.example.bot.tgbot.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseDto {
    private LocationDto location;

    private CurrentDto current;

}
