package main;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WriteJSON {
    @SuppressWarnings("unchecked")
    WriteJSON( String[] args )
    {
        //First Employee
        JSONObject startingParameters = new JSONObject();
        startingParameters.put("width", args[0]);
        startingParameters.put("height", args[1]);
        startingParameters.put("startEnergy", args[2]);
        startingParameters.put("moveEnergy", args[3]);
        startingParameters.put("plantEnergy", args[4]);
        startingParameters.put("jungleRatio", args[5]);
        startingParameters.put("initialNumber", args[6]);
        startingParameters.put("frameRate", args[7]);
        try (FileWriter file = new FileWriter("startingParameters.json")) {

            file.write(startingParameters.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
