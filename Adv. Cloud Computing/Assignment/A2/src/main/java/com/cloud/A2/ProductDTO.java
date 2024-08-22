package com.cloud.A2;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Data
public class ProductDTO {

    private String name;
    private String price;
    private boolean availability;
}
