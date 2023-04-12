package com.example.bot.tgbot.api;

import com.example.bot.tgbot.TestTelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    TestTelegramBot bot;

    @GetMapping("/")
    public void method(@RequestParam String message){
        bot.testmethod(message);
    }
}
