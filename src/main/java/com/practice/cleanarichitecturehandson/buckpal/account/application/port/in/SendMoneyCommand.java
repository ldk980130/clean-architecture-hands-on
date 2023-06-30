package com.practice.cleanarichitecturehandson.buckpal.account.application.port.in;

import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Money;
import jakarta.validation.constraints.NotNull;

public record SendMoneyCommand(
        @NotNull
        AccountId sourceAccountId,

        @NotNull
        AccountId targetAccountId,

        @NotNull
        Money money
) {
}
