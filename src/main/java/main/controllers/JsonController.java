package main.controllers;

import main.dao.DocumentDao;
import main.dao.UserDao;
import main.models.Document;
import main.models.User;
import main.models.Error;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;


/**
 * Created by Dean on 21/03/2017.
 */
@RestController
@RequestMapping("/rest")
public class JsonController {

    private final DocumentDao documentDao;

    private final UserDao userDao;

    @Autowired
    JsonController(DocumentDao documentDao, UserDao userDao){
        this.documentDao = documentDao;
        this.userDao = userDao;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/byId/{userId}")
    Object getUserById(@PathVariable long userId) {
        try{
        User user = this.userDao.findById(userId);
        return user;
        }
        catch (Exception e){
            e.printStackTrace();
            return new Error("Invalid user ID", e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/byUsername/{username}")
    Object getUserByUsername(@PathVariable String userName) {
        try {
            User user = this.userDao.findByUsername(userName);
            return user;
        }
        catch (Exception e){
            e.printStackTrace();
            return new Error("Invalid Username", e.getMessage());
        }
    }

    @RequestMapping (method = RequestMethod.GET, value = "/user/getAll")
    Iterable<User> getAllUsers() {
        try {
            return this.userDao.findAll();
        }
        catch (Exception e){
            return null;
        }
    }

    @RequestMapping (method = RequestMethod.GET, value = "/document/byId/{docId}")
    Object getDocumentById(@PathVariable long docId) {
        try {
            Document document = this.documentDao.findById(docId);
            return document;
        }
        catch (Exception e){
            e.printStackTrace();
            return new Error("Invalid document ID", e.getMessage());
        }
    }

    @RequestMapping (method = RequestMethod.GET, value = "/document/byTitle/{docTitle}")
    Object getDocumentByTitle(@PathVariable String title) {
        try {
            Document document = this.documentDao.findByTitle(title);
            return document;
        }
        catch (Exception e){
            e.printStackTrace();
            return new Error("Invalid document title", e.getMessage());
        }
    }

    @RequestMapping (method = RequestMethod.GET, value = "/document/getAll")
    Iterable<Document> getAllDocuments() {
        try {
            return this.documentDao.findAll();
        }
        catch (Exception e) {
            return null;
         }
        }

    @RequestMapping (method = RequestMethod.GET, value = "document/byAuthor/{userId}")
    public Iterable<Document> getAllDocumentsByAuthor(@PathVariable("userId") String userId) {
        return documentDao.findAllByAuthor(Long.parseLong(userId));
    }

    @RequestMapping (value ="/getLoggedUser")
    Object getLoggedUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userDao.findByUsername(userDetails.getUsername());
            return user;
        }
        catch (Exception e) {
            return new Error("Error getting logged user", e.getMessage());
        }
    }

}

