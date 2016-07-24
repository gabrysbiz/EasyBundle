/*
 * EasyBundle
 * http://easy-bundle.projects.gabrys.biz/
 *
 * Copyright (c) 2013 Adam Gabryś
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 * - a copy of the License at project page
 * - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.easybundle;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * <p>
 * Standard implementation of {@link BundleFactory} which store translations in multiple properties files (one per
 * bundle). The name of the property file must be the same as the interface's name (plus additional information about
 * locale). Correct hierarchy of file names:
 * </p>
 * <ol>
 * <li>{@code interfaceName_language_country_variant.properties}</li>
 * <li>{@code interfaceName_language_country.properties}</li>
 * <li>{@code interfaceName_language.properties}</li>
 * <li>{@code interfaceName.properties}</li>
 * </ol>
 * <p>
 * Example: {@code Messages} is an interface.
 * </p>
 * <ul>
 * <li>{@code Messages_pl_PL_WINDOWS.properties}</li>
 * <li>{@code Messages_pl_PL.properties}</li>
 * <li>{@code Messages_pl.properties}</li>
 * <li>{@code Messages.properties}</li>
 * </ul>
 * <p>
 * The property file name should be localized at the same package as the user specified interface.
 * </p>
 * <p>
 * Example: If full name for the user specified interface is equal {@code org.example.Message}, then the property file
 * should be localized in the directory {@code example} which is subdirectory of {@code org}.
 * </p>
 * <p>
 * The content of the property file should have the following structure:
 * </p>
 * 
 * <pre>
 * #comment are available
 * key1=value1
 * key2=value2
 * key3=value3
 * </pre>
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * title=Bundle Factory 
 * topic=How to create property file
 * #comment
 * copyrightInformation=Copyright@ 2013, All rights Reserved
 * </pre>
 * <p>
 * How to use the factory:
 * </p>
 * 
 * <pre>
 * public interface Messages {
 *  
 *      String getTitle();
 *      
 *      String getTopic();
 *      
 *      String getCopyrightInformation();
 * }
 * 
 * final {@link BundleFactory} factory = new {@link MultiplePropertyResourceBundleFactory}();
 * final Messages bundle = (Messages) factory.{@link #create(Class, Locale) create}(Messages.class, {@link Locale}.{@link Locale#getDefault() getDefault()});
 * System.out.println(bundle.getTitle());
 * System.out.println(bundle.getTopic());
 * System.out.println(bundle.getCopyrightInformation());
 * </pre>
 * <p>
 * will return:
 * </p>
 * 
 * <pre>
 * Bundle Factory
 * How to create property file
 * Copyright@ 2013, All rights Reserved
 * </pre>
 * <p>
 * The factory creates a hierarchy of bundles which allows the sharing of values. This functionality will be presented
 * on the following example.
 * </p>
 * <p>
 * Example: An user defined two bundles {@code Messages.properties} and {@code Messages_pl.properties}. The files
 * content is equal:
 * </p>
 * <ul>
 * <li>{@code Messages.properties}:
 * 
 * <pre>
 * name=Name
 * topic=default
 * </pre>
 * 
 * </li>
 * <li>{@code Messages_pl.properties}:
 * 
 * <pre>
 * name=Nazwa
 * #topic is undefined
 * </pre>
 * 
 * </li>
 * </ul>
 * <p>
 * When the user will execute the following code:
 * </p>
 * 
 * <pre>
 * final {@link BundleFactory} factory = new {@link MultiplePropertyResourceBundleFactory}();
 * Messages bundle = (Messages) factory.{@link #create(Class, Locale) create}(Messages.class, {@link Locale#ENGLISH});
 * System.out.println(bundle.getName());
 * System.out.println(bundle.getTopic());
 * bundle = (Messages) factory.{@link #create(Class, Locale) create}(Messages.class, new {@link Locale}("pl"));
 * System.out.println(bundle.getName());
 * System.out.println(bundle.getTopic());
 * </pre>
 * <p>
 * He will get the following results:
 * </p>
 * 
 * <pre>
 * Name
 * default
 * Nazwa
 * default
 * </pre>
 * 
 * @since 1.0
 * @see BundleValidator
 * @see PropertyResourceBundleFactory
 */
public class MultiplePropertyResourceBundleFactory implements BundleFactory {

    /**
     * Constructs a new instance.
     * @since 1.0
     */
    public MultiplePropertyResourceBundleFactory() {
        // do nothing
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if the interface class is {@code null}.
     * @throws IllegalArgumentException if the locale is {@code null}.
     * @since 1.0
     * @see BundleValidator#validateInterface(Class)
     */
    @Override
    public Bundle create(final Class<?> interfaceClass, final Locale locale) {
        if (interfaceClass == null) {
            throw new IllegalArgumentException("Interface class cannot be null");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }

        return (Bundle) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass, Bundle.class },
                new MultiplePropertyResourceInvocationHandler(interfaceClass, locale));
    }

    private static final class MultiplePropertyResourceInvocationHandler implements InvocationHandler {

        private final Object mutex = new Object();

        private final Class<?> interfaceClass;
        private Locale locale;
        private ResourceBundle bundle;

        private MultiplePropertyResourceInvocationHandler(final Class<?> interfaceClass, final Locale locale) {
            this.interfaceClass = interfaceClass;
            this.locale = locale;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) {
            synchronized (mutex) {
                if (BundleValidator.isMethodCorrect(method)) {
                    return getValue(method.getName());

                } else if (Bundle.CHANGE_LANGUAGE_METHOD_NAME.equals(method.getName())) {
                    locale = (Locale) args[0];
                    bundle = null;
                    return null;
                }
            }

            throw new InvalidInterfaceException(
                    String.format("Definition of the bundle interface \"%s\" is invalid (unsupported method: \"%s\")",
                            interfaceClass.getName(), method.getName()));
        }

        private String getValue(final String methodName) {
            initBundle();

            String key = methodName.substring(BundleValidator.METHOD_NAME_PREFIX.length());
            key = key.substring(0, 1).toLowerCase(locale) + key.substring(1);
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            } else {
                throw new UndefinedTranslationException(
                        String.format("Cannot find the message associated with the key \"%s\" for locale \"%s\"", key, locale));
            }
        }

        private void initBundle() {
            if (bundle == null) {
                try {
                    bundle = ResourceBundle.getBundle(interfaceClass.getName(), locale, interfaceClass.getClassLoader(),
                            Control.getNoFallbackControl(Control.FORMAT_PROPERTIES));
                } catch (final MissingResourceException e) {
                    throw new ReloadBundleException(e);
                }
            }
        }
    }
}
