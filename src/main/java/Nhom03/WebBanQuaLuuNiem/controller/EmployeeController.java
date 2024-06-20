package Nhom03.WebBanQuaLuuNiem.controller;

import Nhom03.WebBanQuaLuuNiem.model.Employee;
import Nhom03.WebBanQuaLuuNiem.model.Product;
import Nhom03.WebBanQuaLuuNiem.service.CategoryService;
import Nhom03.WebBanQuaLuuNiem.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping
    public String showEmployeeList(Model model) {
        model.addAttribute("products", employeeService.getAllProducts());
        return "/products/product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/add-product";
    }
    @PostMapping("/add")
    public String addEmployee(@Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }
        employeeService.addProduct(product);
        return "redirect:/products";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = employeeService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/update-product";
    }
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            product.setId(id);
            return "/products/update-product";
        }
        employeeService.updateProduct(product);
        return "redirect:/products";
    }
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteProductById(id);
        return "redirect:/products";
    }
}
