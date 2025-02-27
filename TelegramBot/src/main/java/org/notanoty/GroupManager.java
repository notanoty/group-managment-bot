package org.notanoty;

import org.notanoty.Chat.Chat;
import org.notanoty.ConsoleMessages.ConsoleMessages;
import org.notanoty.Poll.ActivePollInfo;
import org.notanoty.Poll.StrikePoll;
import org.notanoty.Strike.Strike;
import org.notanoty.User.BotUser;
import org.notanoty.Scheduler.Scheduler;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMemberCount;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.ChatFullInfo;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.util.*;


public class GroupManager implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private HashMap<String, ActivePollInfo> activeStrikePollsMap;
    private final Scheduler scheduler;

    public GroupManager(String token) {
        this.telegramClient = new OkHttpTelegramClient(token);
        this.activeStrikePollsMap = new HashMap<String, ActivePollInfo>();
        this.scheduler = new Scheduler(this.telegramClient);
    }

    public HashMap<String, ActivePollInfo> getActiveStrikePollsMap() {
        return activeStrikePollsMap;
    }

    public void setActiveStrikePollsMap(HashMap<String, ActivePollInfo> activeStrikePollsMap) {
        this.activeStrikePollsMap = activeStrikePollsMap;
    }

    public TelegramClient getTelegramClient() {
        return telegramClient;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void sendMessageToChat(long chatId, String text) throws TelegramApiException {
        sendMessageToChat(chatId, text, telegramClient);
    }

    public static void sendMessageToChat(long chatId, String text, TelegramClient telegramClient) throws TelegramApiException {
        SendMessage message = SendMessage.builder().chatId(chatId).text(text).build();
        telegramClient.execute(message);
    }

    @Override
    public void consume(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                long telegramChatId = update.getMessage().getChatId();
                long telegramMessageId = update.getMessage().getMessageId();
                long telegramUserId = update.getMessage().getFrom().getId();

                String messageText = update.getMessage().getText();

                List<String> words = List.of(messageText.split(" "));

                Chat.addNewChatIfNotExist(telegramChatId, update.getMessage().getChat().getTitle());
                Chat.addNewUserToChatIfNotExist(telegramChatId, telegramUserId, update.getMessage().getFrom().getUserName(), update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName());

                String command = GroupManager.getCommand(words.getFirst());
                System.out.println("Command: " + command);
                System.out.println("Update: " + update);
                switch (command) {
                    case "/strike":
                    case "/s": {
//                        Strike.strikeHandling(update, words, telegramChatId, telegramUserId, getTelegramClient());
//                        Strike.giveStrike(telegramChatId, telegramUserId, LocalDate.now());
                        break;
                    }
                    case "/test": {
                        sendMessageToChat(telegramChatId, "Test message", telegramClient);
                        break;
                    }
                    case "/help":
                    case "/start":
                    case "/markup": {
//                        Chat.getChatHelp(telegramChatId, getTelegramClient());
                        break;
                    }
                    case "/seeMyInfo":
                    case "/seemyinfo": {
//                        BotUser.seeMyInfo(telegramClient, update);
                        break;
                    }
                    case "/vote": {
//                        StrikePoll.sendPoll(telegramChatId, getTelegramClient());
                    }
                    case "/sch":
                    case "/schedule_task": {

//                        getScheduler().makeScheduledTask(telegramChatId, "bi bi bo bo", LocalDate.of(2024,10, 19), LocalTime.of(13, 30));
                        break;
                    }
                    default: {
                        ConsoleMessages.printError("Unknown command", String.valueOf(words));
                    }
                }
            }
//            if (update.hasPoll())
//            {
//                StrikePoll.handlePollUpdate(update.getPoll(), getTelegramClient());
//                StrikePoll.pollUpdateInfo(update.getPoll());
//            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    public static String getCommand(String word) {
        if (word.indexOf('@') == -1) {
            return word;
        }
        return word.substring(0, word.indexOf('@'));
    }

    public static boolean isUserAdmin(long chatId, long userId, TelegramClient telegramClient) {
        try {
            GetChatMember getChatMember = GetChatMember.builder()
                    .chatId(chatId)
                    .userId(userId)
                    .build();

            ChatMember chatMember = telegramClient.execute(getChatMember);

            String status = chatMember.getStatus();
            if (status.equals("administrator")) {
                return true;
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return false;
    }


    public void sendScheduledMessage(long chatId, String text) throws TelegramApiException {
        SendMessage message = SendMessage.builder().chatId(chatId).text(text).build();
        ConsoleMessages.printInfo("Scheduled message is sent");
        telegramClient.execute(message);
    }


    public int getChatMembersCount(long chatId) throws TelegramApiException {
        return getChatMembersCount(chatId, telegramClient);
    }

    public int getChatMembersCountWithoutBots(long chatId) throws TelegramApiException //TODO make it support other bots
    {
        return Chat.getChatMembersCountWithoutBots(chatId, telegramClient);
    }

    public static int getChatMembersCount(long chatId, TelegramClient telegramClient) throws TelegramApiException {
        GetChatMemberCount getChatMemberCount = GetChatMemberCount.builder().chatId(chatId).build();
        return telegramClient.execute(getChatMemberCount);
    }
}


