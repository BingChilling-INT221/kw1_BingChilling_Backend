package sit.int221.sas.sit_announcement_system_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.sas.sit_announcement_system_backend.entity.Category;
import sit.int221.sas.sit_announcement_system_backend.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("api/categories")
@CrossOrigin(origins = "http://localhost:80")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("")
    private Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }
    @DeleteMapping("/id")
    private void  deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
    }
}
