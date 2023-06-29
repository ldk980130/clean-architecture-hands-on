package com.practice.cleanarichitecturehandson.buckpal.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Activity {

    private final ActivityId id;
    private final AccountId ownerAccountId;
    private final AccountId sourceAccountId;
    private final AccountId targetAccountId;
    private final LocalDateTime timestamp;
    private final Money money;

    @Builder
    public Activity(AccountId ownerAccountId,
                    AccountId sourceAccountId,
                    AccountId targetAccountId,
                    LocalDateTime timestamp,
                    Money money
    ) {
        this(null, ownerAccountId, sourceAccountId, targetAccountId, timestamp, money);
    }
}
