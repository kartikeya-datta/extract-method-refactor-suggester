error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17899.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17899.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17899.java
text:
```scala
p@@ublic static class ConfigurationParser

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
import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.conf.OpenJPAProductDerivation;
import org.apache.openjpa.lib.conf.AbstractProductDerivation;
import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.conf.ConfigurationProvider;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.conf.MapConfigurationProvider;
import org.apache.openjpa.lib.conf.ProductDerivations;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.meta.XMLMetaDataParser;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.lib.util.Localizer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Sets JPA specification defaults and parses JPA specification XML files.
 * 
 * For globals, looks in <code>openjpa.properties</code> system property for
 * the location of a file to parse. If no system property is defined, the
 * default resource location of <code>META-INF/openjpa.xml</code> is used.
 *
 * For defaults, looks for <code>META-INF/persistence.xml</code>.
 * Within <code>persistence.xml</code>, look for the named persistence unit, or
 * if no name given, an OpenJPA unit (preferring an unnamed OpenJPA unit to 
 * a named one).
 *
 * @author Abe White
 * @nojavadoc
 */
public class PersistenceProductDerivation 
    extends AbstractProductDerivation
    implements OpenJPAProductDerivation {

    public static final String SPEC_JPA = "jpa";
    public static final String ALIAS_EJB = "ejb";
    public static final String RSRC_GLOBAL = "META-INF/openjpa.xml";
    public static final String RSRC_DEFAULT = "META-INF/persistence.xml";

    private static final Localizer _loc = Localizer.forPackage
        (PersistenceProductDerivation.class);

    public void putBrokerFactoryAliases(Map m) {
    }

    public int getType() {
        return TYPE_SPEC;
    }

    @Override
    public void validate()
        throws Exception {
        // make sure JPA is available
        AccessController.doPrivileged(J2DoPrivHelper.getClassLoaderAction(
            javax.persistence.EntityManagerFactory.class));
    }
    
    @Override
    public boolean beforeConfigurationLoad(Configuration c) {
        if (!(c instanceof OpenJPAConfigurationImpl))
            return false;
        
        OpenJPAConfigurationImpl conf = (OpenJPAConfigurationImpl) c;
        conf.metaFactoryPlugin.setAlias(ALIAS_EJB,
            PersistenceMetaDataFactory.class.getName());
        conf.metaFactoryPlugin.setAlias(SPEC_JPA,
            PersistenceMetaDataFactory.class.getName());
        
        conf.addValue(new EntityManagerFactoryValue());
        return true;
    }

    @Override
    public boolean afterSpecificationSet(Configuration c) {
      if (!(c instanceof OpenJPAConfigurationImpl)
 !SPEC_JPA.equals(((OpenJPAConfiguration) c).getSpecification()))
          return false;
 
        OpenJPAConfigurationImpl conf = (OpenJPAConfigurationImpl) c;
        conf.metaFactoryPlugin.setDefault(SPEC_JPA);
        conf.metaFactoryPlugin.setString(SPEC_JPA);
        conf.lockManagerPlugin.setDefault("version");
        conf.lockManagerPlugin.setString("version");
        conf.nontransactionalWrite.setDefault("true");
        conf.nontransactionalWrite.set(true);
        return true;
    }

    /**
     * Load configuration from the given persistence unit with the specified
     * user properties.
     */
    public ConfigurationProvider load(PersistenceUnitInfo pinfo, Map m)
        throws IOException {
        if (pinfo == null)
            return null;
        if (!isOpenJPAPersistenceProvider(pinfo, null)) {
            warnUnknownProvider(pinfo);
            return null;
        }

        ConfigurationProviderImpl cp = new ConfigurationProviderImpl();
        cp.addProperties(PersistenceUnitInfoImpl.toOpenJPAProperties(pinfo));
        cp.addProperties(m);
        if (pinfo instanceof PersistenceUnitInfoImpl) {
            PersistenceUnitInfoImpl impl = (PersistenceUnitInfoImpl) pinfo;
            if (impl.getPersistenceXmlFileUrl() != null)
                cp.setSource(impl.getPersistenceXmlFileUrl().toString());
        }
        return cp;
    }

    /**
     * Load configuration from the given resource and unit names, which may
     * be null.
     */
    public ConfigurationProvider load(String rsrc, String name, Map m)
        throws IOException {
        boolean explicit = !StringUtils.isEmpty(rsrc);
        if (!explicit)
            rsrc = RSRC_DEFAULT;
        
        ConfigurationProviderImpl cp = new ConfigurationProviderImpl();
        Boolean ret = load(cp, rsrc, name, m, null, explicit);
        if (ret != null)
            return (ret.booleanValue()) ? cp : null;
        if (explicit)
            return null;

        // persistence.xml does not exist; just load map
        PersistenceUnitInfoImpl pinfo = new PersistenceUnitInfoImpl();
        pinfo.fromUserProperties(m);
        if (!isOpenJPAPersistenceProvider(pinfo, null)) {
            warnUnknownProvider(pinfo);
            return null;
        }
        cp.addProperties(pinfo.toOpenJPAProperties());
        return cp;
    }

    @Override
    public ConfigurationProvider load(String rsrc, String anchor, 
        ClassLoader loader)
        throws IOException {
        if (rsrc != null && !rsrc.endsWith(".xml"))
            return null;
        ConfigurationProviderImpl cp = new ConfigurationProviderImpl();
        if (load(cp, rsrc, anchor, null, loader, true) == Boolean.TRUE)
            return cp;
        return null;
    }

    @Override
    public ConfigurationProvider load(File file, String anchor) 
        throws IOException {
        if (!file.getName().endsWith(".xml"))
            return null;

        ConfigurationParser parser = new ConfigurationParser(null);
        parser.parse(file);
        return load(findUnit((List<PersistenceUnitInfoImpl>) 
            parser.getResults(), anchor, null), null);
    }

    @Override
    public ConfigurationProvider loadGlobals(ClassLoader loader)
        throws IOException {
        String[] prefixes = ProductDerivations.getConfigurationPrefixes();
        String rsrc = null;
        for (int i = 0; i < prefixes.length && StringUtils.isEmpty(rsrc); i++)
           rsrc = System.getProperty(prefixes[i] + ".properties"); 
        boolean explicit = !StringUtils.isEmpty(rsrc);
        String anchor = null;
        int idx = (!explicit) ? -1 : rsrc.lastIndexOf('#');
        if (idx != -1) {
            // separate name from <resrouce>#<name> string
            if (idx < rsrc.length() - 1)
                anchor = rsrc.substring(idx + 1);
            rsrc = rsrc.substring(0, idx);
        }
        if (StringUtils.isEmpty(rsrc))
            rsrc = RSRC_GLOBAL;
        else if (!rsrc.endsWith(".xml"))
            return null;

        ConfigurationProviderImpl cp = new ConfigurationProviderImpl();
        if (load(cp, rsrc, anchor, null, loader, explicit) == Boolean.TRUE)
            return cp;
        return null;
    }

    @Override
    public ConfigurationProvider loadDefaults(ClassLoader loader)
        throws IOException {
        ConfigurationProviderImpl cp = new ConfigurationProviderImpl();
        if (load(cp, RSRC_DEFAULT, null, null, loader, false) == Boolean.TRUE)
            return cp;
        return null;
    }

    /**
     * Looks through the resources at <code>rsrc</code> for a configuration
     * file that matches <code>name</code> (or an unnamed one if
     * <code>name</code> is <code>null</code>), and loads the XML in the
     * resource into a new {@link PersistenceUnitInfo}. Then, applies the
     * overrides in <code>m</code>.
     *
     * @return {@link Boolean#TRUE} if the resource was loaded, null if it
     * does not exist, or {@link Boolean#FALSE} if it is not for OpenJPA
     */
    private Boolean load(ConfigurationProviderImpl cp, String rsrc, 
        String name, Map m, ClassLoader loader, boolean explicit)
        throws IOException {
        if (loader == null)
            loader = (ClassLoader) AccessController.doPrivileged(
                J2DoPrivHelper.getContextClassLoaderAction());

        Enumeration<URL> urls = null;
        try {
            urls = (Enumeration) AccessController.doPrivileged(
                J2DoPrivHelper.getResourcesAction(loader, rsrc)); 
            if (!urls.hasMoreElements()) {
                if (!rsrc.startsWith("META-INF"))
                    urls = (Enumeration) AccessController.doPrivileged(
                        J2DoPrivHelper.getResourcesAction(
                            loader, "META-INF/" + rsrc)); 
                if (!urls.hasMoreElements())
                    return null;
            }
        } catch (PrivilegedActionException pae) {
            throw (IOException) pae.getException();
        }

        ConfigurationParser parser = new ConfigurationParser(m);
        PersistenceUnitInfoImpl pinfo = parseResources(parser, urls, name, 
            loader);
        if (pinfo == null) {
            if (!explicit)
                return Boolean.FALSE;
            throw new MissingResourceException(_loc.get("missing-xml-config", 
                rsrc, String.valueOf(name)).getMessage(), getClass().getName(), 
                rsrc);
        } else if (!isOpenJPAPersistenceProvider(pinfo, loader)) {
            if (!explicit) {
                warnUnknownProvider(pinfo);
                return Boolean.FALSE;
            }
            throw new MissingResourceException(_loc.get("unknown-provider", 
                rsrc, name, pinfo.getPersistenceProviderClassName()).
                getMessage(), getClass().getName(), rsrc);
        }
        cp.addProperties(pinfo.toOpenJPAProperties());
        cp.setSource(pinfo.getPersistenceXmlFileUrl().toString());
        return Boolean.TRUE;
    }

    /**
     * Parse resources at the given location. Searches for a
     * PersistenceUnitInfo with the requested name, or an OpenJPA unit if
     * no name given (preferring an unnamed OpenJPA unit to a named one).
     */
    private PersistenceUnitInfoImpl parseResources(ConfigurationParser parser,
        Enumeration<URL> urls, String name, ClassLoader loader)
        throws IOException {
        List<PersistenceUnitInfoImpl> pinfos = 
            new ArrayList<PersistenceUnitInfoImpl>();
        for (URL url : Collections.list(urls)) {
            parser.parse(url);
            pinfos.addAll((List<PersistenceUnitInfoImpl>) parser.getResults());
        }
        return findUnit(pinfos, name, loader);
    }

    /**
     * Find the unit with the given name, or an OpenJPA unit if no name is
     * given (preferring an unnamed OpenJPA unit to a named one).
     */
    private PersistenceUnitInfoImpl findUnit(List<PersistenceUnitInfoImpl> 
        pinfos, String name, ClassLoader loader) {
        PersistenceUnitInfoImpl ojpa = null;
        for (PersistenceUnitInfoImpl pinfo : pinfos) {
            // found named unit?
            if (name != null) {
                if (name.equals(pinfo.getPersistenceUnitName()))
                    return pinfo;
                continue;
            }

            if (isOpenJPAPersistenceProvider(pinfo, loader)) {
                // if no name given and found unnamed unit, return it.  
                // otherwise record as default unit unless we find a 
                // better match later
                if (StringUtils.isEmpty(pinfo.getPersistenceUnitName()))
                    return pinfo;
                if (ojpa == null)
                    ojpa = pinfo;
            }
        }
        return ojpa;
    }

    /**
     * Return whether the given persistence unit uses an OpenJPA provider.
     */
    private static boolean isOpenJPAPersistenceProvider
        (PersistenceUnitInfo pinfo, ClassLoader loader) {
        String provider = pinfo.getPersistenceProviderClassName();
        if (StringUtils.isEmpty(provider) 
 PersistenceProviderImpl.class.getName().equals(provider))
            return true;

        if (loader == null)
            loader = (ClassLoader) AccessController.doPrivileged(
                J2DoPrivHelper.getContextClassLoaderAction());
        try {
            if (PersistenceProviderImpl.class.isAssignableFrom
                (Class.forName(provider, false, loader)))
                return true;
        } catch (Throwable t) {
            log(_loc.get("unloadable-provider", provider, t).getMessage());
            return false;
        }
        return false;
    }

    /**
     * Warn the user that we could only find an unrecognized persistence 
     * provider.
     */
    private static void warnUnknownProvider(PersistenceUnitInfo pinfo) {
        log(_loc.get("unrecognized-provider", 
            pinfo.getPersistenceProviderClassName()).getMessage());
    }
    
    /**
     * Log a message.   
     */
    private static void log(String msg) {
        // at this point logging isn't configured yet
        System.err.println(msg);
    }

    /**
     * Custom configuration provider.   
     */
    public static class ConfigurationProviderImpl
        extends MapConfigurationProvider {

        private String _source;

        public ConfigurationProviderImpl() {
        }

        public ConfigurationProviderImpl(Map props) {
            super(props);
        }

        /**
         * Set the source of information in this provider.
         */
        public void setSource(String source) {
            _source = source;
        }

        @Override
        public void setInto(Configuration conf) {
            if (conf instanceof OpenJPAConfiguration) {
                OpenJPAConfiguration oconf = (OpenJPAConfiguration) conf;
                oconf.setSpecification(SPEC_JPA);

                // we merge several persistence.xml elements into the 
                // MetaDataFactory property implicitly.  if the user has a
                // global openjpa.xml with this property set, its value will
                // get overwritten by our implicit setting.  so instead, combine
                // the global value with our settings
                String orig = oconf.getMetaDataFactory();
                if (!StringUtils.isEmpty(orig)) {
                    String key = ProductDerivations.getConfigurationKey
                        ("MetaDataFactory", getProperties());
                    Object override = getProperties().get(key);
                    if (override instanceof String)
                        addProperty(key, Configurations.combinePlugins(orig, 
                            (String) override));
                }
            }

            super.setInto(conf, null);
            Log log = conf.getConfigurationLog();
            if (log.isTraceEnabled()) {
                String src = (_source == null) ? "?" : _source;
                log.trace(_loc.get("conf-load", src, getProperties()));
            }
        }
    }

    /**
     * SAX handler capable of parsing an JPA persistence.xml file.
     * Package-protected for testing.
     */
    static class ConfigurationParser
        extends XMLMetaDataParser {

        private final Map _map;
        private PersistenceUnitInfoImpl _info = null;
        private URL _source = null;

        public ConfigurationParser(Map map) {
            _map = map;
            setCaching(false);
            setValidating(true);
            setParseText(true);
        }

        @Override
        public void parse(URL url)
            throws IOException {
            _source = url;
            super.parse(url);
        }

        @Override
        public void parse(File file)
            throws IOException {
            _source = file.toURL();
            super.parse(file);
        }

        @Override
        protected Object getSchemaSource() {
            return getClass().getResourceAsStream("persistence-xsd.rsrc");
        }

        @Override
        protected void reset() {
            super.reset();
            _info = null;
            _source = null;
        }

        protected boolean startElement(String name, Attributes attrs)
            throws SAXException {
            if (currentDepth() == 1)
                startPersistenceUnit(attrs);
            else if (currentDepth() == 3 && "property".equals(name))
                _info.setProperty(attrs.getValue("name"),
                    attrs.getValue("value"));
            return true;
        }

        protected void endElement(String name)
            throws SAXException {
            if (currentDepth() == 1) {
                _info.fromUserProperties(_map);
                addResult(_info);
            }
            if (currentDepth() != 2)
                return;

            switch (name.charAt(0)) {
                case 'c': // class
                    _info.addManagedClassName(currentText());
                case 'e': // exclude-unlisted-classes
                    _info.setExcludeUnlistedClasses("true".equalsIgnoreCase
                        (currentText()));
                    break;
                case 'j':
                    if ("jta-data-source".equals(name))
                        _info.setJtaDataSourceName(currentText());
                    else // jar-file
                    {
                        try {
                            _info.addJarFileName(currentText());
                        } catch (IllegalArgumentException iae) {
                            throw getException(iae.getMessage());
                        }
                    }
                    break;
                case 'm': // mapping-file
                    _info.addMappingFileName(currentText());
                    break;
                case 'n': // non-jta-data-source
                    _info.setNonJtaDataSourceName(currentText());
                    break;
                case 'p':
                    if ("provider".equals(name))
                        _info.setPersistenceProviderClassName(currentText());
                    break;
            }
        }

        /**
         * Parse persistence-unit element.
         */
        private void startPersistenceUnit(Attributes attrs)
            throws SAXException {
            _info = new PersistenceUnitInfoImpl();
            _info.setPersistenceUnitName(attrs.getValue("name"));

            // we only parse this ourselves outside a container, so default
            // transaction type to local
            String val = attrs.getValue("transaction-type");
            if (val == null)
                _info.setTransactionType
                    (PersistenceUnitTransactionType.RESOURCE_LOCAL);
            else
                _info.setTransactionType(Enum.valueOf
                    (PersistenceUnitTransactionType.class, val));

            if (_source != null)
                _info.setPersistenceXmlFileUrl(_source);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17899.java