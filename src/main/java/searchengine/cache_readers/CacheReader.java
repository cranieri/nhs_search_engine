package searchengine.cache_readers;

import org.springframework.stereotype.Component;
import searchengine.HealthConditionPage;

import java.util.ArrayList;

@Component
public interface CacheReader {
    ArrayList<HealthConditionPage> readCache();
}
