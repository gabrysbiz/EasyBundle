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
 * Parent class for all exceptions associated with bundles.
 * @since 1.0
 */
public class BundleException extends RuntimeException {

    private static final long serialVersionUID = 7442025308696585863L;

    /**
     * Constructs a new instance of {@link BundleException} with the specified detail message.
     * @param message the detail message.
     * @since 1.0
     */
    protected BundleException(final String message) {
        super(message);
    }

    /**
     * Constructs a new instance of {@link BundleException} with the specified cause.
     * @param cause the cause.
     * @since 1.0
     */
    protected BundleException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new instance of {@link BundleException} with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause.
     * @since 1.0
     */
    protected BundleException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
