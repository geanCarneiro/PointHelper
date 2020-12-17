package persistencia;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Coluna {
    String nome() default "";
    String tipo();
}
