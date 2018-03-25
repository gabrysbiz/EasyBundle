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
 * <p>
 * Responsible for the management of bundles (returning and changing locale). Also contains methods to manage
 * {@link BundleReloadListener listeners} which will be informed about the bundles changes.
 * </p>
 * <p>
 * How to use the manager:
 * </p>
 * <ol>
 * <li>Define {@link BundleValidator correct interface} for the bundle:
 * 
 * <pre>
 * public interface Messages {
 * 
 *     String getTitle();
 * }
 * </pre>
 * 
 * To check whether the interface is correct you can add a <a href="http://junit.org/">JUnit</a> test:
 * 
 * <pre>
 * public final MessagesTest {
 *      
 *      &#64;Test
 *      public void isCorrectInterface() {
 *         {@link BundleValidator}.{@link BundleValidator#validateInterface(Class) validateInterface}(Messages.class);
 *      }
 * }
 * </pre>
 * 
 * </li>
 * <li>Create a new instance of {@link BundleManager} (e.g. {@link BundleManagerImpl}) with specified implementation of
 * {@link BundleFactory} (e.g. {@link MultiplePropertyResourceBundleFactory}) - before define translations in accordance
 * with the guidelines of the factory:
 * 
 * <pre>
 * final {@link BundleFactory} factory = new {@link MultiplePropertyResourceBundleFactory}();
 * final {@link BundleManager} manager = new {@link BundleManagerImpl}(factory);
 * </pre>
 * <p>
 * For simplicity, we use {@link MultiplePropertyResourceBundleManagerStorage}.
 * </p>
 * </li>
 * <li>Get the bundle and register listener:
 * 
 * <pre>
 * public class FrameImpl extends Frame implements {@link BundleReloadListener} {
 *      private static final Messages BUNDLE =
 *          {@link MultiplePropertyResourceBundleManagerStorage#getManager()}.{@link #getBundle(Class) getBundle}(Messages.class);
 *       
 *      public FrameImpl() {
 *          {@link MultiplePropertyResourceBundleManagerStorage#getManager()}.{@link #register(BundleReloadListener) register}(this);
 *          onBundleReload();
 *      }
 *      
 *      public void {@link BundleReloadListener#onBundleReload onBundleReload}() {
 *          this.setTitle(BUNDLE.getTitle());
 *      }
 *      
 *      public void destroy() {
 *          {@link MultiplePropertyResourceBundleManagerStorage#getManager()}.{@link #unregister(BundleReloadListener) unregister}(this);
 *      }
 * }
 * </pre>
 * 
 * </li>
 * <li>Now you can change the locale of displayed frame:
 * 
 * <pre>
 * final Frame frame = new FrameImpl();
 * frame.show();
 * // change to english
 * {@link MultiplePropertyResourceBundleManagerStorage#getManager()}.{@link #setLocale(Locale) setLocale}({@link Locale#ENGLISH Locale.ENGLISH}));
 * // do something...
 * 
 * // change to polish
 * {@link MultiplePropertyResourceBundleManagerStorage#getManager()}.{@link #setLocale(Locale) setLocale}(new {@link Locale Locale}("pl"));
 * // do something...
 * </pre>
 * 
 * </li>
 * </ol>
 * @since 1.0
 * @see BundleFactory
 * @see BundleValidator
 * @see MultiplePropertyResourceBundleFactory
 * @see PropertyResourceBundleFactory
 */
public interface BundleManager {

    /**
     * Sets a new locale for all bundles (those created and which will be created) and after the successful set - reload
     * all listeners.
     * @param locale the new locale.
     * @throws ReloadBundleException if error occurred while reloading the bundles.
     * @throws UndefinedTranslationException if a bundle cannot find a translation for specified key while reloading the
     *             listener.
     * @since 1.0
     * @see #getLocale()
     * @see #register(BundleReloadListener)
     */
    void setLocale(Locale locale);

    /**
     * Returns a current manager locale.
     * @return the current manager locale.
     * @since 1.0
     * @see #setLocale(Locale)
     */
    Locale getLocale();

    /**
     * Returns a bundle for specified interface. The manager does not check whether passed parameter is
     * {@link BundleValidator correct interface}.
     * @param interfaceClass the bundle interface class which defines getters methods.
     * @param <E> the bundle interface which defines getters methods.
     * @return the bundle for specified interface.
     * @since 1.0
     * @see BundleValidator#validateInterface(Class)
     */
    <E> E getBundle(Class<E> interfaceClass);

    /**
     * Adds a {@link BundleReloadListener} to the listener list.
     * @param listener the {@link BundleReloadListener} to be added.
     * @since 1.0
     */
    void register(BundleReloadListener listener);

    /**
     * Removes a {@link BundleReloadListener} from the listener list.
     * @param listener the {@link BundleReloadListener} to be removed.
     * @since 1.0
     */
    void unregister(BundleReloadListener listener);

    /**
     * Removes all listeners from the listener list.
     * @since 1.0
     */
    void unregisterAll();
}
