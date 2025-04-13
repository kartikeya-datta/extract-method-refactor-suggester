error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/682.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/682.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/682.java
text:
```scala
b@@uilder.field("_indexed_chars", size);

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

package org.elasticsearch.plugin.mapper.attachments.test;

import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.cli.CliTool;
import org.elasticsearch.common.cli.CliToolConfig;
import org.elasticsearch.common.cli.Terminal;
import org.elasticsearch.common.cli.commons.CommandLine;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.mapper.DocumentMapperParser;
import org.elasticsearch.index.mapper.ParseContext;
import org.elasticsearch.index.mapper.attachment.AttachmentMapper;
import org.elasticsearch.index.mapper.xcontent.MapperTestUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import static org.elasticsearch.common.cli.CliToolConfig.Builder.cmd;
import static org.elasticsearch.common.cli.CliToolConfig.Builder.option;
import static org.elasticsearch.common.io.Streams.copyToByteArray;
import static org.elasticsearch.common.io.Streams.copyToStringFromClasspath;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * This class provides a simple main class which can be used to test what is extracted from a given binary file.
 * You can run it using
 *  -u file://URL/TO/YOUR/DOC
 *  -s set extracted size (default to mapper attachment size)
 *  BASE64 encoded binary
 *
 * Example:
 *  StandaloneTest BASE64Text
 *  StandaloneTest -u /tmp/mydoc.pdf
 *  StandaloneTest -u /tmp/mydoc.pdf -s 1000000
 */
public class StandaloneTest extends CliTool {

    private static final CliToolConfig CONFIG = CliToolConfig.config("tika", StandaloneTest.class)
                        .cmds(TikaTest.CMD)
                .build();

    static class TikaTest extends Command {
        private static final String NAME = "tika";
        private final String url;
        private final Integer size;
        private final String base64text;
        private final DocumentMapper docMapper;

        private static final CliToolConfig.Cmd CMD = cmd(NAME, TikaTest.class)
                .options(option("u", "url").required(false).hasArg(false))
                .options(option("s", "size").required(false).hasArg(false))
                .build();

        protected TikaTest(Terminal terminal, String url, Integer size, String base64text) throws IOException {
            super(terminal);
            this.size = size;
            this.url = url;
            this.base64text = base64text;
            DocumentMapperParser mapperParser = MapperTestUtils.newMapperParser();
            mapperParser.putTypeParser(AttachmentMapper.CONTENT_TYPE, new AttachmentMapper.TypeParser());

            String mapping = copyToStringFromClasspath("/org/elasticsearch/index/mapper/xcontent/test-mapping.json");
            docMapper = mapperParser.parse(mapping);
        }

        @Override
        public ExitStatus execute(Settings settings, Environment env) throws Exception {
            XContentBuilder builder = jsonBuilder().startObject().field("_id", 1).field("file").startObject();

            if (base64text != null) {
                // If base64 is provided
                builder.field("_content", base64text);
            } else {
                // A file is provided
                File file = new File(new URL(url).getFile());
                boolean exists = file.exists();
                if (!exists) {
                    return ExitStatus.IO_ERROR;
                }

                byte[] bytes = copyToByteArray(file);
                builder.field("_content", bytes);
            }

            if (size >= 0) {
                builder.field("_indexed_chars", 10);
            }

            BytesReference json = builder.endObject().endObject().bytes();

            ParseContext.Document doc = docMapper.parse(json).rootDoc();

            terminal.println("## Extracted text");
            terminal.println("--------------------- BEGIN -----------------------");
            terminal.println(doc.get(docMapper.mappers().smartName("file").mapper().names().indexName()));
            terminal.println("---------------------- END ------------------------");
            terminal.println("## Metadata");
            printMetadataContent(doc, AttachmentMapper.FieldNames.AUTHOR);
            printMetadataContent(doc, AttachmentMapper.FieldNames.CONTENT_LENGTH);
            printMetadataContent(doc, AttachmentMapper.FieldNames.CONTENT_TYPE);
            printMetadataContent(doc, AttachmentMapper.FieldNames.DATE);
            printMetadataContent(doc, AttachmentMapper.FieldNames.KEYWORDS);
            printMetadataContent(doc, AttachmentMapper.FieldNames.LANGUAGE);
            printMetadataContent(doc, AttachmentMapper.FieldNames.NAME);
            printMetadataContent(doc, AttachmentMapper.FieldNames.TITLE);

            return ExitStatus.OK;
        }

        private void printMetadataContent(ParseContext.Document doc, String field) {
            terminal.println("- %s: %s", field, doc.get(docMapper.mappers().smartName("file." + field).mapper().names().indexName()));
        }

        public static Command parse(Terminal terminal, CommandLine cli) throws IOException {
            String url = cli.getOptionValue("u");
            String base64text = null;
            String sSize = cli.getOptionValue("s");
            Integer size = sSize != null ? Integer.parseInt(sSize) : -1;
            if (url == null && cli.getArgs().length == 0) {
                    return exitCmd(ExitStatus.USAGE, terminal, "url or BASE64 content should be provided (type -h for help)");
            }
            if (url == null) {
                if (cli.getArgs().length == 0) {
                    return exitCmd(ExitStatus.USAGE, terminal, "url or BASE64 content should be provided (type -h for help)");
                }
                base64text = cli.getArgs()[0];
            } else {
                if (cli.getArgs().length == 1) {
                    return exitCmd(ExitStatus.USAGE, terminal, "url or BASE64 content should be provided. Not both. (type -h for help)");
                }
            }
            return new TikaTest(terminal, url, size, base64text);
        }
    }

    public StandaloneTest() {
        super(CONFIG);
    }


    public static void main(String[] args) {
        StandaloneTest pluginManager = new StandaloneTest();
        pluginManager.execute(args);
    }

    @Override
    protected Command parse(String cmdName, CommandLine cli) throws Exception {
        switch (cmdName.toLowerCase(Locale.ROOT)) {
            case TikaTest.NAME: return TikaTest.parse(terminal, cli);
            default:
                    assert false : "can't get here as cmd name is validated before this method is called";
                    return exitCmd(ExitStatus.CODE_ERROR);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/682.java