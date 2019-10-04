package springbook.user.sqlservice;

public class SqlNotFoundException extends Exception {
    public SqlNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SqlNotFoundException() {
    }

    public SqlNotFoundException(String s) {
        super(s);
    }

    public SqlNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
