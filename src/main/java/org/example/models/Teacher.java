package org.example.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Teacher {
    private String id;
    private String firstName;
    private String secondName;
    private String surname;
    private String password;
    private String login;



    public Teacher(String id) {
        this.id = id;
    }
}
