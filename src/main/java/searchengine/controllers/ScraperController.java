package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import searchengine.services.NHSScraperService;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@EnableAsync
public class ScraperController {
    @Autowired
    private NHSScraperService nhsScraperService;

    @RequestMapping("/scrape")
    public void index() {
        try {
            for (char conditionIndex = 'A'; conditionIndex <= 'Z'; conditionIndex++) {
                nhsScraperService.scrapeConditions(String.valueOf(conditionIndex));
            }

            nhsScraperService.scrapeConditions("0-9");
        } catch (InterruptedException e) {
            Logger.getLogger(ScraperController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}