package com.goldencashbunny.demo.core.usecases.implementations;

import com.goldencashbunny.demo.clients.CepClient;
import com.goldencashbunny.demo.core.data.dtos.CreateWorkSpaceDto;
import com.goldencashbunny.demo.core.data.dtos.ViaCepDto;
import com.goldencashbunny.demo.core.data.requests.CreateCustomerRequest;
import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.core.data.requests.UpdateWorkSpaceRequest;
import com.goldencashbunny.demo.core.messages.ErrorMessages;
import com.goldencashbunny.demo.core.usecases.AccountUseCase;
import com.goldencashbunny.demo.core.usecases.CepUseCase;
import com.goldencashbunny.demo.core.usecases.WorkSpaceUseCase;
import com.goldencashbunny.demo.core.utils.AsciiUtils;
import com.goldencashbunny.demo.core.utils.UuidUtils;
import com.goldencashbunny.demo.presentation.entities.Customer;
import com.goldencashbunny.demo.presentation.entities.Workspace;
import com.goldencashbunny.demo.presentation.exceptions.CepNotFoundException;
import com.goldencashbunny.demo.presentation.exceptions.WorkSpaceNotFoundException;
import com.goldencashbunny.demo.presentation.repositories.CustomerRepository;
import com.goldencashbunny.demo.presentation.repositories.WorkspaceRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CepUseCaseImpl implements CepUseCase {

    private final CepClient cepClient;

    @Autowired
    public CepUseCaseImpl(CepClient cepClient) {
        this.cepClient = cepClient;
    }

    @Override
    public ViaCepDto getAddressByZipCode(String zipCode) {
        try {
            return cepClient.getAddressByZipCode(AsciiUtils.cleanString(zipCode));
        } catch (FeignException ex) {
            throw new CepNotFoundException(zipCode);
        }
    }

}
