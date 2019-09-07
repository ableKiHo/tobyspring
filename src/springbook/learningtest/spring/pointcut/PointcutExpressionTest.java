package springbook.learningtest.spring.pointcut;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PointcutExpressionTest {
    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int " +
                "springbook.learningtest.spring.pointcut.Target.minus(int,int) " +
                "throws java.lang.RuntimeException)" );

        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("minus", int.class, int.class), null), is(true));

        assertThat(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("plus", int.class, int.class), null ), is(false));

        assertThat(pointcut.getClassFilter().matches(Bean.class) &&
                pointcut.getMethodMatcher().matches(
                        Target.class.getMethod("method"), null) , is(false));
    }
}
