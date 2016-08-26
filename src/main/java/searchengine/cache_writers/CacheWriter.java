package searchengine.cache_writers;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface CacheWriter {
    void writeCache(String conditionIndex, Map<String, String> urls);
}
