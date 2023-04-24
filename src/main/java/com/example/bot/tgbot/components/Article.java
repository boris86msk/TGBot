package com.example.bot.tgbot.components;

public class Article {
    public String article = "Как уже упоминалось, очень часто используются строки. Используя тот факт, что они неизменяемы, JVM сохраняет все строковые литералы в памяти кучи . Каждый раз, когда мы неявно создаем экземпляр String , его буквальное значение сравнивается с значениями в памяти кучи, и, если он уже существует, ссылочная переменная присваивается уже существующей ячейке памяти.\n" +
            "\n" +
            "Такой подход может значительно сэкономить память, поскольку нет повторяющихся значений. Этот набор сохраненных ячеек памяти называется пулом строк .";
}
