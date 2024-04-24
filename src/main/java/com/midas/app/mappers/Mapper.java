package com.midas.app.mappers;

import com.midas.app.models.Account;
import com.midas.app.models.enums.Provider;
import com.midas.generated.model.AccountDto;
import com.stripe.model.Customer;
import lombok.NonNull;

public class Mapper {
  // Prevent instantiation
  private Mapper() {}

  /**
   * toAccountDto maps an account to an account dto.
   *
   * @param account is the account to be mapped
   * @return AccountDto
   */
  public static AccountDto toAccountDto(@NonNull Account account) {
    var accountDto = new AccountDto();

    accountDto
        .id(account.getId())
        .firstName(account.getFirstName())
        .lastName(account.getLastName())
        .email(account.getEmail())
        .createdAt(account.getCreatedAt())
        .updatedAt(account.getUpdatedAt())
        .provider(AccountDto.ProviderEnum.valueOf(account.getProvider().name()))
        .providerAccountId(account.getProviderAccountId());

    return accountDto;
  }

  public static Account toAccount(@NonNull Customer customer, Provider providerType) {
    String[] fullName = customer.getName().split(" ");
    return Account.builder()
        .firstName(fullName[0])
        .lastName(fullName[1])
        .email(customer.getEmail())
        .providerAccountId(customer.getId())
        .provider(Provider.valueOf(providerType.name()))
        .build();
  }
}
