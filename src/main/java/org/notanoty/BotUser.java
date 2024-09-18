package org.notanoty;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public static void addNewUserIfNotExist(TelegramClient telegramClient, Update update)
    {

        String query = "SELECT COUNT(*) FROM users WHERE users.user_id = ?";
        PreparedStatement preparedStatement = null;

        try (Connection connection = DB.connect())
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Math.toIntExact(update.getMessage().getFrom().getId()));
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check the result
            if (resultSet.next())
            {
                int count = resultSet.getInt(1); // Get the count from the result
                if (count == 0)
                {
                    BotUser.addUserGlobal(telegramClient, update, connection);
                    System.out.println("User with ID " + Math.toIntExact(update.getMessage().getFrom().getId()) + " is not present in the table.");

                }
                else
                {
                    System.out.println("User with ID " + Math.toIntExact(update.getMessage().getFrom().getId()) + " exists in the table.");
                }

            }

            preparedStatement.close();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static void addUserGlobal(TelegramClient telegramClient, Update update, Connection connection) throws SQLException
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

    public static String getUsernameByUserId(long user_id)
    {
        String username = null;

        String findUsernameQuery = "SELECT username FROM users WHERE user_id = ?";
        try
        {
            Connection connection = DB.connect();

            PreparedStatement preparedStatementFindUserId = connection.prepareStatement(findUsernameQuery);
            preparedStatementFindUserId.setLong(1, user_id);

            ResultSet resultSet = preparedStatementFindUserId.executeQuery();

            if (resultSet.next())
            {
                username = resultSet.getString("username");
            }

            resultSet.close();
            preparedStatementFindUserId.close();
            connection.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return username;
    }

    public static List<String> getGroupUsernamesFromChat(long chatId)
    {
        List<Long> userIds = Chat.getGroupUsersIdFromChat(chatId);

        List<String> usernames = new ArrayList<>();

        for (Long userId : userIds)
        {
            String username = BotUser.getUsernameByUserId(userId);
            if (username != null)
            {
                usernames.add(username);
            }
        }
        return usernames;
    }
}
