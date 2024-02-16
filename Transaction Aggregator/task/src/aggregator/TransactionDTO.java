package aggregator;

import java.time.LocalDateTime;

public record TransactionDTO(
        String id,
        String serverId,
        String account,
        String amount,
        String timestamp
) { }
