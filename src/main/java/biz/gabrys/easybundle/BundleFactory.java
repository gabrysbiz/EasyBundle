/*
 * EasyBundle
 * http://easy-bundle.projects.gabrys.biz/
 *
 * Copyright (c) 2013 Adam Gabrys
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 * - a copy of the License at project page
 * - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.easybundle;

import java.util.Locale;

/**
 * Responsible for creating new instances of {@link Bundle} and message bundle interface specified by user. The correct
 * structure of user specified interface was described in documentation of {@link BundleValidator} class.
 * @since 1.0
 * @see Bundle
 * @see BundleValidator
 */
public interface BundleFactory {

    /**
     * Creates a new instance of {@link Bundle} which implements interface {@code interfaceClass}.
     * @param interfaceClass the bundle interface class which defines getters methods.
     * @param locale the locale for which a bundle is desired.
     * @return a new instance of {@link Bundle}.
     * @since 1.0
     * @see BundleValidator#validateInterface(Class)
     */
    Bundle create(Class<?> interfaceClass, Locale locale);
}
