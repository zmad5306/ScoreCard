package us.zacharymaddox.scorecard.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import us.zacharymaddox.scorecard.core.domain.Action;
import us.zacharymaddox.scorecard.core.domain.Transaction;
import us.zacharymaddox.scorecard.core.domain.TransactionAction;

public interface TransactionActionRepository extends JpaRepository<TransactionAction, Long> {
	
	public List<TransactionAction> findByTransactionAndAction(Transaction transaction, Action action);

}
