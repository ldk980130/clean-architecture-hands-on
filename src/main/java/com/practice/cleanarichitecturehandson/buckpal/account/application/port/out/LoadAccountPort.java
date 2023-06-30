package com.practice.cleanarichitecturehandson.buckpal.account.application.port.out;

import com.practice.cleanarichitecturehandson.buckpal.account.domain.Account;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;

import java.time.LocalDateTime;

public interface LoadAccountPort {

    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
