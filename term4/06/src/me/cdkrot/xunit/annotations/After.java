package me.cdkrot.xunit.annotations;

import java.lang.annotation.*;

/**
 * Represents a method, which should be run after each test.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
    
}
