package com.chen.jeneral.annotations.xml;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Documented
@Inherited
public @interface Elements {

    String id() default "";

}
