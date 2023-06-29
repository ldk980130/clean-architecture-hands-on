package com.practice.cleanarichitecturehandson.buckpal.account.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Account {

    private final AccountId id;
    private final Money baselineBalance;
    private final ActivityWindow activityWindow;

    public Money calculateBalance() {
        return Money.add(
                this.baselineBalance,
                this.activityWindow.calculateBalance(this.id)
        );
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {
        if (!mayWithdraw(money)) {
            return false;
        }
        Activity withdrawal = Activity.builder()
                .ownerAccountId(this.id)
                .sourceAccountId(this.id)
                .targetAccountId(targetAccountId)
                .timestamp(LocalDateTime.now())
                .money(money)
                .build();
        this.activityWindow.addActivity(withdrawal);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        Money addedMoney = Money.add(this.calculateBalance(), money.negate());
        return addedMoney.isPositive();
    }

    public boolean deposit(Money money, AccountId sourceAccountId) {
        Activity deposit = Activity.builder()
                .ownerAccountId(this.id)
                .sourceAccountId(sourceAccountId)
                .targetAccountId(this.id)
                .timestamp(LocalDateTime.now())
                .money(money)
                .build();
        this.activityWindow.addActivity(deposit);
        return true;
    }
}
