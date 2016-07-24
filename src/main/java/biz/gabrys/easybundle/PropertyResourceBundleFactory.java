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
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * Standard implementation of {@link BundleFactory} which store translations in "one" property file (one for all
 * bundles). The path to the property file must be the same as the file path prefix plus additional information about
 * locale. Correct hierarchy of file names:
 * </p>
 * <ol>
 * <li>{@code fileName_language_country_variant.properties}</li>
 * <li>{@code fileName_language_country.properties}</li>
 * <li>{@code fileName_language.properties}</li>
 * <li>{@code fileName.properties}</li>
 * </ol>
 * <p>
 * Example: file path prefix is equal {@code directory/languages}.
 * </p>
 * <ul>
 * <li>{@code directory/languages_pl_PL_WINDOWS.properties}</li>
 * <li>{@code directory/languages_pl_PL.properties}</li>
 * <li>{@code directory/languages_pl.properties}</li>
 * <li>{@code directory/languages.properties}</li>
 * </ul>
 * <p>
 * The content of the property file should have the following structure:
 * </p>
 * 
 * <pre>
 * #comment are available
 * interfaceName1.key1=value1
 * interfaceName1.key2=value2
 * interfaceName2.key1=value3
 * </pre>
 * <p>
 * *InterfaceName means package name plus class name, e.g. {@code org.example.Messages}.
 * </p>
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * org.example.Messages.title=Bundle Factory 
 * org.example.Messages.topic=How to create property file
 * #comment
 * org.example.Copyright.information=Copyright@ 2013, All rights Reserved
 * </pre>
 * <p>
 * How to use the factory:
 * </p>
 * 
 * <pre>
 * package org.example;
 * 
 * public interface Messages {
 *  
 *      String getTitle();
 *      
 *      String getTopic();
 * }
 * 
 * public interface Copyright {
 *  
 *      String getInformation();
 * }
 * 
 * final String filePathPrefix = "directory/languages";
 * final {@link BundleFactory} factory = new {@link PropertyResourceBundleFactory}(filePathPrefix);
 * final Messages messagesBundle =
 *  (Messages) factory.{@link #create(Class, Locale) create}(Messages.class, {@link Locale}.{@link Locale#getDefault() getDefault()});
 * final Copyright copyrightBundle =
 *  (Copyright) factory.{@link #create(Class, Locale) create}(Copyright.class, {@link Locale}.{@link Locale#getDefault() getDefault()});
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
 * Example: An user defined two bundles {@code languages.properties} and {@code languages_pl.properties}. The files
 * content is equal:
 * </p>
 * <ul>
 * <li>{@code languages.properties}:
 * 
 * <pre>
 * org.example.Messages.name=Name
 * org.example.Messages.topic=default
 * </pre>
 * 
 * </li>
 * <li>{@code languages_pl.properties}:
 * 
 * <pre>
 * org.example.Messages.name=Nazwa
 * #org.example.Messages is undefined
 * </pre>
 * 
 * </li>
 * </ul>
 * <p>
 * When the user will execute the following code:
 * </p>
 * 
 * <pre>
 * final String filePathPrefix = "directory/languages";
 * final {@link BundleFactory} factory = new {@link PropertyResourceBundleFactory}(filePathPrefix);
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
 * @see MultiplePropertyResourceBundleFactory
 */
public class PropertyResourceBundleFactory implements BundleFactory {

    private final Map<Locale, ResourceBundle> bundles;
    private final String filePathPrefix;

    /**
     * Constructs a new instance of {@link PropertyResourceBundleFactory} and sets file path prefix.
     * @param filePathPrefix the file path prefix.
     * @throws IllegalArgumentException if the file path prefix is {@code null}.
     * @since 1.0
     */
    public PropertyResourceBundleFactory(final String filePathPrefix) {
        if (filePathPrefix == null) {
            throw new IllegalArgumentException("File path prefix cannot be null");
        }

        this.filePathPrefix = filePathPrefix;
        bundles = new ConcurrentHashMap<Locale, ResourceBundle>();
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
                new PropertyResourceInvocationHandler(interfaceClass, locale));
    }

    private final class PropertyResourceInvocationHandler implements InvocationHandler {

        private final Class<?> interfaceClass;
        private Locale locale;

        public PropertyResourceInvocationHandler(final Class<?> interfaceClass, final Locale locale) {
            this.interfaceClass = interfaceClass;
            this.locale = locale;
        }

        @Override
        public synchronized Object invoke(final Object proxy, final Method method, final Object[] args) {
            if (BundleValidator.isMethodCorrect(method)) {
                return getValue(method.getName());

            } else if (Bundle.CHANGE_LANGUAGE_METHOD_NAME.equals(method.getName())) {
                locale = (Locale) args[0];
                return null;
            }

            throw new InvalidInterfaceException("Definition of the bundle interface \"" + interfaceClass.getName()
                    + "\" is invalid (unsupported method: \"" + method.getName() + "\")");
        }

        private String getValue(final String methodName) {
            final ResourceBundle bundle = getBundle();

            String key = methodName.substring(BundleValidator.METHOD_NAME_PREFIX.length());
            key = key.substring(0, 1).toLowerCase(locale) + key.substring(1);
            key = interfaceClass.getName() + '.' + key;
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            } else {
                throw new UndefinedTranslationException(
                        "Cannot find the message associated with the key \"" + key + "\" for locale \"" + locale + '"');
            }
        }

        private ResourceBundle getBundle() {
            ResourceBundle bundle = bundles.get(locale);
            if (bundle == null) {
                synchronized (PropertyResourceBundleFactory.this) {
                    bundle = bundles.get(locale);
                    if (bundle == null) {
                        try {
                            bundle = ResourceBundle.getBundle(filePathPrefix, locale,
                                    Control.getNoFallbackControl(Control.FORMAT_PROPERTIES));
                            bundles.put(locale, bundle);
                        } catch (final MissingResourceException e) {
                            throw new ReloadBundleException(e);
                        }
                    }
                }
            }
            return bundle;
        }
    }
}
