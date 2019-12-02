package com.example.quantifydemo;

import java.util.List;

public class Tests {
    private String section;
    private String semester;
    private String teacher;
    private String subject;
    private String key;
    private List<TestQuestion> questions;

    public Tests() {
    }

    public Tests(String section, String semester, String teacher, String subject,String key) {
        this.section = section;
        this.semester = semester;
        this.teacher = teacher;
        this.subject=subject;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubject() {
        return subject;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public List<TestQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TestQuestion> questions) {
        this.questions = questions;
    }
}
