package TienLe.DemoValidation.service;

import TienLe.DemoValidation.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private List<Product> listProduct = new ArrayList<>();

    public List<Product> getAll() {
        return listProduct;
    }

    public Product get(int id) {
        return listProduct.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void add(Product newProduct) {
        int maxId = listProduct.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0);
        newProduct.setId(maxId + 1);
        listProduct.add(newProduct);
    }

    public void updateImage(Product product, MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                String uploadDir = "./static/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String newFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(newFileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                product.setImage(newFileName);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }

    public void update(Product editProduct) {
        Product existingProduct = get(editProduct.getId());
        if (existingProduct != null) {
            existingProduct.setName(editProduct.getName());
            existingProduct.setPrice(editProduct.getPrice());
            existingProduct.setImage(editProduct.getImage());
        }
    }

    public List<Product> findByNameContaining(String keyword) {
        List<Product> result = new ArrayList<>();
        for (Product product : listProduct) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }
}
