error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12739.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12739.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12739.java
text:
```scala
s@@tore.setDefault(PreferenceConstants.SIGN_KEY_NAME, "");

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
package org.eclipse.wst.xml.security.core.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.xml.security.core.XmlSecurityPlugin;

/**
 * <p>This class is used to initialize all default preference values of the
 * XML Security Tools.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

  /**
   * Constructor.
   */
  public PreferenceInitializer() {
    super();
  }

  /**
   * Initializes default preference values for the XML Security Tools. This method is called
   * automatically by the preference initializer when the appropriate default preference node is
   * accessed.
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = XmlSecurityPlugin.getDefault().getPreferenceStore();
    // Canonicalization
    store.setDefault(PreferenceConstants.CANON_TYPE, "exclusive");
    store.setDefault(PreferenceConstants.CANON_TARGET, "internal");
    // Encryption
    store.setDefault(PreferenceConstants.ENCRYPT_RESOURCE, "document");
    store.setDefault(PreferenceConstants.ENCRYPT_XPATH, "");
    store.setDefault(PreferenceConstants.ENCRYPT_TYPE, "enveloping");
    store.setDefault(PreferenceConstants.ENCRYPT_ENCRYPTION, "AES 128");
    store.setDefault(PreferenceConstants.ENCRYPT_KEY_WRAP, "AES-128 Key Wrap");
    store.setDefault(PreferenceConstants.ENCRYPT_ID, "myEncryption");
    store.setDefault(PreferenceConstants.ENCRYPT_KEY_STORE, "");
    store.setDefault(PreferenceConstants.ENCRYPT_KEY_NAME, "");
    // Signature
    store.setDefault(PreferenceConstants.SIGN_RESOURCE, "document");
    store.setDefault(PreferenceConstants.SIGN_XPATH, "");
    store.setDefault(PreferenceConstants.SIGN_TYPE, "enveloped");
    store.setDefault(PreferenceConstants.SIGN_ID, "mySignature");
    store.setDefault(PreferenceConstants.SIGN_CANON, "Exclusive without comments");
    store.setDefault(PreferenceConstants.SIGN_TRANS, "None");
    store.setDefault(PreferenceConstants.SIGN_MDA, "SHA 1");
    store.setDefault(PreferenceConstants.SIGN_SA, "DSA with SHA 1 (DSS)");
    store.setDefault(PreferenceConstants.SIGN_KEYSTORE_FILE, "");
    store.setDefault(PreferenceConstants.SIGN_KEY_ALIAS, "");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12739.java