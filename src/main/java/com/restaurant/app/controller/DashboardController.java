/*
 * @project ResturantApp
 * @fileName DashboardController
 * @author Jaya Prasad.M --> jaya_muthukrishnan
 * @email jaya_muthukrishnan@thbs.com
 * @date 04 01 2022 02:24 PM
 */
package com.restaurant.app.controller;

import com.restaurant.app.model.Order;
import com.restaurant.app.service.CartService;
import com.restaurant.app.service.OrderService;
import com.restaurant.app.service.ProductService;
import com.restaurant.app.dao.UserServiceImpl;
import com.restaurant.app.service.SaleService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/admin/dashboard/")
public class DashboardController {
    @Autowired
    ProductService productService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;
    @Autowired
    SaleService saleService;
    Double totalRevenue=0.0;
    @GetMapping("/")
    public  String index(Model model, HttpSession session) throws JSONException {
        long cartCount=cartService.count();
        long productCount=productService.count();
        List<String> productQuantityStockList=productService.findAllQuantity();
        List<String> productNameStockList=productService.findAllProductName();
        long userCount=userService.userRepository.count();
        long orderCount=orderService.count();
        JSONArray productArray=quantityCount();
        String userName = String.valueOf(session.getAttribute("userName"));
        model.addAttribute("productCount",productCount);
        model.addAttribute("userCount",userCount);
        model.addAttribute("cartCount",cartCount);
        model.addAttribute("orderCount",orderCount);
        model.addAttribute("totalRevenue",revenueCalculator());
        model.addAttribute("productQuantityStockList",productQuantityStockList);
        model.addAttribute("productNameStockList",productNameStockList);
        model.addAttribute("productArray",productArray);
        model.addAttribute("userName", userName);
        return "dashboard";
    }

    public JSONArray quantityCount() throws JSONException {
        List<String> productNameStockList = productService.findAllProductName();
        String week[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        JSONArray productArray = new JSONArray();
        int quantityArray[] = new int[week.length];
        for (String productName : productNameStockList) {
            for (int i = 0; i < week.length; i++) {
                Integer quantity = saleService.orderQuantityDetails(productName, week[i]);
                System.out.println(quantity);
                if (quantity != null) {
                    quantityArray[i] = quantity;

                } else {
                    quantityArray[i] = 0;
                }
            }
            JSONArray mJSONArray = new JSONArray();
            for(int value : quantityArray)
            {
                mJSONArray.put(value);
            }
            JSONObject data= new JSONObject();
            data.put("name", productName);
            data.put("data", mJSONArray);
            productArray.put(data);

        }
        System.out.println(productArray);

        return productArray;
    }


    public Double revenueCalculator(){
        totalRevenue=0.0;
        List<Order> orderList=orderService.findAll();
        for (Order orderItems:
             orderList) {
            totalRevenue+=orderItems.getGrandTotal();
        }
        return totalRevenue;
    }
}
