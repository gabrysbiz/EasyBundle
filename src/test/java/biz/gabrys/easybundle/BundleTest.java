package biz.gabrys.easybundle;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Locale;

import org.junit.Test;

public final class BundleTest {

    @Test
    public void checkMethodsQuantity() {
        final Method[] methods = Bundle.class.getDeclaredMethods();
        assertThat(methods).hasSize(1);
    }

    @Test
    public void checkMethodSignature() throws NoSuchMethodException, SecurityException {
        final Method method = Bundle.class.getDeclaredMethod(Bundle.CHANGE_LANGUAGE_METHOD_NAME, Locale.class);
        assertThat(method).isNotNull();
    }
}
