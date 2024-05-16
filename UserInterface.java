package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

import logging.Logger;
import processor.Processor;
import util.CovidData;
import util.PopulationData;
import util.PropertyData;

public class UserInterface {
	
    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("Available Actions:");
        System.out.println("0. Exit the program.");
        System.out.println("1. Show the available actions.");
        System.out.println("2. Show the total population for all ZIP Codes.");
        System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date.");
        System.out.println("4. Show the average market value for properties in a specified ZIP Code.");
        System.out.println("5. Show the average total livable area for properties in a specified ZIP Code.");
        System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code.");
        System.out.println("7. Show the total market value to total vaccinations ratio per capita for all ZIP Code associated for the specified date.");
    }

    public void start(List<CovidData> covidList, List<PopulationData> populationList, List<PropertyData> propertyList) {
    	
        while (true) {
            displayMenu();
            System.out.print("> ");
            System.out.flush();

            String input = scanner.nextLine().trim();
            Logger logger = Logger.getInstance();
            logger.log(input);
            
            // print an empty line
            System.out.println();

            // Check if the input is an integer between 0 and 7
            if (!isValidInput(input)) {
                System.out.println("Error: Invalid input. Please enter a number between 0-7.");
                continue;
            }
            Processor processor = new Processor(propertyList, populationList, covidList);
            int action = Integer.parseInt(input);
            List<Integer> availOptionList = new ArrayList<Integer>();
        	availOptionList = processor.availOptions();
            switch (action) {
                
            	// exit the program
            	case 0:
                    System.out.println("Exiting the program.");
                    return;
                
                // Available Actions
                case 1:
                	printList(availOptionList);
                    break;
                
                // Total Population for All ZIP Codes
                case 2:
                	if (!availOptionList.contains(2)) {
                		System.out.println("Error: Data is not available");
                	} else {
	                	int totalPopulation = processor.totalPopulationForAllZipCodes();
	                	printResult(Integer.toString(totalPopulation));
                	}
                	break;
                
                // Total Partial or Full Vaccinations Per Capita
                case 3:
                	
                	if (!availOptionList.contains(3)) {
                		System.out.println("Error: Data is not available");
                	} else {
	                	String type = askVaccineType();
	                	String date = askDate();
	                	// print results
	                	
	            		Map<String, Double> VaccinationsPerCapita = new TreeMap<String, Double> ();
	            		VaccinationsPerCapita = processor.VaccinationsPerCapita(date, type);
	            		printMap(VaccinationsPerCapita);
                	}
                	break;
                	
                // average market value 
                case 4:
                    
                	if (!availOptionList.contains(4)) {
                		System.out.println("Error: Data is not available");
                	} else {
                		
	                	String zipCode = askZipCode();
	                    int avgMarketValue = processor.averageMarketValue(zipCode);
	                    printResult(Integer.toString(avgMarketValue));
                	}
                    break;
                
                // Average Total Livable Area
                case 5:
                	if (!availOptionList.contains(5)) {
                		System.out.println("Error: Data is not available");
                	} else {
                	
	                	String zipCode1 = askZipCode();
	                    int avgTotalLivArea = processor.averageTotalLivableArea(zipCode1);
	                    printResult(Integer.toString(avgTotalLivArea));
                    
                	}
                	break;
                    
                //	Total Market Value Per Capita
                case 6:
                	
                	if (!availOptionList.contains(6)) {
                		System.out.println("Error: Data is not available");
                	} else {
                	
	                	String zipCode2 = askZipCode();
	                	int totalMarValPerCap = processor.TotalMarketValuePerCapita(zipCode2);
	                	printResult(Integer.toString(totalMarValPerCap));
                	}
                 	break;

                // Show the total market value to total vaccinations ratio per capita for all ZIP Code associated for the specified date.
                case 7:
                    
                	if (!availOptionList.contains(2)) {
                		System.out.println("Error: Data is not available");
                	} else {
	                	String dateForRatios = askDate();
	                    Map<String, Double> ratios = processor.calculateMarketValueToVaccinationRatiosForAllZipCodes(dateForRatios);
	                    printMap(ratios);
                	}
                    break;
                	
                default:
                    System.out.println("Error: Unknown action.");
                    break;
            }
        }
    }
    
    private void printMap(Map<String, Double> map) {

    	System.out.println("BEGIN OUTPUT");
    	
    	if (map.isEmpty() || map == null) {
    		System.out.println("0"); // if no data for the provided date, display 0
    	} else {
    	
	    	for (Map.Entry<String, Double> entry : map.entrySet()) {
	            System.out.println(entry.getKey() + " " + String.format("%.4f", entry.getValue()));
	        }
    	}
    	System.out.println("END OUTPUT");
		
	}

	public void printResult(String result) {
    	System.out.println("BEGIN OUTPUT");
    	System.out.println(result);
    	System.out.println("END OUTPUT");
    }
    
    public void printList(List<Integer> list) {
    	
    	System.out.println("BEGIN OUTPUT");
    	
    	for (Integer s : list) {
    		System.out.println(s);
    	}
    	System.out.println("END OUTPUT");
    }
    
    public String askZipCode() {
    	
    	while (true) {
			System.out.println("Please enter a 5-digit ZIP code");
			System.out.print("> ");
		    System.out.flush();
		    String zipCode = scanner.nextLine().trim();
		    System.out.println();
            Logger logger = Logger.getInstance();
            logger.log(zipCode);
		    if (zipCode.matches("[0-9]{5}")) {
		    	return zipCode;
		    } else {
                System.out.println("Invalid zip code format. Please try again.");
            }
    	}
    }

    // New method to validate user input
    private boolean isValidInput(String input) {
        try {
            int value = Integer.parseInt(input);
            return value >= 0 && value <= 7;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Ask user to type partial or full vaccine 
     * @return a string either full or partial 
     */
    
    public String askVaccineType() {
        
    	while (true) {
            System.out.println("Please type vaccination type: full or partial");
            System.out.print("> ");
            System.out.flush();
            String type = scanner.nextLine().trim();
            System.out.println();
            Logger logger = Logger.getInstance();
            logger.log(type);
            
            if (type.equalsIgnoreCase("full") || type.equalsIgnoreCase("partial")) {
                return type.toLowerCase();
            } else {
                System.out.println("Invalid vaccination type. Please enter 'full' or 'partial'.");
            }
        }
    }
    
    /**
     * ask the user to prompt a date 
     * @return
     */
    
    public String askDate() {
    	
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.println("Please type in a date in the format: YYYY-MM-DD.");
            System.out.print("> ");
            System.out.flush();            
            String timeStr = scanner.nextLine().trim();
            System.out.println();
            Logger logger = Logger.getInstance();
            logger.log(timeStr);

            if (datePattern.matcher(timeStr).matches()) {
            	 try {
                     LocalDate.parse(timeStr, dateFormatter);
                     return timeStr; // The date is valid
                 } catch (DateTimeParseException e) {
                     // The format is correct, but the date is invalid (e.g., "2012-20-38")
                     System.out.println("The date is not valid. Please try again.");
                 }
            } else {
                System.out.println("Invalid date format. Please try again.");
            }
        }
    }
    
	

}
