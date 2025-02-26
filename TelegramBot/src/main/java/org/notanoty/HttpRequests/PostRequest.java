package org.notanoty.HttpRequests;

class PostRequest {
    public String title;
    public String body;
    public int userId;

    public PostRequest(String title, String body, int userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
    }
}
