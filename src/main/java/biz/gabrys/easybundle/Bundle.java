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
 * The interface implemented by all bundles, which defines method to change bundle locale.
 * @since 1.0
 */
public interface Bundle {

    /**
     * Name of the method which is responsible for selecting bundle locale.
     * @since 1.0
     */
    String CHANGE_LANGUAGE_METHOD_NAME = "setLocale";

    /**
     * Sets the bundle locale.
     * @param locale the new locale.
     * @since 1.0
     */
    void setLocale(Locale locale);
}
