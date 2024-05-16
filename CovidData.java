package edu.upenn.cit594.util;
import java.time.LocalDateTime;

public class CovidData {

    // Fields
    private String zipCode;
    private int partiallyVaccinated;
    private int fullyVaccinated;
    private String reportTimestamp;
    
    // Constructor
    public CovidData(String zipCode, int partiallyVaccinated, int fullyVaccinated, String reportTimestamp) {
        this.zipCode = zipCode;
        this.partiallyVaccinated = partiallyVaccinated;
        this.fullyVaccinated = fullyVaccinated;
        this.reportTimestamp = reportTimestamp;
    }

    // Getters and Setters
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getPartiallyVaccinated() {
        return partiallyVaccinated;
    }

    public void setPartiallyVaccinated(int partiallyVaccinated) {
        this.partiallyVaccinated = partiallyVaccinated;
    }

    public int getFullyVaccinated() {
        return fullyVaccinated;
    }

    public void setFullyVaccinated(int fullyVaccinated) {
        this.fullyVaccinated = fullyVaccinated;
    }
    
    public String getReportTimestamp() {
        return reportTimestamp;
    }

    public void setReportTimestamp(String reportTimestamp) {
        this.reportTimestamp = reportTimestamp;
    }

    
    // Method to display CovidData
    @Override
    public String toString() {
        return "CovidData{" +
               "zipCode='" + zipCode + '\'' +
               ", partiallyVaccinated=" + partiallyVaccinated +
               ", fullyVaccinated=" + fullyVaccinated +
               ", reportTimestamp=" + reportTimestamp +
               '}';
    }


}
