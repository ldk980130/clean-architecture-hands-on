package com.practice.cleanarichitecturehandson.buckpal;

import com.practice.cleanarichitecturehandson.buckpal.account.application.port.out.LoadAccountPort;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Account;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.AccountId;
import com.practice.cleanarichitecturehandson.buckpal.account.domain.Money;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountSystemTest {

    @LocalServerPort
    private int port;

    @Autowired
    private LoadAccountPort loadAccountPort;

    private final long sourceAccountId = 1L;
    private final long targetAccountId = 2L;
    private final long amount = 500L;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @Sql("/SendMoneySystemTest.sql")
    void sendMoney() {
        // given
        Money initialSourceBalance = sourceAccount().calculateBalance();
        Money initialTargetBalance = targetAccount().calculateBalance();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .contentType(ContentType.JSON)
                .post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
                        sourceAccountId, targetAccountId, amount)
                .then().log().all()
                .extract();

        // then
        then(response.statusCode())
                .isEqualTo(HttpStatus.OK.value());

        then(sourceAccount().calculateBalance())
                .isEqualTo(initialSourceBalance.minus(transferredAmount()));

        then(targetAccount().calculateBalance())
                .isEqualTo(initialTargetBalance.plus(transferredAmount()));

    }

    private Money transferredAmount() {
        return Money.of(amount);
    }

    private Account sourceAccount() {
        return loadAccount(new AccountId(sourceAccountId));
    }

    private Account targetAccount() {
        return loadAccount(new AccountId(targetAccountId));
    }

    private Account loadAccount(AccountId accountId) {
        return loadAccountPort.loadAccount(
                accountId,
                LocalDateTime.now()
        );
    }

}
