package main.dao;

import main.models.Document;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Lewis on 23/02/2017.
 */
public interface DocumentDao extends CrudRepository<Document, Long> {

    public Document findById(long id);
    public Document findByTitle(String title);
}
