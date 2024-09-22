package org.notanoty;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMemberCount;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chat.ChatFullInfo;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;
import org.telegram.telegrambots.meta.api.objects.polls.input.InputPollOption;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GroupManager implements LongPollingSingleThreadUpdateConsumer
{
    private final TelegramClient telegramClient;
    private HashMap<String, ActivePollInfo> activeStrikePollsMap;


    public GroupManager(String token)
    {
        this.telegramClient = new OkHttpTelegramClient(token);
        this.activeStrikePollsMap = new HashMap<String, ActivePollInfo>();
    }

    public HashMap<String, ActivePollInfo> getActiveStrikePollsMap()
    {
        return activeStrikePollsMap;
    }

    public void setActiveStrikePollsMap(HashMap<String, ActivePollInfo> activeStrikePollsMap)
    {
        this.activeStrikePollsMap = activeStrikePollsMap;
    }

    public TelegramClient getTelegramClient()
    {
        return telegramClient;
    }

    public void sendMessageToChat(long chatId, String text) throws TelegramApiException
    {
        SendMessage message = SendMessage.builder().chatId(chatId).text(text).build();
        telegramClient.execute(message);
    }

    @Override
    public void consume(Update update)
    {
        try
        {

            if (update.hasMessage() && update.getMessage().hasText())
            {
                long chatId = update.getMessage().getChatId();
                String message_text = update.getMessage().getText();

                List<String> words = List.of(message_text.split(" "));
                System.out.println(words);
                if (Chat.addNewChatIfNotExist(telegramClient, update))
                {
                    chatInit(chatId);
                    return;
                }
                Chat.addNewUserToChatIfNotExist(telegramClient, update);

                PollAnswer pollRes = update.getPollAnswer();


                String command = GroupManager.getCommand(words.getFirst());
                switch (command)
                {
                    case "/test":
                    {
                        Message message = update.getMessage();
                        System.out.println(message.getFrom());
                        System.out.println(message.getChat());
                        System.out.println(message);
                        break;
                    }
                    case "/strike":
                    case "/s":
                    {
                        Message message = update.getMessage();
                        if (words.size() >= 2)
                        {
                            String username = words.get(1).substring(1);

                            makeStrikePole(chatId, BotUser.getUserIdByUsername(username));
                        }
                        else
                        {
                            Strike.giveStrikeWithoutUser(telegramClient, message);
                        }
                        break;
                    }
                    case "/help":
                    case "/start":
                    case "/markup":
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
                        telegramClient.execute(message);
                        break;
                    }
                    case "/seeMyInfo":
                    {
                        BotUser.seeMyInfo(telegramClient, update);
                        break;
                    }
                    case "/vote":
                    {
                        sendPoll(chatId);
                    }
                    default:
                    {
                        System.out.println("Unknown command: " + words);
                    }
                }
            }
            if (update.hasPoll())
            {
                handlePollUpdate(update.getPoll());
                pollUpdateInfo(update.getPoll());
            }

        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

    }


    public static String getCommand(String word)
    {
        if (word.indexOf('@') == -1)
        {
            return word;
        }
        return word.substring(0, word.indexOf('@'));
    }

    public void addActivePoll(String pollId, long chatId, long userId)
    {
        ActivePollInfo pollInfo = new ActivePollInfo(chatId, userId);
        this.activeStrikePollsMap.put(pollId, pollInfo);
    }

    private void pollUpdateInfo(Poll poll)
    {
        System.out.println("Poll ID: " + poll.getId());
        System.out.println("Poll Question: " + poll.getQuestion());
        System.out.println("Poll: " + poll);
        List<PollOption> pollOptions = poll.getOptions();
        for (PollOption option : pollOptions)
        {
            System.out.println("Option: " + option.getText() + " | Votes: " + option.getVoterCount());
        }
    }

    private void handlePollUpdate(Poll poll) throws TelegramApiException
    {
        if (poll.getOptions().size() == 2 && poll.getOptions().getFirst().getText().equals("Yes") && poll.getOptions().get(1).getText().equals("No"))
        {
            ActivePollInfo info = activeStrikePollsMap.get(poll.getId());
            int amountOfUsers = getChatMembersCountWithoutBots(info.getChatID());
            System.out.println("ChatID: " + info.getChatID() + " amountOfUsers: " + amountOfUsers);
            int votesYes = poll.getOptions().getFirst().getVoterCount();
            int votesNo = poll.getOptions().get(1).getVoterCount();
            int currentVotes = votesYes + votesNo;

            System.out.println("Amount of users: " + amountOfUsers);
            if (currentVotes == amountOfUsers)
            {
                if (votesYes >= votesNo)
                {
                    Strike.giveStrike(
                            info.getChatID(),
                            BotUser.getUsernameByUserId(info.getUserID()),
                            LocalDate.now().toString());
                    SendMessage messageOutcome = SendMessage
                            .builder()
                            .chatId(info.getChatID())
                            .text("The strike is given to user @".concat(BotUser.getUsernameByUserId(info.getUserID())))
                            .build();
                    telegramClient.execute(messageOutcome);
                }
                else
                {
                    SendMessage messageOutcome = SendMessage
                            .builder()
                            .chatId(info.getChatID())
                            .text("The strike is not given to user @".concat(BotUser.getUsernameByUserId(info.getUserID())))
                            .build();
                    telegramClient.execute(messageOutcome);
                }
            }

        }

    }


    private void sendPoll(long chatId) throws TelegramApiException
    {
        SendPoll poll = SendPoll.builder()
                .chatId(chatId)
                .question("What's your favorite programming language?")
                .isAnonymous(false)
                .allowMultipleAnswers(false)
                .options(Stream.of("Java", "Python", "C++", "JavaScript")
                        .map(InputPollOption::new)
                        .collect(Collectors.toList()))
                .build();
        telegramClient.execute(poll);
    }

    private void makeStrikePole(long chatId, long userId) throws TelegramApiException
    {
        SendPoll poll = SendPoll.builder()
                .chatId(chatId)
                .question("Should we give Strike to @" + BotUser.getUsernameByUserId(userId) + "?")
                .isAnonymous(false)
                .allowMultipleAnswers(false)
                .options(Stream.of("Yes", "No")
                        .map(InputPollOption::new)
                        .collect(Collectors.toList()))
                .build();
        Message pollMessage = telegramClient.execute(poll);
        addActivePoll(pollMessage.getPoll().getId(), chatId, userId);
    }

    public boolean isUserAdmin(long chatId, long userId)
    {
        try
        {
            GetChatMember getChatMember = GetChatMember.builder()
                    .chatId(chatId)
                    .userId(userId)
                    .build();

            ChatMember chatMember = telegramClient.execute(getChatMember);

            String status = chatMember.getStatus();
            if (status.equals("administrator"))
            {
                return true;  // The user is an admin
            }

        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

        return false;  // The user is not an admin
    }

    public boolean isUserCreator(long chatId, long userId)
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

        return false;  // The user is not an admin
    }

    public int getChatMembersCount(long chatId) throws TelegramApiException
    {
        GetChatMemberCount getChatMemberCount = GetChatMemberCount.builder().chatId(chatId).build();
        return telegramClient.execute(getChatMemberCount);
    }

    public int getChatMembersCountWithoutBots(long chatId) throws TelegramApiException //TODO make it support other bots
    {
        GetChatMemberCount getChatMemberCount = GetChatMemberCount.builder().chatId(chatId).build();
        return telegramClient.execute(getChatMemberCount) - 1;
    }

    public ChatMember getChatMember(long chatId, long userId) throws TelegramApiException
    {
        GetChatMember getChatMember = GetChatMember.builder().chatId(chatId).userId(userId).build();
        return telegramClient.execute(getChatMember);
    }

    public ChatFullInfo getChat(long chatId) throws TelegramApiException
    {
        GetChat getChat = GetChat.builder().chatId(chatId).build();
        return telegramClient.execute(getChat);
    }

    public void chatInit(long chatId) throws TelegramApiException
    {

        sendMessageToChat(chatId, "This new chat for me.");
        sendMessageToChat(chatId, "Let's do some preparations first.");
        sendMessageToChat(chatId, "Right now I need an owner to setup this chat, otherwise it will use default settings.");
        sendMessageToChat(chatId, "Use /setup to see what you can do.");

    }


}
