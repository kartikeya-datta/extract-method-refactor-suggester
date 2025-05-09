error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10505.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10505.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10505.java
text:
```scala
c@@ls == null ? null:AccessController.doPrivileged(

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openjpa.lib.util;

import java.security.AccessController;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The Localizer provides convenient access to localized
 * strings. It inlcudes built-in support for parameter substitution through
 * the use of the {@link MessageFormat} utility.
 * Strings are stored in per-package {@link Properties} files.
 * The property file for the default locale must be named
 * <code>localizer.properties</code>. Additional locales can be supported
 * through additional property files using the naming conventions specified
 * in the {@link ResourceBundle} class. For example, the german locale
 * could be supported through a <code>localizer_de_DE.properties</code> file.
 *
 * @author Abe White
 */
public class Localizer {

    // static cache of package+loc name to localizer mappings
    private static final Map _localizers = new ConcurrentHashMap();

    // list of resource providers to delegate to when locating resources
    private static final Collection _providers = new CopyOnWriteArraySet
        (Arrays.asList(new Object[]{
            new SimpleResourceBundleProvider(),
            new StreamResourceBundleProvider(),
            new ZipResourceBundleProvider(), }));

    /**
     * Return a Localizer instance that will access the properties file
     * in the package of the given class using the system default locale.
     *
     * @see #forPackage(Class,Locale)
     */
    public static Localizer forPackage(Class cls) {
        return forPackage(cls, null);
    }

    /**
     * Return a Localizer instance that will access the properties file
     * in the package of the given class using the given locale.
     *
     * @param cls the class whose package to check for the localized
     * properties file; if null, the system will check for
     * a top-level properties file
     * @param locale the locale to which strings should be localized; if
     * null, the system default will be assumed
     */
    public static Localizer forPackage(Class cls, Locale locale) {
        if (locale == null)
            locale = Locale.getDefault();

        int dot = (cls == null) ? -1 : cls.getName().lastIndexOf('.');
        String pkg;
        String file;
        if (dot == -1) {
            pkg = "";
            file = "localizer";
        } else {
            pkg = cls.getName().substring(0, dot);
            file = pkg + ".localizer";
        }
        String key = file + locale.toString();

        // no locking; ok if bundle created multiple times
        // check for cached version
        Localizer loc = (Localizer) _localizers.get(key);
        if (loc != null)
            return loc;
        else {
            loc = new Localizer(pkg, file, locale,
                cls == null ? null:(ClassLoader) AccessController.doPrivileged(
                    J2DoPrivHelper.getClassLoaderAction(cls)));
            _localizers.put(key, loc);
            return loc;
        }
    }

    /**
     * Register a resource provider.
     */
    public static void addProvider(ResourceBundleProvider provider) {
        _providers.add(provider);
    }

    /**
     * Remove a resource provider.
     */
    public static boolean removeProvider(ResourceBundleProvider provider) {
        return _providers.remove(provider);
    }

    private String _file;
    private String _pkg;
    private ResourceBundle _bundle = null;
    private Locale _locale;
    private ClassLoader _loader;

    private Localizer(String pkg, String f, Locale locale, ClassLoader loader) {
        _pkg = pkg;
        _file = f;
        _locale = locale;
        _loader = loader;
    }

    private ResourceBundle getBundle() {
        // no locking; it's ok to create multiple bundles
        if (_bundle == null) {
            // find resource bundle
            for (Iterator itr = _providers.iterator();
                itr.hasNext() && _bundle == null; ) {
                _bundle = ((ResourceBundleProvider) itr.next())
                    .findResource(_file, _locale, _loader);
            }
        }
        return _bundle;
    }

    /**
     * Return the localized string matching the given key.
     */
    public Message get(String key) {
        return get(key, null);
    }

    /**
     * Return the localized string matching the given key.
     */
    public Message getFatal(String key) {
        return getFatal(key, null);
    }

    /**
     * Return the localized string matching the given key. The given
     * <code>sub</code> object will be packed into an array and substituted
     * into the found string according to the rules of the
     * {@link MessageFormat} class.
     *
     * @see #get(String)
     */
    public Message get(String key, Object sub) {
        return get(key, new Object[]{ sub });
    }

    /**
     * Return the localized string matching the given key. The given
     * <code>sub</code> object will be packed into an array and substituted
     * into the found string according to the rules of the
     * {@link MessageFormat} class.
     *
     * @see #getFatal(String)
     */
    public Message getFatal(String key, Object sub) {
        return getFatal(key, new Object[]{ sub });
    }

    /**
     * Return the localized string for the given key.
     *
     * @see #get(String,Object)
     */
    public Message get(String key, Object sub1, Object sub2) {
        return get(key, new Object[]{ sub1, sub2 });
    }

    /**
     * Return the localized string for the given key.
     *
     * @see #getFatal(String,Object)
     */
    public Message getFatal(String key, Object sub1, Object sub2) {
        return getFatal(key, new Object[]{ sub1, sub2 });
    }

    /**
     * Return the localized string for the given key.
     *
     * @see #get(String,Object)
     */
    public Message get(String key, Object sub1, Object sub2, Object sub3) {
        return get(key, new Object[]{ sub1, sub2, sub3 });
    }

    /**
     * Return the localized string matching the given key. The given
     * <code>subs</code> objects will be substituted
     * into the found string according to the rules of the
     * {@link MessageFormat} class.
     *
     * @see #get(String)
     */
    public Message get(String key, Object[] subs) {
        return new Message(_pkg, getBundle(), key, subs, false);
    }

    /**
     * Return the localized string matching the given key. The given
     * <code>subs</code> objects will be substituted
     * into the found string according to the rules of the
     * {@link MessageFormat} class.
     *
     * @see #getFatal(String)
     */
    public Message getFatal(String key, Object[] subs) {
        return new Message(_pkg, getBundle(), key, subs, true);
    }

    /**
     * A <code>Message</code> can provide a localized message via the
     * {@link #getMessage} method call, and can also provide the original key,
     * package, and substitution array that were used to assemble the message.
     */
    public static class Message {

        private final String _pkg;
        private final String _key;
        private final Object[] _subs;
        private final String _localizedMessage;

        private Message(String packageName, ResourceBundle bundle, String key,
            Object[] subs, boolean fatal) {
            if (bundle == null && fatal)
                throw new MissingResourceException(key, key, key);

            _pkg = packageName;
            _key = key;
            _subs = subs;
            if (bundle == null) {
                _localizedMessage = key;
            } else {
                String localized = null;
                try {
                    localized = bundle.getString(key);
                } catch (MissingResourceException mre) {
                    if (fatal)
                        throw mre;
                }
                _localizedMessage = (localized == null) ? key : localized;
            }
        }

        /**
         * The localized message.
         */
        public String getMessage() {
            return MessageFormat.format(_localizedMessage, _subs);
        }

        /**
         * The unique key for the localized message.
         */
        public String getKey() {
            return _key;
        }

        /**
         * Substitutions inserted into the message.
         */
        public Object[] getSubstitutions() {
            return _subs;
        }

        public String getPackageName() {
            return _pkg;
        }

        public String toString() {
            return getMessage();
        }
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10505.java