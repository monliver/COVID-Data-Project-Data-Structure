//aaron


package datamanagement;

import logging.Logger;
import util.CovidData;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class JsonCovidDataReader implements CovidDataReader {
    private final String fileLocation;
    // Constructor to initialize the file location
    public JsonCovidDataReader(String fileLocation){
        this.fileLocation = fileLocation;
    }
    // Method to read data from the JSON file and return a list of CovidData objects
    public List<CovidData> read() throws IOException, ParseException {
        Logger logger = Logger.getInstance();
        logger.log(fileLocation);
        List<CovidData> jsonData = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONArray objectArray = (JSONArray) parser.parse(new FileReader(fileLocation));

        for (Object line : objectArray) {
            JSONObject eachLine = (JSONObject) line;

            // Extracting data from each JSONObject
            String zipCode = String.valueOf(eachLine.getOrDefault("zip_code", 0));
            String timeStamp = (String) eachLine.getOrDefault("etl_timestamp", "");
            String firstDoseCount = String.valueOf(eachLine.getOrDefault("partially_vaccinated", 0));
            String secondDoseCount = String.valueOf(eachLine.getOrDefault("fully_vaccinated", 0));

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            boolean isValidDate = false;
            try {
                dateFormat.parse(timeStamp);
                isValidDate = true;
            } catch (java.text.ParseException e) {}

            Pattern zipCodePattern = Pattern.compile("\\d{5}");
            boolean isValidZipCode = zipCodePattern.matcher(zipCode).matches();

            if (!isValidDate || !isValidZipCode) continue;

            int partiallyVaccinated = Integer.parseInt(firstDoseCount);
            int fullyVaccinated = Integer.parseInt(secondDoseCount);
            CovidData data = new CovidData(zipCode, partiallyVaccinated, fullyVaccinated, timeStamp);
            jsonData.add(data);
        }
        return jsonData;
    }
}
