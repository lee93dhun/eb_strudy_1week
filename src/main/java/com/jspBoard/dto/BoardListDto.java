package com.jspBoard.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BoardListDto {
    private int postId;
    private String postWriter;
    private String postTitle;
    private int postHits;
    private LocalDateTime uploadDatetime;
    private LocalDateTime updateDatetime;
    private int categoryId;
    private String categoryName;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostWriter() {
        return postWriter;
    }

    public void setPostWriter(String postWriter) {
        this.postWriter = postWriter;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public int getPostHits() {
        return postHits;
    }

    public void setPostHits(int postHits) {
        this.postHits = postHits;
    }

    public LocalDateTime getUploadDatetime() {
        return uploadDatetime;
    }

    public void setUploadDatetime(LocalDateTime uploadDatetime) {
        this.uploadDatetime = uploadDatetime;
    }

    public LocalDateTime getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(LocalDateTime updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
