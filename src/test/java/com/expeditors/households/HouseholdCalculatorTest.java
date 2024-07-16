package com.expeditors.households;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HouseholdCalculatorTest {

    private static final String TEST_CSV_FILENAME = "test_data.csv";

    @BeforeEach
    public void setUp() {
        try {
            // Create the test CSV file
            BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_CSV_FILENAME));
            writer.write("\"John\",\"Doe\",\"123 Main St.\",\"Seattle\",\"WA\",\"25\"\n");
            writer.write("\"Jane\",\"Doe\",\"456 Elm St.\",\"Tacoma\",\"WA\",\"30\"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        // Delete the test CSV file
        File file = new File(TEST_CSV_FILENAME);
        file.delete();
    }

    @Test
    void importCSVData_ValidData_NoExceptionThrown() {
        HouseholdCalculator calculator = new HouseholdCalculator();
        calculator.importCSVData(TEST_CSV_FILENAME, false);
        Map<Address, Collection<Individual>> households = calculator.getHouseholds();
        assertNotNull(households);
        assertEquals(2, households.size());
    }

    @Test
    void importCSVData_InvalidData_ExceptionThrown() {
        HouseholdCalculator calculator = new HouseholdCalculator();
        try {
            calculator.importCSVData(TEST_CSV_FILENAME, false);
        } catch (RuntimeException e) {
            assertEquals("Failed to read input file.", e.getMessage());
        }
    }

    @Test
    void parseLine_ValidData_CreatesIndividual() {
        String line = "\"John\",\"Doe\",\"123 Main St.\",\"Anytown\",\"CA\",\"25\"";
        Individual individual = null;
        try {
            individual = HouseholdCalculator.parseLine(line, 1);
        } catch (InvalidInputDataException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(individual);
        assertEquals("John", individual.getFirstName());
        assertEquals("Doe", individual.getLastName());
        assertEquals("123 Main St", individual.getAddress().getAddressLine());
        assertEquals("Anytown", individual.getAddress().getCity());
        assertEquals("CA", individual.getAddress().getState());
        assertEquals(25, individual.getAge());
    }

    @Test
    void parseAddress_ValidData_CreatesAddress() {
        String addressLine = "123 Main St.";
        String city = "Anytown";
        String state = "CA";
        Address address = HouseholdCalculator.parseAddress(addressLine, city, state);
        assertNotNull(address);
        assertEquals("123 Main St", address.getAddressLine());
        assertEquals("Anytown", address.getCity());
        assertEquals("CA", address.getState());
    }

    @Test
    void unifyAddressLineFormat_InputWithPunctuation_RemovesPunctuationAndCapitalizes() {
        String addressLine = "123 main st., apt. 200";
        String unifiedAddress = HouseholdCalculator.unifyAddressLineFormat(addressLine);
        assertEquals("123 Main St Apt 200", unifiedAddress);
    }

    @Test
    void capitalize_InputWithMixedCase_CapitalizesWords() {
        String line = "123 main st.";
        String capitalizedLine = HouseholdCalculator.capitalize(line);
        assertEquals("123 Main St.", capitalizedLine);
    }
}