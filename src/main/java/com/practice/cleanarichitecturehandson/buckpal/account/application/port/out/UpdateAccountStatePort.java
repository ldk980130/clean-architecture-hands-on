package com.practice.cleanarichitecturehandson.buckpal.account.application.port.out;

import com.practice.cleanarichitecturehandson.buckpal.account.domain.Account;

public interface UpdateAccountStatePort {

    void updateActivities(Account account);
}
