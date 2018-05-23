package me.cdkrot.xunit.annotations;

import java.lang.annotation.*;

/**
 * Represents a method, which should be run after testing the class.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterClass {
    
}
