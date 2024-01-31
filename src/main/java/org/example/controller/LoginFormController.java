package org.example.controller;

import animatefx.animation.SlideInUp;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Mail;
import org.example.client.Client;
import org.example.dto.UserDTO;
import org.example.model.UserModel;
import org.example.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Pattern;

public class LoginFormController {
    public TextField txtUserName;
    public static String name;
    public Button btnSignUp;
    public PasswordField txtPassword;
    public Label lblForgetPassword;
    public static int oneTimePassword;
    public static String tempUserName;


    //1. If we gonna start the application we need to say to the server to start.
    //2. Then we need to create the server part..
    public void initialize() throws IOException {
        stratServer();
    }

    private void stratServer() throws IOException {
        Server server = Server.getServerSocket();
        Thread thread = new Thread(server);
        //we created a thread for the server.. it means we called the run method of runnable interface
        //then server will run on a separate thread while program is running on main thread
        thread.start();
    }

    //3. We need to create a client handler
    //4. Then we need to create a Client when we click on login
    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException, SQLException {
        loadUserDetails();
        txtUserName.clear();
        txtPassword.clear();
    }

    private void loadUserDetails() throws IOException, SQLException {
        UserDTO user = UserModel.getUser(txtUserName.getText(), txtPassword.getText());
        if(user != null){
            Client client = new Client(user.getName(),user.getProfilePic());
            Thread thread = new Thread(client);
            thread.start();

            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/chat_form.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setTitle(user.getName() + "'s Chat");
            stage.setAlwaysOnTop(true);
        }else{
            new Alert(Alert.AlertType.ERROR, "Invalid Username or Password check again !", ButtonType.OK).showAndWait();
        }
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/view/signup_form.fxml"));
            Scene scene1 = new Scene(root);
            Stage stage1 = (Stage) btnSignUp.getScene().getWindow();
            stage1.setScene(scene1);
            stage1.setTitle("Let's get started");
            stage1.centerOnScreen();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void lblForgetPasswordOnAction(MouseEvent mouseEvent) {
        System.out.println("User request a password reset..");
        if(!txtUserName.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Do you want to Change Your Password ?");
            alert.setContentText("Send OTP To Your Email");

            ButtonType buttonTypeYes = new ButtonType("OK");
            ButtonType buttonTypeNo = new ButtonType("Cancel");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeYes) {
                    try {
                        oneTimePassword = generateOTP();
                        Mail mail = new Mail();
                        mail.setMsg("Hello," + "\n\n\tAn OTP Request Detected at :  " + LocalDateTime.now() + " \n\n\tOTP : " + oneTimePassword + " \n\nThank You,\n" +
                                "StreamSync Team");
                        mail.setTo(txtUserName.getText());
                        tempUserName=txtUserName.getText();
                        mail.setSubject("OTP Verification");

                        Thread thread = new Thread(mail);
                        thread.start();

                        Parent load = FXMLLoader.load(getClass().getResource("/view/forgetPassword_form.fxml"));
                        Scene scene1 = new Scene(load);
                        Stage stage1 = (Stage) lblForgetPassword.getScene().getWindow();
                        stage1.setScene(scene1);
                        stage1.setTitle("Change Password");
                        stage1.centerOnScreen();

                        new SlideInUp(load).play();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } else if (response == buttonTypeNo) {
                    System.out.println("User Canceled the Operation");
                }
            });
        }else{
            new Alert(Alert.AlertType.ERROR, "Enter your email first..").show();
        }
    }
    public static int generateOTP(){
        Random random = new Random();
        int password = random.nextInt(9000000) + 1000000;
        System.out.println(password);
        return password;
    }
}
