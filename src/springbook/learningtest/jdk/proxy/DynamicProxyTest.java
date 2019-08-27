package springbook.learningtest.jdk.proxy;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DynamicProxyTest {

    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Kiho"), is("Hello Kiho"));
        assertThat(hello.sayHi("Kiho"), is("Hi Kiho"));
        assertThat(hello.sayThankyou("Kiho"), is("Thank You Kiho"));

        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Kiho"), is("HELLO KIHO"));
        assertThat(proxiedHello.sayHi("Kiho"), is("HI KIHO"));
        assertThat(proxiedHello.sayThankyou("Kiho"), is("THANK YOU KIHO"));
    }

    static interface Hello {
        String sayHello(String name);
        String sayHi(String name);
        String sayThankyou(String name);
    }

    static class HelloTarget implements Hello {

        @Override
        public String sayHello(String name) {
            return "Hello " + name;
        }

        @Override
        public String sayHi(String name) {
            return "Hi " + name;
        }

        @Override
        public String sayThankyou(String name) {
            return "Thank You " + name;
        }
    }

    static class HelloUppercase implements Hello {

        Hello hello;

        public HelloUppercase(Hello hello) {
            this.hello = hello;
        }

        @Override
        public String sayHello(String name) {
            return hello.sayHello(name).toUpperCase();
        }

        @Override
        public String sayHi(String name) {
            return hello.sayHi(name).toUpperCase();
        }

        @Override
        public String sayThankyou(String name) {
            return hello.sayThankyou(name).toUpperCase();
        }
    }
}
