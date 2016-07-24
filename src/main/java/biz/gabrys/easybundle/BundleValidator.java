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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Contains method to checks whether interfaces are correct. Interface is correct when it meets following conditions:
 * <ul>
 * <li>is public</li>
 * <li>contains only getters method (methods without any parameters whose names start with word {@code get})</li>
 * <li>all methods return a {@link String} value</li>
 * </ul>
 * Example:
 * 
 * <pre>
 * public interface Messages {
 * 
 *     String getTitle();
 * 
 *     String getTopic();
 * 
 *     String getCopyrightInformation();
 * }
 * </pre>
 * 
 * @since 1.0
 */
public final class BundleValidator {

    /**
     * The correct name prefix for the methods defined in the interface.
     * @since 1.0
     */
    public static final String METHOD_NAME_PREFIX = "get";

    private BundleValidator() {
        // blocks the possibility of create a new instance
    }

    /**
     * Checks whether an interface is correct.
     * @param interfaceClass the tested interface.
     * @throws IllegalArgumentException if the interface class is {@code null}.
     * @throws InvalidInterfaceException if the interface declaration is incorrect.
     * @since 1.0
     */
    public static void validateInterface(final Class<?> interfaceClass) {
        if (interfaceClass == null) {
            throw new IllegalArgumentException("Interface class cannot be null");
        }

        if (!interfaceClass.isInterface()) {
            throw new InvalidInterfaceException("Given class \"" + interfaceClass.getName() + "\" is not an interface.");
        }
        if (!Modifier.isPublic(interfaceClass.getModifiers())) {
            throw new InvalidInterfaceException("Given interface \"" + interfaceClass.getName() + "\" must be public.");
        }
        if (interfaceClass.getMethods().length == 0) {
            throw new InvalidInterfaceException("Given interface \"" + interfaceClass.getName() + "\" has not specify any methods.");
        }
        for (final Method method : interfaceClass.getMethods()) {
            if (!isMethodCorrect(method)) {
                throw new InvalidInterfaceException("Given interface \"" + interfaceClass.getName()
                        + "\" must contain only public getters method that return simple string value.");
            }
        }
    }

    /**
     * Checks whether a method declaration is allowed in the correct interface.
     * @param method the tested method.
     * @return {@code true} whether the method declaration is allowed, otherwise {@code false}.
     * @throws IllegalArgumentException if the method is {@code null}.
     * @since 1.0
     */
    public static boolean isMethodCorrect(final Method method) {
        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }

        return method.getName().startsWith(METHOD_NAME_PREFIX) && method.getName().length() > METHOD_NAME_PREFIX.length()
                && String.class.equals(method.getReturnType()) && method.getParameterTypes().length == 0;
    }
}
