package springbook.learningtest.jdk.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

        Hello proxiedHello2 = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {Hello.class},
                (java.lang.reflect.InvocationHandler) new UppercaseHandler(new HelloTarget()));
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Kiho"), is("HELLO KIHO"));
        assertThat(proxiedHello.sayHi("Kiho"), is("HI KIHO"));
        assertThat(proxiedHello.sayThankyou("Kiho"), is("THANK YOU KIHO"));
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Kiho"), is("HELLO KIHO"));
        assertThat(proxiedHello.sayHi("Kiho"), is("HI KIHO"));
        assertThat(proxiedHello.sayThankyou("Kiho"), is("Thank You Kiho"));
    }

    @Test
    public void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
          public ClassFilter getClassFilter() {
            return new ClassFilter() {
                @Override
                public boolean matches(Class<?> aClass) {
                    return aClass.getSimpleName().startsWith("HelloT");
                }
            };
          }
        };

        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {};
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {};
        checkAdviced(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if(adviced){
            assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
            assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
            assertThat(proxiedHello.sayThankyou("Toby"),is("Thank You Toby"));
        }else {
            assertThat(proxiedHello.sayHello("Toby"), is("Hello Toby"));
            assertThat(proxiedHello.sayHi("Toby"), is("Hi Toby"));
            assertThat(proxiedHello.sayThankyou("Toby"),is("Thank You Toby"));
        }
    }

    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            String ret = (String) methodInvocation.proceed();
            return ret.toUpperCase();
        }
    }

    interface InvocationHandler {
        Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
    }

    class UppercaseHandler implements InvocationHandler {
        Hello hello;

        public UppercaseHandler(Hello hello) {
            this.hello = hello;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
            String ret = (String)method.invoke(hello, args);
            return ret.toUpperCase();
        }
    }

    interface Hello {
        String sayHello(String name);

        String sayHi(String name);

        String sayThankyou(String name);
    }

    class HelloTarget implements Hello {

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

    class HelloUppercase implements Hello {

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
