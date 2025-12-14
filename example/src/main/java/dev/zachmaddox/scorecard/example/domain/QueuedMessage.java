package dev.zachmaddox.scorecard.example.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.OffsetDateTime;
import java.util.Map;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "QUEUED_MESSAGE", schema = "EXAMPLE")
public class QueuedMessage {

    public enum Status {
        PENDING,RESUBMITTED,ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUEUED_MESSAGE_ID")
    private Long id;

    @Type(JsonBinaryType.class)
    @Column(name = "SCORE_CARD_HEADER", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> scoreCardHeader;

    @Type(JsonBinaryType.class)
    @Column(name = "MESSAGE_BODY", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> messageBody;

    @Column(name = "QUEUED_AT", nullable = false)
    private OffsetDateTime queuedAt = OffsetDateTime.now();

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "ERROR", nullable = true)
    private String error;
}
