package Nhom03.WebBanQuaLuuNiem.controller;

import Nhom03.WebBanQuaLuuNiem.model.Product;
import Nhom03.WebBanQuaLuuNiem.model.ProductImages;
import Nhom03.WebBanQuaLuuNiem.service.CategoryService;
import Nhom03.WebBanQuaLuuNiem.service.ProductImageService;
import Nhom03.WebBanQuaLuuNiem.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result, @RequestParam("productImages") MultipartFile[] imageList) {
        if (result.hasErrors()) {
            return "products/add-product";
        }
        productService.addProduct(product);
        for (MultipartFile image : imageList) {
            if (!image.isEmpty()) {
                try {
                    String imageUrl = saveImageStatic(image);
                    ProductImages productImage = new ProductImages();
                    productImage.setImagePath("/images/" + imageUrl);
                    productImage.setProduct(product);
                    product.getImages().add(productImage);
                    productImageService.addProductImage(productImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/products";
    }

    private String saveImageStatic(MultipartFile image) throws IOException {
        Path dirImages = Paths.get("target/classes/static/images");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }
        String fileName = "";
        if(image.getOriginalFilename() != null) {
            fileName = StringUtils.cleanPath(image.getOriginalFilename());
        }
        String newFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path filePath = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product, BindingResult result, @RequestParam("productImages") MultipartFile[] productImages) {
        if (result.hasErrors()) {
            return "products/update-product";
        }
        try {
            List<ProductImages> images = new ArrayList<>();
            for (MultipartFile file : productImages) {
                if (!file.isEmpty()) {
                    String fileName = saveImageStatic(file);
                    ProductImages image = new ProductImages();
                    image.setImagePath("/images/" + fileName);
                    images.add(image);
                }
            }
            product.setImages(images);
            productService.updateProduct(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/list")
    public String HienThiDanhMuc(@RequestParam(name = "keyword", required = false) String keyword,Model model) {
        List<Product> danhmuc;
        if (keyword != null && !keyword.isEmpty()) {
            danhmuc = productService.searchDanhMucs(keyword);
        } else {
            danhmuc = productService.getAllProducts();
        }
        model.addAttribute("products", danhmuc);
        model.addAttribute("keyword", keyword);
        return "/products/product-list";
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocomplete(@RequestParam("term") String term) {
        List<String> suggestions = productService.GoiY(term);
        return ResponseEntity.ok(suggestions);
    }
}