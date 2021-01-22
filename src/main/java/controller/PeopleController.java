package controller;

import dao.PeopleDAO;
import entity.Person;
import exception.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleDAO peopleDAO;

    public PeopleController(@Qualifier("springJdbcDAO") PeopleDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
    }

    @GetMapping
    public String index(Model model){
        try {
            model.addAttribute("people", peopleDAO.getAllPeople());
        } catch (FetchException e) {
            e.printStackTrace();
        }
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model){
        try {
            model.addAttribute("person", peopleDAO.getById(id));
        } catch (FetchException e) {
            e.printStackTrace();
        }
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person){
        return "people/new";
    }
//    Equal to method above
//    @GetMapping("/new")
//    public String newPerson(Model model){
//        model.addAttribute("person", new Person());
//        return "people/new";
//    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        try {
            if(bindingResult.hasErrors()) {
                return "people/new";
            }
            peopleDAO.save(person);
        } catch (SaveException e) {
            e.printStackTrace();
        }
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id){
        try {
            model.addAttribute("person", peopleDAO.getById(id));
        } catch (FetchException e) {
            e.printStackTrace();
        }
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id){
        try {
            if(bindingResult.hasErrors()){
                return "people/edit";
            }
            peopleDAO.update(id, person);
        } catch (UpdateException e) {
            e.printStackTrace();
        }
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        try {
            peopleDAO.delete(id);
        } catch (DeleteException e) {
            e.printStackTrace();
        }
        return "redirect:/people";
    }
}

