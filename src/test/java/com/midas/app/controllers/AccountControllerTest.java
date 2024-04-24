package com.midas.app.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.midas.app.models.Account;
import com.midas.app.services.AccountService;
import com.midas.generated.model.AccountDto;
import com.midas.generated.model.CreateAccountDto;
import com.midas.generated.model.UpdateAccountDto;
import com.stripe.exception.StripeException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AccountControllerTest {

  @Mock private AccountService accountService;

  @InjectMocks private AccountController accountController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createUserAccount() throws StripeException {

    CreateAccountDto createAccountDto = new CreateAccountDto();
    createAccountDto.setEmail("test@example.com");
    createAccountDto.setFirstName("John");
    createAccountDto.setLastName("Doe");
    Account createdAccount = new Account();
    createdAccount.setEmail("test@example.com");
    createdAccount.setFirstName("John");
    createdAccount.setLastName("Doe");
    createdAccount.setProvider(com.midas.app.models.enums.Provider.STRIPE);
    createdAccount.setId(UUID.fromString("fba5b240-0e68-4e64-aca6-f12022dc4be3"));
    createdAccount.setProviderAccountId("acct_1233437t");
    createdAccount.setCreatedAt(OffsetDateTime.parse("2022-04-13T01:52:40.690387Z"));
    createdAccount.setUpdatedAt(OffsetDateTime.parse("2022-04-13T01:52:40.690387Z"));

    when(accountService.createAccount(any())).thenReturn(createdAccount);

    ResponseEntity<AccountDto> responseEntity =
        accountController.createUserAccount(createAccountDto);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals("test@example.com", responseEntity.getBody().getEmail());
    assertEquals("John", responseEntity.getBody().getFirstName());
    assertEquals("Doe", responseEntity.getBody().getLastName());
  }

  @Test
  void getUserAccounts() {
    Account createdAccount = new Account();
    createdAccount.setEmail("test@example.com");
    createdAccount.setFirstName("John");
    createdAccount.setLastName("Doe");
    createdAccount.setProvider(com.midas.app.models.enums.Provider.STRIPE);
    createdAccount.setId(UUID.fromString("fba5b240-0e68-4e64-aca6-f12022dc4be3"));
    createdAccount.setProviderAccountId("acct_1233437t");
    createdAccount.setCreatedAt(OffsetDateTime.parse("2022-04-13T01:52:40.690387Z"));
    createdAccount.setUpdatedAt(OffsetDateTime.parse("2022-04-13T01:52:40.690387Z"));

    when(accountService.getAccounts()).thenReturn(Collections.singletonList(createdAccount));

    ResponseEntity<List<AccountDto>> responseEntity = accountController.getUserAccounts();
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(1, responseEntity.getBody().size());
    assertEquals("test@example.com", responseEntity.getBody().get(0).getEmail());
    assertEquals("John", responseEntity.getBody().get(0).getFirstName());
    assertEquals("Doe", responseEntity.getBody().get(0).getLastName());
  }

  @Test
  void updateUserAccount() throws StripeException {
    UUID accountId = UUID.randomUUID();
    UpdateAccountDto updateAccountDto = new UpdateAccountDto();
    updateAccountDto.setEmail("updated@example.com");
    updateAccountDto.setFirstName("John");
    updateAccountDto.setLastName("Doe");
    updateAccountDto.setId(accountId);

    Account updatedAccount = new Account();
    updatedAccount.setEmail("updated@example.com");
    updatedAccount.setId(accountId);
    updatedAccount.setFirstName("John");
    updatedAccount.setLastName("Doe");
    updatedAccount.setProvider(com.midas.app.models.enums.Provider.STRIPE);
    updatedAccount.setProviderAccountId("acct_1233437t");
    updatedAccount.setCreatedAt(OffsetDateTime.parse("2022-04-13T01:52:40.690387Z"));
    updatedAccount.setUpdatedAt(OffsetDateTime.parse("2022-04-13T01:52:40.690387Z"));

    when(accountService.updateAccount(any())).thenReturn(updatedAccount);

    ResponseEntity<AccountDto> responseEntity =
        accountController.updateUserAccount(accountId, updateAccountDto);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals("updated@example.com", responseEntity.getBody().getEmail());
  }
}
