package searchengine;

/**
 * Created by cosimoranieri on 26/08/2016.
 */
public class HealthConditionPage {
    private String url;

    private String conditionName;
    private String content;

    public HealthConditionPage(String url, String conditionName, String content) {
        this.url = url;
        this.conditionName = conditionName;
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }
}
