package com.midas.app.activities;

import com.midas.app.models.Account;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface UpdateAccountActivity extends AccountActivity {
  @ActivityMethod
  Account updatePaymentAccount(Account account);
}
