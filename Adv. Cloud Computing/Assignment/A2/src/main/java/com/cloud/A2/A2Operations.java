package com.cloud.A2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class A2Operations {

    @Autowired
    Repository repository;

    @PostMapping("/store-products")
    public ResponseEntity<ResponseSuccess> save(@RequestBody ProductArray productArray )
    {
        repository.deleteAll();

        repository.saveAll(productArray.getProducts());

       ResponseSuccess responseSuccess = new ResponseSuccess();
       responseSuccess.setMessage("Success.");

       return new ResponseEntity<>(responseSuccess, HttpStatus.CREATED);
    }

    @GetMapping("/list-products")
    public ResponseEntity<Map<String, List<ProductDTO>>> get(){

        List<ProductEntity> response =  repository.findAll();

        List<ProductDTO> dtos = new ArrayList<>();

        for (ProductEntity entity : response){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(entity.getName());
            productDTO.setPrice(entity.getPrice());
            productDTO.setAvailability(entity.isAvailability());

            dtos.add(productDTO);
        }

        Map<String, List<ProductDTO>> map = new HashMap<>();
        map.put("products", dtos);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
