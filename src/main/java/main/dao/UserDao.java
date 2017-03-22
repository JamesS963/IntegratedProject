package main.dao;

import main.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Lewis on 26/02/2017.
 */
@Repository
public interface UserDao extends CrudRepository<User, Long> {

    /**
     * Find a User by their username.
     * @param username the username.
     * @return the specified User if found or null if not found.
     */
    public User findByUsername(String username);
    public User findById(long id);
}


