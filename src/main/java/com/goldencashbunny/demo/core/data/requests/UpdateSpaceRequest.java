package com.goldencashbunny.demo.core.data.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateSpaceRequest {

    private String name;

    private Boolean isFavorite;
}
