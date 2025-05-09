error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1503.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1503.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1503.java
text:
```scala
r@@eturn new Long(event.getTimeStamp());

/*
 * Copyright 1999,2004 The Apache Software Foundation.
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

package org.apache.log4j.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



/**
 * A singleton helper utility which accepts a field name and a LoggingEvent and returns the
 * String value of that field.
 *
 * This class defines a grammar used in creation of an expression-based Rule.
 *
 * The only available method is Object getField(String fieldName, LoggingEvent event).
 *
 * Here is a description of the mapping of field names in the grammar
 * to fields on the logging event.  While the getField method returns an Object, the
 * individual types returned per field are described here:
 *
 * Field Name                Field value (String representation                Return type
 * LOGGER                    category name (logger)                            String
 * LEVEL                     level                                             Level
 * CLASS                     locationInformation's class name                  String
 * FILE                      locationInformation's file name                   String
 * LINE                      locationInformation's line number                 String
 * METHOD                    locationInformation's method name                 String
 * MSG                       message                                           Object
 * NDC                       NDC                                               String
 * EXCEPTION                 throwable string representation                   ThrowableInformation
 * TIMESTAMP                 timestamp                                         Long
 * THREAD                    thread                                            String
 * MDC.keyName               entry in the MDC hashtable                        Object
 *                           mapped to key [keyName]
 * PROP.keyName              entry in the Property hashtable                   String
 *                           mapped to the key [keyName]

 * NOTE:  the values for the 'keyName' portion of the MDC and PROP mappings must
 * be an exact match to the key in the hashTable (case sensitive).
 *
 * If the passed-in field is null or doesn't match an entry in the above-described
 * mapping, an exception is thrown.
 *
 * @author Scott Deboy <sdeboy@apache.org>
 * @author Paul Smith <psmith@apache.org>
 *
 */
public final class LoggingEventFieldResolver {
  public static final List keywordList = new ArrayList();
  public static final String LOGGER_FIELD = "LOGGER";
  public static final String LEVEL_FIELD = "LEVEL";
  public static final String CLASS_FIELD = "CLASS";
  public static final String FILE_FIELD = "FILE";
  public static final String LINE_FIELD = "LINE";
  public static final String METHOD_FIELD = "METHOD";
  public static final String MSG_FIELD = "MSG";
  public static final String NDC_FIELD = "NDC";
  public static final String EXCEPTION_FIELD = "EXCEPTION";
  public static final String TIMESTAMP_FIELD = "TIMESTAMP";
  public static final String THREAD_FIELD = "THREAD";
  public static final String MDC_FIELD = "MDC.";
  public static final String PROP_FIELD = "PROP.";
  public static final String EMPTY_STRING = "";
  private static final LoggingEventFieldResolver resolver =
    new LoggingEventFieldResolver();

  private LoggingEventFieldResolver() {
    keywordList.add(LOGGER_FIELD);
    keywordList.add(LEVEL_FIELD);
    keywordList.add(CLASS_FIELD);
    keywordList.add(FILE_FIELD);
    keywordList.add(LINE_FIELD);
    keywordList.add(METHOD_FIELD);
    keywordList.add(MSG_FIELD);
    keywordList.add(NDC_FIELD);
    keywordList.add(EXCEPTION_FIELD);
    keywordList.add(TIMESTAMP_FIELD);
    keywordList.add(THREAD_FIELD);
    keywordList.add(MDC_FIELD);
    keywordList.add(PROP_FIELD);
  }
  
  public String applyFields(String replaceText, LoggingEvent event) {
  	  if (replaceText == null) {
  	  	return null;
  	  }
      StringTokenizer tokenizer = new StringTokenizer(replaceText);
      StringBuffer result = new StringBuffer();
      
      while (tokenizer.hasMoreTokens()) {
          String token = tokenizer.nextToken();
          if (isField(token)  || (token.toUpperCase().startsWith(MDC_FIELD) || token.toUpperCase().startsWith(PROP_FIELD))) {
              result.append(getValue(token, event).toString());
          } else { 
              result.append(token);
          }
      }
      return result.toString();
  }

  public static LoggingEventFieldResolver getInstance() {
    return resolver;
  }

  public boolean isField(String fieldName) {
    if (fieldName != null) {
        return (keywordList.contains(fieldName.toUpperCase()) || fieldName.toUpperCase().startsWith(MDC_FIELD) || fieldName.toUpperCase().startsWith(PROP_FIELD));
    }
    return false;
  }

  public Object getValue(String fieldName, LoggingEvent event) {
    String upperField = fieldName.toUpperCase();
    LocationInfo info = null;
    if (event.locationInformationExists()) {
        info = event.getLocationInformation();
    }
    if (LOGGER_FIELD.equals(upperField)) {
      return event.getLoggerName();
    } else if (LEVEL_FIELD.equals(upperField)) {
      return event.getLevel();
    } else if (CLASS_FIELD.equals(upperField)) {
      return ((info == null) ? EMPTY_STRING : info.getClassName());
    } else if (FILE_FIELD.equals(upperField)) {
      return ((info == null) ? EMPTY_STRING : info.getFileName());
    } else if (LINE_FIELD.equals(upperField)) {
      return ((info == null) ? EMPTY_STRING : info.getLineNumber());
    } else if (METHOD_FIELD.equals(upperField)) {
      return ((info == null) ? EMPTY_STRING : info.getMethodName());
    } else if (MSG_FIELD.equals(upperField)) {
      return event.getMessage();
    } else if (NDC_FIELD.equals(upperField)) {
      String ndcValue = event.getNDC();
      return ((ndcValue == null) ? EMPTY_STRING : ndcValue);
    } else if (EXCEPTION_FIELD.equals(upperField)) {
      return (event.getThrowableStrRep() == null ? EMPTY_STRING : getExceptionMessage(event.getThrowableStrRep()));
    } else if (TIMESTAMP_FIELD.equals(upperField)) {
      return new Long(event.timeStamp);
    } else if (THREAD_FIELD.equals(upperField)) {
      return event.getThreadName();
    } else if (upperField.startsWith(MDC_FIELD)) {
      //note: need to use actual fieldname since case matters
      Object mdcValue = event.getMDC(fieldName.substring(4));
      return ((mdcValue == null) ? EMPTY_STRING : mdcValue.toString());
    } else if (upperField.startsWith(PROP_FIELD)) {
      //note: need to use actual fieldname since case matters
      String propValue = event.getProperty(fieldName.substring(5));
      return ((propValue == null) ? EMPTY_STRING : propValue);
    }

    //there wasn't a match, so throw a runtime exception
    throw new IllegalArgumentException("Unsupported field name: " + fieldName);
  }

    private String getExceptionMessage(String[] exception) {
        StringBuffer buff = new StringBuffer();
        for (int i=0;i<exception.length;i++) {
            buff.append(exception[i]);
        }
    	return buff.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1503.java