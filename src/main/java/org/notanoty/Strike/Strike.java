package org.notanoty.Strike;

import org.notanoty.Chat.Chat;
import org.notanoty.Colors;
import org.notanoty.ConsoleMessages.ConsoleMessages;
import org.notanoty.DB.DB;
import org.notanoty.GroupManager;
import org.notanoty.Poll.StrikePoll;
import org.notanoty.Role.Role;
import org.notanoty.Role.Roles;
import org.notanoty.User.BotUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
                ConsoleMessages.printInfo("Strike was given to user " + Colors.YELLOW + username + " " + user_id + " " + dateOfIssue + Colors.RESET);
            }

            preparedStatementStrike.close();
            connection.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void giveStrike(Long chat_id, Long user_id, String dateOfIssue)
    {
        String strikeQuery = "INSERT INTO strikes (chat_id, user_id, date_of_issue) VALUES (?, ?,  ?)";
        try
        {
            Connection connection = DB.connect();


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
        List<String> usernames = Chat.getGroupUsernamesFromChat(message.getChatId());
        usernames.replaceAll(s -> "/s @" + s);
        System.out.println(Chat.getGroupUsersIdFromChat(message.getChatId()));

        SendMessage strikeMessage = SendMessage
                .builder()
                .chatId(message.getChatId())
                .text("Which user do you want to strike?")
                .build();

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String username : usernames)
        {

            KeyboardRow row = new KeyboardRow();
            row.add(username);
            keyboardRows.add(row);
        }

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup
                .builder()
                .keyboard(keyboardRows)
                .build();

        strikeMessage.setReplyMarkup(replyKeyboardMarkup);

        telegramClient.execute(strikeMessage);
    }


    public static void strikeHandling(Update update, List<String> words, long chatId, long userId, TelegramClient telegramClient) throws TelegramApiException
    {
        Message message = update.getMessage();
        if (words.size() >= 2)
        {
            String username = words.get(1).substring(1);
            if (Chat.isUserCreatorOrAdmin(chatId, userId, telegramClient) && false)
            {
                Strike.giveStrike(chatId, username, LocalDate.now().toString());
                GroupManager.sendMessageToChat(chatId, "Strike was given to the user @" + username, telegramClient);
            }

//            else if (Role.getRole(chatId, userId) == Roles.SUPER_USER)
//            {
//                Strike.giveStrike(chatId, username, LocalDate.now().toString());
//                GroupManager.sendMessageToChat(chatId, "Strike was given to the user @" + username + " by a superuser", telegramClient);
//            }
            else
            {
                StrikePoll.makeStrikePole(chatId, BotUser.getUserIdByUsername(username), telegramClient);
            }
        }
        else
        {
            Strike.giveStrikeWithoutUser(telegramClient, message);
        }
    }
}
