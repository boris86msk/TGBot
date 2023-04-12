package com.example.bot.tgbot.dto;

import lombok.Data;

@Data
public class LocationDto {
    private String name;
    private String region;
    private String country;
    private String lat;
    private String lon;
    private String tz_id;
    private String localtime_epoch;
    private String localtime;
}
