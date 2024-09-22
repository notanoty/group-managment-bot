package org.notanoty;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Chat
{
    public static void addChat(TelegramClient telegramClient, Update update, Connection connection) throws SQLException
    {
        String queryInsert = "INSERT INTO chats (chat_id, chat_name) VALUES (?, ?)";
        PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert);


        preparedStatementInsert.setLong(1, update.getMessage().getChatId());
        preparedStatementInsert.setString(2, update.getMessage().getChat().getTitle());

        // Execute the insert
        int rowsAffected = preparedStatementInsert.executeUpdate();
        if (rowsAffected > 0)
        {
            System.out.println("Chat with ID " + update.getMessage().getChatId() + " successfully added to the table (chats).");
        }
    }

    public static void addUserToChatGlobal(TelegramClient telegramClient, Update update, Connection connection) throws SQLException
    {
        String queryInsert = "INSERT INTO users_in_chats (chat_id, user_id) VALUES (?, ?)";
        PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert);


        preparedStatementInsert.setLong(1, update.getMessage().getChatId());
        preparedStatementInsert.setLong(2, update.getMessage().getFrom().getId());

        int rowsAffected = preparedStatementInsert.executeUpdate();
        if (rowsAffected > 0)
        {
            System.out.println("User with ID " + update.getMessage().getFrom().getId() + " successfully added to the table (users_in_chats).");
        }
    }

    public static void addNewUserToChatIfNotExist(TelegramClient telegramClient, Update update)
    {

        String query = "SELECT COUNT(*) FROM users_in_chats WHERE users_in_chats.user_id = ? AND users_in_chats.chat_id = ?";

        try (Connection connection = DB.connect())
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, update.getMessage().getFrom().getId());
            preparedStatement.setLong(2, update.getMessage().getChatId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt(1);
                if (count == 0)
                {
                    Chat.addUserToChatGlobal(telegramClient, update, connection);
//                    System.out.println("User with ID " + update.getMessage().getFrom().getId() + " was not present in the table (users_in_chats).");

                }
                else
                {
//                    System.out.println("User with ID " + update.getMessage().getFrom().getId() + " exists in the table (users_in_chats).");
                }

            }

            preparedStatement.close();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static boolean addNewChatIfNotExist(TelegramClient telegramClient, Update update)
    {

        String query = "SELECT COUNT(*) FROM chats WHERE chats.chat_id = ?";

        try (Connection connection = DB.connect())
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, update.getMessage().getChatId());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt(1);
                if (count == 0)
                {
                    Chat.addChat(telegramClient, update, connection);
                    return true;
//                    System.out.println("Chat with ID " + update.getMessage().getChatId() + " was not present in the table (chats).");
                }
                else
                {
                    return false;
//                    System.out.println("Chat with ID " + update.getMessage().getChatId() + " exists in the table (chats).");
                }
            }

        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return false;

    }

    public static List<Long> getGroupUsersIdFromChat(long chat_id) {
        List<Long> userIds = new ArrayList<>();

        String findUserQuery = "SELECT user_id FROM users_in_chats WHERE chat_id = ?";
        try {
            Connection connection = DB.connect();

            PreparedStatement preparedStatementFindUserId = connection.prepareStatement(findUserQuery);
            preparedStatementFindUserId.setLong(1, chat_id);

            ResultSet resultSet = preparedStatementFindUserId.executeQuery();

            while (resultSet.next()) {
                userIds.add(resultSet.getLong("user_id"));
            }


            resultSet.close();
            preparedStatementFindUserId.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userIds;
    }

    public static int getGroupUsersAmount(long chat_id) {
        List<Long> userIds = new ArrayList<>();

        String findUserQuery = "SELECT user_id FROM users_in_chats WHERE chat_id = ?";
        try {
            Connection connection = DB.connect();

            PreparedStatement preparedStatementFindUserId = connection.prepareStatement(findUserQuery);
            preparedStatementFindUserId.setLong(1, chat_id);

            ResultSet resultSet = preparedStatementFindUserId.executeQuery();

            while (resultSet.next()) {
                userIds.add(resultSet.getLong("user_id"));
            }


            resultSet.close();
            preparedStatementFindUserId.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userIds.size();
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
