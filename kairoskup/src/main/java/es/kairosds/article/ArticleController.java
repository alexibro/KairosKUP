package es.kairosds.article;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles")
@Produces({"applicaction/json", "application/xml"})
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("")
    public ResponseEntity<List<Article>> getArticles() {
        return new ResponseEntity<>(articleService.findAll(), HttpStatus.OK);
    }

    @GET
    @Path("/{id}")
    @ApiOperation(
            value = "Returns an article"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable long id) {
        Optional<Article> article = articleService.findOne(id);

        return article.map(value
                -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Article createArticle(@RequestBody Article article) {
        return articleService.save(article);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody Article updatedArticle) {
        if (articleService.exists(id)) {

            updatedArticle.setId(id);
            articleService.save(updatedArticle);

            return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable long id) {
        Optional<Article> article = articleService.findOne(id);

        if(article.isPresent()) {
            articleService.delete(id);
            return new ResponseEntity<>(article.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
