error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5417.java
text:
```scala
t@@hrow new RestTestParseException("malformed test section: field name expected but found " + token);

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.test.rest.parser;

import com.google.common.collect.Maps;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.test.rest.section.*;

import java.io.IOException;
import java.util.Map;

/**
 * Context shared across the whole tests parse phase.
 * Provides shared parse methods and holds information needed to parse the test sections (e.g. es version)
 */
public class RestTestSuiteParseContext {

    private static final SetupSectionParser SETUP_SECTION_PARSER = new SetupSectionParser();
    private static final RestTestSectionParser TEST_SECTION_PARSER = new RestTestSectionParser();
    private static final SkipSectionParser SKIP_SECTION_PARSER = new SkipSectionParser();
    private static final DoSectionParser DO_SECTION_PARSER = new DoSectionParser();
    private static final Map<String, RestTestFragmentParser<? extends ExecutableSection>> EXECUTABLE_SECTIONS_PARSERS = Maps.newHashMap();
    static {
        EXECUTABLE_SECTIONS_PARSERS.put("do", DO_SECTION_PARSER);
        EXECUTABLE_SECTIONS_PARSERS.put("set", new SetSectionParser());
        EXECUTABLE_SECTIONS_PARSERS.put("match", new MatchParser());
        EXECUTABLE_SECTIONS_PARSERS.put("is_true", new IsTrueParser());
        EXECUTABLE_SECTIONS_PARSERS.put("is_false", new IsFalseParser());
        EXECUTABLE_SECTIONS_PARSERS.put("gt", new GreaterThanParser());
        EXECUTABLE_SECTIONS_PARSERS.put("lt", new LessThanParser());
        EXECUTABLE_SECTIONS_PARSERS.put("length", new LengthParser());
    }

    private final String api;
    private final String suiteName;
    private final XContentParser parser;
    private final String currentVersion;

    public RestTestSuiteParseContext(String api, String suiteName, XContentParser parser, String currentVersion) {
        this.api = api;
        this.suiteName = suiteName;
        this.parser = parser;
        this.currentVersion = currentVersion;
    }

    public String getApi() {
        return api;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public XContentParser parser() {
        return parser;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public SetupSection parseSetupSection() throws IOException, RestTestParseException {

        advanceToFieldName();

        if ("setup".equals(parser.currentName())) {
            parser.nextToken();
            SetupSection setupSection = SETUP_SECTION_PARSER.parse(this);
            parser.nextToken();
            return setupSection;
        }

        return SetupSection.EMPTY;
    }

    public TestSection parseTestSection() throws IOException, RestTestParseException {
        return TEST_SECTION_PARSER.parse(this);
    }

    public SkipSection parseSkipSection() throws IOException, RestTestParseException {

        advanceToFieldName();

        if ("skip".equals(parser.currentName())) {
            SkipSection skipSection = SKIP_SECTION_PARSER.parse(this);
            parser.nextToken();
            return skipSection;
        }

        return SkipSection.EMPTY;
    }

    public ExecutableSection parseExecutableSection() throws IOException, RestTestParseException {
        advanceToFieldName();
        String section = parser.currentName();
        RestTestFragmentParser<? extends ExecutableSection> execSectionParser = EXECUTABLE_SECTIONS_PARSERS.get(section);
        if (execSectionParser == null) {
            throw new RestTestParseException("no parser found for executable section [" + section + "]");
        }
        ExecutableSection executableSection = execSectionParser.parse(this);
        parser.nextToken();
        return executableSection;
    }

    public DoSection parseDoSection() throws IOException, RestTestParseException {
        return DO_SECTION_PARSER.parse(this);
    }

    public void advanceToFieldName() throws IOException, RestTestParseException {
        XContentParser.Token token = parser.currentToken();
        //we are in the beginning, haven't called nextToken yet
        if (token == null) {
            token = parser.nextToken();
        }
        if (token == XContentParser.Token.START_ARRAY) {
            token = parser.nextToken();
        }
        if (token == XContentParser.Token.START_OBJECT) {
            token = parser.nextToken();
        }
        if (token != XContentParser.Token.FIELD_NAME) {
            throw new RestTestParseException("malformed test section: field suiteName expected but found " + token);
        }
    }

    public String parseField() throws IOException, RestTestParseException {
        parser.nextToken();
        assert parser.currentToken().isValue();
        String field = parser.text();
        parser.nextToken();
        return field;
    }

    public Tuple<String, Object> parseTuple() throws IOException, RestTestParseException {
        parser.nextToken();
        advanceToFieldName();
        Map<String,Object> map = parser.map();
        assert parser.currentToken() == XContentParser.Token.END_OBJECT;
        parser.nextToken();

        if (map.size() != 1) {
            throw new RestTestParseException("expected key value pair but found " + map.size() + " ");
        }

        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return Tuple.tuple(entry.getKey(), entry.getValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5417.java