package es.kairosds.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    private String swearingApiUrl = "http://localhost:8000/check";

    private static class Request {
        String comment;

        public Request(String comment) {
            this.comment = comment;
        }

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

    public boolean checkSwearingWords(String comment) throws NullPointerException {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Request> request = new HttpEntity<>(new Request(comment));
        String response = restTemplate.postForObject(swearingApiUrl, request, String.class);


        if (response != null) {
            return !response.contains("invalid");
        } else {
            throw new NullPointerException();
        }
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> findOne(long id) {
        return commentRepository.findById(id);
    }

    public boolean exists(long id) {
        return commentRepository.existsById(id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(long id) {
        commentRepository.deleteById(id);
    }
}
