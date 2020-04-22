package ru.liga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.liga.domain.Person;
import ru.liga.service.AppService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class AppController {

    private AppService appService;
    private Boolean isLogged = false;
    private Integer next = 0;
    private Person person;

    private Person personTemp;

    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/")
    public String downloadMainPage(Model model) {
        if (isLogged) {
            model.addAttribute("gender", person.getMale());
            model.addAttribute("matches", appService.getMatches());
        }

        Person tempPerson = appService.getPerson(next);
        if (tempPerson != null) {
            model.addAttribute("person", appService.getPerson(next));
            model.addAttribute("emptyPersons", false);
        }
        else
            model.addAttribute("emptyPersons", "true");

        model.addAttribute("isLogged", isLogged);

        return "main";
    }

    @GetMapping("/persons/{id}")
    public String getSomeMatch(@PathVariable String id, Model model) {
        model.addAttribute("person", appService.getSomePersonFromServerByUniqueID(id));
        return "person";
    }

    @GetMapping("/dislike")
    public String nextPerson(Model model) {
        if (isLogged) {
            appService.deletePersonFromList(next);
            model.addAttribute("matches", appService.getMatches());
        }
        else
            next = appService.getNextID(next);
        model.addAttribute("isLogged", isLogged);
        Person tempPerson = appService.getPerson(next);
        if (tempPerson != null) {
            model.addAttribute("person", appService.getPerson(next));
            model.addAttribute("emptyPersons", false);
        }
        else
            model.addAttribute("emptyPersons", "true");

        return "main";
    }

    @GetMapping("/like")
    public String likePerson(Model model) {

        model.addAttribute("isLogged", isLogged);
        if(isLogged) {
            person = appService.likePerson(person, next);
            appService.deletePersonFromList(next);
            appService.updatePersonOnServer(person);
            model.addAttribute("matches", appService.getMatches());
        }
        else
            next = appService.getNextID(next);

        Person tempPerson = appService.getPerson(next);
        if (tempPerson != null) {
            model.addAttribute("person", appService.getPerson(next));
            model.addAttribute("emptyPersons", false);
        }
        else
            model.addAttribute("emptyPersons", "true");

        return "main";
    }

    @GetMapping("/new")
    public String openPageForCreatingNewPerson(Model model) {
      model.addAttribute("person", new Person());
      return "new";
    }

    @PostMapping("/new")
    public String createNewPerson(
            @ModelAttribute Person pers,
            Model model
    ) {
        pers.setUniqueID(UUID.randomUUID().toString());
        pers.setLikeList(new ArrayList<>());
        next = 0;
        person = pers;
        isLogged = true;
        appService.createNewPersonOnServer(pers);
        appService.updatePersons(person);
        model.addAttribute("name", pers.getLogin());
        return "result";
    }

    @GetMapping("/change")
    public String openPageForChangingPerson(Model model) {
      model.addAttribute("person", new Person());
      return "change";
    }

    @PostMapping("/change")
    public String changePerson(
            @ModelAttribute Person pers,
            Model model
    ) {
        person.setTitle(pers.getTitle());
        appService.createNewPersonOnServer(person);
        appService.updatePersons(person);
        model.addAttribute("name", person.getLogin());
        return "changed";
    }

    @GetMapping("/login")
    public String openPageForLoginPerson(Model model) {
      model.addAttribute("person", new Person());
      return "login";
    }

    @PostMapping("/login")
    public String loginPerson(
            @ModelAttribute Person pers,
            Model model
    ) {
        Person tempPerson = appService.loginPerson(pers);
        if (tempPerson != null) {
            next = 0;
            isLogged = true;
            person = tempPerson;
            appService.updatePersons(person);
            model.addAttribute("name", person.getLogin());
        }
        else
            model.addAttribute("name", "");
        return "result";
    }

    @GetMapping("/logout")
    public String openPageForLogoutPerson(Model model) {
        model.addAttribute("name", person.getLogin());
        //appService.updatePersonOnServer(person);
        appService.resetPersons();
        isLogged = false;
        person = new Person();
        return "logout";
    }

}
