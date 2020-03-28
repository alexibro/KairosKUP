package es.kairosds.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private Environment env;

    private String swearingApiHost = "http://localhost";
    private String swearingApiPort = "8080";

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

    public boolean checkSwearingWords(String comment) {
        RestTemplate restTemplate = new RestTemplate();
        this.initEnvironment();
        String swearingApiUrl = swearingApiHost + ":" + swearingApiPort + "/check";

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

    private void initEnvironment() {
        String host = env.getProperty("DOCKER_INTERNAL_HOST");
        if (host != null && !host.equals("")) {
            swearingApiHost = host;
        }

        String port = env.getProperty("SWEARING_API_PORT");
        if (port != null && !port.equals("")) {
            swearingApiPort = port;
        }
    }
}
