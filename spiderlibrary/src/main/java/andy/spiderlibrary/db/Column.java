package andy.spiderlibrary.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by andyli on 2015/8/2.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    public String name() default "fieldName";
    public String type() default "fieldtype";
    public int index() default 0;
}

