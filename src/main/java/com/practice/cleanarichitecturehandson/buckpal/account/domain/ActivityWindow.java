package com.practice.cleanarichitecturehandson.buckpal.account.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityWindow {

    private final List<Activity> activities;

    public ActivityWindow(List<Activity> activities) {
        this.activities = activities;
    }

    public ActivityWindow(Activity... activities) {
        this.activities = new ArrayList<>(Arrays.asList(activities));
    }

    public Money calculateBalance(AccountId accountId) {
        return null;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }
}
