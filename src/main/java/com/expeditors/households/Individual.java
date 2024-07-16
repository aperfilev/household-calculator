package com.expeditors.households;

/**
 * The Individual class represents a person with attributes such as first name, last name, address, and age.
 * It implements the Comparable interface to define lexicographical order based on last name followed by first name.
 */
public class Individual implements Comparable<Individual> {
    private String firstName;
    private String lastName;
    private Address address;
    private int age;

    /**
     * The first name of the individual.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * The last name of the individual.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * The address of the individual.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * The age of the individual.
     */
    public int getAge() {
        return age;
    }

    /**
     * Defines lexicographical order based on LastName, FirstName
     * @param t other individual to compare with.
     * @return comparison result.
     */
    @Override
    public int compareTo(Individual t) {
        int v = getLastName().compareTo(t.getLastName());

        if (v == 0) {
            v = getFirstName().compareTo(t.getFirstName());
        }

        return v;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
