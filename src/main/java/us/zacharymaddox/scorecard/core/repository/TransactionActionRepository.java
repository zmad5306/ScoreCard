package us.zacharymaddox.scorecard.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import us.zacharymaddox.scorecard.core.domain.TransactionAction;

public interface TransactionActionRepository extends JpaRepository<TransactionAction, Long> {
	
	@Query("from TransactionAction where transaction.transactionId = :transaction_id and action.actionId = :action_id")
	public List<TransactionAction> findByTransactionIdAndActionId(@Param("transaction_id") Long transactionId, @Param("action_id") Long actionId);
	
	@Query("from TransactionAction where action.actionId = :action_id")
	public List<TransactionAction> findByActionId(@Param("action_id") Long actionId);

}
