package main.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.dao.UserDao;
import main.dao.DocumentDao;
import main.models.Document;
import main.models.Error;
import main.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Lewis on 14/02/2017.
 */
@Controller
public class UserController {

    // This pulls in the dependency automatically using Dependency Injection (DI).
    // Basically, in Spring you store Beans in a Context, and you can use Dependency Injection to inject them into
    // the classes to make things easier
    @Autowired
    private DocumentDao documentDao;
    private UserDao userDao;

    @RequestMapping(value = "/create", method=RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView mv = new ModelAndView("testCreate");
        mv.addObject("document", new Document());
        return mv;
    }

    /**
     * POST /create  --> Create a new document and save it in the database.
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute("document") Document document) {
        Document savedDocument;
        try {
            System.out.println(document);
            savedDocument = documentDao.save(document);
        } catch (Exception ex) {
            return "Error creating the document: " + ex.toString();
        }
        return "redirect:/view/" + savedDocument.getId();
    }

    @RequestMapping(value = "/view/{docId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable("docId") long docId) {
        Document document = documentDao.findOne(docId);
        System.out.println(document);
        ModelAndView mv = new ModelAndView("testView");
        mv.addObject("document", document);
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/testRest")
    public ModelAndView testRest() {
        ModelAndView mv = new ModelAndView("testRest");
        return mv;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/testLogged")
    public ModelAndView testLogged() {
        ModelAndView mv = new ModelAndView("testLogged");
        return mv;
    }

    /***
     * Take a stringified user object from the site as http request and maps to object
     * @param userId
     * @param jsonString
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value ="editUser/{userId}/{jsonString}")
    public Object editUser(@PathVariable("userId") String userId,
                           @PathVariable("jsonString") String jsonString){
        long id;
        User newUser;
        User loadedUser;

        try{ // Checks user exists and id is valid number
            id = Long.parseLong(userId);
            loadedUser = userDao.findById(id);
        }
        catch(Exception e) { return new Error("Invalid user id", e.getMessage());}

        try{ // Checks jsonString can be mapped as valid User object
            ObjectMapper mapper = new ObjectMapper();
            newUser = mapper.readValue(jsonString, User.class);
        }
        catch(Exception e){ return new Error("Invalid user string", e.getMessage()); }

        userDao.delete(id); // Deletes previous entry
        return userDao.save(newUser); // Saves and returns new entry
    }

}
