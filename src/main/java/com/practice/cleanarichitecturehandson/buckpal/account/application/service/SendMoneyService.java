package com.practice.cleanarichitecturehandson.buckpal.account.application.service;

import com.practice.cleanarichitecturehandson.buckpal.account.application.port.in.SendMoneyCommand;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.in.SendMoneyUseCase;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.AccountLock;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.LoadAccountPort;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.UpdateAccountStatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        // 비즈니스 규칙 검증
        // 모델 상태 조작
        return true;
    }
}
