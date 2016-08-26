package searchengine.services;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import searchengine.HealthConditionPage;
import searchengine.NLPHelper;
import searchengine.cache_readers.CacheReader;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)

@SpringBootTest
public class SearchEngineTest {

    @Test
    public void search() throws Exception {
        CacheReader cacheReader = mock(CacheReader.class);
        ArrayList<HealthConditionPage> conditionUrls = new ArrayList<>();
        HealthConditionPage conditionPage = new HealthConditionPage("www.example.com", "name condition", "Treatments for headaches");
        conditionUrls.add(conditionPage);
        when(cacheReader.readCache()).thenReturn(conditionUrls);

        SearchEngine searchEngine = new SearchEngine(cacheReader, new NLPHelper());
        Assert.assertEquals("www.example.com", searchEngine.search("headaches").get(0));
    }
}