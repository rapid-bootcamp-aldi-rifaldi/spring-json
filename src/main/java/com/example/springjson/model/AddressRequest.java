package com.example.springjson.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String name;
    private String address;
    private String village;
    private String district;
    private String city;
    private String province;
}
