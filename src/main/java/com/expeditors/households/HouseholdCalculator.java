package com.expeditors.households;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The HouseholdCalculator class represents a tool for calculating demographics based on household data.
 * It provides methods for importing data from a CSV file, processing the data, and printing the results.
 * This class utilizes the Individual and Address classes to represent individuals and their addresses.
 */
public class HouseholdCalculator {
    /**
     * The minimum age considered as an adult for demographic calculations.
     */
    private static final int AdultAge = 18;
    /**
     * The number of fields expected in each input CSV record.
     */
    private static final int InputFormatRecordCount = 6;

    /**
     * A map that stores households and their corresponding individuals.
     */
    private final Map<Address, Collection<Individual>> households = new LinkedHashMap<>();

    /**
     * The main method entry point for running the HouseholdCalculator from the command line.
     * Reads input CSV file, imports data, and prints household demographics.
     * Usage: java HouseholdCalculator input.csv
     * @param args Command-line arguments containing the input CSV filename.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java DemographyCalculator input.csv");
            return;
        }

        String inputFilename = args[0];

        System.out.println("Input file: " + inputFilename);

        HouseholdCalculator calc = new HouseholdCalculator();
        calc.importCSVData(inputFilename, false);
        calc.printHouseHolds();
    }

    /**
     * Retrieves the map of households and their corresponding individuals.
     * Each household is represented by an address, and the individuals living at that address are stored in a collection.
     * @return A map where the keys are addresses and the values are collections of individuals.
     */
    public Map<Address, Collection<Individual>> getHouseholds() {
        return households;
    }

    /**
     * Imports occupant data from the given CSV input file.
     * @param inputFilename The CSV input file containing occupant data.
     * @param skipHeader Flag indicating whether to skip the CSV file header.
     */
    public void importCSVData(String inputFilename, boolean skipHeader) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            int lineCounter = 1; //1 based line number for error reference
            String line;
            if (skipHeader) {
                line = reader.readLine();
                lineCounter++;
                if (line == null) {
                    return;
                }
            }
            while ((line = reader.readLine()) != null) {
                try {
                    Individual ind = parseLine(line, lineCounter++);

                    Collection<Individual> individuals = households.getOrDefault(ind.getAddress(), new PriorityQueue<>());
                    individuals.add(ind);

                    if ( ! households.containsKey(ind.getAddress())) {
                        households.put(ind.getAddress(), individuals);
                    }
                } catch (Exception e) {
                    System.out.printf("Error: Unable to parse record: %s\n\n", e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input file.");
        }
    }

    /**
     * Prints the current households and their occupants' demographics.
     */
    public void printHouseHolds() {
        System.out.println("Current Households:");
        for (Map.Entry<Address,Collection<Individual>> entry : households.entrySet()) {
            Address address = entry.getKey();
            Collection<Individual> individuals = entry.getValue();
            System.out.printf("Household: '%s, %s, %s' has %d total occupant(s)\n", address.getAddressLine(), address.getCity(), address.getState(), individuals.size());
            System.out.println("Adult occupant(s):");
            for (Individual i : individuals) {
                if (i.getAge() >= AdultAge) {
                    System.out.printf("\t%s, %s, '%s, %s, %s', %d\n", i.getFirstName(), i.getLastName(), i.getAddress().getAddressLine(), i.getAddress().getCity(), i.getAddress().getState(), i.getAge());
                }
            }
            System.out.println();
        }
    }

    /**
     * Defines quoted string token with trailing ',' comma or end of line.
     */
    private static final Pattern QuotedTokenPattern = Pattern.compile("\"([^\"]*)\"(?:,|$)");

    /**
     * Parses input record line and creates corresponding Individual.
     * @param line - input line
     * @param lineNumber - reference line number
     * @return corresponding Individual.
     */
    public static Individual parseLine(String line, int lineNumber) throws InvalidInputDataException {
        Matcher matcher = QuotedTokenPattern.matcher(line);

        // Store matched elements in an array
        String[] parts = new String[InputFormatRecordCount]; // Assuming there are 7 elements in the CSV line
        int index = 0;
        while (matcher.find() && index < parts.length) {
            parts[index++] = matcher.group(1);
        }

        if (index != 6) {
            throw new InvalidInputDataException(String.format("Invalid record at line %d", lineNumber));
        }

        Individual i = new Individual();
        i.setFirstName(parts[0]);
        i.setLastName(parts[1]);
        i.setAddress(parseAddress(parts[2], parts[3], parts[4]));
        i.setAge(Integer.parseInt(parts[5]));

        if (i.getAge() < 0) {
            throw new InvalidInputDataException("Age should be positive value.");
        }
        return i;
    }

    /**
     * Parses the address components and creates an Address instance.
     * @param addressLine The raw address line input.
     * @param city The raw city input.
     * @param state The raw state input.
     * @return An Address instance created from the parsed components.
     */
    public static Address parseAddress(String addressLine, String city, String state) {
        Address address = new Address();
        address.setAddressLine(unifyAddressLineFormat(addressLine));
        address.setCity(capitalize(city));
        address.setState(state.toUpperCase());
        return address;
    }

    /**
     * Brings the address line to a unified and capitalized format.
     * Modifies the address line to unify some user input spelling variations.
     * @param addressLine The raw address line input.
     * @return The unified and capitalized address line.
     */
    static String unifyAddressLineFormat(String addressLine) {
        // Remove unnecessary punctuation using regular expressions
        addressLine = addressLine.replaceAll("[,;\\/]", ""); // Remove comma, semicolon, slash
        addressLine = addressLine.replaceAll("[-]", " "); // Replace hyphen with space

        // Split the address into individual words
        String[] words = addressLine.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            // Remove trailing punctuation (like periods) from abbreviations
            word = word.replaceAll("\\.$", "");

            // Capitalize the first letter of each word
            String capitalizedWord = capitalize(word);
            result.append(capitalizedWord).append(" ");
        }

        // Trim any extra whitespace and return the result
        return result.toString().trim();
    }

    /**
     * Capitalizes each word in the given line.
     * @param line The input line.
     * @return The capitalized line.
     */
    static String capitalize(String line) {
        String[] words = line.trim().split("\\s+");

        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if ( ! word.isEmpty()) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            }
            result.append(word).append(" ");
        }

        return result.toString().trim();
    }
}
