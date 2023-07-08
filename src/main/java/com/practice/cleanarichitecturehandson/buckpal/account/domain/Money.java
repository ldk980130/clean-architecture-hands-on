package com.practice.cleanarichitecturehandson.buckpal.account.domain;

import java.math.BigInteger;


public record Money(BigInteger amount) {

    public static final Money ZERO = new Money(BigInteger.ZERO);

    public static Money add(Money money1, Money money2) {
        return new Money(money1.amount.add(money2.amount));
    }

    public static Money of(long amount) {
        return new Money(BigInteger.valueOf(amount));
    }

    public static Money subtract(Money money1, Money money2) {
        return new Money(money1.amount.subtract(money2.amount));
    }

    public Money negate() {
        return new Money(this.amount.negate());
    }

    public boolean isPositiveOrZero() {
        return this.amount.compareTo(BigInteger.ZERO) >= 0;
    }

    public Money minus(Money money){
        return new Money(this.amount.subtract(money.amount));
    }

    public Money plus(Money money) {
        return new Money(this.amount.add(money.amount));
    }
}
