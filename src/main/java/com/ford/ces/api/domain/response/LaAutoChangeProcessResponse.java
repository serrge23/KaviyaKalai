package com.ford.ces.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LaAutoChangeProcessResponse {

    @JsonProperty("WIARBCPOperationResponse")
    @NotNull
    private WIARBCPOperationResponse wiarbcpOperationResponse;
}