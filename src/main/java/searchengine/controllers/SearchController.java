package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

import searchengine.services.SearchEngine;

@RestController
@EnableAsync
public class SearchController {
    @Autowired
    private SearchEngine searchEngine;

    @RequestMapping("/search")
    @ResponseBody
    public ArrayList index(@RequestParam(value = "keywords") String keywords) {
        return searchEngine.search(keywords);
    }
}