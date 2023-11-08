package com.example.practiceapp_elearning;

public class Article {
    // creating variables for our different fields.
    private String articleName;
    private String articleType;
    private String articleImageLink;
    private String articleLink;
    private String articleDescription;
    private String articleID;

    public Article(String articleName, String articleType, String articleImageLink, String articleLink, String articleDescription, String articleID) {

        this.articleName = articleName;
        this.articleType = articleType;
        this.articleImageLink = articleImageLink;
        this.articleLink = articleLink;
        this.articleDescription = articleDescription;
        this.articleID = articleID;
    }

    public Article() {
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getArticleImageLink() {
        return articleImageLink;
    }

    public void setArticleImageLink(String articleImageLink) {
        this.articleImageLink = articleImageLink;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }
}
