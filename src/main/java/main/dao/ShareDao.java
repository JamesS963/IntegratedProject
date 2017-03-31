package main.dao;

import main.models.Share;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dean on 30/03/2017.
 */
public interface ShareDao extends CrudRepository<Share, Long> {

    public Share findByShareId(long Id);

    public Share findByDocId(long docId);

    public Share findByAuthor(long authorId);

    public Share findByDistributee(long distribId);

    public Iterable<Share> findAllByDistributee(long distribId);

    public Iterable<Share> findAllByAuthor (long authorId);
}
