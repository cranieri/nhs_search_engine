package searchengine.cache_readers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import searchengine.HealthConditionPage;
import searchengine.controllers.SearchController;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JSONReader implements CacheReader {

    private String cacheFolder;

    public JSONReader(String cacheFolder) {
        this.cacheFolder = cacheFolder;
    }

    public JSONReader() {
        this(System.getProperty("user.dir") + "/cache");
    }

    public ArrayList<HealthConditionPage> readCache() {
        ArrayList<HealthConditionPage> conditionPages = new ArrayList<>();

        for (char conditionIndex = 'A'; conditionIndex <= 'Z'; conditionIndex++) {
            String fileName = cacheFolder + "/condition" + String.valueOf(conditionIndex);

            conditionPages.addAll(readFromFile(fileName));
        }

        String fileName = cacheFolder + "/condition0-9";
        conditionPages.addAll(readFromFile(fileName));

        return conditionPages;
    }

    private ArrayList<HealthConditionPage> readFromFile(String fileName) {
        ArrayList<HealthConditionPage> conditionPages = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(fileName));

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray conditionsList = (JSONArray) jsonObject.get("conditions");
            Iterator<JSONObject> iterator = conditionsList.iterator();
            while (iterator.hasNext()) {
                JSONObject condition = iterator.next();
                String pageContent = condition.get("content").toString();
                String conditionUrl = condition.get("url").toString();
                String conditionName = condition.get("name").toString();

                HealthConditionPage conditionPage = new HealthConditionPage(conditionUrl, conditionName, pageContent);
                conditionPages.add(conditionPage);
            }
        } catch (ParseException | IOException e) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, e);
        }
        return conditionPages;
    }
}
