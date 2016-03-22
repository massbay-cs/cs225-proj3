package massbay.cs225.api.carbuild.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Inherited
public @interface CarField {
    String label();

    CarFieldType type();

    boolean readonly() default false;
}
