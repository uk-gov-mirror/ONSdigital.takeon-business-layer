package uk.gov.ons.collection.exception;

public class FormulaCalculationException extends Exception { 
    
    private static final long serialVersionUID = -2823503212948915075L;

    public FormulaCalculationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public FormulaCalculationException(String errorMessage) {
        super(errorMessage);
    }
    
}