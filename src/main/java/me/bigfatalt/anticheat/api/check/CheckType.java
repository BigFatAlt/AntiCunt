package me.bigfatalt.anticheat.api.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckType {

    String label();

    Category category();

    boolean enabled() default true;
    boolean experimental() default false;
}
