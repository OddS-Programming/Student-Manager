package JFX.kt2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AddStudentController {

    @FXML
    private TextField fioField;

    @FXML
    private ChoiceBox<String> groupChoiceBox;


    @FXML
    private VBox gradesContainer;

    private List<TextField> gradeFields = new ArrayList<>();
    private Student student;
    private int numAssignments;

    public void setNumAssignments(int num) {
        this.numAssignments = num;
        createGradeFields();
    }

    private void createGradeFields() {
        gradesContainer.getChildren().clear();
        gradeFields.clear();

        for (int i = 0; i < numAssignments; i++) {
            Label label = new Label("Задание " + (i + 1) + ":");
            TextField field = new TextField();
            field.setPromptText("Оценка (2-5 или 0)");
            gradeFields.add(field);
            gradesContainer.getChildren().addAll(label, field);
        }
    }
    public void initialize() {
        groupChoiceBox.getItems().addAll("Исп9-kh11", "Исп9-kh12", "Исп9-kh13", "Исп9-kh14", "Исп9-kh15", "Исп9-kh16","Исп9-kh17","Исп9-kh18","Исп9-kh19","Исп9-kh21", "Исп9-kh22", "Исп9-kh23", "Исп9-kh24", "Исп9-kh25", "Исп9-kh26","Исп9-kh27","Исп9-kh28","Исп9-kh29");
        groupChoiceBox.setValue("Группа");
    }
    @FXML
    private void handleAdd() {
        String fio = fioField.getText().trim();
        String group = groupChoiceBox.getValue();

        if (fio.isEmpty() || group.isEmpty()) {
            showAlert("Ошибка", "Заполните ФИО и группу");
            return;
        }

        ObservableList<Integer> grades = FXCollections.observableArrayList();
        for (TextField field : gradeFields) {
            String text = field.getText().trim();
            try {
                int grade = text.isEmpty() ? 0 : Integer.parseInt(text);
                if (grade < 0 || grade > 5) {
                    showAlert("Ошибка", "Оценки должны быть от 0 до 5");
                    return;
                }
                grades.add(grade);
            } catch (NumberFormatException e) {
                showAlert("Ошибка", "Введите корректные числа для оценок");
                return;
            }
        }

        student = new Student(fio, group, grades);
        closeWindow();
    }
    @FXML
    private void handleCancel() {
        student = null;
        closeWindow();
    }

    public Student getStudent() {
        return student;
    }

    private void closeWindow() {
        Stage stage = (Stage) fioField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
