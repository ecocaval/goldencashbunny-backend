package com.goldencashbunny.demo.core.data.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateWorkSpaceRequest {

    private String companyName;

    private String socialCompanyName;

    private Boolean isFavorite;
}
