package dev.zachmaddox.scorecard.common.domain;

import java.util.Collection;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generic paged response wrapper")
public record DataPage<T>(
        @Schema(description = "Page of data items") Collection<T> data,
        @Schema(description = "Rows per page") Integer rows,
        @Schema(description = "Current page (0-based or 1-based depending on caller)") Integer page,
        @Schema(description = "Total item count") Long count
) {

    public Long getPages() {
        if (count < rows) {
            return 1L;
        } else if (count % rows == 0) {
            return count / rows;
        } else {
            return (count / rows) + 1;
        }
    }

}
