package com.epam.stockexchange;

import com.epam.stockexchange.entity.Participant;
import com.epam.stockexchange.exchangesystem.TransactionPerformer;
import com.epam.stockexchange.exchangesystem.TransactionType;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionPerformerTest {
    private static final TransactionPerformer TRANSACTION_PERFORMER = new TransactionPerformer();

    @Test
    public void testPerformTransactionShouldTransferCurrenciesBetweenParticipantsWhenArgumentsAreValid() {
        //given
        Participant firstParticipant = new Participant(1, BigDecimal.valueOf(1000.75), BigDecimal.valueOf(2000.50), BigDecimal.valueOf(500.00));
        Participant secondParticipant = new Participant(2, BigDecimal.valueOf(1000.75), BigDecimal.valueOf(2000.50), BigDecimal.valueOf(500.00));
        TransactionType transactionType = TransactionType.BYN_TO_USD;
        BigDecimal amountToGive = new BigDecimal("450.11");
        BigDecimal amountToReceive = new BigDecimal("763.89");
        BigDecimal expectedFirstByn = BigDecimal.valueOf(500.00 - 450.11).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal expectedFirstUsd = BigDecimal.valueOf(1000.75 + 763.89).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal expectedSecondByn = BigDecimal.valueOf(500.00 + 450.11).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal expectedSecondUsd = BigDecimal.valueOf(1000.75 - 763.89).setScale(2, RoundingMode.HALF_DOWN);
        //when
        TRANSACTION_PERFORMER.performTransaction(firstParticipant, secondParticipant, transactionType, amountToGive, amountToReceive);
        //then
        Assert.assertEquals(expectedFirstByn, firstParticipant.getByn());
        Assert.assertEquals(expectedFirstUsd, firstParticipant.getUsd());
        Assert.assertEquals(expectedSecondUsd, secondParticipant.getUsd());
        Assert.assertEquals(expectedSecondByn, secondParticipant.getByn());
    }
}
