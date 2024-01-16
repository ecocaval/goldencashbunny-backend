package com.goldencashbunny.demo.core.data.dtos;

import com.goldencashbunny.demo.core.data.requests.CreateWorkSpaceRequest;
import com.goldencashbunny.demo.presentation.entities.Account;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class CreateWorkSpaceDto {

    private String companyName;

    private String socialCompanyName;

    private Account account;

    public static CreateWorkSpaceDto fromCreateWorkSpaceRequestAndAccount(
            CreateWorkSpaceRequest createWorkSpaceRequest, Account account
    ) {
        return CreateWorkSpaceDto.builder()
                .companyName(createWorkSpaceRequest.getCompanyName())
                .socialCompanyName(createWorkSpaceRequest.getSocialCompanyName())
                .account(account)
                .build();
    }
}
