# nhs_search_engine

The app is basic REST service built with Spring Boot (http://projects.spring.io/spring-boot/) with 2 functionalities: scraping content from http://nhs.uk website and provide a search engine to search across that content. The OpenNLP library (https://opennlp.apache.org/) has also been used to enhance the search. Cached files are already provided in the cache folder.

# Below are the endpoints:

```
GET /scrape
```
The endpoint above triggers few futures that will scrape the condition pages from the NHS website and write in json format into files under the cache folder.

```
GET /search?keywords=<keywords>
```
The endpoint above allows to make a search across all the condition pages, whose content has been previously cached. The response payload will be a json array containing the urls related to pages with relevant content based on the search keywords. Look below for an example of response:

```
["http://www.nhs.uk/conditions/achalasia","http://www.nhs.uk/conditions/Acoustic-neuroma"]
```

# Run the application

```
mvn package && java -jar target/search-engine-0.1.0.jar
```

# Area of improvement

- Better Caching system
- More tests coverage
- Several improvements can be done around the search engine, which at the current version only searches for the nouns included in the keywords provided by the client 
