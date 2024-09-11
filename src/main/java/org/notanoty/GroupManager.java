package org.notanoty;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class GroupManager implements LongPollingSingleThreadUpdateConsumer
{
    private final TelegramClient telegramClient;

    public GroupManager(String token)
    {
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    public TelegramClient getTelegramClient()
    {
        return telegramClient;
    }

    public void giveStrike(Long chat_id, String username, String dateOfIssue)
    {

        String findUserQuery = "SELECT user_id FROM users WHERE username = ? ";
        String strikeQuery = "INSERT INTO strikes (chat_id, user_id, username, date_of_issue) VALUES (?, ?, ?, ?)";
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
            preparedStatementStrike.setString(3, username);
            preparedStatementStrike.setString(4, dateOfIssue);


            int rowsInserted = preparedStatementStrike.executeUpdate();

            if (rowsInserted > 0)
            {
                System.out.println("A new strike record was inserted successfully!");
            }

            preparedStatementStrike.close();
            connection.close();

        } catch (SQLException e)
        {
            // Handle any SQL exceptions
            e.printStackTrace();
        }
    }

    @Override
    public void consume(Update update)
    {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText())
        {
            // Set variables
            String message_text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            List<String> words = List.of(message_text.split(" "));
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
                        BotUser.addUser(telegramClient, update, connection);
                        System.out.println("User with ID " + Math.toIntExact(update.getMessage().getFrom().getId()) + " is not present in the table.");

                    }
                    else
                    {
                        System.out.println("User with ID " + Math.toIntExact(update.getMessage().getFrom().getId()) + " exists in the table.");
                    }
                }

            } catch (SQLException e)
            {
                throw new RuntimeException(e);
            }


            System.out.println(words);
            try
            {
                switch (words.getFirst())
                {
                    case "/start" ->
                    {
                        SendMessage message = SendMessage
                                .builder()
                                .chatId(chatId)
                                .text(message_text)
                                .build();
                        telegramClient.execute(message);
                    }
                    case "/test" ->
                    {
                        Message message = update.getMessage();
                        System.out.println(message.getFrom());
                        System.out.println(message.getChat());
                    }
                    case "/strike" ->
                    {
                        Message message = update.getMessage();
                        if (words.size() >= 2)
                        {
                            this.giveStrike(message.getChatId(), words.get(1).substring(1), message.getDate().toString());

                            SendMessage messageOutcome = SendMessage
                                    .builder()
                                    .chatId(chatId)
                                    .text("The strike is given to user ".concat(words.get(1)))
                                    .build();
                            telegramClient.execute(messageOutcome);
                        }
                        else
                        {
                            System.out.println("Strike should have a user");
                        }

                    }
                    case "/markup" ->
                    {
                        SendMessage message = SendMessage
                                .builder()
                                .chatId(chatId)
                                .text("Here is your keyboard")
                                .build();

                        message.setReplyMarkup(ReplyKeyboardMarkup
                                .builder()
                                .keyboardRow(new KeyboardRow("/seeMyInfo", "/strike", "/help"))
                                .build());
                        telegramClient.execute(message); // Sending our message object to user
                    }
                    case "/seeMyInfo" ->
                    {
                        BotUser.seeMyInfo(telegramClient, update);
                    }
                    default ->
                    {
                        System.out.println("Unknown command");
                        System.out.println(words);
                    }
                }
            } catch (TelegramApiException e)
            {
                e.printStackTrace();
            }
        }

    }


}
