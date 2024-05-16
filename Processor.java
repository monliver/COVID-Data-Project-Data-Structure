package processor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import util.CovidData;
import util.PopulationData;
import util.PropertyData;

public class Processor {

	private List<PropertyData> propertyList;
	private List<PopulationData> populationList;
	private List<CovidData> covidList;

	// Cache variables
	private Integer totalPopulationCache = null;
	private Map<String, Integer> averageMarketValueCache = new HashMap<>();
	private Map<String, Integer> averageTotalLivableAreaCache = new HashMap<>();
	private Map<String, Integer> totalMarketValuePerCapitaCache = new HashMap<>();
	private Map<String, Map<String, Double>> vaccinationsPerCapitaCache = new HashMap<>();
	private Map<String, Map<String, Double>> marketValueToVaccinationRatiosCache = new HashMap<>();


	// Constructor
	public Processor(List<PropertyData> propertyList, List<PopulationData> populationList, List<CovidData> covidList) {

		this.setPropertyList(propertyList);
		this.setPopulationList(populationList);
		this.setCovidList(covidList);
	}

	// Calculate total population for all ZIP Codes
	public int totalPopulationForAllZipCodes() {
		if (totalPopulationCache != null) {
			return totalPopulationCache;
		}
		int total = 0;
		for (PopulationData population : populationList) {
			total += population.getPopulation();
		}
		totalPopulationCache = total;
		return total;
	}

	// Calculate average market value for a given ZIP Code
	public int averageMarketValue(String zipCode) {
		if (averageMarketValueCache.containsKey(zipCode)) {
			return averageMarketValueCache.get(zipCode);
		}
		int avgMarketValue = averageStrategy(zipCode, new AvgValue());
		averageMarketValueCache.put(zipCode, avgMarketValue);
		return avgMarketValue;
	}

	// Calculate average total livable area for a given ZIP Code
	public int averageTotalLivableArea(String zipCode) {
		if (averageTotalLivableAreaCache.containsKey(zipCode)) {
			return averageTotalLivableAreaCache.get(zipCode);
		}
		int avgTotalLivableArea = averageStrategy(zipCode, new AvgArea());
		averageTotalLivableAreaCache.put(zipCode, avgTotalLivableArea);
		return avgTotalLivableArea;
	}

	// Calculate the ratios of total market value of properties to total number of vaccinations per capita for all ZIP Codes for a specified date
	public Map<String, Double> calculateMarketValueToVaccinationRatiosForAllZipCodes(String date) {

		Map<String, Double> ratios = new TreeMap<>();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate parsedDate = LocalDate.parse(date, dateFormatter);

		if (marketValueToVaccinationRatiosCache.containsKey(date)) {
			return marketValueToVaccinationRatiosCache.get(date);
		}

		// Group properties by ZIP Code
		Map<String, Double> totalMarketValues = propertyList.stream()
				.collect(Collectors.groupingBy(PropertyData::getZipCode,
						Collectors.summingDouble(PropertyData::getMarketValue)));

		// Group Covid data by ZIP Code
		Map<String, Integer> totalVaccinations = new TreeMap<>();
		for (CovidData covid : covidList) {
			LocalDateTime parsedTimestamp = LocalDateTime.parse(covid.getReportTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			if (parsedTimestamp.toLocalDate().equals(parsedDate)) {
				String zipCode = covid.getZipCode();
				totalVaccinations.compute(zipCode, (k, v) -> (v == null ? 0 : v) + covid.getFullyVaccinated() + covid.getPartiallyVaccinated());
			}
		}

		// Calculate ratios for each ZIP Code
		for (String zipCode : totalMarketValues.keySet()) {
			double totalMarketValue = totalMarketValues.getOrDefault(zipCode, 0.0);
			int totalVaccinationCount = totalVaccinations.getOrDefault(zipCode, 0);
			int population = getPopulation(zipCode);

			if (population != 0 && totalVaccinationCount != 0) {
				double ratio = totalMarketValue / totalVaccinationCount / population;
				ratios.put(zipCode, ratio);
			}
		}

		marketValueToVaccinationRatiosCache.put(date, ratios);
		return ratios;
	}

	// Calculate the average of a property attribute (e.g., market value or total livable area) for a given ZIP Code
	public int averageStrategy(String zipCode, AvgCalculator avgCal) {
		int count = 0;
		int sum = 0;
		double cal = 0;
		for (PropertyData property : propertyList) {
			if (property.getZipCode().equals(zipCode)) {
				cal = avgCal.avgItem(property);
				if (cal != -1) {
					count++;
					sum += cal;
				}
			}
		}

		if (count == 0) {
			return 0;
		} else {
			double avg = (double) sum / count;
			int avgInt = (int) avg; // Cast to integer
			return avgInt;
		}
	}

	// Calculate total market value per capita for a given ZIP Code
	public int TotalMarketValuePerCapita(String zipCode) {

		if (totalMarketValuePerCapitaCache.containsKey(zipCode)) {
			return totalMarketValuePerCapitaCache.get(zipCode);
		}
		int totalPopulation = 0;
		for (PopulationData popData : populationList) {
			if (popData.getZipCode().equals(zipCode)) {
				totalPopulation = popData.getPopulation();
				break;
			}
		}
		if (totalPopulation == 0) {
			return 0;
		}
		double totalMarketVal = 0;
		for (PropertyData property : propertyList) {
			if (property.getZipCode().equals(zipCode)) {
				totalMarketVal += property.getMarketValue();
			}
		}
		if (totalMarketVal == 0) {
			return 0;
		}
		double result = totalMarketVal / totalPopulation;
		int truncatedResult = (int) result; // Cast to integer

		totalMarketValuePerCapitaCache.put(zipCode, truncatedResult);
		return truncatedResult;
	}

	// Get a list of available options for processing
	public List<Integer> availOptions() {
		List<Integer> availOptionList = new ArrayList<Integer>();

		availOptionList.add(0);
		availOptionList.add(1);

		boolean covidExist = covidList != null && !covidList.isEmpty();
		boolean populationExist = populationList != null && !populationList.isEmpty();
		boolean propertyExist = propertyList != null && !propertyList.isEmpty();

		if (populationExist) {
			availOptionList.add(2);
		}
		if (covidExist) {
			availOptionList.add(3);
		}
		if (propertyExist) {
			availOptionList.add(4);
			availOptionList.add(5);
		}
		if (populationExist && propertyExist) {
			availOptionList.add(6);
		}
		if (covidExist && propertyExist) {
			availOptionList.add(7);
		}

		return availOptionList;
	}

	// Calculate vaccinations per capita for a given date and type (full or partial)
	public Map<String, Double> VaccinationsPerCapita(String Date, String type) {

		String key = Date + "-" + type;
		if (vaccinationsPerCapitaCache.containsKey(key)) {
			return vaccinationsPerCapitaCache.get(key);
		}

		DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Map<String, Double> VaccinationsPerCapita = new TreeMap<String, Double>();

		String timestamp;
		LocalDate parsedDate = LocalDate.parse(Date, dateFormatter);

		for (CovidData covid : covidList) {
			timestamp = covid.getReportTimestamp();
			LocalDateTime parsedTimestamp = LocalDateTime.parse(timestamp, timestampFormatter);

			if (parsedTimestamp.toLocalDate().equals(parsedDate)) {

				String zipCode = covid.getZipCode();
				int population = getPopulation(zipCode);
				if (population != 0) {

					Double vaccinations = 0.0;

					if (type.toLowerCase().equals("full")) {
						vaccinations = (double) covid.getFullyVaccinated();
					} else if (type.toLowerCase().equals("partial")) {
						vaccinations = (double) covid.getPartiallyVaccinated();
					} else {
						return null;
					}

					if (vaccinations != 0.0) {

						Double vaccinationsRatio = vaccinations / (double) population;
						VaccinationsPerCapita.put(zipCode, vaccinationsRatio);

					}
				}
			}

		}

		vaccinationsPerCapitaCache.put(key, VaccinationsPerCapita);
		return VaccinationsPerCapita;
	}

	// Get population for a given ZIP Code
	public int getPopulation(String zipCode) {
		for (PopulationData popData : populationList) {
			if (popData.getZipCode().equals(zipCode)) {
				return popData.getPopulation();
			}
		}
		return 0;
	}

	public List<PropertyData> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<PropertyData> propertyList) {
		this.propertyList = propertyList;
	}

	public List<PopulationData> getPopulationList() {
		return populationList;
	}

	public void setPopulationList(List<PopulationData> populationList) {
		this.populationList = populationList;
	}

	public List<CovidData> getCovidList() {
		return covidList;
	}

	public void setCovidList(List<CovidData> covidList) {
		this.covidList = covidList;
	}
}
