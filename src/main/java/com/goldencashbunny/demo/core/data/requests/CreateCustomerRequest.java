package com.goldencashbunny.demo.core.data.requests;

import com.goldencashbunny.demo.core.data.enums.BrazilState;
import com.goldencashbunny.demo.presentation.entities.Address;
import com.goldencashbunny.demo.presentation.entities.Customer;
import com.goldencashbunny.demo.presentation.entities.CustomerAdditionalEmail;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateCustomerRequest {

    @NotBlank(message = "O nome do cliente precisa ser informado.")
    private String firstName;

    private String lastName;

    private String cpf;

    private String cnpj;

    private String companyName;

    private String socialCompanyName;

    private String email;

    private String phone;

    private CreateAddressRequest Address;

    private List<String> customerAdditionalEmails;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class CreateAddressRequest {

        private String zipCode;

        private String name;

        private String number;

        private String neighborhood;

        private String complement;

        private String city;

        private BrazilState state;

        private String ibgeCode;
    }
}
