package com.practice.cleanarichitecturehandson.buckpal.account.adapter.out.persistence;

import com.practice.cleanarichitecturehandson.buckpal.account.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMapper {


    public static Account toDomain(JpaAccount account,
                                   List<JpaActivity> activities,
                                   Long withdrawalBalance,
                                   Long depositBalance) {

        Money baselineBalance = Money.subtract(
                Money.of(depositBalance),
                Money.of(withdrawalBalance)
        );

        return Account.withId(
                new AccountId(account.getId()),
                baselineBalance,
                mapToActivityWindow(activities)
        );
    }

    private static ActivityWindow mapToActivityWindow(List<JpaActivity> activities) {
        return new ActivityWindow(
                activities.stream()
                        .map(activity -> new Activity(
                                new ActivityId(activity.getId()),
                                new AccountId(activity.getOwnerAccountId()),
                                new AccountId(activity.getSourceAccountId()),
                                new AccountId(activity.getTargetAccountId()),
                                activity.getTimestamp(),
                                Money.of(activity.getAmount())
                        )).toList()
        );
    }

    public static JpaActivity toJpaActivity(Activity activity) {
        return new JpaActivity(
                checkNull(activity.getId()),
                activity.getTimestamp(),
                activity.getOwnerAccountId().value(),
                activity.getSourceAccountId().value(),
                activity.getTargetAccountId().value(),
                activity.getMoney().amount().longValue()
        );
    }

    private static Long checkNull(ActivityId id) {
        if (id == null) {
            return null;
        }
        return id.value();
    }
}
