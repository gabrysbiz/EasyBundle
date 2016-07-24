package biz.gabrys.easybundle;

import org.junit.Test;

public final class BundleValidatorTest {

    @Test(expected = InvalidInterfaceException.class)
    public void validate_class_throwInvalidInterfaceException() {
        BundleValidator.validateInterface(Clazz.class);
    }

    @Test
    public void validate_correctInterface_success() {
        BundleValidator.validateInterface(Correct.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_interfaceClassIsNull_throwIllegalArgumentException() {
        BundleValidator.validateInterface(null);
    }

    @Test(expected = InvalidInterfaceException.class)
    public void validate_incorrectInterfaceBecauseEmpty_throwInvalidInterfaceException() {
        BundleValidator.validateInterface(IncorrectBecauseEmpty.class);
    }

    @Test(expected = InvalidInterfaceException.class)
    public void validate_incorrectInterfaceBecausePrivate_throwInvalidInterfaceException() {
        BundleValidator.validateInterface(IncorrectBecausePrivate.class);
    }

    @Test(expected = InvalidInterfaceException.class)
    public void validate_incorrectInterfaceBecauseMethodRetrunObject_throwInvalidInterfaceException() {
        BundleValidator.validateInterface(IncorrectBecauseMethodRetrunObject.class);
    }

    @Test(expected = InvalidInterfaceException.class)
    public void validate_incorrectInterfaceBecauseMethodHasParameters_throwInvalidInterfaceException() {
        BundleValidator.validateInterface(IncorrectBecauseMethodHasParameters.class);
    }

    @Test(expected = InvalidInterfaceException.class)
    public void validate_incorrectInterfaceBecauseMethodHasWrongNameEqualget_throwInvalidInterfaceException() {
        BundleValidator.validateInterface(IncorrectBecauseMethodHasWrongName1.class);
    }

    @Test(expected = InvalidInterfaceException.class)
    public void validate_incorrectInterfaceBecauseMethodHasWrongNameEqualabc_throwInvalidInterfaceException() {
        BundleValidator.validateInterface(IncorrectBecauseMethodHasWrongName2.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isMethodCorrect_methodIsNull_throwIllegalArgumentException() {
        BundleValidator.isMethodCorrect(null);
    }

    public class Clazz {
    }

    public interface Correct {

        String getName();

        String getTitle();
    }

    public interface IncorrectBecauseEmpty {

    }

    private interface IncorrectBecausePrivate {

        String getName();
    }

    public interface IncorrectBecauseMethodRetrunObject {

        Object getName();
    }

    public interface IncorrectBecauseMethodHasParameters {

        String getName(Object param);
    }

    public interface IncorrectBecauseMethodHasWrongName1 {

        String get();
    }

    public interface IncorrectBecauseMethodHasWrongName2 {

        String abc();
    }
}
