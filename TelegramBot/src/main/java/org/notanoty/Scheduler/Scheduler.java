package org.notanoty.Scheduler;

import org.notanoty.Colors;
import org.notanoty.ConsoleMessages.ConsoleMessages;
import org.notanoty.DB.DB;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler
{
    private final ScheduledExecutorService scheduler;
    private final TelegramClient telegramClient;


    public Scheduler(TelegramClient telegramClient)
    {
        this.telegramClient = telegramClient;
        this.scheduler = Executors.newScheduledThreadPool(1);
//        scheduleDatabaseChecker();
    }

    public ScheduledExecutorService getScheduler()
    {
        return scheduler;
    }

    public TelegramClient getTelegramClient()
    {
        return telegramClient;
    }

    private void scheduleDatabaseChecker()
    {
        Runnable checkTask = () ->
        {
//            try
//            {
                System.out.println("Checking database for scheduled tasks...");
//                ScheduledTask task = Scheduler.getScheduledTaskByDateTime(LocalDate.now(), LocalTime.now());
//                if (task != null)
//                {
//                    sendScheduledNotifications(task);
//                }
//                else
//                {
//                    ConsoleMessages.printWarning("No scheduled tasks found for scheduled tasks.");
//                }
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
        };

        scheduler.scheduleAtFixedRate(checkTask, 0, 15, TimeUnit.SECONDS);
    }

    public void testSendScheduledNotification()
    {
        LocalDate date = LocalDate.of(2024, 10, 19);
        LocalTime time = LocalTime.of(13, 30);

        ScheduledTask task = Scheduler.getScheduledTaskByDateTime(date, time);

        if (task != null)
        {
            System.out.println("Scheduled task found: " + task);
//            sendScheduledNotifications(task);
        }
        else
        {
            System.out.println("No scheduled task found at 13:30 on " + date + ".");
        }
    }

    private void sendScheduledNotifications(ScheduledTask task)
    {
        System.out.println("Sending notifications to the groups...");
        try
        {
            SendMessage message = SendMessage.builder()
                    .chatId(task.getChatId())
                    .text(task.getMessage())
                    .build();

            telegramClient.execute(message);

            System.out.println("Notification sent to chat: " + task.getChatId());
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public static void makeScheduledTask(long chatId, String message, LocalDate date, LocalTime time)
    {
        String queryCheck = "SELECT COUNT(*) FROM scheduled_task WHERE chat_id = ? AND date = ? AND time = ?";
        String queryInsert = "INSERT INTO scheduled_task (chat_id, message, date, time) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;

        try (Connection connection = DB.connect())
        {
            preparedStatement = connection.prepareStatement(queryCheck);
            preparedStatement.setLong(1, chatId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(date));
            preparedStatement.setTime(3, java.sql.Time.valueOf(time));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                int count = resultSet.getInt(1);
                if (count == 0)
                {
                    preparedStatement = connection.prepareStatement(queryInsert);
                    preparedStatement.setLong(1, chatId);
                    preparedStatement.setString(2, message);
                    preparedStatement.setDate(3, java.sql.Date.valueOf(date));
                    preparedStatement.setTime(4, java.sql.Time.valueOf(time));
                    preparedStatement.executeUpdate();
                    System.out.println("New scheduled task created for chat: " + chatId);
                }
                else
                {
                    System.out.println("Task already exists for this chat, date, and time.");
                }
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static ScheduledTask getScheduledTaskByDateTime(LocalDate date, LocalTime time)
    {
        String query = "SELECT * FROM scheduled_task WHERE date = ? AND time = ?";
        PreparedStatement preparedStatement = null;
        ScheduledTask task = null;

        try (Connection connection = DB.connect())
        {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, java.sql.Date.valueOf(date)); // Set the date parameter
            preparedStatement.setTime(2, java.sql.Time.valueOf(time)); // Set the time parameter

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                long chatId = resultSet.getLong("chat_id");
                String message = resultSet.getString("message");
                LocalDate scheduledDate = resultSet.getDate("date").toLocalDate();
                LocalTime scheduledTime = resultSet.getTime("time").toLocalTime();

                task = new ScheduledTask(chatId, message, scheduledDate, scheduledTime);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return task;
    }
}
