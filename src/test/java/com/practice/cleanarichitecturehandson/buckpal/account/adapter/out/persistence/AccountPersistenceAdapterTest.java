package com.practice.cleanarichitecturehandson.buckpal.account.adapter.out.persistence;

import com.practice.cleanarichitecturehandson.buckpal.account.domain.Account;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.ActivityWindow;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static com.practice.cleanarichitecturehandson.buckpal.common.AccountTestData.defaultAccount;
import static com.practice.cleanarichitecturehandson.buckpal.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountMapper.class})
class AccountPersistenceAdapterTest {

    @Autowired
    private AccountPersistenceAdapter adapterUnderTest;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    @Sql("/AccountPersistenceAdapterTest.sql")
    void loadsAccount() {
        AccountId accountId = new AccountId(1L);
        LocalDateTime baselineTime = LocalDateTime.of(2018, 8, 10, 0, 0);
        Account account = adapterUnderTest.loadAccount(accountId, baselineTime);

        assertThat(account.getActivityWindow().getActivities()).hasSize(2);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(500));
    }

    @Test
    void updatesActivities() {
        Account account = defaultAccount()
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(new ActivityWindow(
                        defaultActivity()
                                .withId(null)
                                .withMoney(Money.of(1L)).build()))
                .build();

        adapterUnderTest.updateActivities(account);

        assertThat(activityRepository.count()).isEqualTo(1);

        JpaActivity savedActivity = activityRepository.findAll().get(0);
        assertThat(savedActivity.getAmount()).isEqualTo(1L);
    }
}
