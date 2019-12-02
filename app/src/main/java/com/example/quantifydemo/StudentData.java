package com.example.quantifydemo;

public class StudentData {

    private String userName;
    private String section;
    private String USN;
    private String department;
    private String semester;

    public StudentData() {
    }

    public StudentData(String userName, String section, String USN, String department, String semester) {
        this.userName = userName;
        this.section = section;
        this.USN = USN;
        this.department = department;
        this.semester = semester;
    }

    public String getUserName() {
        return userName;
    }

    public String getSection() {
        return section;
    }

    public String getUSN() {
        return USN;
    }

    public String getDepartment() {
        return department;
    }

    public String getSemester() {
        return semester;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setUSN(String USN) {
        this.USN = USN;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
