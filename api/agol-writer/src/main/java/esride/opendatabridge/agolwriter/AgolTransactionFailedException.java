package esride.opendatabridge.agolwriter;

/**
 * User: nik
 * Date: 02.05.13
 * Time: 11:41
 */
public class AgolTransactionFailedException extends Exception {
    public AgolTransactionFailedException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolTransactionFailedException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolTransactionFailedException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public AgolTransactionFailedException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
