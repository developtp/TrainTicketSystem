package exceptions;

/**
 * Custom checked exception representing failures that occur during the ticket issuance process.
 */
public class TicketIssuanceException extends Exception {
    
    public TicketIssuanceException(String message) {
        super(message);
    }
    
    public TicketIssuanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
