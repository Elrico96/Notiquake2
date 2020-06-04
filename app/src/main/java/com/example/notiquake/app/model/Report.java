package com.example.notiquake.app.model;

public class Report {
    private String feltIt;
    private String timeOfEarthqauke;
    private String locationCoordinates;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private String comments;
    private String name;
    private String email;
    private String phone;

    public Report(String feltIt, String timeOfEarthqauke, String locationCoordinates, String answer1, String answer2, String answer3, String answer4, String answer5, String answer6, String comments, String name, String email, String phone) {
        this.feltIt = feltIt;
        this.timeOfEarthqauke = timeOfEarthqauke;
        this.locationCoordinates = locationCoordinates;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.answer6 = answer6;
        this.comments = comments;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getFeltIt() {
        return feltIt;
    }

    public String getTimeOfEarthqauke() {
        return timeOfEarthqauke;
    }

    public String getLocationCoordinates() {
        return locationCoordinates;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public String getAnswer6() {
        return answer6;
    }

    public String getComments() {
        return comments;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
