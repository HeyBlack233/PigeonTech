package heyblack.pigeontech;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface Pigeon {
    String displayName();
    String desc();
    boolean notifyPlayers();
}
