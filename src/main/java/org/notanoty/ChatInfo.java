package org.notanoty;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChatInfo
{
    public static void addChatGlobal(TelegramClient telegramClient, Update update, Connection connection) throws SQLException
    {
        String queryInsert = "INSERT INTO chats (chat_id, user_id, chat_name) VALUES (?, ?, ? )";
        PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert);

        int userId = Math.toIntExact(update.getMessage().getFrom().getId());
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();

        preparedStatementInsert.setInt(1, userId);
        preparedStatementInsert.setInt(2, userId);
        preparedStatementInsert.setString(3, update.getMessage().getChat().getTitle());

        // Execute the insert
        int rowsAffected = preparedStatementInsert.executeUpdate();
        if (rowsAffected > 0)
        {
            System.out.println("Chat with ID " + userId + " successfully added to the table.");
        }

    }
}
