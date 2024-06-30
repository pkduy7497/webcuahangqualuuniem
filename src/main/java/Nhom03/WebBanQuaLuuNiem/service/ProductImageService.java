package Nhom03.WebBanQuaLuuNiem.service;

import Nhom03.WebBanQuaLuuNiem.model.ProductImages;
import Nhom03.WebBanQuaLuuNiem.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public void deleteProductImage(ProductImages productImage) {
        productImageRepository.delete(productImage);
    }

    public List<ProductImages> getAllProductImages() {
        return productImageRepository.findAll();
    }

    public void addProductImage(ProductImages productImages) {
        productImageRepository.save(productImages);
    }
}