package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import searchengine.HealthConditionPage;
import searchengine.NLPHelper;
import searchengine.cache_readers.CacheReader;
import searchengine.cache_readers.JSONReader;
import searchengine.controllers.SearchController;

import java.io.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SearchEngine {

    @Autowired
    private NLPHelper nlpHelper;

    @Autowired
    private CacheReader cacheReader;

    public SearchEngine(CacheReader cacheReader, NLPHelper nlpHelper) {
        this.cacheReader = cacheReader;
        this.nlpHelper = nlpHelper;
    }

    public SearchEngine() {
        this(new JSONReader(), new NLPHelper());
    }

    public ArrayList<String> search(String keywords) {
        ArrayList<String> conditionUrls = new ArrayList<>();
        try {
            for (HealthConditionPage conditionPage : cacheReader.readCache()) {
                Set nouns = nlpHelper.getNouns(keywords);

                if (relevantPage(conditionPage, nouns)) {
                    conditionUrls.add(conditionPage.getUrl());
                }
            }
        } catch (IOException e) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, e);
        }


        return conditionUrls;
    }

    private boolean relevantPage(HealthConditionPage conditionPage, Set<String> keywords) {
        for (String k : keywords) {
            if (!conditionPage.getContent().contains(k)) {
                return false;
            }
        }
        return true;
    }
}
