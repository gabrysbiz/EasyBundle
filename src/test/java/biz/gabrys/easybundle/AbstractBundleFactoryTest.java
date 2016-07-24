package biz.gabrys.easybundle;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public abstract class AbstractBundleFactoryTest {

    private static final Locale POLISH_LOCALE = new Locale("pl");

    private static final String NAME_ENG = "Name";
    private static final String NAME_PL = "Nazwa";
    private static final String DEFAULT = "default";
    private static final String A = "short";

    protected abstract BundleFactory createFactory();

    @Test
    public final void create_correctInterfaceAndLocale_bundleReturnsCorrectValue() {
        final CorrectBundleInterface bundle = (CorrectBundleInterface) createFactory().create(CorrectBundleInterface.class, Locale.ENGLISH);
        Assertions.assertThat(bundle.getName()).isEqualTo(NAME_ENG);
    }

    @Test
    public final void changeLocale_changeFromEnglishToPolish_bundleReturnsCorrectValue() {
        final Bundle bundle = createFactory().create(CorrectBundleInterface.class, Locale.ENGLISH);
        final CorrectBundleInterface bundleMessages = (CorrectBundleInterface) bundle;

        bundle.setLocale(POLISH_LOCALE);
        Assertions.assertThat(bundleMessages.getName()).isEqualTo(NAME_PL);
    }

    @Test
    public final void checkValueInheritance_englishLocale_bundleReturnsCorrectValue() {
        final Bundle bundle = createFactory().create(CorrectBundleInterface.class, Locale.ENGLISH);
        Assertions.assertThat(((CorrectBundleInterface) bundle).getDefault()).isEqualTo(DEFAULT);
    }

    @Test
    public final void checkValueInheritance_changeLocaleFromEnglishToPolish_bundleReturnsValueFromParent() {
        final Bundle bundle = createFactory().create(CorrectBundleInterface.class, Locale.ENGLISH);
        bundle.setLocale(POLISH_LOCALE);
        Assertions.assertThat(((CorrectBundleInterface) bundle).getDefault()).isEqualTo(DEFAULT);
    }

    @Test
    public final void getValue_bundleAndLocaleAreConstant_alwaysReturnsTheSameValue() {
        final CorrectBundleInterface bundle = (CorrectBundleInterface) createFactory().create(CorrectBundleInterface.class, Locale.ENGLISH);

        Assertions.assertThat(bundle.getName()).isEqualTo(NAME_ENG);
        Assertions.assertThat(bundle.getName()).isEqualTo(NAME_ENG);
        Assertions.assertThat(bundle.getName()).isEqualTo(NAME_ENG);
    }

    @Test
    public final void useShortKey_correctInterfaceAndLocale_correctValue() {
        final Bundle bundle = createFactory().create(CorrectBundleInterface.class, Locale.ENGLISH);
        Assertions.assertThat(((CorrectBundleInterface) bundle).getA()).isEqualTo(A);
    }

    @Test
    public final void checkBundleIndependence_factoryCreatesTwoBundlesWithDifferentLocale_bundlesReturnCorrectValues() {
        final BundleFactory factory = createFactory();
        final CorrectBundleInterface englishBundle = (CorrectBundleInterface) factory.create(CorrectBundleInterface.class, Locale.ENGLISH);
        final CorrectBundleInterface polishBundle = (CorrectBundleInterface) factory.create(CorrectBundleInterface.class, POLISH_LOCALE);
        Assertions.assertThat(englishBundle.getName()).isEqualTo(NAME_ENG);
        Assertions.assertThat(polishBundle.getName()).isEqualTo(NAME_PL);
    }

    @Test
    public final void checkBundleIndependence_factoryCreatesTwoBundlesWithEnglishLocaleButAfterChangeSecondBundleLocaleToPolish_bundlesReturnCorrectValues() {
        final BundleFactory factory = createFactory();
        final CorrectBundleInterface bundle1 = (CorrectBundleInterface) factory.create(CorrectBundleInterface.class, Locale.ENGLISH);
        final CorrectBundleInterface bundle2 = (CorrectBundleInterface) factory.create(CorrectBundleInterface.class, Locale.ENGLISH);

        ((Bundle) bundle2).setLocale(POLISH_LOCALE);
        Assertions.assertThat(bundle1.getName()).isEqualTo(NAME_ENG);
        Assertions.assertThat(bundle2.getName()).isEqualTo(NAME_PL);
    }

    @Test(expected = UndefinedTranslationException.class)
    public final void getUndefinedMessage_correctInterfaceAndLocale_throwUndefinedTranslationException() {
        final CorrectBundleInterface bundle = (CorrectBundleInterface) createFactory().create(CorrectBundleInterface.class, Locale.ENGLISH);
        bundle.getUndefined();
    }

    @Test(expected = InvalidInterfaceException.class)
    public final void checkInvalidGetMethod_incorrectInterface_throwInvalidInterfaceException() {
        final IncorrectBundleInterface bundle = (IncorrectBundleInterface) createFactory().create(IncorrectBundleInterface.class,
                Locale.ENGLISH);
        bundle.getData();
    }

    @Test(expected = InvalidInterfaceException.class)
    public final void checkInvalidGetMethodWithParams_incorrectInterface_throwInvalidInterfaceException() {
        final IncorrectBundleInterface bundle = (IncorrectBundleInterface) createFactory().create(IncorrectBundleInterface.class,
                Locale.ENGLISH);
        bundle.getData(0);
    }

    @Test(expected = InvalidInterfaceException.class)
    public final void checkInvalidNotGetMethod_incorrectInterface_throwInvalidInterfaceException() {
        final IncorrectBundleInterface bundle = (IncorrectBundleInterface) createFactory().create(IncorrectBundleInterface.class,
                Locale.ENGLISH);
        bundle.doSomething();
    }
}
