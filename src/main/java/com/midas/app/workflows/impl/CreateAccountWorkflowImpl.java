package com.midas.app.workflows.impl;

import com.midas.app.activities.CreateAccountActivity;
import com.midas.app.exceptions.ApiException;
import com.midas.app.models.Account;
import com.midas.app.workflows.CreateAccountWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

@WorkflowImpl(taskQueues = CreateAccountWorkflow.QUEUE_NAME)
public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {
  private final Logger logger = Workflow.getLogger(CreateAccountWorkflowImpl.class);
  private final ActivityOptions activityOptions =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofSeconds(30))
          .setRetryOptions(
              RetryOptions.newBuilder()
                  .setMaximumAttempts(1)
                  .setInitialInterval(Duration.ofSeconds(5))
                  .build())
          .build();
  private CreateAccountActivity accountActivity =
      Workflow.newActivityStub(CreateAccountActivity.class, activityOptions);

  @Override
  public Account createAccount(Account details) {
    Account accountSaved = new Account();
    try {
      Account paymentAccountCreated = accountActivity.createPaymentAccount(details);
      if (!ObjectUtils.isEmpty(paymentAccountCreated)) {
        accountSaved = accountActivity.saveAccount(paymentAccountCreated);
      } else {
        throw new ApiException(
            HttpStatus.EXPECTATION_FAILED, "Exception occured while saving account");
      }
    } catch (Exception e) {
      logger.error("Exception Occured :{}", e.getMessage());
    }
    return accountSaved;
  }
}
