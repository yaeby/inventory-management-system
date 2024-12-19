package service;

import model.Category;
import repository.IRepository;

import java.util.List;

public class CategoryService extends Service<Category, Long>{

    public CategoryService(IRepository<Category, Long> repository) {
        super(repository);
    }

    @Override
    public List<Category> findAll() {
        return super.findAll();
    }

    @Override
    public Category findById(Long aLong) {
        return super.findById(aLong);
    }

    @Override
    public void add(Category entity) {
        super.add(entity);
    }

    @Override
    public void update(Category entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long aLong) {
        super.delete(aLong);
    }
}
