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

/**
 * The listener interface for receiving bundle reload events.
 * @since 1.0
 */
public interface BundleReloadListener {

    /**
     * Invoked when the bundle has been reloaded.
     * @since 1.0
     */
    void onBundleReload();
}
