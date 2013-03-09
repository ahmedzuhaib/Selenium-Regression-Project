package com.examples.core;

import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;; 

/**
 * 
 * This interface is our custom annotation that will represent what browser(s) to test against
 * 
 * @author Mohammed Imran
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public  @interface Browsers {  
    String[] value();  
} 