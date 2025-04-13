error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5438.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5438.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5438.java
text:
```scala
public static final S@@tring KEYSTORE_TYPE = "JCEKS";

/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.utils;

/**
 * <p>This utility class defines global variables and their values for the XML Security Tools.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public final class Globals {
    /**
     * Utility class, no instance allowed.
     */
    private Globals() {
    }

    /** Maximum number of characters of signature and encryption ID. */
    public static final int ID_LIMIT = 20;
    /** Maximum number of characters of each key information (like OU). */
    public static final int KEY_DATA_LIMIT = 50;
    /** Minimum key alias size. */
    public static final int KEY_ALIAS_MIN_SIZE = 4;
    /** Maximum key alias size. */
    public static final int KEY_ALIAS_MAX_SIZE = 20;
    /** Minimum KeyStore password size. */
    public static final int KEYSTORE_PASSWORD_MIN_SIZE = 6;
    /** Maximum KeyStore password size. */
    public static final int KEYSTORE_PASSWORD_MAX_SIZE = 20;
    /** Minimum key password size. */
    public static final int KEY_PASSWORD_MIN_SIZE = 6;
    /** Maximum key password size. */
    public static final int KEY_PASSWORD_MAX_SIZE = 20;
    /** Group numerator in wizards. */
    public static final int GROUP_NUMERATOR = 100;
    /** Default margin for GUI elements. */
    public static final int MARGIN = 10;
    /** Large margin for GUI elements. */
    public static final int LARGE_MARGIN = 10;
    /** Default width for buttons. */
    public static final int BUTTON_WIDTH = 60;
    /** Large width for buttons. */
    public static final int LARGE_BUTTON_WIDTH = 100;
    /** Default width for combo boxes. */
    public static final int COMBO_WIDTH = 150;
    /** Default large width for combo boxes. */
    public static final int COMBO_LARGE_WIDTH = 200;
    /** Default margin for combo boxes. */
    public static final int COMBO_MARGIN = 20;
    /** Small width for textfields. */
    public static final int SHORT_TEXT_WIDTH = 200;
    /** Medium width for textfields. */
    public static final int MEDIUM_TEXT_WIDTH = 225;
    /** Large width for textfields. */
    public static final int LARGE_TEXT_WIDTH = 300;
    /** Default extension for a JKS file. */
    public static final String KEYSTORE_EXTENSION = ".jks";
    /** Default extension name for KeyStore dialog. */
    public static final String[] KEY_STORE_EXTENSION_NAME = {"Java KeyStore (*.jks)", "All Files (*.*)"};
    /** Default extension for KeyStore dialog. */
    public static final String[] KEY_STORE_EXTENSION = {"*.jks", "*.*"};
    /** Default extensions for detached file dialog. */
    public static final String[] DETACHED_FILE_EXTENSION = {"*.xml"};
    /** Default extension names for detached file dialog. */
    public static final String[] DETACHED_FILE_EXTENSION_NAME = {"XML document (*.xml)"};
    /** The Java Keystore type. */
    public static final String KEYSTORETYPE = "JKS";
    /** Schema URI. */
    public static final String SCHEMA = "http://apache.org/xml/features/validation/schema";
    /** Defer node expansion URI. */
    public static final String DOM = "http://apache.org/xml/features/dom/defer-node-expansion";
    /** SAX validation URI. */
    public static final String SAX = "http://xml.org/sax/features/validation";
    /** SAX namespaces URI. */
    public static final String SAX_NAMESPACES = "http://xml.org/sax/features/namespaces";
    /** External schema location URI. */
    public static final String EXTERNAL_SCHEMA_LOC =
        "http://apache.org/xml/properties/schema/external-schemaLocation";
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5438.java