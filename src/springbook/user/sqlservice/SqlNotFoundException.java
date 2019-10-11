package springbook.user.sqlservice;

public class SqlNotFoundException extends RuntimeException {
    public SqlNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SqlNotFoundException() {
        super();
    }

    public SqlNotFoundException(String s) {
        super(s);
    }

    public SqlNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
