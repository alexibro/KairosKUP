package es.kairosds.swearingapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SwearingApiMiddleware {

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

        HttpEntity<SwearingApiMiddleware.Request> request = new HttpEntity<>(new SwearingApiMiddleware.Request(comment));
        String response = restTemplate.postForObject(swearingApiUrl, request, String.class);


        if (response != null) {
            return !response.contains("invalid");
        } else {
            throw new NullPointerException();
        }
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
