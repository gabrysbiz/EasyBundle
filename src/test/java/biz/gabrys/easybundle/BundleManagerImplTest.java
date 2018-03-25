package biz.gabrys.easybundle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;

public final class BundleManagerImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructs_factoryIsNull_throwIllegalAgrumentException() {
        new BundleManagerImpl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructs_localeIsNull_throwIllegalAgrumentException() {
        new BundleManagerImpl(mock(BundleFactory.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLocale_localeIsNull_throwIllegalArgumentException() {
        final BundleManager manager = new BundleManagerImpl(mock(BundleFactory.class));
        manager.setLocale(null);
    }

    @Test
    public void setLocale_correctLocales_reloadBundlesAndCallListenersMethods() {
        final Bundle bundle = mock(Bundle.class);

        final BundleFactory factory = mock(BundleFactory.class);
        when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(bundle);
        when(factory.create(Bundle.class, Locale.CANADA)).thenReturn(bundle);

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        // create bundle
        manager.getBundle(Bundle.class);

        final BundleReloadListener listener = mock(BundleReloadListener.class);
        manager.register(listener);

        manager.setLocale(Locale.CANADA);
        verify(bundle).setLocale(Locale.CANADA);
        verify(listener).onBundleReload();
        manager.setLocale(Locale.CANADA);
        verify(bundle, times(2)).setLocale(Locale.CANADA);
        verify(listener, times(2)).onBundleReload();
        manager.setLocale(Locale.ENGLISH);
        verify(bundle).setLocale(Locale.ENGLISH);
        verify(listener, times(3)).onBundleReload();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBundle_interfaceClassIsNull_throwIllegalArgumentException() {
        new BundleManagerImpl(mock(BundleFactory.class), Locale.ENGLISH).getBundle(null);
    }

    @Test
    public void getBundle_correctLocales_createManyBundles() {
        final Bundle bundleEnglish = mock(Bundle.class);
        final Bundle bundleCanada = mock(Bundle.class);

        final BundleFactory factory = mock(BundleFactory.class);
        when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(bundleEnglish);
        when(factory.create(Bundle.class, Locale.CANADA)).thenReturn(bundleCanada);

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundleEnglish);
        verify(factory).create(Bundle.class, Locale.ENGLISH);

        manager.setLocale(Locale.CANADA);
        assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundleEnglish);
        verify(factory, never()).create(Bundle.class, Locale.CANADA);
    }

    @Test
    public void getBundle_correctLocale_oneBundle() {
        final BundleFactory factory = mock(BundleFactory.class);
        when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(mock(Bundle.class));

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        manager.getBundle(Bundle.class);
        verify(factory).create(Bundle.class, Locale.ENGLISH);
    }

    @Test
    public void getBundle_correctLocale_manyBundles() {
        final Bundle bundle = mock(Bundle.class);

        final BundleFactory factory = mock(BundleFactory.class);
        when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(bundle);

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundle);
        assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundle);
        assertThat(manager.getBundle(Bundle.class)).isEqualTo(bundle);
        verify(factory).create(Bundle.class, Locale.ENGLISH);
    }

    @Test
    public void getBundle_correctLocales_createOneBundlePerClass() {
        final BundleFactory factory = mock(BundleFactory.class);
        when(factory.create(Bundle.class, Locale.ENGLISH)).thenReturn(mock(Bundle.class));

        final BundleManager manager = new BundleManagerImpl(factory, Locale.ENGLISH);
        manager.getBundle(Bundle.class);
        manager.getBundle(Bundle.class);
        manager.getBundle(Bundle.class);
        verify(factory).create(Bundle.class, Locale.ENGLISH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_interfaceClassIsNull_throwIllegalArgumentException() {
        new BundleManagerImpl(mock(BundleFactory.class), Locale.ENGLISH).register(null);
    }

    @Test
    public void unregister_interfaceClassIsNull_success() {
        new BundleManagerImpl(mock(BundleFactory.class), Locale.ENGLISH).unregister(null);
    }
}
