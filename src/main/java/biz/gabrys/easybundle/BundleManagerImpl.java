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

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link BundleManager}.
 * @since 1.0
 */
public class BundleManagerImpl implements BundleManager {

    private final BundleFactory factory;
    private final Map<Class<?>, Bundle> bundles;
    private final Set<BundleReloadListener> listeners;
    private Locale currentLocale;

    /**
     * Constructs a new instance of {@link BundleManagerImpl} and sets current locale with value of default locale for
     * this instance of the Java Virtual Machine.
     * @param factory bundle factory.
     * @throws IllegalArgumentException if the factory is {@code null}.
     * @since 1.0
     */
    public BundleManagerImpl(final BundleFactory factory) {
        this(factory, Locale.getDefault());
    }

    /**
     * Constructs a new instance of {@link BundleManagerImpl} and sets current locale.
     * @param factory bundle factory.
     * @param locale the current locale.
     * @throws IllegalArgumentException if the factory is {@code null}.
     * @throws IllegalArgumentException if the locale is {@code null}.
     * @since 1.0
     */
    public BundleManagerImpl(final BundleFactory factory, final Locale locale) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }

        bundles = new ConcurrentHashMap<Class<?>, Bundle>();
        listeners = new HashSet<BundleReloadListener>();
        this.factory = factory;
        currentLocale = locale;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if the locale is {@code null}.
     * @since 1.0
     * @see #getLocale()
     * @see #register(BundleReloadListener)
     */
    @Override
    public synchronized void setLocale(final Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale cannot be null");
        }

        for (final Bundle bundle : bundles.values()) {
            bundle.setLocale(locale);
        }
        currentLocale = locale;
        for (final BundleReloadListener listener : listeners) {
            listener.onBundleReload();
        }
    }

    /**
     * {@inheritDoc}
     * @since 1.0
     * @see #setLocale(Locale)
     */
    @Override
    public synchronized Locale getLocale() {
        return currentLocale;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if the interface class in {@code null}.
     * @since 1.0
     * @see BundleValidator#validateInterface(Class)
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized <E> E getBundle(final Class<E> interfaceClass) {
        if (interfaceClass == null) {
            throw new IllegalArgumentException("Interface class cannot be null");
        }

        if (bundles.containsKey(interfaceClass)) {
            return (E) bundles.get(interfaceClass);
        }

        final Bundle bundle = factory.create(interfaceClass, currentLocale);
        bundles.put(interfaceClass, bundle);
        return (E) bundle;
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if the listener is {@code null}.
     * @since 1.0
     */
    @Override
    public synchronized void register(final BundleReloadListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }

        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     * @since 1.0
     */
    @Override
    public synchronized void unregister(final BundleReloadListener listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     * @since 1.0
     */
    @Override
    public synchronized void unregisterAll() {
        listeners.clear();
    }
}
