package JFX.kt2;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.text.DecimalFormat;

public class HelloController {

    @FXML
    private TableView<Student> mainTable;

    @FXML
    private MenuItem addStudentMenuItem;

    @FXML
    private MenuItem settingsMenuItem;

    @FXML
    private MenuItem helpMenuItem;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private int numAssignments = 3;
    private DecimalFormat df = new DecimalFormat("#.##");

    @FXML
    public void initialize() {
        mainTable.setEditable(true);
        setupTable();

        addStudentMenuItem.setOnAction(event -> openAddStudentDialog());
        settingsMenuItem.setOnAction(event -> openSettingsDialog());
        helpMenuItem.setOnAction(event -> showHelpDialog());

        mainTable.setItems(studentList);

        mainTable.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("DELETE")) {
                Student selected = mainTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    studentList.remove(selected);
                }
            }
        });
    }

    private void setupTable() {
        mainTable.getColumns().clear();

        TableColumn<Student, String> fioColumn = new TableColumn<>("ФИО студента");
        fioColumn.setCellValueFactory(new PropertyValueFactory<>("fio"));
        fioColumn.setPrefWidth(150);
        fioColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fioColumn.setOnEditCommit(event -> {
            event.getRowValue().setFio(event.getNewValue());
        });

        TableColumn<Student, String> groupColumn = new TableColumn<>("Группа");
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        groupColumn.setPrefWidth(80);
        groupColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        groupColumn.setOnEditCommit(event -> {
            event.getRowValue().setGroup(event.getNewValue());
        });

        mainTable.getColumns().add(fioColumn);
        mainTable.getColumns().add(groupColumn);

        // Колонки оценок
        for (int i = 0; i < numAssignments; i++) {
            TableColumn<Student, Integer> gradeColumn = new TableColumn<>("Задание " + (i + 1));
            int finalI = i;
            gradeColumn.setCellValueFactory(data -> {
                ObservableList<Integer> grades = data.getValue().getGrades();
                if (finalI < grades.size()) {
                    return new SimpleObjectProperty<>(grades.get(finalI));
                }
                return new SimpleObjectProperty<>(0);
            });
            gradeColumn.setPrefWidth(80);
            gradeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            gradeColumn.setOnEditCommit(event -> {
                Student student = event.getRowValue();
                if (finalI < student.getGrades().size()) {
                    student.getGrades().set(finalI, event.getNewValue());
                    student.updateAverageGrade();
                    mainTable.refresh();
                }
            });
            mainTable.getColumns().add(gradeColumn);
        }

        TableColumn<Student, String> avgColumn = new TableColumn<>("Средний балл");
        avgColumn.setCellValueFactory(data -> {
            double avg = data.getValue().getAverageGrade();
            return new SimpleObjectProperty<>(df.format(avg));
        });
        avgColumn.setPrefWidth(100);
        mainTable.getColumns().add(avgColumn);
    }

    private void openAddStudentDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-student.fxml"));
            Scene scene = new Scene(loader.load());

            AddStudentController controller = loader.getController();
            controller.setNumAssignments(numAssignments);

            Stage stage = new Stage();
            stage.setTitle("Добавить студента");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            Student newStudent = controller.getStudent();
            if (newStudent != null) {
                studentList.add(newStudent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSettingsDialog() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(numAssignments));
        dialog.setTitle("Настройки");
        dialog.setHeaderText("Изменить количество заданий");
        dialog.setContentText("Количество заданий:");

        dialog.showAndWait().ifPresent(result -> {
            try {
                int newNum = Integer.parseInt(result);
                if (newNum > 0 && newNum <= 20) {
                    numAssignments = newNum;
                    for (Student student : studentList) {
                        student.resizeGrades(numAssignments);
                    }
                    setupTable();
                } else {
                    showAlert("Ошибка", "Количество заданий должно быть от 1 до 20");
                }
            } catch (NumberFormatException e) {
                showAlert("Ошибка", "Введите корректное число");
            }
        });
    }

    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О приложении");
        alert.setHeaderText("Отчисление!!!!");
        alert.setContentText(
                "Отчисление\n" +
                        "Отчисление\n" +
                        "Отчисление\n" +
                        "Отчисление\n" +
                        "Отчисление\n" +
                        "Отчисление\n" +
                        "Отчисление\n\n"
        );
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
