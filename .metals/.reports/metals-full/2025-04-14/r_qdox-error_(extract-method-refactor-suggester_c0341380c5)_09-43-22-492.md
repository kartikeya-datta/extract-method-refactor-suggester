error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4245.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4245.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4245.java
text:
```scala
n@@ewInstance(customizerClass, conf, (String)null,

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
package org.apache.openjpa.jdbc.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfigurationImpl;
import org.apache.openjpa.jdbc.meta.PropertiesReverseCustomizer;
import org.apache.openjpa.jdbc.meta.ReverseCustomizer;
import org.apache.openjpa.jdbc.meta.ReverseMappingTool;
import org.apache.openjpa.lib.ant.AbstractTask;
import org.apache.openjpa.lib.conf.ConfigurationImpl;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.util.CodeFormat;
import org.apache.openjpa.lib.util.Files;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Executes the {@link ReverseMappingTool} on the specified XML files.
 * This task can take the following arguments:
 * <ul>
 * <li><code>package</code></li>
 * <li><code>directory</code></li>
 * <li><code>useSchemaName</code></li>
 * <li><code>useForeignKeyName</code></li>
 * <li><code>nullableAsObject</code></li>
 * <li><code>blobAsObject</code></li>
 * <li><code>typeMap</code></li>
 * <li><code>primaryKeyOnJoin</code></li>
 * <li><code>useDatastoreIdentity</code></li>
 * <li><code>useBuiltinIdentityClass</code></li>
 * <li><code>detachable</code></li>
 * <li><code>inverseRelations</code></li>
 * <li><code>discriminatorStrategy</code></li>
 * <li><code>versionStrategy</code></li>
 * <li><code>innerIdentityClasses</code></li>
 * <li><code>identityClassSuffix</code></li>
 * <li><code>metadata</code></li>
 * <li><code>customizerClass</code></li>
 * <li><code>customizerProperties</code></li>
 * </ul> The task also accepts an embedded <code>codeFormat</code> element with
 * attributes for the bean properties of the {@link CodeFormat}.
 */
public class ReverseMappingToolTask
    extends AbstractTask {

    protected ReverseMappingTool.Flags flags = new ReverseMappingTool.Flags();
    protected String dirName = null;
    protected String typeMap = null;
    protected String customizerProperties = null;
    protected String customizerClass =
        PropertiesReverseCustomizer.class.getName();

    /**
     * Default constructor.
     */
    public ReverseMappingToolTask() {
        flags.metaDataLevel = "package";
        flags.format = new CodeFormat();
    }

    /**
     * Set the package name for the generated classes.
     */
    public void setPackage(String pkg) {
        flags.packageName = pkg;
    }

    /**
     * Set the output directory for the generated classes.
     */
    public void setDirectory(String dirName) {
        this.dirName = dirName;
    }

    /**
     * Set whether to use the schema name when naming the classes.
     */
    public void setUseSchemaName(boolean useSchemaName) {
        flags.useSchemaName = useSchemaName;
    }

    /**
     * Set whether to use foreign key names to name relations.
     */
    public void setUseForeignKeyName(boolean useForeignKeyName) {
        flags.useForeignKeyName = useForeignKeyName;
    }

    /**
     * Set whether to represent nullable columns as primitive wrappers.
     */
    public void setNullableAsObject(boolean nullableAsObject) {
        flags.nullableAsObject = nullableAsObject;
    }

    /**
     * Set whether to represent blob columns as Java objects rather than
     * byte[] fields.
     */
    public void setBlobAsObject(boolean blobAsObject) {
        flags.blobAsObject = blobAsObject;
    }

    /**
     * Set whether to use generic collections on one-to-many and many-to-many
     * relations instead of untyped collections.
     */
    public void setUseGenericCollections(boolean useGenericCollections) {
        flags.useGenericCollections = useGenericCollections; 
    }

    /**
     * Set the SQL type map overrides.
     */
    public void setTypeMap(String typeMap) {
        this.typeMap = typeMap;
    }

    /**
     * Set whether to allow primary keys on join tables.
     */
    public void setPrimaryKeyOnJoin(boolean primaryKeyOnJoin) {
        flags.primaryKeyOnJoin = primaryKeyOnJoin;
    }

    /**
     * Set whether to use datastore identity by default.
     */
    public void setUseDataStoreIdentity(boolean useDataStoreIdentity) {
        flags.useDataStoreIdentity = useDataStoreIdentity;
    }

    /**
     * Set whether to use single field identity where possible.
     */
    public void setUseBuiltinIdentityClass(boolean useBuiltinIdentityClass) {
        flags.useBuiltinIdentityClass = useBuiltinIdentityClass;
    }

    /**
     * Set whether to generate inverse 1-many/1-1 relations for all many-1/1-1
     * relations.
     */
    public void setInverseRelations(boolean inverseRelations) {
        flags.inverseRelations = inverseRelations;
    }

    /**
     * Set whether to make generated classes detachable.
     */
    public void setDetachable(boolean detachable) {
        flags.detachable = detachable;
    }

    /**
     * Default discriminator strategy for base class mappings.
     */
    public void setDiscriminatorStrategy(String discStrat) {
        flags.discriminatorStrategy = discStrat;
    }

    /**
     * Default version strategy for base class mappings.
     */
    public void setVersionStrategy(String versionStrat) {
        flags.versionStrategy = versionStrat;
    }

    /**
     * Whether or not to generate application identity classes as inner classes.
     */
    public void setInnerIdentityClasses(boolean innerAppId) {
        flags.innerIdentityClasses = innerAppId;
    }

    /**
     * The suffix to use to create the identity class name for a class, or
     * for inner classes, the name of the inner class.
     */
    public void setIdentityClassSuffix(String suffix) {
        flags.identityClassSuffix = suffix;
    }

    /**
     * Set the level of the generated metadata.
     */
    public void setMetadata(Level level) {
        flags.metaDataLevel = level.getValue();
    }

    /**
     * Whether to generate annotations along with generated code. Defaults
     * to false.
     */
    public void setGenerateAnnotations(boolean genAnnotations) {
        flags.generateAnnotations = genAnnotations;
    }

    /**
     * Whether to use field or property-based access on generated code.
     * Defaults to field-based access.
     */
    public void setAccessType(AccessType accessType) {
        flags.accessType = accessType.getValue();
    }
    
    /**
     * Set a customizer class to use.
     */
    public void setCustomizerClass(String customizerClass) {
        this.customizerClass = customizerClass;
    }

    /**
     * Set a properties file to pass to the customizer class.
     */
    public void setCustomizerProperties(String customizerProperties) {
        this.customizerProperties = customizerProperties;
    }

    public Object createCodeFormat() {
        return flags.format;
    }

    protected ConfigurationImpl newConfiguration() {
        return new JDBCConfigurationImpl();
    }

    protected void executeOn(String[] files)
        throws Exception {
        ClassLoader loader = getClassLoader();
        if (!StringUtils.isEmpty(dirName))
            flags.directory = Files.getFile(dirName, loader);
        if (!StringUtils.isEmpty(typeMap))
            flags.typeMap = Configurations.parseProperties(typeMap);

        // load customizer properties
        Properties customProps = new Properties();
        File propsFile = Files.getFile(customizerProperties, loader);
        if (propsFile != null && (AccessController.doPrivileged(
            J2DoPrivHelper.existsAction(propsFile))).booleanValue()) {
            FileInputStream fis = null;
            try {
                fis = AccessController.doPrivileged(
                    J2DoPrivHelper.newFileInputStreamAction(propsFile));
            } catch (PrivilegedActionException pae) {
                 throw (FileNotFoundException) pae.getException();
            }
            customProps.load(fis);
        }

        // create and configure customizer
        JDBCConfiguration conf = (JDBCConfiguration) getConfiguration();
        flags.customizer = (ReverseCustomizer) Configurations.
            newInstance(customizerClass, conf, null,
                AccessController.doPrivileged(
                    J2DoPrivHelper.getClassLoaderAction(
                        ReverseCustomizer.class)));
        if (flags.customizer != null)
            flags.customizer.setConfiguration(customProps);

        ReverseMappingTool.run(conf, files, flags, loader);
    }

    public static class Level
        extends EnumeratedAttribute {

        public String[] getValues() {
            return new String[]{
                "package",
                "class",
                "none"
            };
        }
    }

    public static class AccessType
        extends EnumeratedAttribute {

        public String[] getValues() {
            return new String[]{
                "field",
                "property"
            };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4245.java