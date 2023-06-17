package com.driver.Exceptions;

public class ReservationException extends Exception{
    public ReservationException(){
        super("Cannot make reservation");
    }
}
