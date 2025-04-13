error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3500.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3500.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3500.java
text:
```scala
_@@packageAdminTracker = new ServiceTracker(_compositeBundle.getCompositeFramework().getBundleContext(),

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIESOR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.application.runtime.framework;

import static org.apache.aries.application.utils.AppConstants.LOG_ENTRY;
import static org.apache.aries.application.utils.AppConstants.LOG_EXCEPTION;
import static org.apache.aries.application.utils.AppConstants.LOG_EXIT;

import java.util.ArrayList;
import java.util.List;

import org.apache.aries.application.management.AriesApplication;
import org.apache.aries.application.management.BundleFramework;
import org.apache.aries.application.management.BundleRepository.BundleSuggestion;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.service.framework.CompositeBundle;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BundleFrameworkImpl implements BundleFramework
{
  private static final Logger LOGGER = LoggerFactory.getLogger(BundleFrameworkImpl.class);

  List<Bundle> _bundles;
  CompositeBundle _compositeBundle;

  ServiceTracker _packageAdminTracker;

  BundleFrameworkImpl(CompositeBundle cb)
  {
    _compositeBundle = cb;
    _bundles = new ArrayList<Bundle>();
  }

  public void init() throws BundleException
  {
    if (_compositeBundle.getCompositeFramework().getState() != Framework.ACTIVE)
    {
      _compositeBundle.start(Bundle.START_ACTIVATION_POLICY);
  
      _packageAdminTracker = new ServiceTracker(_compositeBundle.getBundleContext(),
          PackageAdmin.class.getName(), null);
      _packageAdminTracker.open();
    }
  }

  public void close() throws BundleException
  {
    // close out packageadmin service tracker
    if (_packageAdminTracker != null) {
      try {
        _packageAdminTracker.close();
      } catch (IllegalStateException ise) {
        // Ignore this error because this can happen when we're trying to close the tracker on a
        // framework that has closed/is closing.
      }
    }

    _compositeBundle.stop();
    _compositeBundle.uninstall();
  }

  public void start(Bundle b) throws BundleException
  {
    if (b.getState() != Bundle.ACTIVE && !isFragment(b)) 
      b.start(Bundle.START_ACTIVATION_POLICY);
  }

  public void stop(Bundle b) throws BundleException
  {
    b.stop();
  }

  public Bundle getFrameworkBundle()
  {
    return _compositeBundle;
  }

  public BundleContext getIsolatedBundleContext()
  {
    return _compositeBundle.getCompositeFramework().getBundleContext();
  }

  public List<Bundle> getBundles()
  {
    return _bundles;
  }

  /**
   * This method uses the PackageAdmin service to identify if a bundle
   * is a fragment.
   * @param b
   * @return
   */
  private boolean isFragment(Bundle b)
  {
    LOGGER.debug(LOG_ENTRY, "isFragment", new Object[] { b });

    PackageAdmin admin = null;
    boolean isFragment = false;

    try {
      if (_packageAdminTracker != null) {
        admin = (PackageAdmin) _packageAdminTracker.getService();
        if (admin != null) {
          isFragment = (admin.getBundleType(b) == PackageAdmin.BUNDLE_TYPE_FRAGMENT);
        }
      }
    } catch (RuntimeException re) {
      LOGGER.debug(LOG_EXCEPTION, re);
    }

    LOGGER.debug(LOG_EXIT, "isFragment", new Object[] { Boolean.valueOf(isFragment) });

    return isFragment;
  }

  public Bundle install(BundleSuggestion suggestion, AriesApplication app) throws BundleException
  {
    Bundle installedBundle = suggestion.install(getIsolatedBundleContext(), app);
    _bundles.add(installedBundle);
    
    return installedBundle;
  }

  public void uninstall(Bundle b) throws BundleException
  {
    b.uninstall();
    _bundles.remove(b);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3500.java