package sit.int221.sas.sit_announcement_system_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;
import sit.int221.sas.sit_announcement_system_backend.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        try {
            return categoryRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public Category addCategory(Category category) {
        try {
            return categoryRepository.saveAndFlush(category);
        } catch (RuntimeException e) {
            throw new RuntimeException();
        }
    }

    public void deleteCategory(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
