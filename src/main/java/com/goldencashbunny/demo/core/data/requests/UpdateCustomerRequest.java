package com.goldencashbunny.demo.core.data.requests;

import com.goldencashbunny.demo.core.data.enums.BrazilState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateCustomerRequest {

    private String firstName;

    private String lastName;

    private String cpf;

    private String cnpj;

    private String companyName;

    private String socialCompanyName;

    private String email;

    private String phone;

    private UpdateAddressRequest Address;

    private List<String> additionalEmailsToAdd;

    private List<String> additionalEmailsToRemove;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UpdateAddressRequest {

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
