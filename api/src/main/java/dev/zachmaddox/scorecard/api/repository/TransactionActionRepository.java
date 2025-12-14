package dev.zachmaddox.scorecard.api.repository;

import java.util.List;

import dev.zachmaddox.scorecard.api.domain.TransactionAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionActionRepository extends JpaRepository<TransactionAction, Long> {
	
	@Query("from TransactionAction where transaction.transactionId = :transaction_id and action.actionId = :action_id")
	List<TransactionAction> findByTransactionIdAndActionId(@Param("transaction_id") Long transactionId, @Param("action_id") Long actionId);
	
	@Query("from TransactionAction where action.actionId = :action_id")
	List<TransactionAction> findByActionId(@Param("action_id") Long actionId);

}
