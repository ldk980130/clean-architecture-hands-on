package com.practice.cleanarichitecturehandson.buckpal.account.application.service;

import com.practice.cleanarichitecturehandson.buckpal.account.application.port.in.SendMoneyCommand;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.AccountLock;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.LoadAccountPort;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.UpdateAccountStatePort;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Account;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SendMoneyServiceTest {

    @Mock
    private LoadAccountPort loadAccountPort;

    @Mock
    private AccountLock accountLock;

    @Mock
    private UpdateAccountStatePort updateAccountStatePort;

    @InjectMocks
    private SendMoneyService sendMoneyService;

    @Test
    void givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased() {
        // given
        AccountId sourceAccountId = new AccountId(41L);
        Account sourceAccount = givenAnAccountWithId(sourceAccountId);

        AccountId targetAccountId = new AccountId(42L);
        Account targetAccount = givenAnAccountWithId(targetAccountId);

        givenWithdrawalWillFail(sourceAccount);
        givenDepositWillSucceed(targetAccount);

        SendMoneyCommand command = new SendMoneyCommand(41L, 42L, 300L);

        // when
        boolean success = sendMoneyService.sendMoney(command);

        // then
        assertThat(success).isFalse();
        then(accountLock).should().lockAccount(eq(sourceAccountId));
        then(accountLock).should().releaseAccount(eq(sourceAccountId));
        then(accountLock).should(times(0)).lockAccount(eq(targetAccountId));
    }

    @Test
    void transactionSucceeds() {
        // given
        Account sourceAccount = givenSourceAccount();
        Account targetAccount = givenTargetAccount();

        givenWithdrawalWillSucceed(sourceAccount);
        givenDepositWillSucceed(targetAccount);

        long amount = 500L;
        Money money = Money.of(amount);

        SendMoneyCommand command = new SendMoneyCommand(
                sourceAccount.getId().value(),
                targetAccount.getId().value(),
                amount
        );

        // when
        boolean success = sendMoneyService.sendMoney(command);

        // then
        assertThat(success).isTrue();

        AccountId sourceAccountId = sourceAccount.getId();
        AccountId targetAccountId = targetAccount.getId();

        then(accountLock).should().lockAccount(eq(sourceAccountId));
        then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
        then(accountLock).should().releaseAccount(eq(sourceAccountId));

        then(accountLock).should().lockAccount(eq(targetAccountId));
        then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
        then(accountLock).should().releaseAccount(eq(targetAccountId));

        thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
    }

    private void thenAccountsHaveBeenUpdated(AccountId... accountIds) {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        then(updateAccountStatePort).should(times(accountIds.length))
                .updateActivities(accountCaptor.capture());

        List<AccountId> updatedAccountIds = accountCaptor.getAllValues()
                .stream()
                .map(Account::getId)
                .collect(Collectors.toList());

        for (AccountId accountId : accountIds) {
            assertThat(updatedAccountIds).contains(accountId);
        }
    }

    private void givenDepositWillSucceed(Account account) {
        lenient().doReturn(true)
                        .when(account).deposit(any(Money.class), any(AccountId.class));
    }

    private void givenWithdrawalWillFail(Account account) {
        lenient().doReturn(false)
                .when(account).withdraw(any(Money.class), any(AccountId.class));
    }

    private void givenWithdrawalWillSucceed(Account account) {
        lenient().doReturn(true)
                .when(account).withdraw(any(Money.class), any(AccountId.class));
    }

    private Account givenTargetAccount() {
        return givenAnAccountWithId(new AccountId(42L));
    }

    private Account givenSourceAccount() {
        return givenAnAccountWithId(new AccountId(41L));
    }

    private Account givenAnAccountWithId(AccountId id) {
        Account account = Mockito.mock(Account.class);
        given(account.getId()).willReturn(id);
        given(loadAccountPort.loadAccount(eq(account.getId()), any(LocalDateTime.class)))
                .willReturn(account);
        return account;
    }
}
