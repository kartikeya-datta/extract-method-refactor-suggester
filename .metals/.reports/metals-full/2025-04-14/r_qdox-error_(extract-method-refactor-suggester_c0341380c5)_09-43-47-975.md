error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2217.java
text:
```scala
S@@tring getProperty(String key) throws IOException;

/*
 * Copyright (c) OSGi Alliance (2009, 2010). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osgi.jmx.framework;

import java.io.IOException;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.SimpleType;

import org.osgi.jmx.Item;
import org.osgi.jmx.JmxConstants;

/**
 * The FrameworkMbean provides mechanisms to exert control over the framework.
 * For many operations, it provides a batch mechanism to avoid excessive message
 * passing when interacting remotely.
 *
 * @version $Revision$
 * @ThreadSafe
 */
public interface FrameworkMBean {
	/**
	 * The fully qualified object name of this mbean.
	 */
	String			OBJECTNAME						= JmxConstants.OSGI_CORE
															+ ":type=framework,version=1.7";

	/**
	 * The SUCCESS, used in {@link #SUCCESS_ITEM}.
	 */
	String			SUCCESS							= "Success";

	/**
	 * The item that indicates if this operation was successful. The key is
	 * {@link #SUCCESS} and the type is {@link SimpleType#BOOLEAN}. It is used
	 * in {@link #BATCH_ACTION_RESULT_TYPE} and
	 * {@link #BATCH_INSTALL_RESULT_TYPE}.
	 */
	Item			SUCCESS_ITEM					= new Item(
															SUCCESS,
															"Whether the operation was successful",
															SimpleType.BOOLEAN);

	/**
	 * The key ERROR, used in {@link #ERROR_ITEM}.
	 */
	String			ERROR							= "Error";

	/**
	 * The item containing the error message of the batch operation. The key is
	 * {@link #ERROR} and the type is {@link SimpleType#STRING}. It is used in
	 * {@link #BATCH_ACTION_RESULT_TYPE} and {@link #BATCH_INSTALL_RESULT_TYPE}.
	 */
	Item			ERROR_ITEM						= new Item(
															ERROR,
															"The error message if unsuccessful",
															SimpleType.STRING);

	/**
	 * The key COMPLETED, used in {@link #COMPLETED_ITEM}.
	 */
	String			COMPLETED						= "Completed";

	/**
	 * The item containing the list of bundles completing the batch operation.
	 * The key is {@link #COMPLETED} and the type is
	 * {@link JmxConstants#LONG_ARRAY_TYPE}. It is used in
	 * {@link #BATCH_ACTION_RESULT_TYPE} and {@link #BATCH_INSTALL_RESULT_TYPE}.
	 */
	Item			COMPLETED_ITEM					= new Item(
															COMPLETED,
															"The bundle ids of the successfully completed installs",
															JmxConstants.LONG_ARRAY_TYPE);

	/**
	 * The key for BUNDLE_IN_ERROR. This key is used with two different items:
	 * {@link #BUNDLE_IN_ERROR_ID_ITEM} and
	 * {@link #BUNDLE_IN_ERROR_LOCATION_ITEM} that each have a different type
	 * for this key. It is used in {@link #BATCH_ACTION_RESULT_TYPE} and
	 * {@link #BATCH_INSTALL_RESULT_TYPE}.
	 */
	String			BUNDLE_IN_ERROR					= "BundleInError";

	/**
	 * The item containing the bundle which caused the error during the batch
	 * operation. This item describes the bundle in error as an id. The key is
	 * {@link #BUNDLE_IN_ERROR} and the type is {@link SimpleType#LONG}. It is
	 * used in {@link #BATCH_ACTION_RESULT_TYPE}.
	 *
	 * @see #BUNDLE_IN_ERROR_LOCATION_ITEM BUNDLE_IN_ERROR_LOCATION_ITEM for the
	 *      item that has a location for the bundle in error.
	 */
	Item			BUNDLE_IN_ERROR_ID_ITEM			= new Item(
															BUNDLE_IN_ERROR,
															"The id of the bundle causing the error",
															SimpleType.LONG);

	/**
	 * The key REMAINING, used in {@link #REMAINING_ID_ITEM} and
	 * {@link #REMAINING_LOCATION_ITEM}.
	 */
	String			REMAINING						= "Remaining";

	/**
	 * The item containing the list of remaining bundles unprocessed by the
	 * failing batch operation. The key is {@link #REMAINING} and the type is
	 * {@link JmxConstants#LONG_ARRAY_TYPE}. It is used in
	 * {@link #BATCH_ACTION_RESULT_TYPE} and {@link #BATCH_INSTALL_RESULT_TYPE}.
	 */
	Item			REMAINING_ID_ITEM				= new Item(
															REMAINING,
															"The ids of the remaining bundles",
															JmxConstants.LONG_ARRAY_TYPE);

	/**
	 * The Composite Type for a batch action result.
	 * {@link #refreshBundle(long)} and {@link #refreshBundles(long[])}.
	 * Notice that a batch action result returns uses an id for the
	 * {@link #BUNDLE_IN_ERROR} while the {@link #BATCH_INSTALL_RESULT_TYPE}
	 * uses a location.
	 *
	 * This Composite Type consists of the following items:
	 * <ul>
	 * <li>{@link #BUNDLE_IN_ERROR_ID_ITEM}</li>
	 * <li>{@link #COMPLETED_ITEM}</li>
	 * <li>{@link #ERROR_ITEM}</li>
	 * <li>{@link #REMAINING_ID_ITEM}</li>
	 * <li>{@link #SUCCESS_ITEM}</li>
	 * </ul>
	 */
	CompositeType	BATCH_ACTION_RESULT_TYPE		= Item
															.compositeType(
																	"BUNDLE_ACTION_RESULT",
																	"This type encapsulates a bundle batch install action result",
																	BUNDLE_IN_ERROR_ID_ITEM,
																	COMPLETED_ITEM,
																	ERROR_ITEM,
																	REMAINING_ID_ITEM,
																	SUCCESS_ITEM
															);

	/**
	 * The item containing the bundle which caused the error during the batch
	 * operation. This item describes the bundle in error as a location. The key
	 * is {@link #BUNDLE_IN_ERROR} and the type is {@link SimpleType#STRING}. It
	 * is used in {@link #BATCH_INSTALL_RESULT_TYPE}.
	 *
	 * @see #BUNDLE_IN_ERROR_ID_ITEM BUNDLE_IN_ERROR_ID_ITEM for the item that
	 *      has the id for the bundle in error.
	 */
	Item			BUNDLE_IN_ERROR_LOCATION_ITEM	= new Item(
															BUNDLE_IN_ERROR,
															"The location of the bundle causing the error",
															SimpleType.STRING);

	/**
	 * The item containing the list of remaining bundles unprocessed by the
	 * failing batch operation. The key is {@link #REMAINING} and the type is
	 * {@link JmxConstants#STRING_ARRAY_TYPE}. It is used in
	 * {@link #BATCH_ACTION_RESULT_TYPE} and {@link #BATCH_INSTALL_RESULT_TYPE}.
	 */
	Item			REMAINING_LOCATION_ITEM			= new Item(
															REMAINING,
															"The locations of the remaining bundles",
															JmxConstants.STRING_ARRAY_TYPE);

	/**
	 * The Composite Type which represents the result of a batch install
	 * operation. It is used in {@link #installBundles(String[])} and
	 * {@link #installBundlesFromURL(String[], String[])}.
	 *
	 * This Composite Type consists of the following items:
	 * <ul>
	 * <li>{@link #BUNDLE_IN_ERROR_LOCATION_ITEM}</li>
	 * <li>{@link #COMPLETED_ITEM}</li>
	 * <li>{@link #ERROR_ITEM}</li>
	 * <li>{@link #REMAINING_LOCATION_ITEM P }</li>
	 * <li>{@link #SUCCESS_ITEM}</li>
	 * </ul>
	 */
	CompositeType	BATCH_INSTALL_RESULT_TYPE		= Item
															.compositeType(
																	"BATCH_INSTALL_RESULT",
																	"This type encapsulates a bundle batch install action result",
																	BUNDLE_IN_ERROR_LOCATION_ITEM,
																	COMPLETED_ITEM,
																	ERROR_ITEM,
																	REMAINING_LOCATION_ITEM,
																	SUCCESS_ITEM
															);

    /**
     * The Composite Type which represents the result of a batch resolve
     * operation. It is used in {@link #refreshBundlesAndWait(String[])}.
     *
     * This Composite Type consists of the following items:
     * <ul>
     * <li>{@link #COMPLETED_ITEM}</li>
     * <li>{@link #SUCCESS_ITEM}</li>
     * </ul>
     */
    CompositeType   BATCH_RESOLVE_RESULT_TYPE       = Item
                                                            .compositeType(
                                                                    "BATCH_RESOLVE_RESULT",
                                                                    "This type encapsulates a bundle batch resolve action result",
                                                                    COMPLETED_ITEM,
                                                                    SUCCESS_ITEM);

    /**
     * Returns the dependency closure for the specified bundles.
     *
     * <p>
     * A graph of bundles is computed starting with the specified bundles. The
     * graph is expanded by adding any bundle that is either wired to a package
     * that is currently exported by a bundle in the graph or requires a bundle
     * in the graph. The graph is fully constructed when there is no bundle
     * outside the graph that is wired to a bundle in the graph. The graph may
     * contain {@code UNINSTALLED} bundles that are
     * {@link #getRemovalPendingBundles() removal pending}.
     *
     * @param bundles The initial bundles IDs for which to generate the dependency
     *        closure.
     * @return A bundle ID array containing a snapshot of the dependency closure of
     *         the specified bundles, or an empty array if there were no
     *         specified bundles.
     * @throws IOException if the operation failed
     */
    long[] getDependencyClosure(long[] bundles) throws IOException;

    /**
	 * Retrieve the framework start level
	 *
	 * @return the framework start level
	 * @throws IOException if the operation failed
	 */
	int getFrameworkStartLevel() throws IOException;

	/**
	 * Answer the initial start level assigned to a bundle when it is first
	 * started
	 *
	 * @return the start level
	 * @throws IOException if the operation failed
	 */
	int getInitialBundleStartLevel() throws IOException;

    /**
     * Returns the value of the specified property. If the key is not found in
     * the Framework properties, the system properties are then searched. The
     * method returns {@code null} if the property is not found.
     *
     * @param key The name of the requested property.
     * @return The value of the requested property, or {@code null} if the
     *         property is undefined.
     * @throws IOException if the operation failed
     */
    String getProperty(String key);

	/**
     * Returns the bundles IDs that have non-current, in use bundle wirings. This
     * is typically the bundles which have been updated or uninstalled since the
     * last call to {@link #refreshBundles(long[])}.
     *
     * @return A bundle ID array containing a snapshot of the bundles which
     *         have non-current, in use bundle wirings, or an empty
     *         array if there are no such bundles.
     * @throws IOException if the operation failed
     */
    long[] getRemovalPendingBundles() throws IOException;

	/**
	 * Install the bundle indicated by the bundleLocations
	 *
	 * @param location the location of the bundle to install
	 * @return the bundle id the installed bundle
	 * @throws IOException if the operation does not succeed
	 */
	long installBundle(String location) throws IOException;

	/**
	 * Install the bundle indicated by the bundleLocations
	 *
	 * @param location the location to assign to the bundle
	 * @param url the URL which will supply the bytes for the bundle
	 * @return the bundle id the installed bundle
	 * @throws IOException if the operation does not succeed
	 */
	long installBundleFromURL(String location, String url) throws IOException;

	/**
	 * Batch install the bundles indicated by the list of bundleLocationUrls
	 *
	 * @see #BATCH_INSTALL_RESULT_TYPE BATCH_INSTALL_RESULT_TYPE for the precise
	 *      specification of the CompositeData type representing the returned
	 *      result.
	 *
	 * @param locations the array of locations of the bundles to install
	 * @return the resulting state from executing the operation
	 * @throws IOException if the operation does not succeed
	 */
	CompositeData installBundles(String[] locations) throws IOException;

	/**
	 * Batch install the bundles indicated by the list of bundleLocationUrls
	 *
	 * @see #BATCH_INSTALL_RESULT_TYPE BATCH_INSTALL_RESULT_TYPE
	 *      BatchBundleResult for the precise specification of the CompositeData
	 *      type representing the returned result.
	 *
	 * @param locations the array of locations to assign to the installed
	 *        bundles
	 * @param urls the array of urls which supply the bundle bytes
	 * @return the resulting state from executing the operation
	 * @throws IOException if the operation does not succeed
	 */
	CompositeData installBundlesFromURL(String[] locations, String[] urls)
			throws IOException;

	/**
	 * Force the update, replacement or removal of the packages identified by
	 * the specified bundle.
	 *
	 * @param bundleIdentifier the bundle identifier
	 * @throws IOException if the operation failed
	 */
	void refreshBundle(long bundleIdentifier) throws IOException;
	boolean refreshBundleAndWait(long bundleIdentifier) throws IOException;

	/**
	 * Force the update, replacement or removal of the packages identified by
	 * the list of bundles.
	 *
	 * @param bundleIdentifiers The identifiers of the bundles to refresh, or
	 *        <code>null</code> for all bundles with packages pending removal.
	 * @throws IOException if the operation failed
	 */
	void refreshBundles(long[] bundleIdentifiers) throws IOException;
	CompositeData refreshBundlesAndWait(long[] bundleIdentifiers) throws IOException;

	/**
	 * Resolve the bundle indicated by the unique symbolic name and version
	 *
	 * @param bundleIdentifier the bundle identifier
	 * @return <code>true</code> if the bundle was resolved, false otherwise
	 * @throws IOException if the operation does not succeed
	 * @throws IllegalArgumentException if the bundle indicated does not exist
	 */
	boolean resolveBundle(long bundleIdentifier) throws IOException;

	/**
	 * Batch resolve the bundles indicated by the list of bundle identifiers
	 *
	 * @param bundleIdentifiers The identifiers of the bundles to resolve, or
	 *        <code>null</code> to resolve all unresolved bundles.
	 * @return <code>true</code> if the bundles were resolved, false otherwise
	 * @throws IOException if the operation does not succeed
	 */
	boolean resolveBundles(long[] bundleIdentifiers) throws IOException;

	/**
	 * Same as {@link #resolveBundles(long[])} but with a more detailed return type.
	 * @param bundleIdentifiers
	 * @return
	 * @throws IOException
	 */
	CompositeData resolve(long[] bundleIdentifiers) throws IOException;

	/**
	 * Restart the framework by updating the system bundle
	 *
	 * @throws IOException if the operation failed
	 */
	void restartFramework() throws IOException;

	/**
	 * Set the start level for the bundle identifier
	 *
	 * @param bundleIdentifier the bundle identifier
	 * @param newlevel the new start level for the bundle
	 * @throws IOException if the operation failed
	 */
	void setBundleStartLevel(long bundleIdentifier, int newlevel)
			throws IOException;

	/**
	 * Set the start levels for the list of bundles.
	 *
	 * @see #BATCH_ACTION_RESULT_TYPE BATCH_ACTION_RESULT_TYPE for the precise
	 *      specification of the CompositeData type representing the returned
	 *      result.
	 *
	 * @param bundleIdentifiers the array of bundle identifiers
	 * @param newlevels the array of new start level for the bundles
	 * @return the resulting state from executing the operation
	 * @throws IOException if the operation failed
	 */
	CompositeData setBundleStartLevels(long[] bundleIdentifiers, int[] newlevels)
			throws IOException;

	/**
	 * Set the start level for the framework
	 *
	 * @param newlevel the new start level
	 * @throws IOException if the operation failed
	 */
	void setFrameworkStartLevel(int newlevel) throws IOException;

	/**
	 * Set the initial start level assigned to a bundle when it is first started
	 *
	 * @param newlevel the new start level
	 * @throws IOException if the operation failed
	 */
	void setInitialBundleStartLevel(int newlevel) throws IOException;

	/**
	 * Shutdown the framework by stopping the system bundle
	 *
	 * @throws IOException if the operation failed
	 */
	void shutdownFramework() throws IOException;

	/**
	 * Start the bundle indicated by the bundle identifier
	 *
	 * @param bundleIdentifier the bundle identifier
	 * @throws IOException if the operation does not succeed
	 * @throws IllegalArgumentException if the bundle indicated does not exist
	 */
	void startBundle(long bundleIdentifier) throws IOException;

	/**
	 * Batch start the bundles indicated by the list of bundle identifier
	 *
	 * @see #BATCH_ACTION_RESULT_TYPE BATCH_ACTION_RESULT_TYPE for the precise
	 *      specification of the CompositeData type representing the returned
	 *      result.
	 *
	 * @param bundleIdentifiers the array of bundle identifiers
	 * @return the resulting state from executing the operation
	 * @throws IOException if the operation does not succeed
	 */
	CompositeData startBundles(long[] bundleIdentifiers) throws IOException;

	/**
	 * Stop the bundle indicated by the bundle identifier
	 *
	 * @param bundleIdentifier the bundle identifier
	 * @throws IOException if the operation does not succeed
	 * @throws IllegalArgumentException if the bundle indicated does not exist
	 */
	void stopBundle(long bundleIdentifier) throws IOException;

	/**
	 * Batch stop the bundles indicated by the list of bundle identifier
	 *
	 * @see #BATCH_ACTION_RESULT_TYPE BATCH_ACTION_RESULT_TYPE for the precise
	 *      specification of the CompositeData type representing the returned
	 *      result.
	 *
	 * @param bundleIdentifiers the array of bundle identifiers
	 * @return the resulting state from executing the operation
	 * @throws IOException if the operation does not succeed
	 */
	CompositeData stopBundles(long[] bundleIdentifiers) throws IOException;

	/**
	 * Uninstall the bundle indicated by the bundle identifier
	 *
	 * @param bundleIdentifier the bundle identifier
	 * @throws IOException if the operation does not succeed
	 * @throws IllegalArgumentException if the bundle indicated does not exist
	 */
	void uninstallBundle(long bundleIdentifier) throws IOException;

	/**
	 * Batch uninstall the bundles indicated by the list of bundle identifiers
	 *
	 * @see #BATCH_ACTION_RESULT_TYPE BATCH_ACTION_RESULT_TYPE for the precise
	 *      specification of the CompositeData type representing the returned
	 *      result.
	 *
	 * @param bundleIdentifiers the array of bundle identifiers
	 * @return the resulting state from executing the operation
	 * @throws IOException if the operation does not succeed
	 */
	CompositeData uninstallBundles(long[] bundleIdentifiers) throws IOException;

	/**
	 * Update the bundle indicated by the bundle identifier
	 *
	 * @param bundleIdentifier the bundle identifier
	 * @throws IOException if the operation does not succeed
	 * @throws IllegalArgumentException if the bundle indicated does not exist
	 */
	void updateBundle(long bundleIdentifier) throws IOException;

	/**
	 * Update the bundle identified by the bundle identifier
	 *
	 * @param bundleIdentifier the bundle identifier
	 * @param url the URL to use to update the bundle
	 * @throws IOException if the operation does not succeed
	 * @throws IllegalArgumentException if the bundle indicated does not exist
	 */
	void updateBundleFromURL(long bundleIdentifier, String url) throws IOException;

	/**
	 * Batch update the bundles indicated by the list of bundle identifier.
	 *
	 * @see #BATCH_ACTION_RESULT_TYPE BATCH_ACTION_RESULT_TYPE for the precise
	 *      specification of the CompositeData type representing the returned
	 *      result.
	 *
	 * @param bundleIdentifiers the array of bundle identifiers
	 * @return the resulting state from executing the operation
	 * @throws IOException if the operation does not succeed
	 */
	CompositeData updateBundles(long[] bundleIdentifiers) throws IOException;

	/**
	 * Update the bundle uniquely identified by the bundle symbolic name and
	 * version using the contents of the supplied urls.
	 *
	 * @see #BATCH_ACTION_RESULT_TYPE BATCH_ACTION_RESULT_TYPE for the precise
	 *      specification of the CompositeData type representing the returned
	 *      result.
	 *
	 * @param bundleIdentifiers the array of bundle identifiers
	 * @param urls the array of URLs to use to update the bundles
	 * @return the resulting state from executing the operation
	 * @throws IOException if the operation does not succeed
	 * @throws IllegalArgumentException if the bundle indicated does not exist
	 */
	CompositeData updateBundlesFromURL(long[] bundleIdentifiers, String[] urls)
			throws IOException;

	/**
	 * Update the framework by updating the system bundle.
	 *
	 * @throws IOException if the operation failed
	 */
	void updateFramework() throws IOException;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2217.java