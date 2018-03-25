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

/**
 * Stores single instance of {@link BundleManager} which gets messages from property file. Programmer can use this
 * class, if there is no need for use more advanced technology (such as
 * <a href="http://en.wikipedia.org/wiki/Dependency_injection">dependency injection</a>).
 * @since 1.0
 * @see MultiplePropertyResourceBundleFactory
 */
public final class MultiplePropertyResourceBundleManagerStorage {

    private static final BundleManager INSTANCE = new BundleManagerImpl(new MultiplePropertyResourceBundleFactory());

    private MultiplePropertyResourceBundleManagerStorage() {
        // blocks the possibility of create a new instance
    }

    /**
     * Returns always the same instance of {@link BundleManager}.
     * @return instance of {@link BundleManager}.
     * @since 1.0
     */
    public static BundleManager getManager() {
        return INSTANCE;
    }
}
