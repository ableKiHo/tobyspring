package springbook.user.sqlservice;

public class SqlUpdateFailureException extends RuntimeException {
    public SqlUpdateFailureException() {
    }

    public SqlUpdateFailureException(String s) {
        super(s);
    }

    public SqlUpdateFailureException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SqlUpdateFailureException(Throwable throwable) {
        super(throwable);
    }
}
