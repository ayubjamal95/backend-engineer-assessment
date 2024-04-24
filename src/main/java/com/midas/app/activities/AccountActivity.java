package com.midas.app.activities;

import com.midas.app.models.Account;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface AccountActivity {
  /**
   * saveAccount saves an account in the data store.
   *
   * @param account is the account to be saved
   * @return Account
   */
  @ActivityMethod
  Account saveAccount(Account account);
}
