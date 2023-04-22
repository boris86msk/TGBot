package com.example.bot.tgbot.service;

import com.example.bot.tgbot.dto.ResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherRestTemplate {
    private static final String URL = "http://localhost:8081/test?param=%s";

    public ResponseDto getWeatherInfo(String param) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseDto resBody = restTemplate.exchange(
                String.format(URL, param),
                HttpMethod.GET,
                new HttpEntity<>(null),
                ResponseDto.class
        ).getBody();
        return resBody;
    }

    public String transformResponseDto(ResponseDto responseDto) {
        String res = "Нашел информацию по запросу: %s\n" +
                "текущая дата/время: %s\n" +
                Icon.TEMP.get() + " температура:  %s°C\n" +
                "давление:  %fмм рт.ст.\n" +
                Icon.CLOUD.get() + " облачность: %s%%\n" +
                " осадки: %smm\n" +
                " скоростьветра: %fм/с";
        String format = String.format(res, responseDto.getLocation().getName(),
                responseDto.getLocation().getLocaltime(),
                responseDto.getCurrent().getTemp_c(),
                Double.parseDouble(responseDto.getCurrent().getPressure_mb())/1.3332d,
                responseDto.getCurrent().getCloud(),
                responseDto.getCurrent().getPrecip_mm(),
                Double.parseDouble(responseDto.getCurrent().getWind_kph())/3.6d);
        return format;
    }
}
