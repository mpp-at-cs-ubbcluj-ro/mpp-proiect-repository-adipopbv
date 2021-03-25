package pr1Java.persistence;

import pr1Java.model.User;
import pr1Java.model.exceptions.NotFoundException;

public interface UserRepository extends Repository<String, User> {

    /**
     * Gets a user by username
     *
     * @param username the username of the searched user
     * @return the searched user
     * @throws NotFoundException if user not found in repo
     */
    User getOne(String username) throws NotFoundException;
}
