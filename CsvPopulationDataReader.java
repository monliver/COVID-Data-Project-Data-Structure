package datamanagement;

import logging.Logger;
import util.PopulationData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvPopulationDataReader {

    public static List<PopulationData> readPopulationData(String filePath) throws IOException {
        List<PopulationData> populationList = new ArrayList<>();
        Logger logger = Logger.getInstance();
        logger.log(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip the header line

            String line;
            while ((line = reader.readLine()) != null) {
                List<String> parts = parseLine(line);
                if (parts.size() == 2) {
                    String zipCode = parts.get(0).trim();
                    int population = parts.get(1).isEmpty() ? 0 : Integer.parseInt(parts.get(1).trim());

                    if (!zipCode.isEmpty() && population != 0) {
                        populationList.add(new PopulationData(zipCode, population));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return populationList;
    }

    private static List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotedField = false;

        for (char c : line.toCharArray()) {
            if (insideQuotedField) {
                if (c == '"') {
                    if (currentField.length() > 0 && currentField.charAt(currentField.length() - 1) == '"') {
                        currentField.deleteCharAt(currentField.length() - 1); // Handle double double quotes
                    } else {
                        insideQuotedField = false;
                        continue;
                    }
                }
                currentField.append(c);
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
}
