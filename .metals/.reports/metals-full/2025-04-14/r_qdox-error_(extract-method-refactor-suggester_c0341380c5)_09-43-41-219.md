error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7589.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7589.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7589.java
text:
```scala
m@@ap.put(VALIDATION_MODE, String.valueOf(info.getValidationMode()).toLowerCase());

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
package org.apache.openjpa.persistence;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.spi.SharedCacheMode;
import javax.persistence.spi.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.conf.ProductDerivations;
import org.apache.openjpa.lib.meta.SourceTracker;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.MultiClassLoader;
import org.apache.openjpa.util.ClassResolver;

/**
 * Implementation of the {@link PersistenceUnitInfo} interface used by OpenJPA 
 * when parsing persistence configuration information.
 *
 * @nojavadoc
 */
public class PersistenceUnitInfoImpl
    implements PersistenceUnitInfo, SourceTracker {

    public static final String KEY_PROVIDER = "javax.persistence.provider";
    public static final String VALIDATION_MODE =
        "javax.persistence.validation.mode";
    public static final String PERSISTENCE_VERSION = "PersistenceVersion";

    private static final Localizer s_loc = Localizer.forPackage
        (PersistenceUnitInfoImpl.class);

    private String _name;
    private final Properties _props = new Properties();
    private PersistenceUnitTransactionType _transType =
        PersistenceUnitTransactionType.RESOURCE_LOCAL;

    private String _providerClassName;
    private List<String> _mappingFileNames;
    private List<String> _entityClassNames;
    private List<URL> _jarFiles;
    private String _jtaDataSourceName;
    private DataSource _jtaDataSource;
    private String _nonJtaDataSourceName;
    private DataSource _nonJtaDataSource;
    private boolean _excludeUnlisted;
    private URL _persistenceXmlFile;
    private String _schemaVersion = "1.0";
    private ValidationMode _validationMode;

    // A persistence unit is defined by a persistence.xml file. The jar
    // file or directory whose META-INF directory contains the
    // persistence.xml file is termed the root of the persistence unit.
    //
    // In Java EE, the root of a persistence unit may be one of the following:
    // - an EJB-JAR file
    // - the WEB-INF/classes directory of a WAR file[38]
    // - a jar file in the WEB-INF/lib directory of a WAR file
    // - a jar file in the root of the EAR
    // - a jar file in the EAR library directory
    // - an application client jar file
    private URL _root;

    public ClassLoader getClassLoader() {
        return null;
    }

    public ClassLoader getNewTempClassLoader() {
        return AccessController.doPrivileged(J2DoPrivHelper
            .newTemporaryClassLoaderAction(AccessController
                .doPrivileged(J2DoPrivHelper.getContextClassLoaderAction())));
    }

    public String getPersistenceUnitName() {
        return _name;
    }

    public void setPersistenceUnitName(String emName) {
        _name = emName;
    }

    public String getPersistenceProviderClassName() {
        return _providerClassName;
    }

    public void setPersistenceProviderClassName(String providerClassName) {
        _providerClassName = providerClassName;
    }

    public PersistenceUnitTransactionType getTransactionType() {
        return _transType;
    }

    public void setTransactionType(PersistenceUnitTransactionType transType) {
        _transType = transType;
    }

    public String getJtaDataSourceName() {
        return _jtaDataSourceName;
    }

    public void setJtaDataSourceName(String jta) {
        _jtaDataSourceName = jta;
        if (jta != null)
            _jtaDataSource = null;
    }

    public DataSource getJtaDataSource() {
        return _jtaDataSource;
    }

    public void setJtaDataSource(DataSource ds) {
        _jtaDataSource = ds;
        if (ds != null)
            _jtaDataSourceName = null;
    }

    public String getNonJtaDataSourceName() {
        return _nonJtaDataSourceName;
    }

    public void setNonJtaDataSourceName(String nonJta) {
        _nonJtaDataSourceName = nonJta;
        if (nonJta != null)
            _nonJtaDataSource = null;
    }

    public DataSource getNonJtaDataSource() {
        return _nonJtaDataSource;
    }

    public void setNonJtaDataSource(DataSource ds) {
        _nonJtaDataSource = ds;
        if (ds != null)
            _nonJtaDataSourceName = null;
    }

    public URL getPersistenceUnitRootUrl() {
        return _root;
    }

    public void setPersistenceUnitRootUrl(URL root) {
        _root = root;
    }

    public boolean excludeUnlistedClasses() {
        return _excludeUnlisted;
    }

    public void setExcludeUnlistedClasses(boolean excludeUnlisted) {
        _excludeUnlisted = excludeUnlisted;
    }

    public List<String> getMappingFileNames() {
        return (_mappingFileNames == null)
            ? (List<String>) Collections.EMPTY_LIST : _mappingFileNames;
    }

    public void addMappingFileName(String name) {
        if (_mappingFileNames == null)
            _mappingFileNames = new ArrayList<String>();
        _mappingFileNames.add(name);
    }

    public List<URL> getJarFileUrls() {
        return (_jarFiles == null) ? (List<URL>) Collections.EMPTY_LIST 
            : _jarFiles;
    }

    public void addJarFile(URL jar) {
        if (_jarFiles == null)
            _jarFiles = new ArrayList<URL>();
        _jarFiles.add(jar);
    }

    public void addJarFileName(String name) {
        MultiClassLoader loader = AccessController
            .doPrivileged(J2DoPrivHelper.newMultiClassLoaderAction());
        loader.addClassLoader(getClass().getClassLoader());
        loader.addClassLoader(MultiClassLoader.THREAD_LOADER);
        URL url = AccessController.doPrivileged(
            J2DoPrivHelper.getResourceAction(loader, name));
        if (url != null) {
            addJarFile(url);
            return;
        }

        // jar file is not a resource; check classpath
        String[] cp = (AccessController.doPrivileged(
            J2DoPrivHelper.getPropertyAction("java.class.path"))) 
            .split(J2DoPrivHelper.getPathSeparator());
        for (int i = 0; i < cp.length; i++) {
            if (cp[i].equals(name)
 cp[i].endsWith(File.separatorChar + name)) {
                try {
                    addJarFile(AccessController
                        .doPrivileged(J2DoPrivHelper
                            .toURLAction(new File(cp[i]))));
                    return;
                } catch (PrivilegedActionException pae) {
                    break;
                } catch (MalformedURLException mue) {
                    break;
                }
            }
        }
        throw new IllegalArgumentException(s_loc.get("bad-jar-name", name).
            getMessage());
    }

    public List<String> getManagedClassNames() {
        return (_entityClassNames == null)
            ? (List<String>) Collections.EMPTY_LIST : _entityClassNames;
    }

    public void addManagedClassName(String name) {
        if (_entityClassNames == null)
            _entityClassNames = new ArrayList<String>();
        _entityClassNames.add(name);
    }

    public Properties getProperties() {
        return _props;
    }

    public void setProperty(String key, String value) {
        _props.setProperty(key, value);
    }

    public void addTransformer(ClassTransformer transformer) {
        throw new UnsupportedOperationException();
    }

    /**
     * The location of the persistence.xml resource. May be null.
     */
    public URL getPersistenceXmlFileUrl() {
        return _persistenceXmlFile;
    }

    /**
     * The location of the persistence.xml resource. May be null.
     */
    public void setPersistenceXmlFileUrl(URL url) {
        _persistenceXmlFile = url;
    }

    /**
     * Load the given user-supplied map of properties into this persistence
     * unit.
     */
    public void fromUserProperties(Map map) {
        if (map == null)
            return;

        Object key;
        Object val;
        for (Object o : map.entrySet()) {
            key = ((Map.Entry) o).getKey();
            val = ((Map.Entry) o).getValue();
            if (KEY_PROVIDER.equals(key))
                setPersistenceProviderClassName((String) val);
            else if ("javax.persistence.transactionType".equals(key)) {
                PersistenceUnitTransactionType ttype;
                if (val instanceof String)
                    ttype = Enum.valueOf(PersistenceUnitTransactionType.class, 
                        (String) val);
                else
                    ttype = (PersistenceUnitTransactionType) val;
                setTransactionType(ttype);
            } else if ("javax.persistence.jtaDataSource".equals(key)) {
                if (val instanceof String)
                    setJtaDataSourceName((String) val);
                else
                    setJtaDataSource((DataSource) val);
            } else if ("javax.persistence.nonJtaDataSource".equals(key)) {
                if (val instanceof String)
                    setNonJtaDataSourceName((String) val);
                else
                    setNonJtaDataSource((DataSource) val);
            } else if (VALIDATION_MODE.equals(key)) {
                if (val instanceof String)
                    setValidationMode((String) val);
                else
                    setValidationMode((ValidationMode) val);
            } else
                _props.put(key, val);
        }
    }

    /**
     * Return a {@link Map} containing the properties necessary to create
     * a {@link Configuration} that reflects the information in this
     * persistence unit info.
     */
    public Map toOpenJPAProperties() {
        return toOpenJPAProperties(this);
    }

    /**
     * Return a {@link Map} containing the properties necessary to create
     * a {@link Configuration} that reflects the information in the given
     * persistence unit info.
     */
    public static Map toOpenJPAProperties(PersistenceUnitInfo info) {
        Map map = new HashMap();
        Set<String> added = new HashSet<String>();
        if (info.getTransactionType() == PersistenceUnitTransactionType.JTA)
            put(map, added, "TransactionMode", "managed");

        boolean hasJta = false;
        DataSource ds = info.getJtaDataSource();
        if (ds != null) {
            put(map, added, "ConnectionFactory", ds);
            put(map, added, "ConnectionFactoryMode", "managed");
            hasJta = true;
        } else if (info instanceof PersistenceUnitInfoImpl
            && ((PersistenceUnitInfoImpl) info).getJtaDataSourceName() != null){
            put(map, added, "ConnectionFactoryName", ((PersistenceUnitInfoImpl)
                info).getJtaDataSourceName());
            put(map, added, "ConnectionFactoryMode", "managed");
            hasJta = true;
        }

        ds = info.getNonJtaDataSource();
        if (ds != null) {
            if (!hasJta)
                put(map, added, "ConnectionFactory", ds);
            else
                put(map, added, "ConnectionFactory2", ds);
        } else if (info instanceof PersistenceUnitInfoImpl
            && ((PersistenceUnitInfoImpl) info).getNonJtaDataSourceName()
            != null) {
            String nonJtaName = ((PersistenceUnitInfoImpl) info).
                getNonJtaDataSourceName();
            if (!hasJta)
                put(map, added, "ConnectionFactoryName", nonJtaName);
            else
                put(map, added, "ConnectionFactory2Name", nonJtaName);
        }

        if (info.getClassLoader() != null)
            put(map, added, "ClassResolver", new ClassResolverImpl(
                info.getClassLoader()));

        Properties props = info.getProperties();
        if (props != null) {

            // remove any of the things that were set above
            for (String key : added) {
                if (Configurations.containsProperty(key, props))
                    Configurations.removeProperty(key, props);
            }

            // add all the non-conflicting props in the <properties> section
            map.putAll(props);

            // this isn't a real config property; remove it
            map.remove(PersistenceProviderImpl.CLASS_TRANSFORMER_OPTIONS);
        }

        if (!Configurations.containsProperty("Id", map))
            map.put("openjpa.Id", info.getPersistenceUnitName());
        
        Properties metaFactoryProps = new Properties();
        if (info.getManagedClassNames() != null 
            && !info.getManagedClassNames().isEmpty()) {
            StringBuffer types = new StringBuffer();
            for (String type : info.getManagedClassNames()) {
                if (types.length() > 0)
                    types.append(';');
                types.append(type);
            }
            metaFactoryProps.put("Types", types.toString());
        }
        if (info.getJarFileUrls() != null && !info.getJarFileUrls().isEmpty()
 (!info.excludeUnlistedClasses()
            && info.getPersistenceUnitRootUrl() != null)) {
            StringBuffer jars = new StringBuffer();
            String file = null;
            if (!info.excludeUnlistedClasses()
                && info.getPersistenceUnitRootUrl() != null) {
                URL url = info.getPersistenceUnitRootUrl();
                if ("file".equals(url.getProtocol())) // exploded jar?
                    file = URLDecoder.decode(url.getPath());
                else
                    jars.append(url);
            }
            for (URL jar : info.getJarFileUrls()) {
                if (jars.length() > 0)
                    jars.append(';');
                jars.append(jar);
            }
            if (file != null)
                metaFactoryProps.put("Files", file);
            if (jars.length() != 0)
                metaFactoryProps.put("URLs", jars.toString());
        }
        if (info.getMappingFileNames() != null
            && !info.getMappingFileNames().isEmpty()) {
            StringBuffer rsrcs = new StringBuffer();
            for (String rsrc : info.getMappingFileNames()) {
                if (rsrcs.length() > 0)
                    rsrcs.append(';');
                rsrcs.append(rsrc);
            }
            metaFactoryProps.put("Resources", rsrcs.toString());
        }

        // set persistent class locations as properties of metadata factory,
        // combining them with any existing metadata factory props
        if (!metaFactoryProps.isEmpty()) {
            String key = ProductDerivations.getConfigurationKey
                ("MetaDataFactory", map);
            map.put(key, Configurations.combinePlugins((String) map.get(key),
                Configurations.serializeProperties(metaFactoryProps)));
        }
        
        // always record provider name for product derivations to access
        if (info.getPersistenceProviderClassName() != null)
            map.put(KEY_PROVIDER, info.getPersistenceProviderClassName());
        
        // convert validation-mode enum to a StringValue
        if (info.getValidationMode() != null)
            map.put(VALIDATION_MODE, String.valueOf(info.getValidationMode()));

        if (info.getPersistenceXMLSchemaVersion() != null) {
            map.put(PERSISTENCE_VERSION, info.getPersistenceXMLSchemaVersion());
        }
        
        return map;
    }

    private static void put(Map map, Set added, String key, Object val) {
        map.put("openjpa." + key, val);
        added.add(key);
    }

    // --------------------

    public File getSourceFile() {
        if (_persistenceXmlFile == null)
            return null;

        try {
            return new File(_persistenceXmlFile.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    public Object getSourceScope() {
        return null;
    }

    public int getSourceType() {
        return SRC_XML;
    }
    
    public int getLineNumber() {
        return 0;
    }
        
    public int getColNumber() {
        return 0;
    }

    public String getResourceName() {
        return "PersistenceUnitInfo:" + _name;
    }

    /**
     * Simple class resolver built around the persistence unit loader.
     */
    public static class ClassResolverImpl
        implements ClassResolver {

        private final ClassLoader _loader;

        public ClassResolverImpl(ClassLoader loader) {
            _loader = loader;
        }

        public ClassLoader getClassLoader(Class ctx, ClassLoader env) {
            return _loader;
        }
	}

    public String getPersistenceXMLSchemaVersion() {
        return _schemaVersion;
    }

    public void setPersistenceXMLSchemaVersion(String version) {
        _schemaVersion = version;
    }

    public SharedCacheMode getCaching() {
        throw new UnsupportedOperationException(
            "JPA 2.0 - Method not yet implemented");
    }
    
    public void setCaching(SharedCacheMode cache) {
        throw new UnsupportedOperationException(
        "JPA 2.0 - Method not yet implemented");
    }

    public ValidationMode getValidationMode() {
        return _validationMode;
    }
    
    protected void setValidationMode(String mode) {
        setValidationMode(Enum.valueOf(ValidationMode.class, mode.toUpperCase()));
    }

    public void setValidationMode(ValidationMode mode) {
        _validationMode = mode;
    }

    public SharedCacheMode getSharedCacheMode() {
        throw new UnsupportedOperationException("JPA 2.0 - Method not yet implemented");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7589.java