package dev.zachmaddox.scorecard.example.bank.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dev.zachmaddox.scorecard.lib.annotation.ProcessAuthorized;
import dev.zachmaddox.scorecard.lib.domain.exception.ProcessingFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.zachmaddox.scorecard.example.bank.domain.Account;
import dev.zachmaddox.scorecard.example.bank.domain.BankErrorCode;
import dev.zachmaddox.scorecard.example.bank.domain.CreditRequest;
import dev.zachmaddox.scorecard.example.bank.domain.DebitRequest;
import dev.zachmaddox.scorecard.example.bank.service.AccountService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionProcessingService {

	private final AccountService accountService;
	private final String errorKey = "error_code";

    private void nsf() {
		Map<String, String> metadata = new HashMap<>();
		metadata.put(errorKey, BankErrorCode.NSF.name());
        throw new ProcessingFailedException(metadata);
	}
	
	private void accountDne() {
		Map<String, String> metadata = new HashMap<>();
		metadata.put(errorKey, BankErrorCode.ACCOUNT_DNE.name());
        throw new ProcessingFailedException(metadata);
	}
	
	private Optional<Map<String, String>> performDebit(DebitRequest request, Account account) {
		Long transactionId = accountService.debitAccount(request, account);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("transaction_id", transactionId.toString());
        return Optional.of(metadata);
	}

    private void performCredit(CreditRequest request, Account account) {
        accountService.creditAccount(request, account);
    }
	
	@JmsListener(destination="account", selector="ACTION='debit'")
	@Transactional
    @ProcessAuthorized(allowMissingHeader = false)
	public Optional<Map<String, String>> debit(Message<DebitRequest> message) {
		log.info("processing debit request");
        DebitRequest request = message.getPayload();
        Optional<Account> account = accountService.getAccount(request.getAccountId());
        if (account.isPresent()) {
            Account foundAccount = account.get();
            if (0 > foundAccount.getBalance().compareTo(request.getAmount())) {
                nsf();
            } else {
                return performDebit(request, foundAccount);
            }
        } else {
            accountDne();
        }

        return Optional.empty();
	}

	@JmsListener(destination="account", selector="ACTION='credit'")
	@Transactional
    @ProcessAuthorized(allowMissingHeader = false)
	public void credit(Message<CreditRequest> message) {
		log.info("processing credit request");
        CreditRequest request = message.getPayload();
        Optional<Account> account = accountService.getAccount(request.getAccountId());
        if (account.isPresent()) {
            performCredit(request, account.get());
        } else {
            accountDne();
        }
	}

}
