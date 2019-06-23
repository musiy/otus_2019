package sample;

import java.time.LocalDate;

public class Author {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private int experience;

    public Author(String firstName, String lastName, LocalDate birthDate, int experience) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.experience = experience;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getExperience() {
        return experience;
    }
}
