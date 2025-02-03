package com.qeema.aps.payment.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PreparePaymentRequestDto {
    @JsonProperty("card_id")
    private Integer cardId;

}
