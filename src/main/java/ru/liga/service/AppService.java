package ru.liga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

import ru.liga.domain.Person;

public class AppService implements Service{

    @Value("${server.url}")
    private String SERVER_URL;

    private RestTemplate restTemplate = new RestTemplate();
    private List<Person> personFromDB;
    private List<Person> matches;

    @Autowired
    @Override
    public void getPersonsFromServer() {
        ResponseEntity<List<Person>> response = restTemplate.exchange(
                SERVER_URL + "/persons",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {});
        personFromDB = response.getBody();
    }

    @Override
    public void createNewPersonOnServer(Person person) {
        restTemplate.postForObject(SERVER_URL + "/persons", person, ResponseEntity.class);
    }

    public Person loginPerson(Person pers) {
        Person personTemp = thisPersonExist(pers);
        if (personTemp != null) {
            return personTemp;
        }
        return null;
    }

    private Person thisPersonExist(Person pers) {
        for (Person person: personFromDB) {
            if (pers.getLogin().equals(person.getLogin()) && pers.getPassword().equals(person.getPassword())) {
                return person;
            }
        }
        return null;
    }

    @Override
    public void getMatchesForSomePerson(String id) {
        ResponseEntity<List<Person>> response = restTemplate.exchange(
                SERVER_URL + "/matches/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {});
        matches = response.getBody();
    }

    @Override
    public void postNewMatchOnServer(String id1, String id2) {
        List<String> uniqueIDlist = new ArrayList<>();
        uniqueIDlist.add(id1);
        uniqueIDlist.add(id2);

        restTemplate.postForObject(SERVER_URL + "/matches",
                uniqueIDlist,
                ResponseEntity.class);
    }

    @Override
    public void updatePersonOnServer(Person person) {
        restTemplate.postForObject(SERVER_URL + "/persons",
                person,
                ResponseEntity.class);
    }

    @Override
    public Person getSomePersonFromServerByUniqueID(String id) {
       Person response = restTemplate.getForObject(SERVER_URL + "/persons/" + id, Person.class);
       return response;
    }

    public Person getPerson(int next) {
        if (next >= personFromDB.size())
            return null;
        return personFromDB.get(next);
    }

    public Integer getNextID(int next) {
        int tempNext = next;
        if(next >= personFromDB.size() - 1)
           return 0;
        tempNext++;
        return tempNext;
    }

    public Person likePerson(Person person, int currentID) {
        Person tempPerson = person;
        tempPerson.addLikeToList(getPerson(currentID).getUniqueID());

        for (String likeID : getPerson(currentID).getLikeList()) {
            if (likeID.equals(person.getUniqueID())) {
                postNewMatchOnServer(person.getUniqueID(), getPerson(currentID).getUniqueID());
                getMatchesForSomePerson(person.getUniqueID());
            }
        }

        return tempPerson;
    }

    @Override
    public void updatePersons(Person person) {
        ResponseEntity<List<Person>> response = restTemplate.exchange(
                SERVER_URL + "/personsfor/" + person.getUniqueID(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {});
        personFromDB = response.getBody();
        getMatchesForSomePerson(person.getUniqueID());
    }

    public void deletePersonFromList(int next) {
        personFromDB.remove(next);
    }

    public void resetPersons() {
        getPersonsFromServer();
    }

    public List<Person> getMatches() {
        return matches;
    }
}
