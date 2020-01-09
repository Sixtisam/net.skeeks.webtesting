package net.skeeks.webtesting;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Test methods annotated with this method will not be executed in the chrome
 * browser
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoChrome {

}
