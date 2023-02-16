package com.kameleoon.trial.controllers;

import com.kameleoon.trial.DAO.UserQuotesDAO;
import com.kameleoon.trial.models.Quote;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quotes")
public class QuotesController {
    private final UserQuotesDAO userQuotesDAO;

    public QuotesController(UserQuotesDAO userQuotesDAO) {
        this.userQuotesDAO = userQuotesDAO;
    }

    @GetMapping("/top10")
    public String showTop(Model model) {
        model.addAttribute("quotes", userQuotesDAO.getQuotes());
        model.addAttribute("randomQuote", userQuotesDAO.getRandomQuote());
        return "index";
    }

    @GetMapping("/{id}/edit")
    public String editQuote(@PathVariable("id") int id, Model model) {
        model.addAttribute("quote", userQuotesDAO.getQuote(id));
        return "update";
    }

    @PatchMapping("/{id}")
    public String updateQuote(@ModelAttribute("quote") Quote quote, @PathVariable("id") int id){
        userQuotesDAO.updateQuote(id, quote);
        return "redirect:/quotes/top10";
    }

    @DeleteMapping("/{id}")
    public String deleteQuote(@PathVariable("id") int id){
        userQuotesDAO.deleteQuote(id);
        return "redirect:/quotes/top10";
    }

    @GetMapping("/new")
    public String newQuote(Model model){
        model.addAttribute("quote", new Quote());
        return "addQuote";
    }

    @PostMapping("/new")
    public String postQuote(@ModelAttribute("quote") Quote quote){
        quote.setScore(0);
        quote.setDate(new java.util.Date(System.currentTimeMillis()));
        userQuotesDAO.saveQuote(quote);
        return "redirect:/quotes/top10";
    }
}
