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
public class DeleteWorkSpacesResponse {

    private List<DeleteWorkSpaceErrorMessage> errorMessages = new ArrayList<>();

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class DeleteWorkSpaceErrorMessage {

        private String workSpaceId;
        private String details;
    }
}
