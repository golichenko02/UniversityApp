package org.example.models;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Result {

    private final IntegerProperty mark;
    private final StringProperty testName;

    public Result() {
        this((Integer) null,null);
    }

    public Result(IntegerProperty mark, StringProperty testName) {
        this.mark = mark;
        this.testName = testName;
    }

    public Result(Integer mark, String testName) {
        this.mark = new SimpleIntegerProperty(mark);
        this.testName =new SimpleStringProperty(testName);
    }


    public Integer getMark() {
        return mark.get();
    }

    public String getTestName() {
        return testName.get();
    }

    public IntegerProperty getMarkProperty() {
        return mark;
    }

    public StringProperty getTestNameProperty() {
        return testName;
    }

    public void setMark(int mark) {
        this.mark.set(mark);
    }

    public void setTestName(String testName) {
        this.testName.set(testName);
    }
}
