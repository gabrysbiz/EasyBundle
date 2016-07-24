package biz.gabrys.easybundle;

import java.util.Locale;

import org.junit.Test;

public final class MultiplePropertyResourceBundleFactoryTest extends AbstractBundleFactoryTest {

    @Override
    protected BundleFactory createFactory() {
        return new MultiplePropertyResourceBundleFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_interaceClassIsNull_throwIllegalArgumentException() {
        createFactory().create(null, Locale.getDefault());
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_localeIsNull_throwIllegalArgumentException() {
        createFactory().create(CorrectBundleInterface.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_interfaceClassAndLocaleEqualNull_throwIllegalArgumentException() {
        createFactory().create(null, null);
    }
}
