package org.notanoty.User;

import org.notanoty.Chat.Chat;
import org.notanoty.Colors;
import org.notanoty.ConsoleMessages.ConsoleMessages;
import org.notanoty.DB.DB;
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

    public static List<String> getChatsWithStrikeCounts(long userId)
    {
        List<String> chatNamesWithCounts = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try
        {
            connection = DB.connect();

            String sql = "SELECT c.chat_name, COUNT(s.chat_id) AS strike_count " +
                    "FROM chats c " +
                    "JOIN strikes s ON c.chat_id = s.chat_id " +
                    "WHERE s.user_id = ? " +
                    "GROUP BY c.chat_name";

            stmt = connection.prepareStatement(sql);
            stmt.setLong(1, userId);

            rs = stmt.executeQuery();

            while (rs.next())
            {
                String chatName = rs.getString("chat_name");
                int strikeCount = rs.getInt("strike_count");
                chatNamesWithCounts.add(" \"" + chatName + "\" - " + strikeCount + " strike(s)");
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return chatNamesWithCounts;
    }

    public static List<String> getChatsWithStrikes(int userId)
    {
        List<String> chatNames = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try
        {
            connection = DB.connect();

            String sql = "SELECT c.chat_name " +
                    "FROM chats c " +
                    "JOIN strikes s ON c.chat_id = s.chat_id " +
                    "WHERE s.user_id = ?";

            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);

            rs = stmt.executeQuery();

            while (rs.next())
            {
                String chatName = rs.getString("chat_name");
                chatNames.add(chatName);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return chatNames;
    }

    public static void seeMyInfo(TelegramClient telegramClient, Update update) throws TelegramApiException
    {
        ConsoleMessages.printInfo(String.valueOf(BotUser.getChatsWithStrikeCounts(update.getMessage().getFrom().getId())));
        String text = "Here is your info:\n" + String.join("\n", BotUser.getChatsWithStrikeCounts(update.getMessage().getFrom().getId()));
        SendMessage SendUserInfo = SendMessage.builder().chatId(update.getMessage().getFrom().getId()).text(text).build();
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
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt(1); // Get the count from the result
                if (count == 0)
                {
                    BotUser.addUserGlobal(telegramClient, update, connection);

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

    public static Long getUserIdByUsername(String username)
    {
        Long user_id = null;

        String findUserIdQuery = "SELECT user_id FROM users WHERE username = ?";
        try
        {
            Connection connection = DB.connect();

            PreparedStatement preparedStatementFindUsername = connection.prepareStatement(findUserIdQuery);
            preparedStatementFindUsername.setString(1, username);

            ResultSet resultSet = preparedStatementFindUsername.executeQuery();

            if (resultSet.next())
            {
                user_id = resultSet.getLong("user_id");
            }

            resultSet.close();
            preparedStatementFindUsername.close();
            connection.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return user_id;
    }
}
