package com.midas.app.activities.impl;

import com.midas.app.activities.UpdateAccountActivity;
import com.midas.app.models.Account;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.providers.payment.UpdateAccount;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.UpdateAccountWorkflow;
import io.temporal.spring.boot.ActivityImpl;
import io.temporal.workflow.Workflow;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@ActivityImpl(taskQueues = UpdateAccountWorkflow.QUEUE_NAME)
public class UpdateAccountActivityImpl implements UpdateAccountActivity {
  private final Logger logger = Workflow.getLogger(UpdateAccountActivityImpl.class);
  private final AccountRepository accountRepository;
  private final PaymentProvider paymentProvider;

  @Override
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  @Override
  public Account updatePaymentAccount(Account account) {
    Account updatedAccount = new Account();
    try {
      Optional<Account> accountFound = accountRepository.findById(account.getId());
      if (accountFound.isPresent()) {
        UpdateAccount updateAccount =
            UpdateAccount.builder()
                .providerAccountId(accountFound.get().getProviderAccountId())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .build();
        updatedAccount = paymentProvider.updateAccount(updateAccount);
      }
    } catch (Exception e) {
      logger.error("Exception Occured :{}", e.getMessage());
    }
    return updatedAccount;
  }
}
