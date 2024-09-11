package org.notanoty;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Hello and welcome!");
        Properties prop = new Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream input = classloader.getResourceAsStream("config.properties");
        try
        {
            prop.load(input);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        String apiKey = prop.getProperty("API_KEY");

        try
        {
            TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication();
            botsApplication.registerBot(apiKey, new GroupManager(apiKey));
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

    }
}
