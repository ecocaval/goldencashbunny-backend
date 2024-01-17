package com.goldencashbunny.demo.core.data.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSpaceRequest {

    @NotBlank(message = "O nome do espaço não pode ser vazio")
    private String name;
}
