package searchengine;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class NLPHelper {

    private static final String[] VALID_POS = new String[] {"_NN", "_NNS", "_NNP", "_NNPS"};

    private POSTaggerME tagger;

    public NLPHelper() {
        POSModel model = new POSModelLoader()
                .load(new File("src/main/resources/en-pos-maxent.bin"));
        tagger = new POSTaggerME(model);

    }

    public String taggedSentence(String sentence) throws IOException {
        String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                .tokenize(sentence);
        String[] tags = tagger.tag(whitespaceTokenizerLine);

        POSSample taggedSentence = new POSSample(whitespaceTokenizerLine, tags);
        return taggedSentence.toString();
    }

    public Set<String> getNouns(String sentence) throws IOException {
        Set<String> nouns = new HashSet<String>();
        for (String taggedWord : taggedSentence(sentence).split(" ")) {
            for(String posTag: VALID_POS) {
                if (taggedWord.contains(posTag)) {
                    nouns.add(taggedWord.split("_")[0]);
                }
            }
        }
        return nouns;
    }
}
