package com.practice.cleanarichitecturehandson.buckpal.account.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ActivityWindow {

    private final List<Activity> activities;

    public ActivityWindow(Activity... activities) {
        this.activities = new ArrayList<>(Arrays.asList(activities));
    }

    public ActivityWindow(List<Activity> activities) {
        this.activities = new ArrayList<>(activities);
    }


    public Money calculateBalance(AccountId accountId) {
        Money depositBalance = activities.stream()
                .filter(activity -> activity.isTargetAccountId(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);
        Money withdrawalBalance = activities.stream()
                .filter(activity -> activity.isSourceAccountId(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);
        return Money.add(depositBalance, withdrawalBalance.negate());
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }
}
