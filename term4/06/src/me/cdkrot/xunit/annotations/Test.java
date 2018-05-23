package me.cdkrot.xunit.annotations;

import java.lang.annotation.*;

/**
 * Represents a test method
 *
 * @param expected: to specify an expected thrown exception
 * @param ignore: specifies a reason why test is disabled
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
    class noneClass {};
    String noneString = "";
    
    Class expected() default noneClass.class;
    String ignore() default noneString;
}
