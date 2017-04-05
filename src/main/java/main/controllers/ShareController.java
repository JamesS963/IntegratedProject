package main.controllers;

import main.models.*;
import main.dao.*;
import main.models.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Dean on 03/04/2017.
 */
@Controller
@RequestMapping("/share")
public class ShareController {

    private final DocumentDao documentDao;
    private final UserDao userDao;
    private final ShareDao shareDao;

    @Autowired
    ShareController(DocumentDao documentDao, ShareDao shareDao, UserDao userDao){
        this.documentDao = documentDao;
        this.userDao = userDao;
        this.shareDao = shareDao;
    }

    /** Creates share request and returns share if permissions etc are correct **/
    @RequestMapping(value = "/shareRequest/{docId}/{distribId}")
    public Object shareRequest(@PathVariable("docID") String docId,
                               @PathVariable("distribId") String distribUsername){
        long id, distrib;
        User loggedUser, distributee;
        Document document;
        Share share;

        try{
            /* Find document and distributee */
            id = Long.parseLong(docId);
            document = documentDao.findById(id);
            distributee = userDao.findByUsername(distribUsername);
        }
        catch(Exception e){
            return new Error("invalid document or distrib username", e.getMessage());
        }
        try{
            /* Get logged user */
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            loggedUser = userDao.findByUsername(userDetails.getUsername());
        }
        catch (Exception e) {
            return new Error("Error loading loading user details", e.getMessage());
        }
        /* check author of doc */
        if (document.getAuthor() == loggedUser.getUsername()){
            /* Check distributee permissions */
            if(distributee.getRole() == Role.ROLE_DISTRIBUTEE || distributee.getRole() == Role.ROLE_AUTHOR){
                share = new Share();
                share.setDocId(document.getId());
                share.setRevisionNo(document.getRevisionNo());
                share.setAuthorId(loggedUser.getId());
                share.setDistribId(distributee.getId());
            }
            else {
                return new Error("Wrong distributee permissions", "Distributee does not have role AUTHOR or DISTRIBUTEE");
            }
        }
        else {
            return new Error("No author permissions", "Logged user not listed as author on doc object");
        }
        return share;
    }

    /* Returns array of all documents shared by loggedUser */
    @RequestMapping(value = "/getSharing")
    public Iterable<Share> getShares(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User loggedUser = userDao.findByUsername(userDetails.getUsername());
        return shareDao.findAllByAuthorId(loggedUser.getId());
    }

    /* Returns array of all documents shared with distributee */
    @RequestMapping(value = "/getSharedWith")
    public Iterable<Share> getSharedWith() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User loggedUser = userDao.findByUsername(userDetails.getUsername());
        return shareDao.findAllByDistribId(loggedUser.getId());
    }
    /* For test page only */
    @RequestMapping(value = "/testSharing")
    public String testSharing() { return "testSharing"; }
}
