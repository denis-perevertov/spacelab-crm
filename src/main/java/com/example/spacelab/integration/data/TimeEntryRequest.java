package com.example.spacelab.integration.data;

public record TimeEntryRequest(
      TimeEntry timelog,
      Object[] tags,
      Object timelogOptions
) {
}