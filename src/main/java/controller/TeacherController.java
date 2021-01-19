package controller;

import controller.fx.ClassFX;
import controller.fx.GradeFX;
import controller.fx.UserFx;
import database.mysql.GroupDAO;
import database.mysql.DBAccess;
import database.mysql.GradeDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import launcher.Main;
import model.*;
import model.Class;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.fx.ObjectConvertor.*;
import static java.lang.String.valueOf;


public class TeacherController implements Initializable {

    private DBAccess dBaccess;
    private GroupDAO dao;
    private GradeDAO gdao;
    private Teacher loggedInUser;
    private User loggedInUser2;


    @FXML
    public TableView classTable;
    @FXML
    public TableView studentTable;

    @FXML
    public TableView quizTable;

    @FXML
    public TableView gradeTable;

    @FXML
    public TableColumn<ClassFX, Integer> classColumn;
    @FXML
    public TableColumn<UserFx, String> studentColumn;
    @FXML
    public TableColumn<GradeFX, Integer> quizColumn;
    @FXML
    public TableColumn<GradeFX, Double> gradeColumn;

    @FXML
    public ComboBox<ClassFX> groupComboBox;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dBaccess = Main.getDBaccess();
        this.dao = new GroupDAO(this.dBaccess);
        this.gdao = new GradeDAO(this.dBaccess);
        //loggedInUser = (User) Main.getPrimaryStage().getUserData();
        loggedInUser = new Teacher(10040,"piet","paulusma");

        fillTable();
    }

    /**
     * Fill each table with respective objects in TableColumn
     */
    public void fillTable() {
        ObservableList<ClassFX> classes;

        //TODO: create StudentFX, use instead of UserFx
        ObservableList<UserFx> students;
        ObservableList<GradeFX> grades;


        classes = convertClassToClassFX(dao.getAllClasses(loggedInUser));
        students = convertUserToUserFX(dao.getAllStudents(loggedInUser));
        grades = convertGradeToGradeFX(gdao.getAllGradesPerTeacher(loggedInUser));
        //System.out.println(grades.get(0).getGrade());
        classColumn.setCellValueFactory(cellData -> cellData.getValue().dbIdProperty().asObject());

        studentColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        quizColumn.setCellValueFactory(cellData -> cellData.getValue().quizIdProperty().asObject());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty().asObject());

        classTable.getItems().addAll(classes);
        studentTable.getItems().addAll(students);
        quizTable.getItems().addAll(grades);
        gradeTable.getItems().addAll(grades);

        groupComboBox.setItems(classes);

        groupComboBox.setConverter(new StringConverter<ClassFX>() {
            @Override
            public String toString(ClassFX classFX) {
                return valueOf(classFX.getDbId());
            }

            @Override
            public ClassFX fromString(String s) {
                return null;
            }
        });


    }
}
