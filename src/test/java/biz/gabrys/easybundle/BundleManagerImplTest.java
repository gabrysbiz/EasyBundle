package biz.gabrys.easybundle;

import java.util.Locale;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public final class BundleManagerImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructs_factoryIsNull_throwIllegalAgrumentException() {
        new BundleManagerImpl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructs_localeIsNull_throwIllegalAgrumentException() {
        new BundleManagerImpl(Mockito.mock(BundleFactory.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLocale_localeIsNull_throwIllegalArgumentException() {
        final BundleManager manager = new BundleManagerImpl(Mockito.mock(BundleFactory.class));
        manager.setLocale(null);
    }

    @Test
    public void setLocale_correctLocales_reloadBundlesAndCallListenersMethods() {
        final Bundle bundle = Mockito.mock(Bundle.class);

        final BundleFactory factory = Mockito.mock(BundleFactory.class);
        Mockito.when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(bundle);
        Mockito.when(factory.create(Bundle.class, Locale.CANADA)).thenReturn(bundle);

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        // create bundle
        manager.getBundle(Bundle.class);

        final BundleReloadListener listener = Mockito.mock(BundleReloadListener.class);
        manager.register(listener);

        int times = 0;
        manager.setLocale(Locale.CANADA);
        Mockito.verify(bundle, Mockito.times(1)).setLocale(Locale.CANADA);
        Mockito.verify(listener, Mockito.times(++times)).onBundleReload();
        manager.setLocale(Locale.CANADA);
        Mockito.verify(bundle, Mockito.times(2)).setLocale(Locale.CANADA);
        Mockito.verify(listener, Mockito.times(++times)).onBundleReload();
        manager.setLocale(Locale.ENGLISH);
        Mockito.verify(bundle, Mockito.times(1)).setLocale(Locale.ENGLISH);
        Mockito.verify(listener, Mockito.times(++times)).onBundleReload();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBundle_interfaceClassIsNull_throwIllegalArgumentException() {
        new BundleManagerImpl(Mockito.mock(BundleFactory.class), Locale.ENGLISH).getBundle(null);
    }

    @Test
    public void getBundle_correctLocales_createManyBundles() {
        final Bundle bundleEnglish = Mockito.mock(Bundle.class);
        final Bundle bundleCanada = Mockito.mock(Bundle.class);

        final BundleFactory factory = Mockito.mock(BundleFactory.class);
        Mockito.when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(bundleEnglish);
        Mockito.when(factory.create(Bundle.class, Locale.CANADA)).thenReturn(bundleCanada);

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        Assertions.assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundleEnglish);
        Mockito.verify(factory).create(Bundle.class, Locale.ENGLISH);

        manager.setLocale(Locale.CANADA);
        Assertions.assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundleEnglish);
        Mockito.verify(factory, Mockito.never()).create(Bundle.class, Locale.CANADA);
    }

    @Test
    public void getBundle_correctLocale_oneBundle() {
        final BundleFactory factory = Mockito.mock(BundleFactory.class);
        Mockito.when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(Mockito.mock(Bundle.class));

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        manager.getBundle(Bundle.class);
        Mockito.verify(factory).create(Bundle.class, Locale.ENGLISH);
    }

    @Test
    public void getBundle_correctLocale_manyBundles() {
        final Bundle bundle = Mockito.mock(Bundle.class);

        final BundleFactory factory = Mockito.mock(BundleFactory.class);
        Mockito.when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(bundle);

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        Assertions.assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundle);
        Assertions.assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundle);
        Assertions.assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundle);
        Mockito.verify(factory).create(Bundle.class, Locale.ENGLISH);
    }

    @Test
    public void getBundle_correctLocales_createOneBundlePerClass() {
        final BundleFactory factory = Mockito.mock(BundleFactory.class);
        Mockito.when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(Mockito.mock(Bundle.class));

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        manager.getBundle(Bundle.class);
        manager.getBundle(Bundle.class);
        manager.getBundle(Bundle.class);
        Mockito.verify(factory).create(Bundle.class, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_interfaceClassIsNull_throwIllegalArgumentException() {
        new BundleManagerImpl(Mockito.mock(BundleFactory.class), Locale.ENGLISH).register(null);
    }

    @Test
    public void unregister_interfaceClassIsNull_success() {
        new BundleManagerImpl(Mockito.mock(BundleFactory.class), Locale.ENGLISH).unregister(null);
    }
}
