error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3038.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3038.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3038.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.queryparser.flexible.standard;

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

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType.NumericType;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;
import org.apache.lucene.queryparser.flexible.standard.config.NumberDateFormat;
import org.apache.lucene.queryparser.flexible.standard.config.NumericConfig;
import org.apache.lucene.queryparser.flexible.standard.parser.EscapeQuerySyntaxImpl;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestNumericQueryParser extends LuceneTestCase {
  
  private static enum NumberType {
    NEGATIVE, ZERO, POSITIVE;
  }
  
  final private static int[] DATE_STYLES = {DateFormat.FULL, DateFormat.LONG,
      DateFormat.MEDIUM, DateFormat.SHORT};
  
  final private static int PRECISION_STEP = 8;
  final private static String FIELD_NAME = "field";
  private static Locale LOCALE;
  private static TimeZone TIMEZONE;
  private static Map<String,Number> RANDOM_NUMBER_MAP;
  final private static EscapeQuerySyntax ESCAPER = new EscapeQuerySyntaxImpl();
  final private static String DATE_FIELD_NAME = "date";
  private static int DATE_STYLE;
  private static int TIME_STYLE;
  
  private static Analyzer ANALYZER;
  
  private static NumberFormat NUMBER_FORMAT;
  
  private static StandardQueryParser qp;
  
  private static NumberDateFormat DATE_FORMAT;
  
  private static Directory directory = null;
  private static IndexReader reader = null;
  private static IndexSearcher searcher = null;
  
  private static boolean checkDateFormatSanity(DateFormat dateFormat, long date) {
    try {
      return date == dateFormat.parse(dateFormat.format(new Date(date)))
        .getTime();
    } catch (ParseException e) {
      return false;
    }
  }
  
  @BeforeClass
  public static void beforeClass() throws Exception {
    ANALYZER = new MockAnalyzer(random());
    
    qp = new StandardQueryParser(ANALYZER);
    
    final HashMap<String,Number> randomNumberMap = new HashMap<>();
    
    SimpleDateFormat dateFormat;
    long randomDate;
    boolean dateFormatSanityCheckPass;
    int count = 0;
    do {
      if (count > 100) {
        fail("This test has problems to find a sane random DateFormat/NumberFormat. Stopped trying after 100 iterations.");
      }
      
      dateFormatSanityCheckPass = true;
      LOCALE = randomLocale(random());
      TIMEZONE = randomTimeZone(random());
      DATE_STYLE = randomDateStyle(random());
      TIME_STYLE = randomDateStyle(random());
      
      // assumes localized date pattern will have at least year, month, day,
      // hour, minute
      dateFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance(
          DATE_STYLE, TIME_STYLE, LOCALE);
      
      // not all date patterns includes era, full year, timezone and second,
      // so we add them here
      dateFormat.applyPattern(dateFormat.toPattern() + " G s Z yyyy");
      dateFormat.setTimeZone(TIMEZONE);
      
      DATE_FORMAT = new NumberDateFormat(dateFormat);
      
      do {
        randomDate = random().nextLong();
        
        // prune date value so it doesn't pass in insane values to some
        // calendars.
        randomDate = randomDate % 3400000000000l;
        
        // truncate to second
        randomDate = (randomDate / 1000L) * 1000L;
        
        // only positive values
        randomDate = Math.abs(randomDate);
      } while (randomDate == 0L);
      
      dateFormatSanityCheckPass &= checkDateFormatSanity(dateFormat, randomDate);
      
      dateFormatSanityCheckPass &= checkDateFormatSanity(dateFormat, 0);
      
      dateFormatSanityCheckPass &= checkDateFormatSanity(dateFormat,
          -randomDate);
      
      count++;
    } while (!dateFormatSanityCheckPass);
    
    NUMBER_FORMAT = NumberFormat.getNumberInstance(LOCALE);
    NUMBER_FORMAT.setMaximumFractionDigits((random().nextInt() & 20) + 1);
    NUMBER_FORMAT.setMinimumFractionDigits((random().nextInt() & 20) + 1);
    NUMBER_FORMAT.setMaximumIntegerDigits((random().nextInt() & 20) + 1);
    NUMBER_FORMAT.setMinimumIntegerDigits((random().nextInt() & 20) + 1);
    
    double randomDouble;
    long randomLong;
    int randomInt;
    float randomFloat;
    
    while ((randomLong = normalizeNumber(Math.abs(random().nextLong()))
        .longValue()) == 0L)
      ;
    while ((randomDouble = normalizeNumber(Math.abs(random().nextDouble()))
        .doubleValue()) == 0.0)
      ;
    while ((randomFloat = normalizeNumber(Math.abs(random().nextFloat()))
        .floatValue()) == 0.0f)
      ;
    while ((randomInt = normalizeNumber(Math.abs(random().nextInt())).intValue()) == 0)
      ;
    
    randomNumberMap.put(NumericType.LONG.name(), randomLong);
    randomNumberMap.put(NumericType.INT.name(), randomInt);
    randomNumberMap.put(NumericType.FLOAT.name(), randomFloat);
    randomNumberMap.put(NumericType.DOUBLE.name(), randomDouble);
    randomNumberMap.put(DATE_FIELD_NAME, randomDate);
    
    RANDOM_NUMBER_MAP = Collections.unmodifiableMap(randomNumberMap);
    
    directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()))
            .setMaxBufferedDocs(TestUtil.nextInt(random(), 50, 1000))
            .setMergePolicy(newLogMergePolicy()));
    
    Document doc = new Document();
    HashMap<String,NumericConfig> numericConfigMap = new HashMap<>();
    HashMap<String,Field> numericFieldMap = new HashMap<>();
    qp.setNumericConfigMap(numericConfigMap);
    
    for (NumericType type : NumericType.values()) {
      numericConfigMap.put(type.name(), new NumericConfig(PRECISION_STEP,
          NUMBER_FORMAT, type));

      FieldType ft = new FieldType(IntField.TYPE_NOT_STORED);
      ft.setNumericType(type);
      ft.setStored(true);
      ft.setNumericPrecisionStep(PRECISION_STEP);
      ft.freeze();
      final Field field;

      switch(type) {
      case INT:
        field = new IntField(type.name(), 0, ft);
        break;
      case FLOAT:
        field = new FloatField(type.name(), 0.0f, ft);
        break;
      case LONG:
        field = new LongField(type.name(), 0l, ft);
        break;
      case DOUBLE:
        field = new DoubleField(type.name(), 0.0, ft);
        break;
      default:
        fail();
        field = null;
      }
      numericFieldMap.put(type.name(), field);
      doc.add(field);
    }
    
    numericConfigMap.put(DATE_FIELD_NAME, new NumericConfig(PRECISION_STEP,
        DATE_FORMAT, NumericType.LONG));
    FieldType ft = new FieldType(LongField.TYPE_NOT_STORED);
    ft.setStored(true);
    ft.setNumericPrecisionStep(PRECISION_STEP);
    LongField dateField = new LongField(DATE_FIELD_NAME, 0l, ft);
    numericFieldMap.put(DATE_FIELD_NAME, dateField);
    doc.add(dateField);
    
    for (NumberType numberType : NumberType.values()) {
      setFieldValues(numberType, numericFieldMap);
      if (VERBOSE) System.out.println("Indexing document: " + doc);
      writer.addDocument(doc);
    }
    
    reader = writer.getReader();
    searcher = newSearcher(reader);
    writer.close();
    
  }
  
  private static Number getNumberType(NumberType numberType, String fieldName) {
    
    if (numberType == null) {
      return null;
    }
    
    switch (numberType) {
      
      case POSITIVE:
        return RANDOM_NUMBER_MAP.get(fieldName);
        
      case NEGATIVE:
        Number number = RANDOM_NUMBER_MAP.get(fieldName);
        
        if (NumericType.LONG.name().equals(fieldName)
 DATE_FIELD_NAME.equals(fieldName)) {
          number = -number.longValue();
          
        } else if (NumericType.DOUBLE.name().equals(fieldName)) {
          number = -number.doubleValue();
          
        } else if (NumericType.FLOAT.name().equals(fieldName)) {
          number = -number.floatValue();
          
        } else if (NumericType.INT.name().equals(fieldName)) {
          number = -number.intValue();
          
        } else {
          throw new IllegalArgumentException("field name not found: "
              + fieldName);
        }
        
        return number;
        
      default:
        return 0;
        
    }
    
  }
  
  private static void setFieldValues(NumberType numberType,
      HashMap<String,Field> numericFieldMap) {
    
    Number number = getNumberType(numberType, NumericType.DOUBLE
        .name());
    numericFieldMap.get(NumericType.DOUBLE.name()).setDoubleValue(
        number.doubleValue());
    
    number = getNumberType(numberType, NumericType.INT.name());
    numericFieldMap.get(NumericType.INT.name()).setIntValue(
        number.intValue());
    
    number = getNumberType(numberType, NumericType.LONG.name());
    numericFieldMap.get(NumericType.LONG.name()).setLongValue(
        number.longValue());
    
    number = getNumberType(numberType, NumericType.FLOAT.name());
    numericFieldMap.get(NumericType.FLOAT.name()).setFloatValue(
        number.floatValue());
    
    number = getNumberType(numberType, DATE_FIELD_NAME);
    numericFieldMap.get(DATE_FIELD_NAME).setLongValue(number.longValue());
  }
  
  private static int randomDateStyle(Random random) {
    return DATE_STYLES[random.nextInt(DATE_STYLES.length)];
  }
  
  @Test
  public void testInclusiveNumericRange() throws Exception {
    assertRangeQuery(NumberType.ZERO, NumberType.ZERO, true, true, 1);
    assertRangeQuery(NumberType.ZERO, NumberType.POSITIVE, true, true, 2);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.ZERO, true, true, 2);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.POSITIVE, true, true, 3);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.NEGATIVE, true, true, 1);
  }
  
   @Test
  // test disabled since standard syntax parser does not work with inclusive and
  // exclusive at the same time
  public void testInclusiveLowerNumericRange() throws Exception {
    assertRangeQuery(NumberType.NEGATIVE, NumberType.ZERO, false, true, 1);
    assertRangeQuery(NumberType.ZERO, NumberType.POSITIVE, false, true, 1);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.POSITIVE, false, true, 2);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.NEGATIVE, false, true, 0);
   }
  
  @Test
  // test disabled since standard syntax parser does not work with inclusive and
  // exclusive at the same time
  public void testInclusiveUpperNumericRange() throws Exception {
    assertRangeQuery(NumberType.NEGATIVE, NumberType.ZERO, true, false, 1);
    assertRangeQuery(NumberType.ZERO, NumberType.POSITIVE, true, false, 1);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.POSITIVE, true, false, 2);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.NEGATIVE, true, false, 0);
  }
  
  @Test
  public void testExclusiveNumericRange() throws Exception {
    assertRangeQuery(NumberType.ZERO, NumberType.ZERO, false, false, 0);
    assertRangeQuery(NumberType.ZERO, NumberType.POSITIVE, false, false, 0);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.ZERO, false, false, 0);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.POSITIVE, false, false, 1);
    assertRangeQuery(NumberType.NEGATIVE, NumberType.NEGATIVE, false, false, 0);
  }
  
  @Test
  public void testOpenRangeNumericQuery() throws Exception {
    assertOpenRangeQuery(NumberType.ZERO, "<", 1);
    assertOpenRangeQuery(NumberType.POSITIVE, "<", 2);
    assertOpenRangeQuery(NumberType.NEGATIVE, "<", 0);
    
    assertOpenRangeQuery(NumberType.ZERO, "<=", 2);
    assertOpenRangeQuery(NumberType.POSITIVE, "<=", 3);
    assertOpenRangeQuery(NumberType.NEGATIVE, "<=", 1);
    
    assertOpenRangeQuery(NumberType.ZERO, ">", 1);
    assertOpenRangeQuery(NumberType.POSITIVE, ">", 0);
    assertOpenRangeQuery(NumberType.NEGATIVE, ">", 2);
    
    assertOpenRangeQuery(NumberType.ZERO, ">=", 2);
    assertOpenRangeQuery(NumberType.POSITIVE, ">=", 1);
    assertOpenRangeQuery(NumberType.NEGATIVE, ">=", 3);
    
    assertOpenRangeQuery(NumberType.NEGATIVE, "=", 1);
    assertOpenRangeQuery(NumberType.ZERO, "=", 1);
    assertOpenRangeQuery(NumberType.POSITIVE, "=", 1);
    
    assertRangeQuery(NumberType.NEGATIVE, null, true, true, 3);
    assertRangeQuery(NumberType.NEGATIVE, null, false, true, 2);
    assertRangeQuery(NumberType.POSITIVE, null, true, false, 1);
    assertRangeQuery(NumberType.ZERO, null, false, false, 1);

    assertRangeQuery(null, NumberType.POSITIVE, true, true, 3);
    assertRangeQuery(null, NumberType.POSITIVE, true, false, 2);
    assertRangeQuery(null, NumberType.NEGATIVE, false, true, 1);
    assertRangeQuery(null, NumberType.ZERO, false, false, 1);
    
    assertRangeQuery(null, null, false, false, 3);
    assertRangeQuery(null, null, true, true, 3);
    
  }
  
  @Test
  public void testSimpleNumericQuery() throws Exception {
    assertSimpleQuery(NumberType.ZERO, 1);
    assertSimpleQuery(NumberType.POSITIVE, 1);
    assertSimpleQuery(NumberType.NEGATIVE, 1);
  }
  
  public void assertRangeQuery(NumberType lowerType, NumberType upperType,
      boolean lowerInclusive, boolean upperInclusive, int expectedDocCount)
      throws QueryNodeException, IOException {
    
    StringBuilder sb = new StringBuilder();
    
    String lowerInclusiveStr = (lowerInclusive ? "[" : "{");
    String upperInclusiveStr = (upperInclusive ? "]" : "}");
    
    for (NumericType type : NumericType.values()) {
      String lowerStr = numberToString(getNumberType(lowerType, type.name()));
      String upperStr = numberToString(getNumberType(upperType, type.name()));
      
      sb.append("+").append(type.name()).append(':').append(lowerInclusiveStr)
          .append('"').append(lowerStr).append("\" TO \"").append(upperStr)
          .append('"').append(upperInclusiveStr).append(' ');
    }
    
    Number lowerDateNumber = getNumberType(lowerType, DATE_FIELD_NAME);
    Number upperDateNumber = getNumberType(upperType, DATE_FIELD_NAME);
    String lowerDateStr;
    String upperDateStr;
    
    if (lowerDateNumber != null) {
      lowerDateStr = ESCAPER.escape(
          DATE_FORMAT.format(new Date(lowerDateNumber.longValue())), LOCALE,
          EscapeQuerySyntax.Type.STRING).toString();
      
    } else {
      lowerDateStr = "*";
    }
    
    if (upperDateNumber != null) {
    upperDateStr = ESCAPER.escape(
          DATE_FORMAT.format(new Date(upperDateNumber.longValue())), LOCALE,
          EscapeQuerySyntax.Type.STRING).toString();
    
    } else {
      upperDateStr = "*";
    }
    
    sb.append("+").append(DATE_FIELD_NAME).append(':')
        .append(lowerInclusiveStr).append('"').append(lowerDateStr).append(
            "\" TO \"").append(upperDateStr).append('"').append(
            upperInclusiveStr);
    
    testQuery(sb.toString(), expectedDocCount);
    
  }
  
  public void assertOpenRangeQuery(NumberType boundType, String operator, int expectedDocCount)
      throws QueryNodeException, IOException {

    StringBuilder sb = new StringBuilder();
    
    for (NumericType type : NumericType.values()) {
      String boundStr = numberToString(getNumberType(boundType, type.name()));
      
      sb.append("+").append(type.name()).append(operator).append('"').append(boundStr).append('"').append(' ');
    }
    
    String boundDateStr = ESCAPER.escape(
        DATE_FORMAT.format(new Date(getNumberType(boundType, DATE_FIELD_NAME)
            .longValue())), LOCALE, EscapeQuerySyntax.Type.STRING).toString();
    
    sb.append("+").append(DATE_FIELD_NAME).append(operator).append('"').append(boundDateStr).append('"');
    
    testQuery(sb.toString(), expectedDocCount);
  }
  
  public void assertSimpleQuery(NumberType numberType, int expectedDocCount)
      throws QueryNodeException, IOException {
    StringBuilder sb = new StringBuilder();
    
    for (NumericType type : NumericType.values()) {
      String numberStr = numberToString(getNumberType(numberType, type.name()));
      sb.append('+').append(type.name()).append(":\"").append(numberStr)
          .append("\" ");
    }
    
    String dateStr = ESCAPER.escape(
        DATE_FORMAT.format(new Date(getNumberType(numberType, DATE_FIELD_NAME)
            .longValue())), LOCALE, EscapeQuerySyntax.Type.STRING).toString();
    
    sb.append('+').append(DATE_FIELD_NAME).append(":\"").append(dateStr)
        .append('"');
    
    testQuery(sb.toString(), expectedDocCount);
    
  }
  
  private void testQuery(String queryStr, int expectedDocCount)
      throws QueryNodeException, IOException {
    if (VERBOSE) System.out.println("Parsing: " + queryStr);
    
    Query query = qp.parse(queryStr, FIELD_NAME);
    if (VERBOSE) System.out.println("Querying: " + query);
    TopDocs topDocs = searcher.search(query, 1000);
    
    String msg = "Query <" + queryStr + "> retrieved " + topDocs.totalHits
        + " document(s), " + expectedDocCount + " document(s) expected.";
    
    if (VERBOSE) System.out.println(msg);
    
    assertEquals(msg, expectedDocCount, topDocs.totalHits);
  }
  
  private static String numberToString(Number number) {
    return number == null ? "*" : ESCAPER.escape(NUMBER_FORMAT.format(number),
        LOCALE, EscapeQuerySyntax.Type.STRING).toString();
  }
  
  private static Number normalizeNumber(Number number) throws ParseException {
    return NUMBER_FORMAT.parse(NUMBER_FORMAT.format(number));
  }
  
  @AfterClass
  public static void afterClass() throws Exception {
    searcher = null;
    reader.close();
    reader = null;
    directory.close();
    directory = null;
    qp = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3038.java