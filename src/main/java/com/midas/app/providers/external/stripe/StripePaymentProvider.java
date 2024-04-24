package com.midas.app.providers.external.stripe;

import com.midas.app.mappers.Mapper;
import com.midas.app.models.Account;
import com.midas.app.models.enums.Provider;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.providers.payment.UpdateAccount;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;
  private final StripeClient stripeClient;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return Provider.STRIPE.name();
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(CreateAccount details) {
    CustomerCreateParams customerCreateParams =
        CustomerCreateParams.builder()
            .setName(details.getFirstName() + " " + details.getLastName())
            .setEmail(details.getEmail())
            .build();
    Customer customer = new Customer();
    try {
      customer = stripeClient.customers().create(customerCreateParams);
    } catch (StripeException e) {
      logger.error("Exception occurred while creating customer at {} ", providerName(), e);
    }
    return Mapper.toAccount(customer, Provider.valueOf(providerName()));
  }

  @Override
  public Account updateAccount(UpdateAccount details) {
    CustomerUpdateParams customerUpdateParamsParams =
        CustomerUpdateParams.builder()
            .setName(details.getFirstName() + " " + details.getLastName())
            .setEmail(details.getEmail())
            .build();
    Customer customer = new Customer();
    try {
      customer =
          stripeClient
              .customers()
              .update(details.getProviderAccountId(), customerUpdateParamsParams);
    } catch (StripeException e) {
      logger.error("Exception occurred while updating customer at {} ", providerName(), e);
    }
    return Mapper.toAccount(customer, Provider.valueOf(providerName()));
  }
}
