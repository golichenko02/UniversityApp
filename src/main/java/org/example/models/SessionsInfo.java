package org.example.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SessionsInfo {

    private StringProperty studentFio;
    private StringProperty studentGroup;
    private IntegerProperty sessionsNumber;

    public SessionsInfo() {
        this(null, null, (Integer) null);
    }

    public SessionsInfo(StringProperty studentFio, StringProperty studentGroup, IntegerProperty sessionsNumber) {
        this.studentFio = studentFio;
        this.studentGroup = studentGroup;
        this.sessionsNumber = sessionsNumber;
    }

    public SessionsInfo(String studentFio, String studentGroup, Integer sessionsNumber) {
        this.studentFio = new SimpleStringProperty(studentFio);
        this.studentGroup = new SimpleStringProperty(studentGroup);
        this.sessionsNumber = new SimpleIntegerProperty(sessionsNumber);
    }

    public Integer getSessionsNumber() {
        return sessionsNumber.get();
    }

    public IntegerProperty getSessionsNumberProperty() {
        return sessionsNumber;
    }

    public void setSessionsNumber(Integer sessionsNumber) {
        this.sessionsNumber.set(sessionsNumber);
    }

    public String getStudentGroup() {
        return studentGroup.get();
    }

    public String getStudentFio() {
        return studentFio.get();
    }

    public StringProperty getStudentFioProperty() {
        return studentFio;
    }

    public StringProperty getStudentGroupProperty() {
        return studentGroup;
    }

    public void setStudentFio(String studentFio) {
        this.studentFio.set(studentFio);
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup.set(studentGroup);
    }

}
