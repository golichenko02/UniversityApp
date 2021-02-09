package org.example.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Info {

    private StringProperty studentFio;
    private StringProperty studentGroup;
    private IntegerProperty studentMark;

    public Info() {
        this(null, null, (Integer) null);
    }

    public Info(StringProperty studentFio, StringProperty studentGroup, IntegerProperty studentMark) {
        this.studentFio = studentFio;
        this.studentGroup = studentGroup;
        this.studentMark = studentMark;
    }


    public Info(String studentFio, String studentGroup, Integer studentMark) {
        this.studentFio = new SimpleStringProperty(studentFio);
        this.studentGroup = new SimpleStringProperty(studentGroup);
        this.studentMark = new SimpleIntegerProperty(studentMark);
    }


    public Integer getStudentMark() {
        return studentMark.get();
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

    public IntegerProperty getStudentMarkProperty() {
        return studentMark;
    }


    public void setStudentMark(Integer mark) {
        this.studentMark.set(mark);
    }


    public void setStudentFio(String studentFio) {
        this.studentFio.set(studentFio);
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup.set(studentGroup);
    }
}
