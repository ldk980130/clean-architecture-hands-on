package com.practice.cleanarichitecturehandson.buckpal.account.adapter.out.persistence;

import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.LoadAccountPort;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.UpdateAccountStatePort;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Account;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final AccountRepository accountRepository;
    private final ActivityRepository activityRepository;

    @Override
    public Account loadAccount(AccountId accountId, LocalDateTime baselineDate) {
        JpaAccount account = accountRepository.findById(accountId.value()).orElseThrow();
        List<JpaActivity> activities = activityRepository.findByOwnerSince(accountId.value(), baselineDate);
        Long withdrawalBalance = activityRepository.getWithdrawalBalanceUntil(accountId.value(), baselineDate);
        Long depositBalance = activityRepository.getDepositBalanceUntil(accountId.value(), baselineDate);
        return AccountMapper.toDomain(account, activities, withdrawalBalance, depositBalance);
    }

    @Override
    public void updateActivities(Account account) {
        account.getActivityWindow()
                .getActivities()
                .forEach(this::updateActivity);
    }

    private void updateActivity(Activity activity) {
        if (activity.getId() == null) {
            activityRepository.save(AccountMapper.toJpaActivity(activity));
        }
    }
}
