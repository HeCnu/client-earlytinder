package ru.liga.domain;

import java.util.List;

public class Match {

    private final String uniqueID;
    private List<Person> personList;

    public Match(String uniqueID, List<Person> personList) {
        this.uniqueID = uniqueID;
        this.personList = personList;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void addMatch(Person person) {
        personList.add(person);
    }


}
