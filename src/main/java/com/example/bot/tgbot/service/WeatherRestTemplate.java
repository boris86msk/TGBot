package com.example.bot.tgbot.service;

import com.example.bot.tgbot.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherRestTemplate {

    @Value("${API_Key}")
    private String api_key;

    private String method_api = "current.json";
    private static final String ROOT_URL = "http://api.weatherapi.com/v1/";
    private static final String PARAM = "%s%s?key=%s&q=%s&aqi=no";

    public ResponseDto getWeatherInfo(String param) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseDto resBody = new ResponseDto();
        try {
            resBody = restTemplate.exchange(
                    String.format(PARAM, ROOT_URL, method_api, api_key, param),
                    HttpMethod.GET,
                    new HttpEntity<>(null),
                    ResponseDto.class
            ).getBody();
            return resBody;
        } catch (Exception e) {
            // добавить исключения
            resBody.setErrorMessage("Неверные параметры ввода");
            return resBody;
        }
    }
    public String transformResponseDto(ResponseDto responseDto) {
        String res = "Нашел информацию по запросу: %s\n" +
                " текущая дата/время: %s\n" +
                " температура:  %s°C" + Icon.TEMP.get() + "\n" +
                " давление:  %fмм рт.ст.\n" +
                " облачность: %s%%" + Icon.CLOUD.get() + "\n" +
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

    /**
     * поле и метод для обращению к WeatherAPI на другом сервисе
     */
//    private static final String URL = "http://localhost:8081/test?param=%s";
//
//    public ResponseDto getWeatherInfo(String param) {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseDto resBody = restTemplate.exchange(
//                String.format(URL, param),
//                HttpMethod.GET,
//                new HttpEntity<>(null),
//                ResponseDto.class
//        ).getBody();
//        return resBody;
//    }
}
