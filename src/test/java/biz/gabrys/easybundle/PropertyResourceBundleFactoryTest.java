package biz.gabrys.easybundle;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Locale;

import org.junit.Test;

public final class PropertyResourceBundleFactoryTest extends AbstractBundleFactoryTest {

    private static final String FILE_NAME = PropertyResourceBundleFactoryTest.class.getPackage().getName().replace('.', File.separatorChar)
            + File.separatorChar + "languages";

    private static final Locale LOCALE_FOR_NOT_EXIST_FILE = new Locale("en");
    private static final String NOT_EXIST_FILE_NAME_PREFIX = FILE_NAME + "not-exist";
    private static final String NOT_EXIST_FILE_NAME_SUFFIX = ".properties";

    @Override
    protected BundleFactory createFactory() {
        return new PropertyResourceBundleFactory(FILE_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void contructs_filePathPrefixIsNull_throwIllegalArgumentException() {
        new PropertyResourceBundleFactory(null);
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

    @Test
    public void filesWithTranslationsCannotExist() {
        assertThat(new File(NOT_EXIST_FILE_NAME_PREFIX + '_' + LOCALE_FOR_NOT_EXIST_FILE + NOT_EXIST_FILE_NAME_SUFFIX).exists()).isFalse();
        assertThat(new File(NOT_EXIST_FILE_NAME_PREFIX).exists()).isFalse();
    }

    @Test(expected = ReloadBundleException.class)
    public void getValue_invalidFile_throwReloadBundleException() {
        final BundleFactory factory = new PropertyResourceBundleFactory(NOT_EXIST_FILE_NAME_PREFIX);
        final CorrectBundleInterface bundle = (CorrectBundleInterface) factory.create(CorrectBundleInterface.class,
                LOCALE_FOR_NOT_EXIST_FILE);
        bundle.getA();
    }
}
