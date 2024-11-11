package service;

import java.util.List;

public interface IService <T, ID>{
    List<T> findAll();
    T findById(ID id);
    void add(T entity);
    void update(T entity);
    void delete(ID id);
}
