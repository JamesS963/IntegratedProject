package main.controllers;

import main.dao.DocumentDao;
import main.dao.UserDao;
import main.models.Document;
import main.models.User;
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

import javax.servlet.http.HttpSession;

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
    User getUserById(@PathVariable long userId) {
        try{
        User user = this.userDao.findById(userId);
        return user;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/byUsername/{username}")
    User getUserByUsername(@PathVariable String userName) {
        try {
            User user = this.userDao.findByUsername(userName);
            return user;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
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
    Document getDocumentById(@PathVariable long docId) {
        try {
            Document document = this.documentDao.findById(docId);
            return document;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping (method = RequestMethod.GET, value = "/document/byTitle/{docTitle}")
    Document getDocumentByTitle(@PathVariable String title) {
        try {
            Document document = this.documentDao.findByTitle(title);
            return document;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
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

    @RequestMapping (value ="/getLoggedUser")
    User getLoggedUser(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userDao.findByUsername(userDetails.getUsername());
        return user;
    }

}

