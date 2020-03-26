package es.kairosds;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Service
public class WordService {

    private HashSet<String> swearingWords;

    @PostConstruct
    public void init() {
        swearingWords = new HashSet<>();
        swearingWords.add("lechuguino");
    }

    public boolean contains(String word) {
        return swearingWords.contains(word);
    }

}
