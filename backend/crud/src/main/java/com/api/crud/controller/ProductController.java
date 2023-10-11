package com.api.crud.controller;


import com.api.crud.dto.Message;
import com.api.crud.dto.ProductDto;
import com.api.crud.entity.Product;
import com.api.crud.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins ="http://localhost:4200") //invito al puerto de angular
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<List<Product>> list() {

        List<Product> list = productService.list();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detailid/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") int id) {

        if (!productService.existsById(id))

            return new ResponseEntity(new Message("no existe"), HttpStatus.NOT_FOUND);

            Product product = productService.getOne(id).get();

            return new ResponseEntity<Product>(product, HttpStatus.OK);
        }

    @GetMapping("/detailname/{name}")
    public ResponseEntity<Product> getByName(@PathVariable("name") String name) {

        if (!productService.existsByName(name))

            return new ResponseEntity(new Message("no existe"), HttpStatus.NOT_FOUND);

        Product product = productService.getByName(name).get();

        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductDto productDto) {

        if(StringUtils.isBlank(productDto.getName()))
            return new ResponseEntity(new Message("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if(productDto.getPrice()<0 || productDto.getPrice()==null)
            return new ResponseEntity(new Message("El precio debe ser mayor a 0"), HttpStatus.BAD_REQUEST);

        if(productService.existsByName(productDto.getName()))
            return new ResponseEntity(new Message("El producto ya existe"), HttpStatus.BAD_REQUEST);

        Product product=new Product(productDto.getName(), productDto.getPrice());
        productService.save(product);
        return  new ResponseEntity(new Message("Producto creado"), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id,@RequestBody ProductDto productDto) {

        if (!productService.existsById(id))
            return new ResponseEntity(new Message("no existe"), HttpStatus.NOT_FOUND);

        if(productService.existsByName(productDto.getName()) && productService.getByName(productDto.getName()).get().getId() != id)
            return new ResponseEntity(new Message("El nombre ya existe"), HttpStatus.BAD_REQUEST);

        if(StringUtils.isBlank(productDto.getName()))
            return new ResponseEntity(new Message("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        if(productDto.getPrice()<0)
            return new ResponseEntity(new Message("El precio debe ser mayor a 0"), HttpStatus.BAD_REQUEST);

        Product product=productService.getOne(id).get();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        productService.save(product);
        return  new ResponseEntity(new Message("Producto actualizado"), HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {

        if (!productService.existsById(id))
            return new ResponseEntity(new Message("no existe"), HttpStatus.NOT_FOUND);

        productService.delete(id);

        return  new ResponseEntity(new Message("Producto eliminado"), HttpStatus.OK);

    }

}