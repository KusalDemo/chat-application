package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

public class SignUpFormController {

    public Button btnAlreadyHaveAnAccount;
    public PasswordField txtPassword;
    public ImageView imgUser;
    public TextField txtUserName;
    public PasswordField txtRePassword;
    public TextField txtEmail;
    private File file;

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        if(!txtUserName.getText().isEmpty() && !txtEmail.getText().isEmpty() && !txtPassword.getText().isEmpty() && !txtRePassword.getText().isEmpty()){
           if(Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", txtEmail.getText())){
               if(Pattern.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", txtPassword.getText())) {
                   if (txtPassword.getText().equals(txtRePassword.getText())) {

                   } else {
                       new Alert(Alert.AlertType.ERROR, "Password not matched").show();
                   }
               } else{
                  new Alert(Alert.AlertType.ERROR,"Weak Password, must contain at least 1 uppercase, 1 lowercase, 1 digit and 1 special character").show();
               }
           }else{
               new Alert(Alert.AlertType.ERROR,"Invalid Email").show();
           }
        }else {
            new Alert(Alert.AlertType.ERROR,"All fields are required").show();
        }
    }

    public void btnAlreadyHaveAnAccountOnAction(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
            Scene scene1 = new Scene(root);
            Stage stage1 = (Stage) btnAlreadyHaveAnAccount.getScene().getWindow();
            stage1.setScene(scene1);
            stage1.setTitle("Employee Management");
            stage1.centerOnScreen();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void imgCameraOnAction(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg","*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtUserName.getScene().getWindow());
        if(file != null){
           try {
               FileInputStream fileInputStream = new FileInputStream(file);
               imgUser.setImage(new Image(fileInputStream));
           }catch (FileNotFoundException e){
               e.printStackTrace();
           }
        }
    }
}
