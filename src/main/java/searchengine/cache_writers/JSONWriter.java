package searchengine.cache_writers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import searchengine.controllers.ScraperController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
public class JSONWriter implements CacheWriter {
    @Autowired
    private ResourceLoader resourceLoader;

    private static final String NHSURL = "http://www.nhs.uk/";

    public void writeCache(String conditionIndex, Map<String, String> urls) {
        JSONObject conditionJSONObj = new JSONObject();
        JSONArray conditions = new JSONArray();
        for (String key : urls.keySet()) {
            conditions.add(getConditionInfo(key, urls.get(key)));
        }
        conditionJSONObj.put("conditions", conditions);

        writeToFile(conditionJSONObj, conditionIndex);
    }

    private void writeToFile(JSONObject conditionJSONObj, String conditionIndex) {
        String cacheDirectory = System.getProperty("user.dir")+"/cache";

        new File(cacheDirectory).mkdir();
        try (FileWriter file = new FileWriter(cacheDirectory +"/condition"+ conditionIndex+".json")) {
            file.write(conditionJSONObj.toJSONString());
        } catch (IOException e) {
            Logger.getLogger(ScraperController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private JSONObject getConditionInfo(String url, String conditionName) {
        System.out.println("urllll"+url);
        String conditionUrl = getUrlDomain(url);
        String conditionContent = getContentFromUrl(conditionUrl);

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
