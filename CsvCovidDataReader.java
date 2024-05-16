package datamanagement;

import logging.Logger;
import util.CovidData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CsvCovidDataReader implements CovidDataReader {
    private final String fileLocation;

    public CsvCovidDataReader(String fileLocation){
        this.fileLocation = fileLocation;
    }

    public List<CovidData> read() throws IOException {
        Logger logger = Logger.getInstance();
        logger.log(fileLocation);
        List<CovidData> csvData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(this.fileLocation))) {
            String headerRow = br.readLine();
            List<String> headers = parseLine(headerRow);
            int zipCodeIndex = findIndex(headers, "zip_code");
            int firstDoseCountIndex = findIndex(headers, "partially_vaccinated");
            int secondDoseCountIndex = findIndex(headers, "fully_vaccinated");
            int timeStampIndex = findIndex(headers, "etl_timestamp");

            String line;
            while ((line = br.readLine()) != null) {
                List<String> parsedString = parseLine(line);
                String zipCode = parsedString.get(zipCodeIndex);
                String timeStamp = parsedString.get(timeStampIndex);
                int firstDoseCount = parsedString.get(firstDoseCountIndex).isEmpty() ? 0 : Integer.parseInt(parsedString.get(firstDoseCountIndex));
                int secondDoseCount = parsedString.get(secondDoseCountIndex).isEmpty() ? 0 : Integer.parseInt(parsedString.get(secondDoseCountIndex));

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                boolean isValidDate = false;
                try {
                    dateFormat.parse(timeStamp);
                    isValidDate = true;
                } catch (java.text.ParseException e) {}

                Pattern zipCodePattern = Pattern.compile("\\d{5}");
                boolean isValidZipCode = zipCodePattern.matcher(zipCode).matches();

                if (!isValidZipCode || !isValidDate) continue;

                CovidData data = new CovidData(zipCode, firstDoseCount, secondDoseCount, timeStamp);
                csvData.add(data);
            }
        }
        return csvData;
    }

    private List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotedField = false;

        for (char c : line.toCharArray()) {
            if (insideQuotedField) {
                if (c == '"') {
                    insideQuotedField = false;
                } else {
                    currentField.append(c);
                }
            } else {
                if (c == '"') {
                    insideQuotedField = true;
                } else if (c == ',') {
                    fields.add(currentField.toString());
                    currentField.setLength(0);
                } else {
                    currentField.append(c);
                }
            }
        }

        // Add the last field
        fields.add(currentField.toString());

        return fields;
    }
    private int findIndex(List<String> headers, String columnName) {
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).trim().equals(columnName)) {
                return i;
            }
        }
        return -1; // Or handle the case where the header is not found
    }
}
