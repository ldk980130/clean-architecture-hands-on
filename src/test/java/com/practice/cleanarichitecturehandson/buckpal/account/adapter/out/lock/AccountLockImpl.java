package com.practice.cleanarichitecturehandson.buckpal.account.adapter.out.lock;

import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.AccountLock;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;
import org.springframework.stereotype.Component;

@Component
public class AccountLockImpl implements AccountLock {

    @Override
    public void lockAccount(AccountId accountId) {

    }

    @Override
    public void releaseAccount(AccountId accountId) {

    }
}
