package com.ford.ces.api.domain.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceivableAccountNumber {

    @NotBlank(message = "Receivable Account Number should not be blank -{NotBlank.accountNumber}")
    @Size(min = 8, max = 15, message = "Receivable Account Number size must be between 8 and 15 -{Size.accountNumber}")
    @Pattern(regexp = "^(0|[1-9][0-9]*)$|^\\s*$", message = "Expect only Numeric values as input -{Pattern.accountNumber}")
    private String receivableAccountNumber;

    @NotBlank(message = "Client Id should not be blank -{NotBlank.clientId}")
    @Size(min = 8, max = 36, message = "Client Id size must be between 8 and 36 -{Size.clientID}")
    @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "Accept Only Alpha Numeric values as input for Client Id")
    private String clientId;

}