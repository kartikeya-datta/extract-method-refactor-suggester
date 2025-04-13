error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2036.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2036.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2036.java
text:
```scala
s@@b.append("<dataSource name=\"hsqldb\" driver=\"${dataimporter.request.dots.in.hsqldb.driver}\" url=\"jdbc:hsqldb:mem:.\" /> \n");

package org.apache.solr.handler.dataimport;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.apache.solr.request.SolrQueryRequest;
import org.junit.Test;

/*
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

public class TestVariableResolverEndToEnd  extends AbstractDIHJdbcTestCase {

  @Test
  public void test() throws Exception {
    h.query("/dataimport", generateRequest());
    SolrQueryRequest req = null;
    try {
      req = req("q", "*:*", "wt", "json", "indent", "true");
      String response = h.query(req);
      log.debug(response);
      response = response.replaceAll("\\s","");
      Assert.assertTrue(response.contains("\"numFound\":1"));
      Pattern p = Pattern.compile("[\"]second1_s[\"][:][\"](.*?)[\"]");
      Matcher m = p.matcher(response);
      Assert.assertTrue(m.find());
      String yearStr = m.group(1);
      Assert.assertTrue(response.contains("\"second1_s\":\"" + yearStr + "\""));
      Assert.assertTrue(response.contains("\"second2_s\":\"" + yearStr + "\""));
      Assert.assertTrue(response.contains("\"second3_s\":\"" + yearStr + "\""));
      Assert.assertTrue(response.contains("\"PORK_s\":\"GRILL\""));
      Assert.assertTrue(response.contains("\"FISH_s\":\"FRY\""));
      Assert.assertTrue(response.contains("\"BEEF_CUTS_mult_s\":[\"ROUND\",\"SIRLOIN\"]"));
    } catch(Exception e) {
      throw e;
    } finally {
      req.close();
    }
  } 
  
  @Override
  protected String generateConfig() {
    String thirdLocaleParam = random().nextBoolean() ? "" : (", '" + Locale.getDefault() + "'");
    StringBuilder sb = new StringBuilder();
    sb.append("<dataConfig> \n");
    sb.append("<dataSource name=\"hsqldb\" driver=\"org.hsqldb.jdbcDriver\" url=\"jdbc:hsqldb:mem:.\" /> \n");
    sb.append("<document name=\"TestEvaluators\"> \n");
    sb.append("<entity name=\"FIRST\" processor=\"SqlEntityProcessor\" dataSource=\"hsqldb\" ");
    sb.append(" query=\"" +
        "select " +
        " 1 as id, " +
        " 'SELECT' as SELECT_KEYWORD, " +
        " CURRENT_TIMESTAMP as FIRST_TS " +
        "from DUAL \" >\n");
    sb.append("  <field column=\"SELECT_KEYWORD\" name=\"select_keyword_s\" /> \n");
    sb.append("  <entity name=\"SECOND\" processor=\"SqlEntityProcessor\" dataSource=\"hsqldb\" transformer=\"TemplateTransformer\" ");
    sb.append("   query=\"" +
        "${dataimporter.functions.encodeUrl(FIRST.SELECT_KEYWORD)} " +
        " 1 as SORT, " +
        " CURRENT_TIMESTAMP as SECOND_TS, " +
        " '${dataimporter.functions.formatDate(FIRST.FIRST_TS, 'yyyy'" + thirdLocaleParam + ")}' as SECOND1_S,  " +
        " 'PORK' AS MEAT, " +
        " 'GRILL' AS METHOD, " +
        " 'ROUND' AS CUTS, " +
        " 'BEEF_CUTS' AS WHATKIND " +
        "from DUAL " +
        "WHERE 1=${FIRST.ID} " +
        "UNION " +        
        "${dataimporter.functions.encodeUrl(FIRST.SELECT_KEYWORD)} " +
        " 2 as SORT, " +
        " CURRENT_TIMESTAMP as SECOND_TS, " +
        " '${dataimporter.functions.formatDate(FIRST.FIRST_TS, 'yyyy'" + thirdLocaleParam + ")}' as SECOND1_S,  " +
        " 'FISH' AS MEAT, " +
        " 'FRY' AS METHOD, " +
        " 'SIRLOIN' AS CUTS, " +
        " 'BEEF_CUTS' AS WHATKIND " +
        "from DUAL " +
        "WHERE 1=${FIRST.ID} " +
        "ORDER BY SORT \"" +
        ">\n");
    sb.append("   <field column=\"SECOND_S\" name=\"second_s\" /> \n");
    sb.append("   <field column=\"SECOND1_S\" name=\"second1_s\" /> \n");
    sb.append("   <field column=\"second2_s\" template=\"${dataimporter.functions.formatDate(SECOND.SECOND_TS, 'yyyy'" + thirdLocaleParam + ")}\" /> \n");
    sb.append("   <field column=\"second3_s\" template=\"${dih.functions.formatDate(SECOND.SECOND_TS, 'yyyy'" + thirdLocaleParam + ")}\" /> \n");
    sb.append("   <field column=\"METHOD\" name=\"${SECOND.MEAT}_s\"/>\n");
    sb.append("   <field column=\"CUTS\" name=\"${SECOND.WHATKIND}_mult_s\"/>\n");
    sb.append("  </entity>\n");
    sb.append("</entity>\n");
    sb.append("</document> \n");
    sb.append("</dataConfig> \n");
    String config = sb.toString();
    log.debug(config); 
    return config;
  }
  @Override
  protected void populateData(Connection conn) throws Exception {
    Statement s = null;
    try {
      s = conn.createStatement();
      s.executeUpdate("create table dual(dual char(1) not null)");
      s.executeUpdate("insert into dual values('Y')");
      conn.commit();
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        s.close();
      } catch (Exception ex) {}
      try {
        conn.close();
      } catch (Exception ex) {}
    }
  }
  @Override
  protected Database setAllowedDatabases() {
    return Database.HSQLDB;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2036.java