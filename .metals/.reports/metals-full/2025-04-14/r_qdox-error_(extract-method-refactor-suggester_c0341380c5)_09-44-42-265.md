error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9393.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9393.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9393.java
text:
```scala
private final L@@ist<ClassTransformer> transformers = new CopyOnWriteArrayList<ClassTransformer>();

// $Id$
/*
 * Copyright (c) 2011, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */

package org.jboss.as.jpa.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.jboss.as.jpa.spi.PersistenceUnitMetadata;
import org.jboss.as.jpa.spi.TempClassLoaderFactory;
import org.jboss.jandex.Index;

/**
 * Represents the persistence unit definition
 *
 * @author Scott Marlow (based on work by Bill Burke)
 */
public class PersistenceUnitMetadataImpl implements PersistenceUnitMetadata {

    // required: name of the persistent unit
    private volatile String name;

    // required: name of the persistent unit scoped to deployment file
    private volatile String scopedName;

    // optional: jndi name of non-jta datasource
    private volatile String nonJtaDataSourceName;

    // optional: jndi name of jta datasource
    private volatile String jtaDataSourceName;


    private volatile DataSource jtaDatasource;

    private volatile DataSource nonJtaDataSource;

    // optional: provider classname (must implement javax.persistence.spi.PersistenceProvider)
    private volatile String provider;

    // optional: specifies if EntityManagers will be JTA (default) or RESOURCE_LOCAL
    private volatile PersistenceUnitTransactionType transactionType;

    // optional: collection of individually named managed entity classes
    private volatile List<String> classes = new ArrayList<String>(1);

    // optional:
    private final List<String> packages = new ArrayList<String>(1);

    // optional:  collection of jar file names that contain entity classes
    private volatile List<String> jarFiles = new ArrayList<String>(1);

    private volatile List<URL> jarFilesUrls = new ArrayList<URL>();

    private volatile URL persistenceUnitRootUrl;

    // optional: collection of orm.xml style entity mapping files
    private volatile List<String> mappingFiles = new ArrayList<String>(1);

    // collection of properties for the persistence provider
    private volatile Properties props = new Properties();

    // optional: specifies whether to include entity classes in the root folder containing the persistence unit.
    private volatile boolean excludeUnlistedClasses;

    // optional:  validation mode can be "auto", "callback", "none".
    private volatile ValidationMode validationMode;

    // optional: version of the JPA specification
    private volatile String version;

    // transformers will be written to when the JPA persistence provider adds their transformer.
    // there should be very few calls to add transformers but potentially many calls to get the
    // transformer list (once per application class loaded).
    private volatile List<ClassTransformer> transformers = new CopyOnWriteArrayList<ClassTransformer>();

    private volatile SharedCacheMode sharedCacheMode;

    private volatile ClassLoader classloader;

    private volatile TempClassLoaderFactory tempClassLoaderFactory;

    private volatile Map<URL, Index> annotationIndex;

    @Override
    public void setPersistenceUnitName(String name) {
        this.name = name;
    }

    @Override
    public String getPersistenceUnitName() {
        return name;
    }

    @Override
    public void setScopedPersistenceUnitName(String scopedName) {
        this.scopedName = scopedName;
    }

    @Override
    public String getScopedPersistenceUnitName() {
        return scopedName;
    }

    @Override
    public void setPersistenceProviderClassName(String provider) {
        if (provider != null && provider.endsWith(".class")) {
            this.provider = provider.substring(0, provider.length() - 6);
        }
        this.provider = provider;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return provider;
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public DataSource getJtaDataSource() {
        return jtaDatasource;
    }

    @Override
    public void setJtaDataSource(DataSource jtaDataSource) {
        this.jtaDatasource = jtaDataSource;
    }

    @Override
    public void setNonJtaDataSource(DataSource nonJtaDataSource) {
        this.nonJtaDataSource = nonJtaDataSource;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return nonJtaDataSource;
    }

    @Override
    public void setJtaDataSourceName(String jtaDatasource) {
        this.jtaDataSourceName = jtaDatasource;
    }

    @Override
    public String getJtaDataSourceName() {
        return jtaDataSourceName;
    }

    @Override
    public void setNonJtaDataSourceName(String nonJtaDatasource) {
        this.nonJtaDataSourceName = nonJtaDatasource;
    }

    @Override
    public String getNonJtaDataSourceName() {
        return this.nonJtaDataSourceName;
    }

    @Override
    public void setPersistenceUnitRootUrl(URL persistenceUnitRootUrl) {
        this.persistenceUnitRootUrl = persistenceUnitRootUrl;
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return persistenceUnitRootUrl;
    }

    @Override
    public void setAnnotationIndex(Map<URL, Index> indexes) {
        annotationIndex = indexes;
    }

    @Override
    public Map<URL, Index> getAnnotationIndex() {
        return annotationIndex;
    }

    @Override
    public List<String> getManagedClassNames() {
        return classes;
    }

    @Override
    public void setManagedClassNames(List<String> classes) {
        this.classes = classes;
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return excludeUnlistedClasses;
    }

    @Override
    public void setExcludeUnlistedClasses(boolean excludeUnlistedClasses) {
        this.excludeUnlistedClasses = excludeUnlistedClasses;
    }

    @Override
    public void setTransactionType(PersistenceUnitTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public void setMappingFiles(List<String> mappingFiles) {
        this.mappingFiles = mappingFiles;
    }

    @Override
    public List<String> getMappingFileNames() {
        return mappingFiles;
    }

    @Override
    public List<URL> getJarFileUrls() {
        return jarFilesUrls;
    }

    @Override
    public void setJarFileUrls(List<URL> jarFilesUrls) {
        this.jarFilesUrls = jarFilesUrls;
    }


    @Override
    public List<String> getJarFiles() {
        return jarFiles;
    }

    @Override
    public void setJarFiles(List<String> jarFiles) {
        this.jarFiles = jarFiles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PersistenceUnitMetadataImpl(version=")
            .append(version)
            .append(") [\n")
            .append("\tname: ").append(name).append("\n")
            .append("\tjtaDataSource: ").append(jtaDataSourceName).append("\n")
            .append("\tnonJtaDataSource: ").append(nonJtaDataSourceName).append("\n")
            .append("\ttransactionType: ").append(transactionType).append("\n")
            .append("\tprovider: ").append(provider).append("\n")
            .append("\tclasses[\n");
        if (classes != null) {
            for (String elt : classes) {
                sb.append("\t\t").append(elt);
            }
        }
        sb.append("\t]\n")
            .append("\tpackages[\n");
        if (packages != null) {
            for (String elt : packages) {
                sb.append("\t\t").append(elt).append("\n");
            }
        }
        sb.append("\t]\n")
            .append("\tmappingFiles[\n");
        if (mappingFiles != null) {
            for (String elt : mappingFiles) {
                sb.append("\t\t").append(elt).append("\n");
            }
        }
        sb.append("\t]\n")
            .append("\tjarFiles[\n");
        if (jarFiles != null) {
            for (String elt : jarFiles) {
                sb.append("\t\t").append(elt).append("\n");
            }
        }
        sb.append("\t]\n");
        if (validationMode != null) {
            sb.append("\tvalidation-mode: ").append(validationMode).append("\n");
        }
        if (sharedCacheMode != null) {
            sb.append("\tshared-cache-mode: ").append(sharedCacheMode).append("\n");
        }

        sb.append("\tproperties[\n");

        if (props != null) {
            for (Map.Entry elt : props.entrySet()) {
                sb.append("\t\t").append(elt.getKey()).append(": ").append(elt.getValue()).append("\n");
            }
        }
        sb.append("\t]").append("]");

        return sb.toString();
    }

    @Override
    public void setValidationMode(ValidationMode validationMode) {
        this.validationMode = validationMode;
    }

    @Override
    public ValidationMode getValidationMode() {
        return validationMode;
    }


    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public Properties getProperties() {
        return props;
    }

    @Override
    public void setPersistenceXMLSchemaVersion(String version) {
        this.version = version;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return version;
    }

    @Override
    public void setClassLoader(ClassLoader cl) {
        classloader = cl;
    }

    /**
     * Return a classloader that the provider can use to load the entity classes.
     * <p/>
     * Note from JPA 8.2:
     * All persistence classes defined at the level of the Java EE EAR must be accessible to other Java EE
     * components in the application—i.e. loaded by the application classloader—such that if the same entity
     * class is referenced by two different Java EE components (which may be using different persistence
     * units), the referenced class is the same identical class.
     *
     * @return
     */
    @Override
    public ClassLoader getClassLoader() {
        return classloader;
    }

    @Override
    public List<ClassTransformer> getTransformers() {
        return transformers;
    }

    @Override
    public void addTransformer(ClassTransformer classTransformer) {
        transformers.add(classTransformer);
    }

    @Override
    public void setTempClassLoaderFactory(TempClassLoaderFactory tempClassloaderFactory) {
        this.tempClassLoaderFactory = tempClassloaderFactory;
    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return tempClassLoaderFactory != null ?
            tempClassLoaderFactory.createNewTempClassLoader() : null;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return sharedCacheMode;
    }

    @Override
    public void setSharedCacheMode(SharedCacheMode sharedCacheMode) {
        this.sharedCacheMode = sharedCacheMode;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9393.java