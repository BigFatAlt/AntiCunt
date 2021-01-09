package me.bigfatalt.anticheat.check.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckInfo {

    String name();
    String description();

    boolean enabled();
    boolean autoban();

    int maxVl();

    CheckType type();

}
