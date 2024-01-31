package org.example.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.client.Client;

import java.io.*;
import java.nio.file.Files;
import java.util.Formatter;
import java.util.Optional;

public class ChatFormController {

    public ImageView imgUser;
    public ScrollPane scrollPane;
    public VBox vBox;
    public TextField txtMsg;
    public AnchorPane emojiPane;
    public AnchorPane header;
    private Client client;
    public Label lblUserName;
    private LoginFormController loginFormController;
    private FileWriter fileWriter;
    private Formatter formatter;
    public String name;

    public void initialize() throws IOException {

//        fileWriter = new FileWriter("/home/kitty/Documents/chat-Backup.txt", true);
//        formatter = new Formatter(fileWriter);
        lblUserName.setText(LoginFormController.name);
        loadSavedMessages();
    }

    private void loadSavedMessages() {
        try (BufferedReader reader = new BufferedReader(new FileReader("/home/kitty/GDSE67/ClientServerArchitecture/chat-application (copy)/src/main/resources/asserts/images/chatBackup/backup.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Append each line to the chat interface
                appendTextBackups(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClient(Client client) throws IOException {
        this.client=client;
        String message=" joined the chat";
        appendText(message);
        client.sendMessage(message);

    }

    private void appendText(String message) {
        if (message.startsWith(" joined")) {
            HBox hBox = new HBox();
            hBox.setStyle("-fx-alignment: center;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
            Label messageLbl = new Label(message);
            messageLbl.setStyle("-fx-background-color: rgb(128,128,128);-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: black;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
            hBox.getChildren().add(messageLbl);
            vBox.getChildren().add(hBox);
        } else {
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
            Label messageLbl = new Label(message);
            messageLbl.setStyle("-fx-background-color:  #b2bec3;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: black;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
            hBox.getChildren().add(messageLbl);
            vBox.getChildren().add(hBox);
        }
    }
    private void appendTextBackups(String message) {
        String[] parts = message.split(" : ", 2); // Split the message into two parts: name and message
        String sender = parts[0].trim(); // Extract the sender's name
        String messageContent = parts[1]; // Extract the message content

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Label messageLbl = new Label(messageContent);
        messageLbl.setStyle("-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: black;-fx-wrap-text: true;-fx-padding: 10;-fx-max-width: 350;");
        System.out.println(sender+" : "+lblUserName.getText()+name);
        if (sender.equals(lblUserName.getText())){
            // Message from the current user, align right
            hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
            messageLbl.setStyle("-fx-background-color:  #b2bec3;-fx-background-radius:15;" + messageLbl.getStyle());
        } else {
            // Message from other user, align left
            hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
            messageLbl.setStyle("-fx-background-color: rgb(128,128,128);-fx-background-radius:15;" + messageLbl.getStyle());
        }

        hBox.getChildren().add(messageLbl);
        vBox.getChildren().add(hBox);
    }

    public void imgPapperClipOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg","*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                byte[] bytes = Files.readAllBytes(selectedFile.toPath());
                HBox hBox = new HBox();
                hBox.setSpacing(50);
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");

                // Display the image in an ImageView or any other UI component
                ImageView imageView = new ImageView(new Image(new FileInputStream(selectedFile)));
                imageView.setStyle("-fx-padding: 10px;");
                imageView.setFitHeight(180);
                imageView.setFitWidth(100);

                hBox.getChildren().addAll(imageView);
                vBox.getChildren().add(hBox);

                client.sendImage(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void emojiOnAction(ActionEvent actionEvent) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    public void txtMessageOnAction(ActionEvent actionEvent) {
        btnMesseageSendOnAction(actionEvent);
    }

    public void btnMesseageSendOnAction(ActionEvent actionEvent) {
        try{
            String message=txtMsg.getText();
            if(message!=null){
                appendText(message);
                client.sendMessage(message);
                txtMsg.clear();
            }else{
                ButtonType ok = new ButtonType("OK");
                ButtonType cancel = new ButtonType("Cancel");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Enter message,It's empty. Is it ok?", ok, cancel);
                alert.showAndWait();
                ButtonType result = alert.getResult();
                if(result.equals(ok)){
                    client.sendMessage(null);
                }
                txtMsg.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void grinningFaceEmojiOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE00");
        emojiPane.setVisible(false);
    }

    public void grinningSquintingOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE06");
        emojiPane.setVisible(false);
    }

    public void smilingFaceWithOpenHandsOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83E\uDD17");
        emojiPane.setVisible(false);
    }

    public void grinningFaceWithSweatOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE05");
        emojiPane.setVisible(false);
    }

    public void faceWithTearsOfJoyOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE02");
        emojiPane.setVisible(false);
    }

    public void cryingFaceOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE22");
        emojiPane.setVisible(false);
    }

    public void sunglassesFaceOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE0E");
        emojiPane.setVisible(false);
    }

    public void smilingFaceWithHeartEyesOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE0D");
        emojiPane.setVisible(false);
    }

    public void smilingFaceWithHornsOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDE08");
        emojiPane.setVisible(false);
    }

    public void thumbsUpOnAction(MouseEvent mouseEvent) {
        txtMsg.appendText("\uD83D\uDC4D");
        emojiPane.setVisible(false);
    }

    public void setImage(byte[] bytes,String sender){
        HBox hBox = new HBox();
        hBox.setSpacing(50);
        Label messageLbl = new Label(sender);
        messageLbl.setStyle("-fx-background-color:   #648BB0;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

        hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; " + (sender.equals(client.getName()) ? "-fx-alignment: center-right;" : "-fx-alignment: center-left;"));
        // Display the image in an ImageView or any other UI component
        Platform.runLater(() -> {
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(bytes)));
            imageView.setStyle("-fx-padding: 10px;");
            imageView.setFitHeight(180);
            imageView.setFitWidth(100);

            hBox.getChildren().addAll(messageLbl, imageView);
            vBox.getChildren().add(hBox);
        });
    }
    public void writeMessage(String message) {
        //print msg on other clients
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        //======================================================================
        try {
            fileWriter = new FileWriter("/home/kitty/GDSE67/ClientServerArchitecture/chat-application (copy)/src/main/resources/asserts/images/chatBackup/backup.txt", true);
            // Create a Formatter with the FileWriter
            formatter = new Formatter(fileWriter);
            // Write to the file
            formatter.format(messageLbl.getText() + "%n");
            // Close the resources
            formatter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //=========================================================================
        messageLbl.setStyle("-fx-background-color:   #648BB0;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(() -> vBox.getChildren().add(hBox));

    }
}
