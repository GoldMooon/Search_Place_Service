/**
 *
 */
package com.goldmooon.exception;

public class SearchException extends RuntimeException
{

    /**
     *
     */
    private static final long serialVersionUID = -6592629933070069593L;





    /**
     *
     */
    public SearchException()
    {
    }





    /**
     * @param message
     */
    public SearchException(final String message)
    {
        super(message);
    }





    /**
     * @param cause
     */
    public SearchException(final Throwable cause)
    {
        super(cause);
    }





    /**
     * @param message
     * @param cause
     */
    public SearchException(final String message, final Throwable cause)
    {
        super(message, cause);
    }





    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public SearchException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
