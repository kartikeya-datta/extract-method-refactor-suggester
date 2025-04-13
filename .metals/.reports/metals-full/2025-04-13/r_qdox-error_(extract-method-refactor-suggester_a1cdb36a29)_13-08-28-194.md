error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3427.java
text:
```scala
public static final V@@irtualFileFilter TRUE = MatchAllVirtualFileFilter.INSTANCE;

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.jdr.vfs;

import org.jboss.as.jdr.util.WildcardPattern;
import org.jboss.vfs.VirtualFile;
import org.jboss.vfs.VirtualFileFilter;
import org.jboss.vfs.util.MatchAllVirtualFileFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author csams@redhat.com
 *         Date: 11/23/12
 */
public class Filters {

    public static VirtualFileFilter TRUE = MatchAllVirtualFileFilter.INSTANCE;

    public static VirtualFileFilter not(final VirtualFileFilter filter) {
        return new VirtualFileFilter() {
            @Override
            public boolean accepts(VirtualFile file) {
                return !filter.accepts(file);
            }
        };
    }

    public static VirtualFileFilter and(final VirtualFileFilter... filters) {
        return new VirtualFileFilter() {
            @Override
            public boolean accepts(VirtualFile file) {
                for(VirtualFileFilter f: filters) {
                    if(!f.accepts(file)){
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static VirtualFileFilter or(final VirtualFileFilter... filters) {
        return new VirtualFileFilter() {
            @Override
            public boolean accepts(VirtualFile file) {
                for(VirtualFileFilter f: filters) {
                    if(f.accepts(file)){
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static VirtualFileFilter wildcard(final String p){
        return new VirtualFileFilter() {
            private WildcardPattern pattern = new WildcardPattern(p);
            @Override
            public boolean accepts(VirtualFile file) {
                return pattern.matches(file.getPathName());
            }
        };
    }

    public static BlacklistFilter wildcardBlackList() {
        return new WildcardBlacklistFilter();
    }

    public static BlacklistFilter wildcardBlacklistFilter(final String... patterns){
        return new WildcardBlacklistFilter(patterns);
    }

    public static BlacklistFilter regexBlackList() {
        return new RegexBlacklistFilter();
    }

    public static BlacklistFilter regexBlackList(String... patterns) {
        return new RegexBlacklistFilter(patterns);
    }

    public static VirtualFileFilter suffix(final String s){
        return new VirtualFileFilter() {
            @Override
            public boolean accepts(VirtualFile file) {
                return file.getPathName().endsWith(s);
            }
        };
    }

    public interface BlacklistFilter extends VirtualFileFilter {
        void add(final String... patterns);
    }

    private static class WildcardBlacklistFilter implements BlacklistFilter {

        private final List<WildcardPattern> patterns;

        public WildcardBlacklistFilter() {
            patterns = new ArrayList<WildcardPattern>();
            patterns.add(new WildcardPattern("*-users.properties"));
        }

        public WildcardBlacklistFilter(final String... patterns) {
            this.patterns = new ArrayList<WildcardPattern>(patterns.length);
            add(patterns);
        }

        @Override
        public boolean accepts(VirtualFile file) {
            for(WildcardPattern p: this.patterns){
                if(p.matches(file.getName())){
                    return false;
                }
            }
            return true;
        }

        public void add(final String... patterns){
            for(String p: patterns) {
                this.patterns.add(new WildcardPattern(p));
            }
        }
    }

    private static class RegexBlacklistFilter implements BlacklistFilter {
        private final List<Pattern> patterns;

        public RegexBlacklistFilter(){
            this.patterns = Arrays.asList(Pattern.compile(".*-users.properties"));
        }

        public RegexBlacklistFilter(final String... patterns){
            this.patterns = new ArrayList<Pattern>(patterns.length);
            add(patterns);
        }

        @Override
        public boolean accepts(final VirtualFile file) {
            for(Pattern p: this.patterns){
                if(p.matcher(file.getName()).matches()){
                    return false;
                }
            }
            return true;
        }

        public void add(final String... patterns){
            for(String p: patterns){
                this.patterns.add(Pattern.compile(p));
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3427.java