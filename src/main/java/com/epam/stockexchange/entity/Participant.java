package com.epam.stockexchange.entity;

import com.epam.stockexchange.exception.TransactionException;
import com.epam.stockexchange.exchangesystem.StockExchangePlatform;
import com.epam.stockexchange.exchangesystem.TransactionType;
import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;

public class Participant implements Runnable{

    private static final Logger LOGGER = LogManager.getLogger(Participant.class);
    private static final Random decisionMaker = new Random();
    private static final double MINIMUM_TRANSACTION_SIZE = 0.01;

    private final int id;
    private BigDecimal usd = new BigDecimal(0).setScale(2, RoundingMode.DOWN);
    private BigDecimal eur = new BigDecimal(0).setScale(2, RoundingMode.DOWN);
    private BigDecimal byn = new BigDecimal(0).setScale(2, RoundingMode.DOWN);

    public Participant(int id, BigDecimal usd, BigDecimal eur, BigDecimal byn) {
        this.id = id;
        this.usd = usd.setScale(2, RoundingMode.DOWN);
        this.eur = eur.setScale(2,RoundingMode.DOWN);
        this.byn = byn.setScale(2, RoundingMode.DOWN);
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }

    public void setEur(BigDecimal eur) {
        this.eur = eur;
    }

    public void setByn(BigDecimal byn) {
        this.byn = byn;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public BigDecimal getEur() {
        return eur;
    }

    public BigDecimal getByn() {
        return byn;
    }

    @Override
    public void run() {
        StockExchangePlatform exchangePlatform = StockExchangePlatform.getInstance();
        exchangePlatform.registerParticipant(this);
        while (true) {
            TransactionType transactionToMake = TransactionType.values()[decisionMaker
                    .nextInt(TransactionType.values()
                            .length)];
            BigDecimal maximumAvailableOfCurrency;
            switch (transactionToMake) {
                case BYN_TO_EUR:
                case BYN_TO_USD:
                    maximumAvailableOfCurrency = byn;
                    break;
                case EUR_TO_BYN:
                case EUR_TO_USD:
                    maximumAvailableOfCurrency = eur;
                    break;
                case USD_TO_BYN:
                case USD_TO_EUR:
                    maximumAvailableOfCurrency = usd;
                    break;
                default:
                    maximumAvailableOfCurrency = BigDecimal.valueOf(0).setScale(2, RoundingMode.DOWN);
                    break;
            }
            BigDecimal amountToGive = new BigDecimal(MINIMUM_TRANSACTION_SIZE + (maximumAvailableOfCurrency.doubleValue() - MINIMUM_TRANSACTION_SIZE) * decisionMaker.nextDouble()).setScale(2, RoundingMode.DOWN);
            BigDecimal amountToReceive = amountToGive;
            switch (transactionToMake) {
                case BYN_TO_EUR:
                    amountToReceive = amountToReceive.divide(StockExchangePlatform.BYN_TO_EUR_RATE, RoundingMode.HALF_UP);
                    break;
                case USD_TO_BYN:
                    amountToReceive = amountToReceive.divide(StockExchangePlatform.USD_TO_BYN_RATE, RoundingMode.HALF_UP);
                    break;
                case BYN_TO_USD:
                    amountToReceive = amountToReceive.divide(StockExchangePlatform.BYN_TO_USD_RATE, RoundingMode.HALF_UP);
                    break;
                case USD_TO_EUR:
                    amountToReceive = amountToReceive.divide(StockExchangePlatform.USD_TO_EUR_RATE, RoundingMode.HALF_UP);
                    break;
                case EUR_TO_BYN:
                    amountToReceive = amountToReceive.divide(StockExchangePlatform.EUR_TO_BYN_RATE, RoundingMode.HALF_UP);
                    break;
                case EUR_TO_USD:
                    amountToReceive = amountToReceive.divide(StockExchangePlatform.EUR_TO_USD_RATE, RoundingMode.HALF_UP);
                    break;
                default:
                    amountToReceive = BigDecimal.valueOf(0).setScale(2, RoundingMode.DOWN);
                    break;
            }
            int numberOfParticipants = exchangePlatform.getParticipantsNumber();
            int participantToExchangeWith = decisionMaker.nextInt(numberOfParticipants);
            try {
                exchangePlatform.validateAndPerformTransaction(this, participantToExchangeWith, transactionToMake, amountToGive, amountToReceive);
            } catch (InterruptedException | TransactionException e) {
                LOGGER.error(e);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return id == that.id && usd.equals(that.usd) && eur.equals(that.eur) && byn.equals(that.byn);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (usd != null ? usd.hashCode() : 0);
        result = 31 * result + (eur != null ? eur.hashCode() : 0);
        result = 31 * result + (byn != null ? byn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Participant{" +
                "id=" + id +
                ", usd=" + usd +
                ", eur=" + eur +
                ", byn=" + byn +
                "}";
    }
}
