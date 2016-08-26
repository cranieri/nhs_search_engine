package searchengine.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import searchengine.controllers.ScraperController;
import searchengine.cache_writers.CacheWriter;
import searchengine.cache_writers.JSONWriter;

@Service
@Qualifier("nhsScraperService")
public class NHSScraperService {
    private static final String NHSURL = "http://www.nhs.uk";

    private CacheWriter cacheWriter;

    public NHSScraperService(CacheWriter cacheWriter) {
        this.cacheWriter = cacheWriter;
    }

    public NHSScraperService() {
        this( new JSONWriter());
    }

    @Async
    public Future<Boolean> scrapeConditions(String conditionIndex) throws InterruptedException {
        try {
            String conditionUrl = NHSURL+"/Conditions/Pages/BodyMap.aspx?Index=" + conditionIndex;
            Document doc = Jsoup.connect(conditionUrl).timeout(10000).get();
            Thread.sleep(1000);
            Elements links = doc.select(".index-section li a");
            cacheWriter.writeCache(conditionIndex, getUrls(links));
        } catch (Exception e) {
            Logger.getLogger(ScraperController.class.getName()).log(Level.SEVERE, null, e);
        }
        return new AsyncResult<>(true);
    }

    private Map<String, String> getUrls(Elements urls) {
        return urls.stream().collect(Collectors.toMap(
                e -> e.text(),
                e -> e.attr("href"),
                (t1, t2) -> t2));
    }

    private JSONObject getConditionInfo(Element url) {
        String conditionUrl = getUrlDomain(url.attr("href"));
        String conditionContent = getContentFromUrl(conditionUrl);
        String conditionName = url.text();

        JSONObject conditionJSONObj = new JSONObject();
        conditionJSONObj.put("name", conditionName);
        conditionJSONObj.put("url", conditionUrl);
        conditionJSONObj.put("content", conditionContent);
        return conditionJSONObj;
    }

    private String getContentFromUrl(String url) {
        String conditionPageContent = "";
        try {
            Document doc = Jsoup.connect(url).get();
            conditionPageContent = doc.text();
        } catch (IOException e) {
            Logger.getLogger(ScraperController.class.getName()).log(Level.SEVERE, null, e);
        }
        return conditionPageContent;
    }

    private String getUrlDomain(String url) {
        String domain = "";
        if (!url.startsWith(NHSURL)) {
            domain = NHSURL;
        }
        return (domain + url).trim();
    }
}
