package com.example.spacelab.integration.teamwork.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TeamworkAccountTimeTotalResponse(
        @JsonProperty("time-totals")
        TimeTotals timeTotals
) {
        public record TimeTotals(
                @JsonProperty("total-mins-sum")
                Integer minutes,
                @JsonProperty("total-hours-sum")
                Double hours
        ) {}
}
