package es.kairosds.comment;

import es.kairosds.swearingapi.SwearingApiMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SwearingApiMiddleware swearingApiMiddleware;

    public boolean checkSwearingWords(String comment) {
        return swearingApiMiddleware.checkSwearingWords(comment);
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
