package main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private GridPane grid1;
    @FXML
    private GridPane grid2;
    @FXML
    private Label statisticsLabel1;
    @FXML
    private Label statisticsLabel2;
    @FXML
    private Label focusedStatisticsLabel1;
    @FXML
    private Label focusedStatisticsLabel2;

    private SimulationEngine engine1;
    private SimulationEngine engine2;
    private int width;
    private int height;
    private float imageHeight;
    private float imageWidth;
    private Image bunny;
    private Image yellowBunny;
    private Image blueBunny;
    private Image redBunny;
    private Image purpleBunny;
    private Image blackBunny;
    private Image carrot;
    private Image grass;
    private Image selectedBunny;
    private int timeline;
    private boolean stop1;
    private boolean stop2;
    private int frameRate;
    private Timeline xSecondsWonder1;
    private Timeline xSecondsWonder2;
    private boolean focusedObservation1;
    private boolean focusedObservation2;
    private int[] observedBunnyGenotype1;
    private int[] observedBunnyGenotype2;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ReadJSON jsonParameters = new ReadJSON();
        int[] parameters = jsonParameters.getParameters();
        IWorldMap map1 = new WorldMap(parameters[0], parameters[1], parameters[5],parameters[2]/2);
        this.engine1 = new SimulationEngine(map1, parameters[2],parameters[3],parameters[4],parameters[6]);
        IWorldMap map2 = new WorldMap(parameters[0], parameters[1], parameters[5],parameters[2]/2);
        this.engine2 = new SimulationEngine(map2, parameters[2],parameters[3],parameters[4],parameters[6]);
        this.width = parameters[0];
        this.height = parameters[1];
        this.imageHeight = (float)250/this.height;
        this.imageWidth = (float)500/this.width;
        this.timeline = Timeline.INDEFINITE;
        this.frameRate = parameters[7];

        this.bunny = new Image("/images/bunny.png");
        this.yellowBunny = new Image("/images/yellowBunny.png");
        this.blueBunny = new Image("/images/blueBunny.png");
        this.redBunny = new Image("/images/redBunny.png");
        this.purpleBunny = new Image("/images/purpleBunny.png");
        this.blackBunny = new Image("/images/blackBunny.png");
        this.carrot = new Image("/images/carrot.png");
        this.grass = new Image("/images/Green_square.png");
        this.selectedBunny = new Image(("/images/selectedBunny.png"));

        this.drawMap(1);
        this.drawMap(2);
        this.xSecondsWonder1 = null;
        this.xSecondsWonder2 = null;
        this.focusedObservation1 = false;
        this.focusedObservation2 = false;
        this.observedBunnyGenotype1 = null;
        this.observedBunnyGenotype2 = null;
        this.stop1 = true;
        this.stop2 = true;
    }
    public void start(ActionEvent event){
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        if (id.equals("startBtn1")) {
            this.stop1 = false;
            //https://stackoverflow.com/questions/9966136/javafx-periodic-background-task
            this.xSecondsWonder1 = new Timeline(
                    new KeyFrame(Duration.millis(this.frameRate),
                            new EventHandler<>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    run(1);
                                }
                            }));
            this.xSecondsWonder1.setCycleCount(timeline);
            this.xSecondsWonder1.play();
        }
        else{
            this.stop2 = false;
            this.xSecondsWonder2 = new Timeline(
                    new KeyFrame(Duration.millis(this.frameRate),
                            new EventHandler<>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    run(2);
                                }
                            }));
            this.xSecondsWonder2.setCycleCount(timeline);
            this.xSecondsWonder2.play();
        }

    }

    private void run(int n){
        if(n == 1) {
            this.engine1.run();
            this.grid1.getChildren().clear();
        }
        else{
            this.engine2.run();
            this.grid2.getChildren().clear();
        }
        this.drawMap(n);
        this.displayStatistics(n);
        this.displayFocusedStatistics(n);
    }

    private void drawMap(int n){
        for (int i = 0; i <= this.width; i++){
            for(int j = 0; j <= this.height; j++){
                Vector2d position = new Vector2d(i,j);
                IMapElement element;
                if(n == 1) {
                    element = this.engine1.getStrongestAnimalOrCarrot(position);
                }
                else{
                    element = this.engine2.getStrongestAnimalOrCarrot(position);
                }
                ImageView imgView;
                if(element == null){
                    imgView = new ImageView(this.grass);
                }
                else if(element.getClass() == Animal.class){
                    if(n == 1 && this.stop1 && this.observedBunnyGenotype1 != null &&
                            Arrays.equals(((Animal)element).getGenotype(),this.observedBunnyGenotype1)){
                        imgView = new ImageView(this.selectedBunny);
                    }
                    else if(n == 2 && this.stop2 && this.observedBunnyGenotype2 != null &&
                            Arrays.equals(((Animal)element).getGenotype(),this.observedBunnyGenotype2)){
                        imgView = new ImageView(this.selectedBunny);
                    }
                    else{
                        imgView = this.selectBunny((Animal)element);
                    }

                }
                else{
                    imgView = new ImageView(this.carrot);
                }
                imgView.setFitHeight(this.imageHeight);
                imgView.setFitWidth(this.imageWidth);
                int finalI = i;
                int finalJ = j;
                if(n == 1)
                    imgView.setOnMouseClicked(e -> this.startFocusedObservation(finalI, finalJ, 1));
                else
                    imgView.setOnMouseClicked(e -> this.startFocusedObservation(finalI, finalJ, 2));
                if(n == 1)
                    this.grid1.add(imgView,i,this.height - j,1,1);
                else
                    this.grid2.add(imgView,i,this.height - j,1,1);
            }
        }
    }

    private ImageView selectBunny(Animal animal){
        float energy = animal.getEnergy();
        if(energy < 10) return new ImageView(this.bunny);
        else if (energy < 20) return new ImageView(this.yellowBunny);
        else if (energy < 50) return new ImageView(this.blueBunny);
        else if (energy < 100) return new ImageView(this.redBunny);
        else if (energy < 200) return new ImageView(this.purpleBunny);
        else return new ImageView(this.blackBunny);
    }
    public void stop(ActionEvent event){
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        if (id.equals("stopBtn1")) {
            if (this.xSecondsWonder1 != null)
                this.xSecondsWonder1.stop();
            this.stop1 = true;
        }
        else{
            if(this.xSecondsWonder2 != null)
                this.xSecondsWonder2.stop();
            this.stop2 = true;
        }
    }

    private void displayStatistics(int n){
        Statistics statistics;
        if (n == 1)
            statistics = this.engine1.getStatisticsForMap();
        else
            statistics = this.engine2.getStatisticsForMap();
        int numberOfAliveAnimals = statistics.getNumberOfAliveAnimals();
        int numberOfCarrot = statistics.getNumberOfCarrots();
        float averageEnergyLevel = statistics.getAverageEnergyLevel();
        float averageLifeTime = statistics.getAverageLifeTime();
        float averageNumberOfChildren = statistics.getAverageNumberOfChildren();
        int[] dominatingGenotype = statistics.getDominatingGenotype();
        int numberOfAnimalWithDominatingGenotype = statistics.getNumberOfAnimalWithDominatingGenotype();
        if(n == 1)
            this.statisticsLabel1.setText(
                "number of alive rabbits: " + numberOfAliveAnimals + "\n" +
                        "number of carrots: " + numberOfCarrot + "\n" +
                        "average energy level: " + averageEnergyLevel + "\n" +
                        "average life time: " + averageLifeTime + "\n" +
                        "average number of children: " + averageNumberOfChildren + "\n" +
                        "dominating genotype:" + "\n" +
                        Arrays.toString(dominatingGenotype) + "\n" +
                        "number of rabbits with dominating genotype: " + numberOfAnimalWithDominatingGenotype
            );
        else
            this.statisticsLabel2.setText(
                    "number of alive rabbits: " + numberOfAliveAnimals + "\n" +
                            "number of carrots: " + numberOfCarrot + "\n" +
                            "average energy level: " + averageEnergyLevel + "\n" +
                            "average life time: " + averageLifeTime + "\n" +
                            "average number of children: " + averageNumberOfChildren + "\n" +
                            "dominating genotype:" + "\n" +
                            Arrays.toString(dominatingGenotype) + "\n" +
                            "number of rabbits with dominating genotype: " + numberOfAnimalWithDominatingGenotype
            );
    }

    private void displayFocusedStatistics(int n){
        if(n == 1) {
            if (!this.focusedObservation1) {
                this.focusedStatisticsLabel1.setText("");
                return;
            }
        }
        else{
            if(!this.focusedObservation2){
                this.focusedStatisticsLabel2.setText("");
                return;
            }
        }
        FocusedObserver focusedObserver;
        if(n == 1)
            focusedObserver = this.engine1.getFocusedObserver();
        else
            focusedObserver = this.engine2.getFocusedObserver();
        int numberOfChildren = focusedObserver.getNumberOfChildren();
        int numberOfDescendants = focusedObserver.getNumberOfDescendants();
        int dayOfDeath = focusedObserver.getDayOfDeath();
        if(n == 1)
            this.focusedStatisticsLabel1.setText(
                "number of children since observation starting: " + numberOfChildren + "\n" +
                        "number of descendants since observation starting: " + numberOfDescendants + "\n" +
                        "day of death: " + dayOfDeath
            );
        else
            this.focusedStatisticsLabel2.setText(
                    "number of children since observation starting: " + numberOfChildren + "\n" +
                            "number of descendants since observation starting: " + numberOfDescendants + "\n" +
                            "day of death: " + dayOfDeath
            );
    }

    private void startFocusedObservation(int x, int y, int n){
        Vector2d position = new Vector2d(x,y);
        if(n == 1) {
            this.focusedObservation1 = this.engine1.startObserving(position);
            if (this.focusedObservation1) {
                this.observedBunnyGenotype1 = ((Animal) this.engine1.getStrongestAnimalOrCarrot(position)).getGenotype();
                this.focusedStatisticsLabel1.setText("selected bunny genotype:" + "\n" +
                        Arrays.toString(this.observedBunnyGenotype1));
            } else {
                this.focusedStatisticsLabel1.setText("");
            }
            this.grid1.getChildren().clear();
            this.drawMap(1);
        }
        else{
            this.focusedObservation2 = this.engine2.startObserving(position);
            if(this.focusedObservation2) {
                this.observedBunnyGenotype2 = ((Animal) this.engine2.getStrongestAnimalOrCarrot(position)).getGenotype();
                this.focusedStatisticsLabel2.setText("selected bunny genotype:" + "\n" +
                        Arrays.toString(this.observedBunnyGenotype2));
            }
            else{
                this.focusedStatisticsLabel2.setText("");
            }
            this.grid1.getChildren().clear();
            this.drawMap(2);
        }
    }

    public void save(ActionEvent event){
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        int n;
        if (id.equals("saveBtn1"))
            n = 1;
        else
            n = 2;
        AverageStatistics averageStatistics;
        if (n == 1)
            averageStatistics = this.engine1.getAverageStatisticsForMap();
        else
            averageStatistics = this.engine2.getAverageStatisticsForMap();
        float numberOfAliveAnimals = averageStatistics.getAverageNumberOfAliveAnimals();
        float numberOfCarrot = averageStatistics.getAverageNumberOfCarrots();
        float averageEnergyLevel = averageStatistics.getAverageEnergyLevel();
        float averageLifeTime = averageStatistics.getAverageLifeTime();
        float averageNumberOfChildren = averageStatistics.getAverageNumberOfChildren();
        int[] dominatingGenotype = averageStatistics.getDominatingGenotype();
        int numberOfAnimalWithDominatingGenotype = averageStatistics.getNumberOfAnimalWithDominatingGenotype();
        String averageStatisticsToSave = "average number of alive rabbits: " + numberOfAliveAnimals + "\n" +
                "average number of carrots: " + numberOfCarrot + "\n" +
                "average energy level: " + averageEnergyLevel + "\n" +
                "average life time: " + averageLifeTime + "\n" +
                "average number of children: " + averageNumberOfChildren + "\n" +
                "dominating genotype:" + "\n" +
                Arrays.toString(dominatingGenotype) + "\n" +
                "number of rabbits with dominating genotype: " + numberOfAnimalWithDominatingGenotype;
        try {
            FileWriter myWriter;
            if(n == 1) {
                myWriter = new FileWriter("averageStatisticsForMap1.txt");
            }
            else{
                myWriter = new FileWriter("averageStatisticsForMap2.txt");
            }
            myWriter.write(averageStatisticsToSave);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
