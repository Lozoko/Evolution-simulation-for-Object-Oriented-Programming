package sample;

/*
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.*;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../main/mainfxml.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            launch(args);

            IWorldMap map = new WorldMap(10, 5,5,2);
            //IWorldMap map = new GrassField(10);
            Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(3, 2),new Vector2d(2, 3),new Vector2d(3, 3)};
            IEngine engine = new SimulationEngine(map, positions);
            engine.run();
        } catch(IllegalArgumentException ex){
            System.out.println(ex);
            System.exit(1);
        }
    }
}
*/
