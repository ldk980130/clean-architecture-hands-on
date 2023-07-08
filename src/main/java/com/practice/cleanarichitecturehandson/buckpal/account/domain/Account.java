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

    public static Account withId(AccountId id, Money baselineBalance, ActivityWindow activityWindow) {
        return new Account(id, baselineBalance, activityWindow);
    }

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
        Activity withdrawal = new Activity(
                this.id,
                this.id,
                targetAccountId,
                LocalDateTime.now(),
                money
        );
        this.activityWindow.addActivity(withdrawal);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        Money addedMoney = Money.add(this.calculateBalance(), money.negate());
        return addedMoney.isPositiveOrZero();
    }

    public boolean deposit(Money money, AccountId sourceAccountId) {
        Activity deposit = new Activity(
                this.id,
                sourceAccountId,
                this.id,
                LocalDateTime.now(),
                money
        );
        this.activityWindow.addActivity(deposit);
        return true;
    }
}
