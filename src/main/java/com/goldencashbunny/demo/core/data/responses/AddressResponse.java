package com.goldencashbunny.demo.core.data.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goldencashbunny.demo.core.data.dtos.ViaCepDto;
import com.goldencashbunny.demo.core.data.enums.BrazilState;
import com.goldencashbunny.demo.core.utils.AsciiUtils;
import com.goldencashbunny.demo.presentation.entities.Address;
import com.goldencashbunny.demo.presentation.entities.Space;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse {

    private UUID id;

    private String zipCode;

    private String name;

    private String complement;

    private String neighborhood;

    private String city;

    private BrazilState state;

    private String ibgeCode;

    public static AddressResponse fromViaCepDto(ViaCepDto viaCepDto) {
        return AddressResponse.builder()
                .zipCode(AsciiUtils.cleanString(viaCepDto.getCep()))
                .name(viaCepDto.getLogradouro())
                .complement(viaCepDto.getComplemento())
                .neighborhood(viaCepDto.getBairro())
                .city(viaCepDto.getLocalidade())
                .state(BrazilState.getFromString(viaCepDto.getUf()))
                .ibgeCode(viaCepDto.getIbge())
                .build();
    }

    public static AddressResponse fromAddress(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .zipCode(address.getZipCode())
                .name(address.getName())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .ibgeCode(address.getIbgeCode())
                .build();
    }
}
