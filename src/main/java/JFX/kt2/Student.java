package JFX.kt2;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Student {
    private StringProperty fio;
    private StringProperty group;
    private ObservableList<Integer> grades;
    private DoubleProperty averageGrade;

    public Student(String fio, String group, ObservableList<Integer> grades) {
        this.fio = new SimpleStringProperty(fio);
        this.group = new SimpleStringProperty(group);
        this.grades = grades;
        this.averageGrade = new SimpleDoubleProperty();
        updateAverageGrade();
    }

    public String getFio() {
        return fio.get();
    }

    public StringProperty fioProperty() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio.set(fio);
    }

    public String getGroup() {
        return group.get();
    }

    public StringProperty groupProperty() {
        return group;
    }

    public void setGroup(String group) {
        this.group.set(group);
    }

    public ObservableList<Integer> getGrades() {
        return grades;
    }

    public void setGrades(ObservableList<Integer> grades) {
        this.grades = grades;
        updateAverageGrade();
    }

    public double getAverageGrade() {
        return averageGrade.get();
    }

    public DoubleProperty averageGradeProperty() {
        return averageGrade;
    }

    public void updateAverageGrade() {
        if (grades.isEmpty()) {
            averageGrade.set(0.0);
            return;
        }
        double sum = 0;
        int count = 0;
        for (Integer grade : grades) {
            if (grade != null && grade > 0) {
                sum += grade;
                count++;
            }
        }
        averageGrade.set(count > 0 ? sum / count : 0.0);
    }

    public void resizeGrades(int newSize) {
        int currentSize = grades.size();
        if (newSize > currentSize) {
            for (int i = currentSize; i < newSize; i++) {
                grades.add(0);
            }
        } else if (newSize < currentSize) {
            grades.remove(newSize, currentSize);
        }
        updateAverageGrade();
    }
}
