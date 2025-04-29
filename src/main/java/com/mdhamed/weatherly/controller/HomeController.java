package com.mdhamed.weatherly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller for home page redirection
 */
@Controller
public class HomeController {

    /**
     * Redirect root URL to index.html
     * 
     * @return RedirectView to index.html
     */
    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/index.html");
    }
}
