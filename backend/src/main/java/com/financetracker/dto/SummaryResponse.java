package com.financetracker.dto;

import java.math.BigDecimal;

public record SummaryResponse(BigDecimal income, BigDecimal expenses, BigDecimal balance) {}
