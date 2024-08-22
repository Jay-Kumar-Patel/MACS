package com.cloud.A2;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Data
public class ProductArray {
    List<ProductEntity> products;
}
