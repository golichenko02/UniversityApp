package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mark {
    private int id;
    private int mark;
    private int testId;
    private int studentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return testId == mark.testId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(testId);
    }

    public Mark(int testId) {
        this.testId = testId;
    }
}

