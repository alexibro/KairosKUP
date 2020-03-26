package es.kairosds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WordController {

    @Autowired
    private WordService service;

    private static final Logger log = LoggerFactory.getLogger(WordController.class);

    private static class Request {
        String comment;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    private static class Response {
        String response;

        Response(String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

    @PostMapping("/check")
    public ResponseEntity<Response> getArticles(@RequestBody Request comment) {
        log.info(comment.comment);
        for (String word : comment.comment.split("\\s+")) {
            if (service.contains(word)) {
                return new ResponseEntity<>(new Response("invalid"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new Response("valid"), HttpStatus.OK);
    }
}