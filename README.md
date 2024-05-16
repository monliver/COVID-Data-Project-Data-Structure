# COVID-Data-Project
This project involves developing a Java application to analyze data from text files, focusing on COVID-19 statistics, property values, and population data for Philadelphia. 

The application reads CSV and JSON files, processes the data, and performs various analyses, such as calculating vaccination rates and property values. It utilizes N-tier architecture, design patterns like Singleton and Strategy, and includes logging functionality. 

The project also requires implementing an additional feature that combines data from all three sources.






Use of Data Structures

1. Data Structure: ArrayList
Usage in Program: Used in the UserInterface class, particularly in the availOptions method.
Reason for Choice: The ArrayList was chosen for its dynamic sizing capabilities and efficient random access. It is particularly useful in this context as the number of available options can dynamically change based on the data present in covidList, populationList, and propertyList.
Alternatives Considered:
LinkedList: Considered for its efficient insertions and deletions. However, it has slower random access compared to ArrayList.
HashSet: Offers uniqueness and constant time performance for basic operations but does not maintain order, which is necessary for displaying options.

3. Data Structure: TreeMap
Usage in Program: Employed in the Processor class, specifically in the 
calculateMarketValueToVaccinationRatiosForAllZipCodes method.
Reason for Choice: The TreeMap was selected for its ability to maintain a sorted order based on the natural ordering of its keys. This is essential for organizing output data, particularly when dealing with geographic locations like ZIP codes.
Alternatives Considered:
HashMap: Provides faster insertions and lookups but does not maintain any order.
LinkedHashMap: Preserves insertion order but does not sort the keys, which is crucial for this specific use case.

3. Data Structure: HashMap
Usage in Program: Used in the Processor class for caching values in variables like averageMarketValueCache, averageTotalLivableAreaCache, and 
totalMarketValuePerCapitaCache.
Reason for Choice: The HashMap is ideal for caching due to its constant time complexity for get and put operations, assuming the hash function disperses the elements properly across the buckets. This results in efficient lookups and updates, which are frequent in caching scenarios.
Alternatives Considered:
TreeMap: Could provide an ordered cache but with O(log n) time complexity for basic operations. This is less efficient than HashMap for situations where the order of cache entries is not relevant.
