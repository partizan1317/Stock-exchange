package com.epam.stockexchange.entity;

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


    @Override
    public void run() {
        throw new UnsupportedOperationException();
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
