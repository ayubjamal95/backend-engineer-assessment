package com.midas.app.activities.impl;

import com.midas.app.activities.CreateAccountActivity;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.CreateAccountWorkflow;
import io.temporal.spring.boot.ActivityImpl;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ActivityImpl(taskQueues = CreateAccountWorkflow.QUEUE_NAME)
public class CreateAccountActivityImpl implements CreateAccountActivity {
  private final Logger logger = Workflow.getLogger(CreateAccountActivityImpl.class);
  private final AccountRepository accountRepository;
  private final PaymentProvider paymentProvider;

  @Override
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  @Override
  public Account createPaymentAccount(Account account) {
    Account createdAccount = new Account();
    try {
      CreateAccount createAccount =
          CreateAccount.builder()
              .userId(account.getId().toString())
              .firstName(account.getFirstName())
              .lastName(account.getLastName())
              .email(account.getEmail())
              .build();
      createdAccount = paymentProvider.createAccount(createAccount);
    } catch (Exception e) {
      logger.error("Exception Occured :{}", e.getMessage());
    }
    return createdAccount;
  }
}
