package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.dto.UserDTO;
import org.example.model.UserModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ForgetPasswordFormController {

    public Button btnBack;
    public PasswordField txtPassword;
    public TextField txtOneTimePassword;
    public PasswordField txtRePassword;
    public Button btnReset;

    public void btnResetOnAction(ActionEvent actionEvent) throws IOException, SQLException {
        System.out.println("Reset Button Clicked..");
        UserDTO user = UserModel.getUser(LoginFormController.tempUserName);
        if(user == null){
            System.out.println("Password Reset Processing...");
            new Alert(Alert.AlertType.ERROR, "User not found | redirecting..", ButtonType.OK).showAndWait();
            Parent load = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
            Scene scene1 = new Scene(load);
            Stage stage1 = (Stage) btnReset.getScene().getWindow();
            stage1.setScene(scene1);
            stage1.setTitle("Login Form");
            stage1.centerOnScreen();
        }else{
            if(txtOneTimePassword.getText().equals(String.valueOf(LoginFormController.oneTimePassword))){
                boolean isPasswordValid = Pattern.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", txtPassword.getText());
                if(isPasswordValid){
                    if(txtPassword.getText().equals(txtRePassword.getText())){
                        final boolean isPasswordChanged = UserModel.updateUserPassword(user.getEmail(),txtPassword.getText());
                        if(isPasswordChanged){
                            new Alert(Alert.AlertType.INFORMATION, "Password Changed | You will be redirected to Login Form", ButtonType.OK).showAndWait();
                            Parent load = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
                            Scene scene1 = new Scene(load);
                            Stage stage1 = (Stage) btnReset.getScene().getWindow();
                            stage1.setScene(scene1);
                            stage1.setTitle("Login Form");
                            stage1.centerOnScreen();
                        }else{
                            new Alert(Alert.AlertType.ERROR, "Password Not Changed", ButtonType.OK).showAndWait();
                        }
                    }else{
                        new Alert(Alert.AlertType.ERROR, "Passwords do not match", ButtonType.OK).showAndWait();
                    }
                }else{
                    new Alert(Alert.AlertType.ERROR,"Password must contain at least one digit, one uppercase letter, one lowercase letter and one special character", ButtonType.OK).showAndWait();
                }
            }
        }
    }



    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
            Scene scene1 = new Scene(root);
            Stage stage1 = (Stage) btnBack.getScene().getWindow();
            stage1.setScene(scene1);
            stage1.setTitle("Login");
            stage1.centerOnScreen();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
