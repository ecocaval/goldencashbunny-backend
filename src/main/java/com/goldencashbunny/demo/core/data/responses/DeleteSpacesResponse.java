package com.goldencashbunny.demo.core.data.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DeleteSpacesResponse {

    private List<DeleteSpaceErrorMessage> errorMessages = new ArrayList<>();

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DeleteSpaceErrorMessage {

        private String spaceId;
        private String details;
    }
}
