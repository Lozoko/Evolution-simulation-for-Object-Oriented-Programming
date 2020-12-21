package main;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJSON {
    private int width;
    private int height;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private int jungleRatio;
    private int initialNumber;
    private int frameRate;
    @SuppressWarnings("unchecked")
    ReadJSON()
    {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("startingParameters.json"))
        {
            Object obj = jsonParser.parse(reader);
            JSONObject parameters = (JSONObject) obj;
            this.parseObject(parameters);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void parseObject(JSONObject parameters)
    {
        this.width = Integer.parseInt((String)parameters.get("width"));
        this.height = Integer.parseInt((String)parameters.get("height"));
        this.startEnergy = Integer.parseInt((String)parameters.get("startEnergy"));
        this.moveEnergy = Integer.parseInt((String)parameters.get("moveEnergy"));
        this.plantEnergy = Integer.parseInt((String)parameters.get("plantEnergy"));
        this.jungleRatio = Integer.parseInt((String)parameters.get("jungleRatio"));
        this.initialNumber = Integer.parseInt((String)parameters.get("initialNumber"));
        this.frameRate = Integer.parseInt((String)parameters.get("frameRate"));
    }
    public int[] getParameters(){
        return new int[]{this.width, this.height, this.startEnergy, this.moveEnergy,
                this.plantEnergy, this.jungleRatio, this.initialNumber, this.frameRate};
    }
}
