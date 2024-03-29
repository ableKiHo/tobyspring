package springbook.user.sqlservice;

public class SqlRetrievalFailureException extends RuntimeException {
    public SqlRetrievalFailureException(String s) {
        super(s);
    }

    public SqlRetrievalFailureException(Throwable throwable) {
        super(throwable);
    }

    public SqlRetrievalFailureException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
