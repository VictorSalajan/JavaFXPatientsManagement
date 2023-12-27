package Repository;

import Domain.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Repo<T extends Entity> implements IRepo<T> {
    ArrayList<T> entities = new ArrayList<>();

    @Override
    public void add(T entity) throws RepoException, IOException {
        if (entity == null) {
            throw new NullPointerException("Entity cannot be null!");
        }
        if (this.getById(entity.getID()) != null) {
            throw new DuplicateIdException("Adding failed! " + entity.getClass() + " with ID " + entity.getID() + " already exists!");
        }
        entities.add(entity);
    }

    @Override
    public T getById(int ID) {
        for (T entity : entities) {
            if (entity.getID() == ID)
                return entity;
        }
        return null;
    }

    /**
     * Returns a list of all entities
     * @return entities: ArrayList<T>
     */
    @Override
    public ArrayList<T> getAll() {
        return entities;
    }

    /**
     * Update an entity by ID
     * @param newEntity: Entity
     * @throws IdNotFoundException
     */
    @Override
    public void update(T newEntity) throws RepoException {
        if (this.getById(newEntity.getID()) == null)
            throw new IdNotFoundException("Update failed! No " + newEntity.getClass() + " with ID " + newEntity.getID() + " was found!");
        int index = entities.indexOf(newEntity);
        entities.set(index, newEntity);
    }

    @Override
    public void deleteById(int ID) throws RepoException {
        if (this.getById(ID) == null)
            throw new IdNotFoundException("Delete failed! No entity with ID " + ID + " was found!");
        entities.remove(this.getById(ID));
    }

    @Override
    public Iterator<T> iterator() {
        return entities.iterator();
    }
}
