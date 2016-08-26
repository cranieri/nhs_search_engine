package searchengine.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import searchengine.controllers.SearchController;
import searchengine.services.SearchEngine;

import java.io.*;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)

@SpringBootTest
public class SearchControllerTest {

    private MockMvc mvc;

    @Mock
    private SearchEngine searchEngine;

    @InjectMocks
    SearchController controllerUnderTest;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
    }

    @Test
    public void searchKeywords() throws Exception {
        ArrayList<String> conditionUrls = new ArrayList<>();
        conditionUrls.add("www.example.com");
        when(searchEngine.search("keyword")).thenReturn(conditionUrls);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/search?keywords=keyword").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        Assert.assertEquals(response, "[\"www.example.com\"]");

    }
}