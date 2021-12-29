/*
 * @project restaurantApp
 * @fileName MainController
 * @author Jaya Prasad.M --> jaya_muthukrishnan
 * @email jaya_muthukrishnan@thbs.com
 * @date 24 12 2021 07:04 PM
 */
package com.restaurant.app;

import com.restaurant.app.user.User;
import com.restaurant.app.user.UserRegistrationDto;
import com.restaurant.app.user.UserRepository;
import com.restaurant.app.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
public class MainController {
    private UserService userService;

    public MainController(UserService userService) {
        super();
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // This Controller function is for loading the reservation page
    @GetMapping("/index")
    public String reservation(Model model){
        return "index";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.save(registrationDto);
        System.out.println(registrationDto);
        return "redirect:/login?success";
    }
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration";
    }


}
