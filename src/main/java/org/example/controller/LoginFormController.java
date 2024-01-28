package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.client.Client;
import org.example.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

public class LoginFormController {

    public ImageView imgUser;
    public TextField txtUserName;
    public static String name;
    public File file;
    public Button btnSignUp;

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
    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        loadUserDetails();
        txtUserName.clear();
        imgUser.setImage(null);
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

    private void loadUserDetails() throws IOException {
        if(Pattern.matches("^[a-zA-Z\\s]+",txtUserName.getText())){
            Client client = new Client(txtUserName.getText(), imgUser);
            Thread thread = new Thread(client);
            thread.start();

            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/chat_form.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setTitle(txtUserName.getText() + "'s Chat");
            stage.setAlwaysOnTop(true);

        }
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/view/signup_form.fxml"));
            Scene scene1 = new Scene(root);
            Stage stage1 = (Stage) btnSignUp.getScene().getWindow();
            stage1.setScene(scene1);
            stage1.setTitle("Employee Management");
            stage1.centerOnScreen();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
