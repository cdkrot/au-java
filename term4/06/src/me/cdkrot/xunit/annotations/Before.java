package me.cdkrot.xunit.annotations;

import java.lang.annotation.*;

/**
 * Represents a method, which should be run before each test.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
    
}
