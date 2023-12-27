package Repository;

import Domain.Entity;

import java.util.Collection;

public interface IRepo<T extends Entity> extends Iterable<T> {
    void add(T Entity) throws Exception;
    void update(T newEntity) throws Exception;
    void deleteById(int ID) throws Exception;
    T getById(int ID);
    Collection<T> getAll();
}
