package com.practice.cleanarichitecturehandson.buckpal.account.application.port.out;

import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;

public interface AccountLock {

    void lockAccount(AccountId accountId);

    void releaseAccount(AccountId accountId);
}
