package com.practice.cleanarichitecturehandson.buckpal.account.application.port.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SendMoneyCommand(
        @NotNull
        Long sourceAccountId,

        @NotNull
        Long targetAccountId,

        @Positive
        Long amount
) {
}
