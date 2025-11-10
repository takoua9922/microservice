package com.esprit.ms.avis.event;



import java.time.Instant;

public class ReviewCreatedEvent {
    private Long id;
    private Long articleId;
    private Long userId;
    private int rating;
    private String comment;
    private Instant createdAt;

    public ReviewCreatedEvent() {}

    public ReviewCreatedEvent(Long id, Long articleId, Long userId, int rating, String comment, Instant createdAt) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
