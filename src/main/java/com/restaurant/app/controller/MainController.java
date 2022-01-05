/*
 * @project restaurantApp
 * @fileName MainController
 * @author Jaya Prasad.M --> jaya_muthukrishnan
 * @email jaya_muthukrishnan@thbs.com
 * @date 24 12 2021 07:04 PM
 */
package com.restaurant.app.controller;

import com.restaurant.app.model.Product;
import com.restaurant.app.service.ProductService;
import com.restaurant.app.model.User;
import com.restaurant.app.dao.UserRegistrationDto;
import com.restaurant.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;


    public MainController(UserService userService) {
        super();
        this.userService = userService;
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }


    // This Controller function is for loading the reservation page
    @GetMapping("/")
    public String reservation(Model model, HttpSession session){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        System.out.println(login);
        User user=userService.loadByEmailId(login);
        session.setAttribute("userName", user.getFirstName());
        String userName= String.valueOf(session.getAttribute("userName"));
        System.out.println(userName);
        List<Product> productList=productService.findAll();
        model.addAttribute("products",productList);
        model.addAttribute("userName",userName);
        System.out.println(productList);
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

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/payment")
    public String paymentPage() {
        return "payment";
    }


}
