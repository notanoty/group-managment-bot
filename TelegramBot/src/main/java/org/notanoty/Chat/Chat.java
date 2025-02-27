package org.notanoty.Chat;

import com.fasterxml.jackson.databind.JsonNode;
import org.notanoty.Colors;
import org.notanoty.DB.DB;
import org.notanoty.GroupManager;
import org.notanoty.HttpRequests.HttpClientSingleton;
import org.notanoty.User.BotUser;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMemberCount;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.ChatFullInfo;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat
{
    public static void addChat(TelegramClient telegramClient, Update update, Connection connection) throws SQLException
    {
        String queryInsert = "INSERT INTO chats (chat_id, chat_name) VALUES (?, ?)";
        PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert);


        preparedStatementInsert.setLong(1, update.getMessage().getChatId());
        preparedStatementInsert.setString(2, update.getMessage().getChat().getTitle());

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

    public static void addNewUserToChatIfNotExist(Long telegramChatId, Long telegramUserId, String userName, String firstName, String lastName)
    {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("telegramChatId", telegramChatId);
            requestBody.put("telegramUserId", telegramUserId);
            requestBody.put("userName", userName);
            requestBody.put("firstName", firstName);
            requestBody.put("lastName", lastName);

            // Convert the map to a JSON string
            JsonNode response = HttpClientSingleton.sendPostRequest("http://localhost:8080/user/create-if-not-exist" , requestBody);
            if(!response.get("error").isNull()){
                System.out.println(Colors.RED + response.get("error").asText() + Colors.RESET);
            }
            else{
                System.out.println(Colors.GREEN + response.get("message").asText() + Colors.RESET);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    {


    }

    public static void addNewChatIfNotExist(Long telegramChatId, String title)
    {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("telegramChatId", telegramChatId);
            requestBody.put("chatName", title);

            // Convert the map to a JSON string
            JsonNode response = HttpClientSingleton.sendPostRequest("http://localhost:8080/chat/create-if-not-exist" , requestBody);
            if(!response.get("error").isNull()){
                System.out.println(Colors.RED + response.get("error").asText() + Colors.RESET);
            }
            else{
                System.out.println(Colors.GREEN + response.get("message").asText() + Colors.RESET);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Long> getGroupUsersIdFromChat(long chat_id)
    {
        return null;
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

    public static int getChatMembersCountWithoutBots(long chatId, TelegramClient telegramClient) throws TelegramApiException //TODO make it support other bots
    {
        GetChatMemberCount getChatMemberCount = GetChatMemberCount.builder().chatId(chatId).build();
        return telegramClient.execute(getChatMemberCount) - 1;
    }

    public static void getChatHelp(long chatId, TelegramClient telegramClient) throws TelegramApiException
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

        chatInit(chatId, telegramClient);
        telegramClient.execute(message);
    }

    public static void chatInit(long chatId, TelegramClient telegramClient) throws TelegramApiException
    {
        GroupManager.sendMessageToChat(chatId, """
                This is a new chat for me.
                Let's do some preparations first.
                Right now I need an owner to setup this chat, otherwise it will use default settings.
                Use /setup to see what you can do.""", telegramClient);
    }

    public static boolean isUserCreator(long chatId, long userId, TelegramClient telegramClient)
    {
        try
        {
            GetChatMember getChatMember = GetChatMember.builder()
                    .chatId(chatId)
                    .userId(userId)
                    .build();

            ChatMember chatMember = telegramClient.execute(getChatMember);

            String status = chatMember.getStatus();
            if (status.equals("creator"))
            {
                return true;
            }

        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isUserCreatorOrAdmin(long chatId, long userId, TelegramClient telegramClient)
    {
        try
        {
            GetChatMember getChatMember = GetChatMember.builder()
                    .chatId(chatId)
                    .userId(userId)
                    .build();

            ChatMember chatMember = telegramClient.execute(getChatMember);

            String status = chatMember.getStatus();
            if (status.equals("creator") || status.equals("administrator"))
            {
                return true;
            }

        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static ChatMember getChatMember(long chatId, long userId, TelegramClient telegramClient) throws TelegramApiException
    {
        GetChatMember getChatMember = GetChatMember.builder().chatId(chatId).userId(userId).build();
        return telegramClient.execute(getChatMember);
    }

    public static ChatFullInfo getChat(long chatId, TelegramClient telegramClient) throws TelegramApiException
    {
        GetChat getChat = GetChat.builder().chatId(chatId).build();
        return telegramClient.execute(getChat);
    }
}
