package repository;

import domain.User;
import domain.exceptions.NotFoundException;

public interface UserRepository extends Repository<Integer, User> {

    /**
     * Sets a user's status
     *
     * @param id     the user's id
     * @param status the new user status
     * @return the user with the updated status
     */
    User setUserStatus(Integer id, String status) throws NotFoundException;

    /**
     * Gets a user by username
     *
     * @param username the username of the searched user
     * @return the searched user
     * @throws NotFoundException if user not found in repo
     */
    User getOne(String username) throws NotFoundException;
}
