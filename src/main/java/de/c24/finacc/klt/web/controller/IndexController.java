package de.c24.finacc.klt.web.controller;

import de.c24.finacc.klt.web.facade.ExchangeServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * IndexController
 */
@Controller
@RequiredArgsConstructor
public class IndexController {
    private final ExchangeServiceFacade exchangeServiceFacade;

    /**
     * Index endpoint to show the index page
     *
     * @param model Spring's view model
     * @return view name
     */
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        return "index";
    }
}
