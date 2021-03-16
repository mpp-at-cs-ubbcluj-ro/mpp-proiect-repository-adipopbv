package repository;

import domain.Entity;
import domain.exceptions.DuplicateException;
import domain.exceptions.NotFoundException;

public interface Repository<ID, E extends Entity<ID>> {

    /**
     * Gets all entities
     * @return iterable with all repo entities
     */
    Iterable<E> getAll();

    /**
     * Gets an entity
     * @param id the id of the searched entity
     * @return the searched entity
     */
    E getOne(ID id);

    /**
     * Adds an entity to the repo
     * @param entity the entity to be added
     * @return the added entity
     * @throws DuplicateException if entity already in repo
     */
    E add(E entity) throws DuplicateException;

    /**
     * Removes an entity from the repo
     * @param id the id of the entity to be removed
     * @return the removed entity
     * @throws NotFoundException if entity not found in repo
     */
    E remove(ID id) throws NotFoundException;

    /**
     * Modifies one entity to another
     * @param id the id of the entity to be modified
     * @param newEntity the new entity that replaces the old one
     * @return the old entity
     * @throws DuplicateException if the new entity is already in the repo
     * @throws NotFoundException if the old entity is not found in the repo
     */
    E modify(ID id, E newEntity) throws DuplicateException, NotFoundException;

    /**
     * Gets the repo as a string
     * @return a string with the entities of the repo
     */
    @Override
    String toString();
}
