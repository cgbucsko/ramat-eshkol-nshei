package com.cgstudios.ramateshkolnshei;

public class Entry {

    private String title;
    private String message;
    private String phone;
    private String email;
    private String website;
    private String category;

    public Entry() {}

    public Entry(String mTitle, String mMessage, String mPhone, String mEmail, String mWebsite, String mCategory) {
        title = mTitle;
        message = mMessage;
        phone = mPhone;
        email = mEmail;
        website = mWebsite;
        category = mCategory;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) { this.website = website; }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getCategory() {
        return category;
    }
}
