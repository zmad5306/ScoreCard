package dev.zachmaddox.scorecard.lib.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that contains only PROCESS logic for a score card action.
 * Invocation is blocked unless the associated score card is authorized to PROCESS.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcessAuthorized {
    boolean useJms() default false;
    boolean allowMissingHeader() default true;
}
