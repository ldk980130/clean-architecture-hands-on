package com.practice.cleanarichitecturehandson.buckpal.account.domain;

import java.math.BigInteger;


public record Money(BigInteger amount) {

    public static Money add(Money money1, Money money2) {
        return new Money(money1.amount.add(money2.amount));
    }

    public Money negate() {
        return new Money(this.amount.negate());
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigInteger.ZERO) > 0;
    }
}
