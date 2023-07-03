package com.practice.cleanarichitecturehandson.buckpal.account.application.service;

import com.practice.cleanarichitecturehandson.buckpal.account.application.port.in.SendMoneyCommand;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.in.SendMoneyUseCase;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.AccountLock;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.LoadAccountPort;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.UpdateAccountStatePort;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Account;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);
        Account sourceAccount = loadAccountPort.loadAccount(
                new AccountId(command.sourceAccountId()),
                baselineDate
        );
        Account targetAccount = loadAccountPort.loadAccount(
                new AccountId(command.targetAccountId()),
                baselineDate
        );
        return send(sourceAccount, targetAccount, Money.of(command.amount()));
    }

    private boolean send(Account sourceAccount, Account targetAccount, Money money) {
        AccountId sourceAccountId = sourceAccount.getId();
        AccountId targetAccountId = targetAccount.getId();
        accountLock.lockAccount(sourceAccountId);
        if (!sourceAccount.withdraw(money, targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            return false;
        }
        accountLock.lockAccount(targetAccountId);
        if (!targetAccount.deposit(money, sourceAccountId)) {
            accountLock.releaseAccount(targetAccountId);
            return false;
        }
        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);
        accountLock.releaseAccount(sourceAccountId);
        accountLock.releaseAccount(targetAccountId);
        return true;
    }
}
