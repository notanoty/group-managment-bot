package org.notanoty.Poll;

public class ActivePollInfo
{
    private long chatID;
    private long userID;

    public ActivePollInfo(long chatID, long userID)
    {
        this.chatID = chatID;
        this.userID = userID;
    }

    public long getChatID()
    {
        return chatID;
    }

    public void setChatID(long chatID)
    {
        this.chatID = chatID;
    }

    public long getUserID()
    {
        return userID;
    }

    public void setUserID(long userID)
    {
        this.userID = userID;
    }
}
