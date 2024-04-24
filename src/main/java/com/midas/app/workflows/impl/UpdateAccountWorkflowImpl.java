package com.midas.app.workflows.impl;

import com.midas.app.activities.UpdateAccountActivity;
import com.midas.app.exceptions.ApiException;
import com.midas.app.models.Account;
import com.midas.app.workflows.UpdateAccountWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

@WorkflowImpl(taskQueues = UpdateAccountWorkflow.QUEUE_NAME)
public class UpdateAccountWorkflowImpl implements UpdateAccountWorkflow {
  private final Logger logger = Workflow.getLogger(UpdateAccountWorkflowImpl.class);
  private final ActivityOptions activityOptions =
      ActivityOptions.newBuilder()
          .setStartToCloseTimeout(Duration.ofSeconds(30))
          .setRetryOptions(
              RetryOptions.newBuilder()
                  .setMaximumAttempts(1)
                  .setInitialInterval(Duration.ofSeconds(5))
                  .build())
          .build();
  private UpdateAccountActivity accountActivity =
      Workflow.newActivityStub(UpdateAccountActivity.class, activityOptions);

  @Override
  public Account updateAccount(Account details) {
    Account accountUpdated = new Account();

    try {
      Account paymentAccountUpdated = accountActivity.updatePaymentAccount(details);
      if (!ObjectUtils.isEmpty(paymentAccountUpdated)) {
        accountUpdated = accountActivity.saveAccount(paymentAccountUpdated);
      } else {
        throw new ApiException(
            HttpStatus.EXPECTATION_FAILED, "Exception occured while updating account");
      }
    } catch (Exception e) {
      logger.error("Exception Occured :{}", e.getMessage());
    }
    return accountUpdated;
  }
}
