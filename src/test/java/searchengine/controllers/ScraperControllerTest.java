package searchengine.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import searchengine.controllers.ScraperController;
import searchengine.services.NHSScraperService;

@RunWith(SpringJUnit4ClassRunner.class)

@SpringBootTest
public class ScraperControllerTest {

    private MockMvc mvc;

    @Mock
    private NHSScraperService nhsScraperService;

    @InjectMocks
    ScraperController controllerUnderTest;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
    }

    @Test
    public void getScraper() throws Exception {
        when(nhsScraperService.scrapeConditions(any())).thenReturn(new AsyncResult<>(true));

        mvc.perform(MockMvcRequestBuilders.get("/scrape").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}