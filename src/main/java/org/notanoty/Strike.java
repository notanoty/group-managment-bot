package org.notanoty;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Strike
{
    public static void giveStrike(Long chat_id, String username, String dateOfIssue)
    {
        String findUserQuery = "SELECT user_id FROM users WHERE username = ? ";
        String strikeQuery = "INSERT INTO strikes (chat_id, user_id, date_of_issue) VALUES (?, ?,  ?)";
        try
        {
            Connection connection = DB.connect();

            PreparedStatement preparedStatementFindUserId = connection.prepareStatement(findUserQuery);
            preparedStatementFindUserId.setString(1, username);

            ResultSet resultSet = preparedStatementFindUserId.executeQuery();
            long user_id = 0;
            if (resultSet.next())
            {
                user_id = resultSet.getLong("user_id");
                System.out.println("User ID for username '" + username + "' is: " + user_id);
            }
            else
            {
                System.out.println("No user found with username: " + username);
                preparedStatementFindUserId.close();
                connection.close();
                return;
            }

            PreparedStatement preparedStatementStrike = connection.prepareStatement(strikeQuery);

            preparedStatementStrike.setLong(1, chat_id);
            preparedStatementStrike.setLong(2, user_id);
            preparedStatementStrike.setString(3, dateOfIssue);

            int rowsInserted = preparedStatementStrike.executeUpdate();

            if (rowsInserted > 0)
            {
                System.out.println("A new strike record was inserted successfully!");
            }

            preparedStatementStrike.close();
            connection.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void giveStrikeWithoutUser(TelegramClient telegramClient, Message message) throws TelegramApiException
    {
        List<String> usernames = BotUser.getGroupUsernamesFromChat(message.getChatId());
        usernames.replaceAll(s -> "/s @" + s);
        System.out.println(Chat.getGroupUsersIdFromChat(message.getChatId()));

        SendMessage strikeMessage = SendMessage
                .builder()
                .chatId(message.getChatId())
                .text("Which user do you want to strike?")
                .build();

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.addAll(usernames);
        keyboardRows.add(row);

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup
                .builder()
                .keyboard(keyboardRows)
                .build();

        strikeMessage.setReplyMarkup(replyKeyboardMarkup);

        telegramClient.execute(strikeMessage);
    }
}
