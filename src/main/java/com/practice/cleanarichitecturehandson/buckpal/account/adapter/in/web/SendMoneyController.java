package com.practice.cleanarichitecturehandson.buckpal.account.adapter.in.web;

import com.practice.cleanarichitecturehandson.buckpal.account.application.port.in.SendMoneyCommand;
import com.practice.cleanarichitecturehandson.buckpal.account.application.port.in.SendMoneyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendMoneyController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    public void sendMoney(@ModelAttribute @Validated SendMoneyCommand command) {
        sendMoneyUseCase.sendMoney(command);
    }
}
