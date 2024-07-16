# HouseholdCalculator

The `HouseholdCalculator` is a Java class that provides functionality for calculating demographics based on household data stored in a CSV file. It allows users to import data from a CSV file, process the data, and print household demographics.

## Features

- Import occupant data from a CSV file.
- Print household demographics, including total occupants and adult occupants.
- Handles variations in address formatting and user input spelling.

## Assumptions and Questions

### Question 1
Should I consider importing data for multiple years? What would I do differently to support gathering annual statistics:
- I would store not ages rather Date Of Birth.
- Assuming answer: this is not required at this moment.

### Assumption 1
We assume that this functionality will process data for short timeframes. Individual's age might change if we import data from different years.

### Assumption 2
We assume that user input has a solid structure and don't need to protect from bad formatted user input.

### Question 2
I assume that this task does not necessitate a highly sophisticated algorithm for parsing user input, which would accurately detect all conceivable variations of common abbreviations such as Ave, Avenue, St, Street, Blvd, Boulevard, Bl, etc. Solving this challenge would entail leveraging extensive dictionaries and employing more advanced techniques.

### Data Integrity: 
How can we ensure the integrity of the input data, especially considering potential inconsistencies or errors in the addresses provided?

###Scalability: 
How scalable is the current solution for processing a large volume of input data? Are there any potential bottlenecks or performance concerns?

###Localization: 
How does the solution handle addresses from different regions or countries with varying address formats and conventions?

## Usage

To use the `HouseholdCalculator`, follow these steps:

1. Ensure you have Java installed on your system.
2. Compile the `HouseholdCalculator.java` file.
3. Run the compiled class file with the input CSV filename as a command-line argument.

Example usage:

```bash
java HouseholdCalculator input.csv
```

## Dependencies

- Java (JDK 8 or higher)

## How to Contribute

Contributions to the `HouseholdCalculator` class are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
