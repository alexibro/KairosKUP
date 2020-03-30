package es.kairosds.persistance;

import es.kairosds.comment.Comment;
import es.kairosds.comment.CommentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;

    private static long notFoundId = 100;

    private long lastCommentInsertedId;

    @Before
    public void initialize() {
        Comment comment1 = new Comment("Test1", "Test1");
        Comment comment2 = new Comment("Test2", "Test2");

        commentRepository.save(comment1);
        Comment comment = commentRepository.save(comment2);
        lastCommentInsertedId = comment.getId();
    }

    @After
    public void finalize() {
        commentRepository.deleteAll();
    }


    @Test
    public void shouldFindAllComments() {
        Iterable<Comment> comments = commentRepository.findAll();

        assertThat(comments).hasSize(2);
    }

    @Test
    public void shouldFindCommentById() {
        Optional<Comment> comment = commentRepository.findById(lastCommentInsertedId);

        assertThat(comment.isPresent()).isTrue();
        assertThat(comment.get().getAuthor()).isEqualTo("Test2");
    }

    @Test
    public void shouldNotFindCommentById() {
        Optional<Comment> comment = commentRepository.findById(notFoundId);

        assertThat(comment.isPresent()).isFalse();
    }

    @Test
    public void shouldExistsCommentById() {
        Boolean exists = commentRepository.existsById(lastCommentInsertedId);

        assertThat(exists).isTrue();
    }

    @Test
    public void shouldNotExistsCommentById() {
        Boolean exists  = commentRepository.existsById(notFoundId);

        assertThat(exists).isFalse();
    }

    @Test
    public void shouldSaveComment() {
        Comment comment = new Comment("Test3", "Test3");

        Comment commentInserted = commentRepository.save(comment);
        assertThat(commentInserted).isNotNull();
        assertThat(commentInserted.getId()).isGreaterThan(0L);
    }

    @Test
    public void shouldDeleteCommentById() {
        commentRepository.deleteById(lastCommentInsertedId);

        Iterable<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(1);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldNotDeleteCommentById() {
        commentRepository.deleteById(notFoundId);
    }

}
