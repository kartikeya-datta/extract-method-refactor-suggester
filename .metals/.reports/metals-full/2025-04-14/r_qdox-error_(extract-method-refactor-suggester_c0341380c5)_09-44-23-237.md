error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10502.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10502.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10502.java
text:
```scala
l@@oader = AccessController.doPrivileged(

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
package org.apache.openjpa.lib.meta;

import java.security.AccessController;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.lib.util.Localizer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import serp.util.Strings;

/**
 * Custom SAX parser used by the system to quickly parse metadata files
 * for classes.
 *
 * @author Abe White
 * @nojavadoc
 */
public class CFMetaDataParser extends XMLMetaDataParser {

    static final String[] PACKAGES = new String[]{
        "java.lang.", "java.util.", "java.math." };
    private static final Localizer _loc = Localizer.forPackage
        (CFMetaDataParser.class);

    // the current package and class being parsed
    private String _package = null;
    private String _class = null;

    public CFMetaDataParser() {
        setParseText(false);
    }

    /**
     * The name of the package element. Defaults to "package".
     */
    protected boolean isPackageElementName(String name) {
        return "package".equals(name);
    }

    /**
     * The attribute of the package element that holds the name, or null to
     * use the element text. Defaults to "name".
     */
    protected String getPackageAttributeName() {
        return "name";
    }

    /**
     * The depth of the package element. Defaults to 1.
     */
    protected int getPackageElementDepth() {
        return 1;
    }

    /**
     * The name of the class element. Defaults to "class".
     */
    protected boolean isClassElementName(String name) {
        return "class".equals(name);
    }

    /**
     * The attribute of the class element that holds the name, or null to
     * use the element text. Defaults to "name".
     */
    protected String getClassAttributeName() {
        return "name";
    }

    /**
     * The depth of the class element. Defaults to 2.
     */
    protected int getClassElementDepth() {
        return 2;
    }

    protected boolean startElement(String name, Attributes attrs)
        throws SAXException {
        // skip root element
        int depth = currentDepth();
        if (depth == 0)
            return true;

        try {
            if (depth == getPackageElementDepth()
                && isPackageElementName(name))
                return startPackage(name, attrs);
            if (depth == getClassElementDepth() && isClassElementName(name))
                return startClass(name, attrs);
            if (depth > getClassElementDepth() && _class != null
                && getClassAttributeName() != null)
                return startClassElement(name, attrs);
            if (depth > getPackageElementDepth() && _package != null
                && getPackageAttributeName() != null)
                return startPackageElement(name, attrs);
            return startSystemElement(name, attrs);
        } catch (SAXException se) {
            throw se;
        } catch (NullPointerException npe) {
            throw getException(_loc.get("parse-error", name), npe);
        }
    }

    protected void endElement(String name) throws SAXException {
        // skip root element
        int depth = currentDepth();
        if (depth == 0)
            return;

        try {
            if (depth == getPackageElementDepth()
                && isPackageElementName(name))
                endPackage(name);
            else if (depth == getClassElementDepth()
                && isClassElementName(name))
                endClass(name);
            else if (depth > getClassElementDepth() && _class != null
                && getClassAttributeName() != null)
                endClassElement(name);
            else if (depth > getPackageElementDepth() && _package != null
                && getPackageAttributeName() != null)
                endPackageElement(name);
            else
                endSystemElement(name);
        } catch (SAXException se) {
            throw se;
        } catch (NullPointerException npe) {
            throw getException(_loc.get("parse-error", name), npe);
        }
    }

    /**
     * Start a package. Parses out package attribute by default.
     * Return false to skip package element and its contents.
     */
    protected boolean startPackage(String elem, Attributes attrs)
        throws SAXException {
        if (getPackageAttributeName() != null) {
            _package = attrs.getValue(getPackageAttributeName());
            if (_package == null)
                _package = "";
        }
        return true;
    }

    /**
     * End a package. Parses contained text by default.
     */
    protected void endPackage(String elem) {
        if (getPackageAttributeName() != null)
            _package = null;
        else
            _package = currentText();
    }

    /**
     * Start a class. Parses out class name by default. Return
     * false to skip class element and its contents.
     */
    protected boolean startClass(String elem, Attributes attrs)
        throws SAXException {
        if (getClassAttributeName() != null) {
            _class = attrs.getValue(getClassAttributeName());
            if (!StringUtils.isEmpty(_package) && _class.indexOf('.') == -1)
                _class = _package + "." + _class;
        }
        return true;
    }

    /**
     * End a class. Parses contained text by default.
     */
    protected void endClass(String elem) throws SAXException {
        if (getClassAttributeName() != null)
            _class = null;
        else {
            _class = currentText();
            if (!StringUtils.isEmpty(_package) && _class.indexOf('.') == -1)
                _class = _package + "." + _class;
        }
    }

    /**
     * Override this method marking the start of an element outside of any
     * package or class.
     */
    protected boolean startSystemElement(String name, Attributes attrs)
        throws SAXException {
        return false;
    }

    /**
     * Override this method marking the end of an element outside of any
     * package or class.
     */
    protected void endSystemElement(String name) throws SAXException {
    }

    /**
     * Override this method marking the start of an element within a declared
     * package.
     */
    protected boolean startPackageElement(String name, Attributes attrs)
        throws SAXException {
        return false;
    }

    /**
     * Override this method marking the end of an element within a declared
     * package.
     */
    protected void endPackageElement(String name) throws SAXException {
    }

    /**
     * Override this method marking the start of an element within a declared
     * class.
     */
    protected boolean startClassElement(String name, Attributes attrs)
        throws SAXException {
        return false;
    }

    /**
     * Override this method marking the end of an element within a declared
     * class.
     */
    protected void endClassElement(String name) throws SAXException {
    }

    /**
     * Override this method to clear any state and ready the parser for
     * a new document. Subclasses should call
     * <code>super.reset()</code> to clear superclass state.
     */
    protected void reset() {
        super.reset();
        _package = null;
        _class = null;
    }

    /**
     * Return the current class being parsed; the returned name will
     * be fully qualified.
     */
    protected String currentClassName() {
        return _class;
    }

    /**
     * Return the current package being parsed.
     */
    protected String currentPackage() {
        return _package;
    }

    /**
     * Helper method to create the {@link Class} for the given name,
     * taking into account the package currently being parsed for relative
     * class names.
     */
    protected Class classForName(String name, boolean resolve)
        throws SAXException {
        if (name == null)
            return null;
        Class cls = classForName(name, _package, resolve, currentClassLoader());
        if (cls == null)
            throw getException(_loc.get("invalid-class", name).getMessage());
        return cls;
    }

    /**
     * Load the given class name against the given package and the set
     * of accepted standard packages. Return null if the class cannot be loaded.
     */
    public static Class classForName(String name, String pkg,
        boolean resolve, ClassLoader loader) {
        if (StringUtils.isEmpty(name))
            return null;

        if (loader == null)
            loader = (ClassLoader) AccessController.doPrivileged(
                J2DoPrivHelper.getContextClassLoaderAction());
        boolean fullName = name.indexOf('.') != -1;
        boolean noPackage = StringUtils.isEmpty(pkg);
        try {
            if (fullName || noPackage)
                return Strings.toClass(name, resolve, loader);
            return Strings.toClass(pkg + "." + name, resolve, loader);
        } catch (RuntimeException re) {
        }

        // if not a full name, now try the name without a package
        if (!fullName && !noPackage) {
            try {
                return Strings.toClass(name, resolve, loader);
            } catch (RuntimeException re) {
            }
        }

        // try with standard packages
        if (!fullName) {
            for (int i = 0; i < PACKAGES.length; i++) {
                try {
                    return Strings.toClass(PACKAGES[i] + name, resolve, loader);
                } catch (RuntimeException re) {
                }
            }
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10502.java