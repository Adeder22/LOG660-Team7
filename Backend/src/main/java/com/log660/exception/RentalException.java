package com.log660.exception;

/**
 * Location refusée par une règle d'affaires (limite du forfait, aucune copie disponible, etc.).
 * Le message est destiné au client et peut être affiché tel quel.
 */
public class RentalException extends RuntimeException {

    public RentalException(String message) {
        super(message);
    }
}
