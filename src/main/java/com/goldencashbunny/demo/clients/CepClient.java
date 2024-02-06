package com.goldencashbunny.demo.clients;

import com.goldencashbunny.demo.core.data.dtos.ViaCepDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cep", url = "https://viacep.com.br/ws")
public interface CepClient {

    @GetMapping(value = "{zipCode}/json", consumes = "application/json")
    ViaCepDto getAddressByZipCode(@PathVariable("zipCode") String zipCode);
}
