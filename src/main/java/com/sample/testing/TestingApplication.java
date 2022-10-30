package com.sample.testing;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.AuthorizationCodeOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@SpringBootApplication
public class TestingApplication implements CommandLineRunner {



	public static void main(String[] args) {
		SpringApplication.run(TestingApplication.class, args);
	}

	@Autowired
	private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;


	@Override
	public void run(String... args) throws Exception {
		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("okta")
				.principal("Demo Service")
				.build();
		OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
		OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();
		//logger.info("Issued: " + accessToken.getIssuedAt().toString() + ", Expires:" + accessToken.getExpiresAt().toString());
		//logger.info("Scopes: " + accessToken.getScopes().toString());
		//logger.info("Token: " + accessToken.getTokenValue());
		System.out.println(accessToken.getTokenValue());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken.getTokenValue());
		headers.add("X-IBM-Client-Id",authorizedClient.getClientRegistration().getClientId());
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("accept", "application/json");
		RestTemplate restTemplate = new RestTemplate();
		var titleFetchObject = new JSONObject();
		titleFetchObject.put("TransactionAmount", "000000100000");
		titleFetchObject.put("TransmissionDateAndTime", "0715133513");
		titleFetchObject.put("STAN", "102050");
		titleFetchObject.put("Time", "133513");
		titleFetchObject.put("Date", "0715");
		titleFetchObject.put("MerchantType", "0003");
		titleFetchObject.put("FromBankIMD", "998876");
		titleFetchObject.put("RRN", "000000024420");
		titleFetchObject.put("CardAcceptorTerminalId", "40260275");
		titleFetchObject.put("CardAcceptorIdCode", "402626030259047");
		var cardAcceptorNameLocation = new JSONObject();
		cardAcceptorNameLocation.put("Location", "Park Towers");
		cardAcceptorNameLocation.put("City", "Karachi");
		cardAcceptorNameLocation.put("State", "Sindh");
		cardAcceptorNameLocation.put("ZipCode", "34234");
		cardAcceptorNameLocation.put("AgentName", "Ali Qazi");
		cardAcceptorNameLocation.put("AgentCity", "Karachi");
		cardAcceptorNameLocation.put("ADCLiteral", "Any Channel");
		cardAcceptorNameLocation.put("BankName", "Allied Bank");
		cardAcceptorNameLocation.put("Country", "PK");
		titleFetchObject.put("CardAcceptorNameLocation", cardAcceptorNameLocation);
		titleFetchObject.put("MerchantDetail", "");
		titleFetchObject.put("PaymentDetail", "0320 Donations  Charity                     ");
		titleFetchObject.put("CurrencyCodeTransaction", "586");
		titleFetchObject.put("AccountNumberFrom", "3528555323354910");
		titleFetchObject.put("AccountNumberTo", "6304532132598659");
		titleFetchObject.put("ToBankIMD", "221166");
		titleFetchObject.put("PAN", "4250108749566");
		titleFetchObject.put("ExpiryDate", "2205");
		titleFetchObject.put("PosEntMode", "000");
		titleFetchObject.put("BeneficiaryBankName", "1Link Model Bank");
		titleFetchObject.put("Reserved1", "Reserved for future");
		titleFetchObject.put("Reserved2", "Reserved for future");
		titleFetchObject.put("Reserved3", "Reserved for future");
		System.out.println(titleFetchObject);

		final ObjectMapper objectMapper = new ObjectMapper();

		HttpEntity<String> request =
				new HttpEntity<String>(titleFetchObject.toString(), headers);
		String createPersonUrl = "https://sandboxapi.1link.net.pk/uat-1link/sandbox/path-1";

		String personResultAsJsonStr =
				restTemplate.postForObject(createPersonUrl, request, String.class);
		JsonNode root = objectMapper.readTree(personResultAsJsonStr);
		System.out.println(root);
	}
}
