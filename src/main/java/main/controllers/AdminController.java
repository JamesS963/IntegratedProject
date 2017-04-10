package main.controllers;

import main.dao.UserDao;
import main.models.User;
import main.security.UserDetailsServiceImpl;
import main.models.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Lewis on 26/02/2017.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView createUser() {
        ModelAndView mv = new ModelAndView("createUser");
        mv.addObject("user", new User());
        return mv;
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String createUser(@ModelAttribute("user") User user) {
        User createdUser;
        try {
            System.out.println(user);
            createdUser = userDetailsService.registerUser(user);
        } catch (Exception e) {
            return "error";
        }
        return "redirect:/admin/user/" + createdUser.getId();
    }

    @RequestMapping(value = "/user/{userId}", method=RequestMethod.GET)
    public ModelAndView viewUser(@PathVariable("userId") long userId) {
        User user = userDao.findOne(userId);
        ModelAndView mv = new ModelAndView("viewUser");
        mv.addObject("user", user);
        return mv;
    }

    @RequestMapping(value = "/user/edit/submit/{userId}", method=RequestMethod.GET)
    public String editUserSubmit(@PathVariable("userId") long userId, @PathVariable("editUser") Object editUser) {
        User user = userDao.findOne(userId);
        user.setId(userId);
        try {
            userDetailsService.registerUser(user);
        }catch(Exception e) {return "error";}
        return "redirect:/admin/user/edit"+user.getId();
    }

    @RequestMapping(value = "/user/edit/{userId}", method=RequestMethod.GET)
    public ModelAndView editUser(@PathVariable("userId") long userId) {
        User user = userDao.findOne(userId);
        ModelAndView mv = new ModelAndView("editUser");
        mv.addObject("user", user);
        return mv;
    }

}
