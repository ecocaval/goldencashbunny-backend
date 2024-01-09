package com.goldencashbunny.demo.core.data.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class AuthenticationResponse {

    private String accessToken;

}
