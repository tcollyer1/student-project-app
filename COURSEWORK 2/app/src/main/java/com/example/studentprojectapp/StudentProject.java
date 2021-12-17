package com.example.studentprojectapp;

public class StudentProject {
    private int projectID;
    private int studentID;
    private String title;
    private String description;
    private int year;
    private String first_name;
    private String second_name;

    public StudentProject(int ProjectID, int StudentID, String Title, String Description, int Year, String First_Name, String Second_Name) {
        projectID = ProjectID;
        studentID = StudentID;
        title = Title;
        description = Description;
        year = Year;
        first_name = First_Name;
        second_name = Second_Name;
    }

    public int getProjectID() {
        return projectID;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getSecond_name() {
        return second_name;
    }
}
