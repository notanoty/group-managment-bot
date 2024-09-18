package org.notanoty;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;



//TODO refactor database TO MANY UNNECESSARY COLUMNS




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


    @Override
    public void consume(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {

            String message_text = update.getMessage().getText();
            System.out.println(update.getMessage());

            long chatId = update.getMessage().getChatId();
            List<String> words = List.of(message_text.split(" "));

            BotUser.addNewUserIfNotExist(telegramClient, update);
            Chat.addNewChatIfNotExist(telegramClient, update);
            Chat.addNewUserToChatIfNotExist(telegramClient, update);

            System.out.println(words);
            try
            {
                switch (words.getFirst())
                {
                    case "/start":
                    {
                        SendMessage message = SendMessage
                                .builder()
                                .chatId(chatId)
                                .text(message_text)
                                .build();
                        telegramClient.execute(message);
                        break;
                    }
                    case "/test":
                    {
                        Message message = update.getMessage();
                        System.out.println(message.getFrom());
                        System.out.println(message.getChat());
                        System.out.println(message);
                        break;
                    }
                    case "/strike" :
                    case "/s":
                    {
                        Message message = update.getMessage();
                        if (words.size() >= 2)
                        {
                            Strike.giveStrike(message.getChatId(), words.get(1).substring(1), message.getDate().toString());

                            SendMessage messageOutcome = SendMessage
                                    .builder()
                                    .chatId(chatId)
                                    .text("The strike is given to user ".concat(words.get(1)))
                                    .build();
                            telegramClient.execute(messageOutcome);
                        }
                        else
                        {
                            List<String> usernames = BotUser.getGroupUsernamesFromChat(message.getChatId());
                            usernames.replaceAll(s -> "/s @" + s);
                            System.out.println(Chat.getGroupUsersIdFromChat(message.getChatId()));
                            System.out.println("Strike should have a user");

                            SendMessage strikeMessage = SendMessage
                                    .builder()
                                    .chatId(chatId)
                                    .text("Which user do you want to strike?")
                                    .build();

                            List<KeyboardRow> keyboardRows = new ArrayList<>();
                            KeyboardRow row = new KeyboardRow();
                            row.addAll(usernames); // Add all usernames to a single row
                            keyboardRows.add(row);

                            ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup
                                    .builder()
                                    .keyboard(keyboardRows)
                                    .build();

                            strikeMessage.setReplyMarkup(replyKeyboardMarkup);

                            telegramClient.execute(strikeMessage);
                        }
                        break;
                    }
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
                    default:
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
