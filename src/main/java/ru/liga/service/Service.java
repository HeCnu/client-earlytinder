package ru.liga.service;

import ru.liga.domain.Person;

public interface Service {

    void getPersonsFromServer();

    void createNewPersonOnServer(Person person);

    void getMatchesForSomePerson(String id);

    void postNewMatchOnServer(String id1, String id2);

    void updatePersonOnServer(Person person);

    Person getSomePersonFromServerByUniqueID(String id);

    void updatePersons(Person person);

}
