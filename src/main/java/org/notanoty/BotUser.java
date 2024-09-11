package org.notanoty;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BotUser
{

    public void searchGroupInfo()
    {

    }

    public static void seeMyInfo(TelegramClient telegramClient, Update update) throws TelegramApiException
    {
        System.out.println(update.getMessage().getChatId());

        SendMessage SendUserInfo = SendMessage.builder().chatId(update.getMessage().getFrom().getId()).text("info").build();
        telegramClient.execute(SendUserInfo);
    }

    public static void addUserGlobal(TelegramClient telegramClient, Update update, Connection connection) throws  SQLException
    {
        String queryInsert = "INSERT INTO users (user_id, username, first_name, last_name) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert);

        // Retrieve user information
        int userId = Math.toIntExact(update.getMessage().getFrom().getId());
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();

        // Set values for the insert statement
        preparedStatementInsert.setInt(1, userId);
        preparedStatementInsert.setString(2, username);
        preparedStatementInsert.setString(3, firstName);
        preparedStatementInsert.setString(4, lastName);

        // Execute the insert
        int rowsAffected = preparedStatementInsert.executeUpdate();
        if (rowsAffected > 0)
        {
            System.out.println("User with ID " + userId + " successfully added to the table.");
        }

    }


}
