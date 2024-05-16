package edu.upenn.cit594.util;

public class PopulationData {
	
	private String zipCode;
    private int population;

    // Constructor
    public PopulationData(String zipCode, int population) {
        if (isValidZipCode(zipCode) && isInteger(String.valueOf(population))) {
            this.zipCode = zipCode;
            this.population = Integer.parseInt(String.valueOf(population));
        } 
    }

    // Getters
    public String getZipCode() {
        return zipCode;
    }

    public int getPopulation() {
        return population;
    }

    // Helper method to validate ZIP code
    private static boolean isValidZipCode(String zipCode) {
        return zipCode != null && zipCode.matches("\\d{5}");
    }

    // Helper method to check if a string is an integer
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

}
