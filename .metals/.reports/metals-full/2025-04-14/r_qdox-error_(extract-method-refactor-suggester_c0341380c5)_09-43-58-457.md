error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1632.java
text:
```scala
i@@f (_cls == null || !_cls.isAnnotationPresent(xmlTypeClass))

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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.DelegatingMetaDataFactory;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.meta.MetaDataFactory;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.meta.XMLFieldMetaData;
import org.apache.openjpa.meta.XMLMetaData;

/**
 * JAXB xml annotation metadata parser.
 *
 * @author Catalina Wei
 * @nojavadoc
 */
public class AnnotationPersistenceXMLMetaDataParser {

    private static final Localizer _loc = Localizer.forPackage
        (AnnotationPersistenceXMLMetaDataParser.class);

    private final OpenJPAConfiguration _conf;
    private final Log _log;
    private MetaDataRepository _repos = null;

    // the class we were invoked to parse
    private Class _cls = null;
    private FieldMetaData _fmd = null;
    
    // cache the JAXB Xml... classes if they are present so we do not
    // have a hard-wired dependency on JAXB here
    private Class xmlTypeClass = null;
    private Class xmlRootElementClass = null;
    private Class xmlAccessorTypeClass = null;
    private Class xmlAttributeClass = null;
    private Class xmlElementClass = null;
    private Method xmlTypeName = null;
    private Method xmlTypeNamespace = null;
    private Method xmlRootName = null;
    private Method xmlRootNamespace = null;
    private Method xmlAttributeName = null;
    private Method xmlAttributeNamespace = null;
    private Method xmlElementName = null;
    private Method xmlElementNamespace = null;
    private Method xmlAccessorValue = null;

    /**
     * Constructor; supply configuration.
     */
    public AnnotationPersistenceXMLMetaDataParser(OpenJPAConfiguration conf) {
        _conf = conf;
        _log = conf.getLog(OpenJPAConfiguration.LOG_METADATA);
        try {
            xmlTypeClass = Class.forName(
                "javax.xml.bind.annotation.XmlType");
            xmlTypeName = xmlTypeClass.getMethod("name", null);
            xmlTypeNamespace = xmlTypeClass.getMethod("namespace", null);
            xmlRootElementClass = Class.forName(
                "javax.xml.bind.annotation.XmlRootElement");
            xmlRootName = xmlRootElementClass.getMethod("name", null);
            xmlRootNamespace = xmlRootElementClass.getMethod("namespace", null);
            xmlAccessorTypeClass = Class.forName(
                "javax.xml.bind.annotation.XmlAccessorType");
            xmlAccessorValue = xmlAccessorTypeClass.getMethod("value", null);
            xmlAttributeClass = Class.forName(
                "javax.xml.bind.annotation.XmlAttribute");
            xmlAttributeName = xmlAttributeClass.getMethod("name", null);
            xmlAttributeNamespace = xmlAttributeClass.getMethod("namespace"
                , null);
            xmlElementClass = Class.forName(
                "javax.xml.bind.annotation.XmlElement");
            xmlElementName = xmlElementClass.getMethod("name", null);
            xmlElementNamespace = xmlElementClass.getMethod("namespace", null);
        } catch (Exception e) {
        }
    }

    /**
     * Configuration supplied on construction.
     */
    public OpenJPAConfiguration getConfiguration() {
        return _conf;
    }

    /**
     * Metadata log.
     */
    public Log getLog() {
        return _log;
    }

    /**
     * Returns the repository for this parser. If none has been set,
     * create a new repository and sets it.
     */
    public MetaDataRepository getRepository() {
        if (_repos == null) {
            MetaDataRepository repos = _conf.newMetaDataRepositoryInstance();
            MetaDataFactory mdf = repos.getMetaDataFactory();
            if (mdf instanceof DelegatingMetaDataFactory)
                mdf = ((DelegatingMetaDataFactory) mdf).getInnermostDelegate();
            if (mdf instanceof PersistenceMetaDataFactory)
                ((PersistenceMetaDataFactory) mdf).setXMLAnnotationParser(this);
            _repos = repos;
        }
        return _repos;
    }

    /**
     * Set the metadata repository for this parser.
     */
    public void setRepository(MetaDataRepository repos) {
        _repos = repos;
    }

    /**
     * Clear caches.
     */
    public void clear() {
        _cls = null;
        _fmd = null;
    }

    /**
     * Parse persistence metadata for the given field metadata.
     */
    public void parse(FieldMetaData fmd) {
        _fmd = fmd;
        _cls = fmd.getDeclaredType();
        if (_log.isTraceEnabled())
            _log.trace(_loc.get("parse-class", _cls.getName()));

        try {
            parseXMLClassAnnotations();
        } finally {
            _cls = null;
            _fmd = null;
        }
    }

    /**
     * Read annotations for the current type.
     */
    private XMLMetaData parseXMLClassAnnotations() {
        // check immediately whether the class has JAXB XML annotations
        if (!_cls.isAnnotationPresent(xmlTypeClass))
            return null;

        // find / create metadata
        XMLMetaData meta = getXMLMetaData();
        
        return meta;
    }

    /**
     * Find or create xml metadata for the current type. 
     */
    private synchronized XMLMetaData getXMLMetaData() {
        XMLMetaData meta = getRepository().getCachedXMLMetaData(_cls);
        if (meta == null) {
            // if not in cache, create metadata
            meta = getRepository().addXMLMetaData(_cls, _fmd.getName());
            parseXmlRootElement(_cls, meta);
            populateFromReflection(_cls, meta);
        }
        return meta;
    }
    
    private void parseXmlRootElement(Class type, XMLMetaData meta) {
        try {
            if (type.getAnnotation(xmlRootElementClass) != null) {
                meta.setXmlRootElement(true);
                meta.setXmlname((String) xmlRootName.invoke(type.getAnnotation
                    (xmlRootElementClass), new Object[]{}));
                meta.setXmlnamespace((String) xmlRootNamespace.invoke(type
                    .getAnnotation(xmlRootElementClass), new Object[]{}));
            }
            else {
                meta.setXmlname((String) xmlTypeName.invoke(type.getAnnotation
                    (xmlTypeClass), new Object[]{}));
                meta.setXmlnamespace((String) xmlTypeNamespace.invoke(type
                    .getAnnotation(xmlTypeClass), new Object[]{}));           
            }
        } catch (Exception e) {            
        }
    }

    private void populateFromReflection(Class cls, XMLMetaData meta) {
        Member[] members;
        
        Class superclass = cls.getSuperclass();

        // handle inheritance at sub-element level
        if (superclass.isAnnotationPresent(xmlTypeClass))
            populateFromReflection(superclass, meta);

        try {
            if (StringUtils.equals(xmlAccessorValue.invoke(cls.getAnnotation(
                xmlAccessorTypeClass), new Object[]{}).toString(), "FIELD"))
                members = cls.getDeclaredFields();
            else
                members = cls.getDeclaredMethods();

            for (int i = 0; i < members.length; i++) {
                Member member = members[i];
                AnnotatedElement el = (AnnotatedElement) member;
                XMLMetaData field = null;
                if (el.getAnnotation(xmlElementClass) != null) {
                    String xmlname = (String) xmlElementName.invoke(el
                        .getAnnotation(xmlElementClass), new Object[]{});
                    // avoid JAXB XML bind default name
                    if (StringUtils.equals(XMLMetaData.defaultName, xmlname))
                        xmlname = member.getName();
                    if (((Field) member).getType()
                        .isAnnotationPresent(xmlTypeClass)) {
                        field = _repos.addXMLMetaData(((Field) member).getType()
                            , member.getName());
                        parseXmlRootElement(((Field) member).getType(), field);
                        populateFromReflection(((Field) member).getType()
                            , field);
                        field.setXmltype(XMLMetaData.XMLTYPE);
                        field.setXmlname(xmlname);
                    }
                    else {
                        field = _repos.newXMLFieldMetaData(((Field) member)
                            .getType(), member.getName());
                        field.setXmltype(XMLMetaData.ELEMENT);
                        field.setXmlname(xmlname);
                        field.setXmlnamespace((String) xmlElementNamespace
                            .invoke(el.getAnnotation(xmlElementClass)
                            , new Object[]{}));                    
                    }
                }
                else if (el.getAnnotation(xmlAttributeClass) != null) {
                    field = _repos.newXMLFieldMetaData(((Field) member)
                        .getType(), member.getName());
                    field.setXmltype(XMLFieldMetaData.ATTRIBUTE);
                    String xmlname = (String) xmlAttributeName.invoke(
                        el.getAnnotation(xmlAttributeClass), new Object[]{});
                    // avoid JAXB XML bind default name
                    if (StringUtils.equals(XMLMetaData.defaultName, xmlname))
                        xmlname = member.getName();
                    field.setXmlname("@"+xmlname);
                    field.setXmlnamespace((String) xmlAttributeNamespace.invoke(
                        el.getAnnotation(xmlAttributeClass), new Object[]{}));               
                }
                meta.addField(member.getName(), field);
            }
        } catch(Exception e) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1632.java