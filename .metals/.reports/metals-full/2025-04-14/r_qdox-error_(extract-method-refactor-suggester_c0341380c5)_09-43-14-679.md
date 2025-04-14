error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2324.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2324.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2324.java
text:
```scala
e@@valuators.put(func, (Evaluator) DocBuilder.loadClass(clz, null)

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.handler.dataimport;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 * <p>
 * Mapping for data-config.xml
 * </p>
 * <p/>
 * <p>
 * Refer to <a
 * href="http://wiki.apache.org/solr/DataImportHandler">http://wiki.apache.org/solr/DataImportHandler</a>
 * for more details.
 * </p>
 * <p/>
 * <b>This API is experimental and subject to change</b>
 *
 * @version $Id$
 * @since solr 1.3
 */
public class DataConfig {
  public List<Document> documents;

  public List<Props> properties;

  private Map<String, Document> documentCache;

  public Map<String, Evaluator> evaluators = new HashMap<String, Evaluator>();

  public Script script;

  public Map<String, Properties> dataSources = new HashMap<String, Properties>();

  public Document getDocumentByName(String name) {
    if (documentCache == null) {
      documentCache = new HashMap<String, Document>();
      for (Document document : documents)
        documentCache.put(document.name, document);
    }

    return documentCache.get(name);
  }

  public static class Document {
    public String name;

    public String deleteQuery;

    public List<Entity> entities = new ArrayList<Entity>();

    public List<Field> fields;

    public Document() {
    }

    public Document(Element element) {
      this.name = getStringAttribute(element, NAME, null);
      this.deleteQuery = getStringAttribute(element, "deleteQuery", null);
      List<Element> l = getChildNodes(element, "entity");
      for (Element e : l)
        entities.add(new Entity(e));
      // entities = new Entity(l.get(0));
      l = getChildNodes(element, "field");
      if (!l.isEmpty())
        fields = new ArrayList<Field>();
      for (Element e : l)
        fields.add(new Field(e));
    }
  }

  public static class Props {
    public String name;

    public String file;
  }

  public static class Entity {
    public String name;

    public String pk;

    public String dataSource;

    public Map<String, String> allAttributes;

    public String proc;

    public String docRoot;

    public boolean isDocRoot = false;

    public List<Field> fields;

    public List<Map<String, String>> allFieldsList = new ArrayList<Map<String, String>>();

    public List<Entity> entities;

    public String[] primaryKeys;

    public Entity parentEntity;

    public EntityProcessor processor;

    @SuppressWarnings("unchecked")
    public DataSource dataSrc;

    public Script script;

    public List<Field> implicitFields;

    public Entity() {
    }

    public Entity(Element element) {
      name = getStringAttribute(element, NAME, null);
      pk = getStringAttribute(element, "pk", null);
      docRoot = getStringAttribute(element, ROOT_ENTITY, null);
      proc = getStringAttribute(element, PROCESSOR, null);
      dataSource = getStringAttribute(element, DataImporter.DATA_SRC, null);
      allAttributes = getAllAttributes(element);
      List<Element> n = getChildNodes(element, "field");
      fields = new ArrayList<Field>();
      for (Element elem : n)
        fields.add(new Field(elem));
      n = getChildNodes(element, "entity");
      if (!n.isEmpty())
        entities = new ArrayList<Entity>();
      for (Element elem : n)
        entities.add(new Entity(elem));

    }

    public void clearCache() {
      if (entities != null) {
        for (Entity entity : entities)
          entity.clearCache();
      }

      try {
        processor.destroy();
      } catch (Exception e) {
        /*no op*/
      }
      processor = null;
      if (dataSrc != null)
        dataSrc.close();

    }
  }

  public static class Script {
    public String language;

    public String script;

    public Script() {
    }

    public Script(Element e) {
      this.language = getStringAttribute(e, "language", "JavaScript");
      StringBuffer buffer = new StringBuffer();
      String script = getTxt(e, buffer);
      if (script != null)
        this.script = script.trim();
    }
  }

  public static class Field {

    public String column;

    public String name;

    public Float boost = 1.0f;

    public boolean toWrite = true;

    public boolean multiValued = false;

    public String nameOrColName;

    public Map<String, String> allAttributes = new HashMap<String, String>() {
      public String put(String key, String value) {
        if (super.containsKey(key))
          return super.get(key);
        return super.put(key, value);
      }
    };

    public Field() {
    }

    public Field(Element e) {
      this.name = getStringAttribute(e, DataImporter.NAME, null);
      this.column = getStringAttribute(e, DataImporter.COLUMN, null);
      this.boost = Float.parseFloat(getStringAttribute(e, "boost", "1.0f"));
      allAttributes.putAll(getAllAttributes(e));
    }

    public Field(String name, boolean b) {
      name = nameOrColName = column = name;
      multiValued = b;

    }

    public String getName() {
      return name == null ? column : name;
    }

    public Entity entity;

  }

  public void readFromXml(Element e) {
    List<Element> n = getChildNodes(e, "document");
    if (!n.isEmpty())
      documents = new ArrayList<Document>();
    for (Element element : n)
      documents.add(new Document(element));

    n = getChildNodes(e, SCRIPT);
    if (!n.isEmpty()) {
      script = new Script(n.get(0));
    }

    // Add the provided evaluators
    evaluators.put(EvaluatorBag.DATE_FORMAT_EVALUATOR, EvaluatorBag
            .getDateFormatEvaluator());
    evaluators.put(EvaluatorBag.SQL_ESCAPE_EVALUATOR, EvaluatorBag
            .getSqlEscapingEvaluator());
    evaluators.put(EvaluatorBag.URL_ENCODE_EVALUATOR, EvaluatorBag
            .getUrlEvaluator());

    n = getChildNodes(e, FUNCTION);
    if (!n.isEmpty()) {
      for (Element element : n) {
        String func = getStringAttribute(element, NAME, null);
        String clz = getStringAttribute(element, CLASS, null);
        if (func == null || clz == null)
          throw new DataImportHandlerException(
                  DataImportHandlerException.SEVERE,
                  "<function> must have a 'name' and 'class' attributes");
        try {
          evaluators.put(func, (Evaluator) DocBuilder.loadClass(clz)
                  .newInstance());
        } catch (Exception exp) {
          throw new DataImportHandlerException(
                  DataImportHandlerException.SEVERE,
                  "Unable to instantiate evaluator: " + clz, exp);
        }
      }
    }
    n = getChildNodes(e, DATA_SRC);
    if (!n.isEmpty()) {
      for (Element element : n) {
        Properties p = new Properties();
        HashMap<String, String> attrs = getAllAttributes(element);
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
          p.setProperty(entry.getKey(), entry.getValue());
        }
        dataSources.put(p.getProperty("name"), p);
      }
    }
  }

  private static String getStringAttribute(Element e, String name, String def) {
    String r = e.getAttribute(name);
    if (r == null || "".equals(r.trim()))
      r = def;
    return r;
  }

  private static HashMap<String, String> getAllAttributes(Element e) {
    HashMap<String, String> m = new HashMap<String, String>();
    NamedNodeMap nnm = e.getAttributes();
    for (int i = 0; i < nnm.getLength(); i++) {
      m.put(nnm.item(i).getNodeName(), nnm.item(i).getNodeValue());
    }
    return m;
  }

  public static String getTxt(Node elem, StringBuffer buffer) {

    if (elem.getNodeType() != Node.CDATA_SECTION_NODE) {
      NodeList childs = elem.getChildNodes();
      for (int i = 0; i < childs.getLength(); i++) {
        Node child = childs.item(i);
        short childType = child.getNodeType();
        if (childType != Node.COMMENT_NODE
                && childType != Node.PROCESSING_INSTRUCTION_NODE) {
          getTxt(child, buffer);
        }
      }
    } else {
      buffer.append(elem.getNodeValue());
    }

    return buffer.toString();
  }

  public static List<Element> getChildNodes(Element e, String byName) {
    List<Element> result = new ArrayList<Element>();
    NodeList l = e.getChildNodes();
    for (int i = 0; i < l.getLength(); i++) {
      if (e.equals(l.item(i).getParentNode())
              && byName.equals(l.item(i).getNodeName()))
        result.add((Element) l.item(i));
    }
    return result;
  }

  public void clearCaches() {
    for (Document document : documents)
      for (Entity entity : document.entities)
        entity.clearCache();

  }

  public static final String SCRIPT = "script";

  public static final String NAME = "name";

  public static final String SCRIPT_LANG = "scriptlanguage";

  public static final String SCRIPT_NAME = "scriptname";

  public static final String PROCESSOR = "processor";

  public static final String IMPORTER_NS = "dataimporter";

  public static final String ROOT_ENTITY = "rootEntity";

  public static final String FUNCTION = "function";

  public static final String CLASS = "class";

  public static final String DATA_SRC = "dataSource";

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2324.java