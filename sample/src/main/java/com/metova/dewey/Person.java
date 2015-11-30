package com.metova.dewey;

import android.support.annotation.NonNull;

public class Person implements Comparable<Person> {

    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int compareTo(@NonNull Person another) {
        return lastName.compareTo(another.lastName);
    }
}
