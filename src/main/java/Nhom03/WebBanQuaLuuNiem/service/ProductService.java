package Nhom03.WebBanQuaLuuNiem.service;

import Nhom03.WebBanQuaLuuNiem.model.Product;
import Nhom03.WebBanQuaLuuNiem.model.ProductImages;
import Nhom03.WebBanQuaLuuNiem.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    private final ProductImageService productImageService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(@NotNull Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new IllegalStateException("Sản phẩm với ID " + product.getId() + " không tồn tại."));
        existingProduct.setName(product.getName());
        existingProduct.setImportPrice(product.getImportPrice());
        existingProduct.setSalesPrice(product.getSalesPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        List<ProductImages> oldImages = existingProduct.getImages();
        for (ProductImages oldImage : oldImages) {
            productImageService.deleteProductImage(oldImage);
        }
        existingProduct.getImages().clear();
        List<ProductImages> newImages = product.getImages();
        for (ProductImages newImage : newImages) {
            newImage.setProduct(existingProduct);
            productImageService.addProductImage(newImage);
            existingProduct.getImages().add(newImage);
        }
        productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    public List<Product> searchDanhMucs(String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return productRepository.findAll().stream()
            .filter(danhMuc -> danhMuc.getName().toLowerCase().contains(lowerCaseKeyword))
            .collect(Collectors.toList());
    }

    public List<String> GoiY(String term) {
        String lowerCaseTerm = term.toLowerCase();
        return productRepository.findAll().stream()
            .map(Product::getName)
            .filter(name -> name.toLowerCase().contains(lowerCaseTerm))
            .distinct()
            .collect(Collectors.toList());
    }
}