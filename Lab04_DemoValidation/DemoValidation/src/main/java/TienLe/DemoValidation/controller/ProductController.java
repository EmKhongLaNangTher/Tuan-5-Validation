package TienLe.DemoValidation.controller;

import jakarta.validation.Valid;
import javassist.NotFoundException;
import TienLe.DemoValidation.model.Product;
import TienLe.DemoValidation.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        return "product/create";
    }

    @PostMapping("/create")
    public String create(@Valid Product newProduct,
                         BindingResult result,
                         @RequestParam MultipartFile imageProduct,
                         Model model)  {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            return "product/create";
        }
        productService.updateImage(newProduct, imageProduct);
        productService.add(newProduct);
        return "redirect:/products";
    }

    @GetMapping()
    public String index(Model model, @RequestParam(required = false) String keyword) {
        if (keyword != null) {
            model.addAttribute("listproduct", productService.findByNameContaining(keyword));
        } else {
            model.addAttribute("listproduct", productService.getAll());
        }
        return "product/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) throws NotFoundException {
        Product find = productService.get(id);
        if (find == null)
            throw new NotFoundException("Product not found with ID: " + id);
        model.addAttribute("product", find);
        return "product/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Product editProduct,
                       BindingResult result,
                       @RequestParam MultipartFile imageProduct,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            return "product/edit"; // Return to the edit form with error messages
        }
        productService.updateImage(editProduct, imageProduct);
        productService.update(editProduct);
        return "redirect:/products"; // Redirect to the products page after successful update
    }
}
