package dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {


    private String prod_id;
    private String brand;
    private String prod_name;
    private int prod_price;
    private int prod_code;
    private String prod_category;
    private BigDecimal prod_size;


}