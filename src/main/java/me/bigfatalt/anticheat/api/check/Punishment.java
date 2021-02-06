package me.bigfatalt.anticheat.api.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Punishment {
    int maxVL() default 20;
    boolean autoban() default false;
    PunishmentType punishment() default PunishmentType.KICK;


}
