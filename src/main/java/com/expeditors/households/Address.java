package com.expeditors.households;

import java.util.Objects;

/**
 * The Address class represents a physical address with attributes such as address line, city, and state.
 * It overrides the hashCode and equals methods to provide custom equality comparison based on the address components.
 */
public class Address {

    private String addressLine;
    private String city;
    private String state;

    /**
     * The address line of the physical address.
     */
    public String getAddressLine() {
        return addressLine;
    }

    /**
     * The city of the physical address.
     */
    public String getCity() {
        return city;
    }

    /**
     * The state of the physical address.
     */
    public String getState() {
        return state;
    }

    /**
     * Computes the hash code for this address based on its components.
     * @return The hash code value for this address.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.getAddressLine());
        hash = 37 * hash + Objects.hashCode(this.getCity());
        hash = 37 * hash + Objects.hashCode(this.getState());
        return hash;
    }

    /**
     * Indicates whether some other object is "equal to" this one by comparing their address components.
     * @param obj The reference object with which to compare.
     * @return true if this address is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if (!Objects.equals(this.getAddressLine(), other.getAddressLine())) {
            return false;
        }
        if (!Objects.equals(this.getCity(), other.getCity())) {
            return false;
        }
        return Objects.equals(this.getState(), other.getState());
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }
}
