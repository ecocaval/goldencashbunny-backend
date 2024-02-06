package com.goldencashbunny.demo.core.usecases;

import com.goldencashbunny.demo.core.data.dtos.ViaCepDto;

public interface CepUseCase {

    ViaCepDto getAddressByZipCode(String zipCode);

}
