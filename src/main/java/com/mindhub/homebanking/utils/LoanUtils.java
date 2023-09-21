package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Loan;

public final class LoanUtils {
    public static Double calcularIntereses(Loan loan, LoanApplicationDTO loanApplicationDTO) {
            int payment = loanApplicationDTO.getPayments();

            double initialInterest = loan.getInterest();
            double interest = 0;

            for (int i = 0; i < payment; i++) {
                interest = initialInterest;
                initialInterest += 0.5;
            }
            return interest;
        }
}