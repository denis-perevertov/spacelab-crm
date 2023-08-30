package com.example.spacelab.util;

import lombok.Data;

@Data
public class FilterBuilder {

    private String name;
    private Long course;
    private String email;
    private String phone;
    private String telegram;
    private Integer rating;
    private String status;
    private String type;
    private String keywords;
    private Long role;
    private String date;
    private Long mentor;
    private Long manager;
    private Boolean active;

    public FilterBuilder name(String name) {this.name = name;return this;}
    public FilterBuilder courseID(Long course) {this.course = course;return this;}
    public FilterBuilder email(String email) {this.email = email;return this;}
    public FilterBuilder phone(String phone) {this.phone = phone;return this;}
    public FilterBuilder telegram(String telegram) {this.telegram = telegram;return this;}
    public FilterBuilder rating(Integer rating) {this.rating = rating;return this;}
    public FilterBuilder status(String status) {this.status = status;return this;}
    public FilterBuilder type(String type) {this.type = type;return this;}
    public FilterBuilder keywords(String keywords) {this.keywords = keywords;return this;}
    public FilterBuilder role(Long role) {this.role = role;return this;}
    public FilterBuilder date(String date) {this.date = date;return this;}
    public FilterBuilder mentor(Long mentor) {this.mentor = mentor;return this;}
    public FilterBuilder manager(Long manager) {this.manager = manager;return this;}
    public FilterBuilder active(Boolean active) {this.active = active;return this;}


    public FilterForm build() {
        FilterForm form = new FilterForm();

        form.setName(this.name);
        form.setCourse(this.course);
        form.setEmail(this.email);
        form.setPhone(this.phone);
        form.setTelegram(this.telegram);
        form.setRating(this.rating);
        form.setStatus(this.status);
        form.setType(this.type);
        form.setKeywords(this.keywords);
        form.setRole(this.role);
        form.setDate(this.date);
        form.setMentor(this.mentor);
        form.setManager(this.manager);
        form.setActive(this.active);

        return form;
    }

}
