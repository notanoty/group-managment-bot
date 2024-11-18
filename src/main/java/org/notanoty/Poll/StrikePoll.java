package org.notanoty.Poll;

import org.notanoty.Chat.Chat;
import org.notanoty.GroupManager;
import org.notanoty.Strike.Strike;
import org.notanoty.User.BotUser;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.api.objects.polls.PollOption;
import org.telegram.telegrambots.meta.api.objects.polls.input.InputPollOption;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StrikePoll
{

    private static HashMap<String, ActivePollInfo> activeStrikePollsMap = new HashMap<>();

    public static void addActivePoll(String pollId, long chatId, long userId)
    {
        ActivePollInfo pollInfo = new ActivePollInfo(chatId, userId);
        activeStrikePollsMap.put(pollId, pollInfo);
    }

    public static void pollUpdateInfo(Poll poll)
    {
        System.out.println("Poll ID: " + poll.getId());
        System.out.println("Poll Question: " + poll.getQuestion());
        List<PollOption> pollOptions = poll.getOptions();
        for (PollOption option : pollOptions)
        {
            System.out.println("Option: " + option.getText() + " | Votes: " + option.getVoterCount());
        }
    }

    public static void handlePollUpdate(Poll poll, TelegramClient telegramClient) throws TelegramApiException
    {
        if (poll.getOptions().size() == 2 && poll.getOptions().getFirst().getText().equals("Yes") && poll.getOptions().get(1).getText().equals("No"))
        {
            ActivePollInfo info = activeStrikePollsMap.get(poll.getId());
            int amountOfUsers = Chat.getChatMembersCountWithoutBots(info.getChatID(), telegramClient);
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
                            info.getUserID(),
                            LocalDate.now().toString());
                    GroupManager.sendMessageToChat(
                            info.getChatID(),
                            "The strike is given to user @".concat(BotUser.getUsernameByUserId(info.getUserID())), telegramClient);
                }
                else
                {
                    GroupManager.sendMessageToChat(info.getChatID(), "The strike is not given to user @".concat(BotUser.getUsernameByUserId(info.getUserID())), telegramClient);

                }
            }

        }

    }


    public static void sendPoll(long chatId, TelegramClient telegramClient) throws TelegramApiException
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

    public static void makeStrikePole(long chatId, long userId, TelegramClient telegramClient) throws TelegramApiException
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

}
