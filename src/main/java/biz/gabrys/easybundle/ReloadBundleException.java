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
 * Thrown to indicate that an error occurred while reloading the bundle.
 * @since 1.0
 * @see BundleManager
 */
public class ReloadBundleException extends BundleException {

    private static final long serialVersionUID = 4521755560396085395L;

    /**
     * Constructs a new instance of {@link ReloadBundleException} with the specified detail message.
     * @param message the detail message.
     * @since 1.0
     */
    public ReloadBundleException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of {@link ReloadBundleException} with the specified cause.
     * @param cause the cause.
     * @since 1.0
     */
    public ReloadBundleException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new instance of {@link ReloadBundleException} with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause.
     * @since 1.0
     */
    public ReloadBundleException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
