package es.kairosds.comment;

import es.kairosds.article.Article;
import es.kairosds.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @GetMapping("")
    public ResponseEntity<List<Comment>> getComments(@PathVariable long articleId) {
        if (articleService.exists(articleId)) {
            return new ResponseEntity<>(commentService.findAll(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable long articleId, @PathVariable long id) {
        if (articleService.exists(articleId)) {
            Optional<Comment> comment = commentService.findOne(id);
            return comment.map(value
                    -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("")
    public ResponseEntity<Comment> createComment(@PathVariable long articleId, @RequestBody Comment comment) {

        Optional<Article> article = articleService.findOne(articleId);
        if (article.isPresent()) {

            try{
                if (!commentService.checkSwearingWords(comment.getMessage())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } catch (NullPointerException e) {
                return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
            }

            article.get().addComment(comment);

            // Necessary to get comment updated with his Id
            comment = commentService.save(comment);

            articleService.save(article.get());
            return new ResponseEntity<>(comment, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable long articleId,
                                                 @PathVariable long id,
                                                 @RequestBody Comment updatedComment) {

        Optional<Article> article = articleService.findOne(articleId);
        Optional<Comment> oldComment = commentService.findOne(id);

        if (article.isPresent() && oldComment.isPresent()) {

            try{
                if (!commentService.checkSwearingWords(updatedComment.getMessage())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } catch (NullPointerException e) {
                return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
            }

            updatedComment.setId(id);
            article.get().updateComments(oldComment.get(), updatedComment);
            articleService.save(article.get());

            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable long articleId, @PathVariable long id) {
        if (articleService.exists(articleId)) {
            Optional<Comment> comment = commentService.findOne(id);
            if (comment.isPresent()) {
                commentService.delete(id);
                return new ResponseEntity<>(comment.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
