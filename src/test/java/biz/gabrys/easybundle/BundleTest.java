package biz.gabrys.easybundle;

import java.lang.reflect.Method;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public final class BundleTest {

    @Test
    public void checkMethodsQuantity() {
        final Method[] methods = Bundle.class.getDeclaredMethods();
        Assertions.assertThat(methods).hasSize(1);
    }

    @Test
    public void checkMethodSignature() throws NoSuchMethodException, SecurityException {
        final Method method = Bundle.class.getDeclaredMethod(Bundle.CHANGE_LANGUAGE_METHOD_NAME, Locale.class);
        Assertions.assertThat(method).isNotNull();
    }
}
