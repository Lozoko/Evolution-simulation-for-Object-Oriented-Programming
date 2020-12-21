package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    @FXML
    Button finishButton;

    @FXML
    TextField mapWidth;

    @FXML
    TextField mapHeight;

    @FXML
    TextField moveEnergy;

    @FXML
    TextField startEnergy;

    @FXML
    TextField plantEnergy;

    @FXML
    TextField jungleRatio;

    @FXML
    TextField initialNumber;

    @FXML
    TextField frameRate;

    public void finish(ActionEvent event){
        this.saveParameters();
        Stage primaryStage = new Stage();
        Parent root;
        try {
            Stage stage = (Stage) finishButton.getScene().getWindow();
            stage.close();
            root = FXMLLoader.load(getClass().getResource("mainfxml.fxml"));
            primaryStage.setTitle("Darwin's Game");
            primaryStage.setScene(new Scene(root, 1200, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void saveParameters(){
        String width = mapWidth.getText();
        String height = mapHeight.getText();
        String moveEnergy = this.moveEnergy.getText();
        String startEnergy = this.startEnergy.getText();
        String plantEnergy = this.plantEnergy.getText();
        String jungleRatio = this.jungleRatio.getText();
        String initialNumber = this.initialNumber.getText();
        String frameRate = this.frameRate.getText();
        String[] parameters = {width,height,startEnergy,moveEnergy,plantEnergy,jungleRatio,initialNumber,frameRate};
        WriteJSON saveParametersToJSOn = new WriteJSON(parameters);
    }
}
