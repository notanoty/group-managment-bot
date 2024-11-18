package org.notanoty.Scheduler;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduledTask {
    private long chatId;
    private String message;
    private LocalDate date;
    private LocalTime time;

    public ScheduledTask(long chatId, String message, LocalDate date, LocalTime time) {
        this.chatId = chatId;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    // Getters and Setters
    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ScheduledTask{" +
                "chatId=" + chatId +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
