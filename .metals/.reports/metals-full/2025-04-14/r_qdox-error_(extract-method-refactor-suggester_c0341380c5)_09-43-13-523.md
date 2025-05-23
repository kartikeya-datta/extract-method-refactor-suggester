error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1253.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1253.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1253.java
text:
```scala
s@@uper(names, 1, fieldType, null, null, provider, null);

package org.elasticsearch.index.mapper.geo;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.FieldInfo;
import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.geo.GeoJSONShapeParser;
import org.elasticsearch.common.geo.GeoShapeConstants;
import org.elasticsearch.common.lucene.spatial.SpatialStrategy;
import org.elasticsearch.common.lucene.spatial.prefix.TermQueryPrefixTreeStrategy;
import org.elasticsearch.common.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.elasticsearch.common.lucene.spatial.prefix.tree.QuadPrefixTree;
import org.elasticsearch.common.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.codec.postingsformat.PostingsFormatProvider;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.Mapper;
import org.elasticsearch.index.mapper.MapperParsingException;
import org.elasticsearch.index.mapper.ParseContext;
import org.elasticsearch.index.mapper.core.AbstractFieldMapper;

import java.io.IOException;
import java.util.Map;

/**
 * FieldMapper for indexing {@link com.spatial4j.core.shape.Shape}s.
 * <p/>
 * Currently Shapes can only be indexed and can only be queried using
 * {@link org.elasticsearch.index.query.GeoShapeFilterParser}, consequently
 * a lot of behavior in this Mapper is disabled.
 * <p/>
 * Format supported:
 * <p/>
 * "field" : {
 * "type" : "polygon",
 * "coordinates" : [
 * [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]
 * ]
 * }
 */
public class GeoShapeFieldMapper extends AbstractFieldMapper<String> {

    public static final String CONTENT_TYPE = "geo_shape";

    public static class Names {
        public static final String TREE = "tree";
        public static final String TREE_LEVELS = "tree_levels";
        public static final String GEOHASH = "geohash";
        public static final String QUADTREE = "quadtree";
        public static final String DISTANCE_ERROR_PCT = "distance_error_pct";
    }

    public static class Defaults {
        public static final String TREE = Names.GEOHASH;
        public static final int GEOHASH_LEVELS = GeohashPrefixTree.getMaxLevelsPossible();
        public static final int QUADTREE_LEVELS = QuadPrefixTree.DEFAULT_MAX_LEVELS;
        public static final double DISTANCE_ERROR_PCT = 0.025d;

        public static final FieldType GEO_SHAPE_FIELD_TYPE = new FieldType();

        static {
            GEO_SHAPE_FIELD_TYPE.setIndexed(true);
            GEO_SHAPE_FIELD_TYPE.setTokenized(false);
            GEO_SHAPE_FIELD_TYPE.setStored(false);
            GEO_SHAPE_FIELD_TYPE.setStoreTermVectors(false);
            GEO_SHAPE_FIELD_TYPE.setOmitNorms(true);
            GEO_SHAPE_FIELD_TYPE.setIndexOptions(FieldInfo.IndexOptions.DOCS_ONLY);
            GEO_SHAPE_FIELD_TYPE.freeze();
        }
    }

    public static class Builder extends AbstractFieldMapper.Builder<Builder, GeoShapeFieldMapper> {

        private String tree = Defaults.TREE;
        private int treeLevels;
        private double distanceErrorPct = Defaults.DISTANCE_ERROR_PCT;

        private SpatialPrefixTree prefixTree;

        public Builder(String name) {
            super(name, new FieldType(Defaults.GEO_SHAPE_FIELD_TYPE));
        }

        public Builder tree(String tree) {
            this.tree = tree;
            return this;
        }

        public Builder treeLevels(int treeLevels) {
            this.treeLevels = treeLevels;
            return this;
        }

        public Builder distanceErrorPct(double distanceErrorPct) {
            this.distanceErrorPct = distanceErrorPct;
            return this;
        }

        @Override
        public GeoShapeFieldMapper build(BuilderContext context) {
            if (tree.equals(Names.GEOHASH)) {
                int levels = treeLevels != 0 ? treeLevels : Defaults.GEOHASH_LEVELS;
                prefixTree = new GeohashPrefixTree(GeoShapeConstants.SPATIAL_CONTEXT, levels);
            } else if (tree.equals(Names.QUADTREE)) {
                int levels = treeLevels != 0 ? treeLevels : Defaults.QUADTREE_LEVELS;
                prefixTree = new QuadPrefixTree(GeoShapeConstants.SPATIAL_CONTEXT, levels);
            } else {
                throw new ElasticSearchIllegalArgumentException("Unknown prefix tree type [" + tree + "]");
            }

            return new GeoShapeFieldMapper(buildNames(context), prefixTree, distanceErrorPct, fieldType, provider);
        }
    }

    public static class TypeParser implements Mapper.TypeParser {

        @Override
        public Mapper.Builder parse(String name, Map<String, Object> node, ParserContext parserContext) throws MapperParsingException {
            Builder builder = new Builder(name);

            for (Map.Entry<String, Object> entry : node.entrySet()) {
                String fieldName = Strings.toUnderscoreCase(entry.getKey());
                Object fieldNode = entry.getValue();
                if (Names.TREE.equals(fieldName)) {
                    builder.tree(fieldNode.toString());
                } else if (Names.TREE_LEVELS.equals(fieldName)) {
                    builder.treeLevels(Integer.parseInt(fieldNode.toString()));
                } else if (Names.DISTANCE_ERROR_PCT.equals(fieldName)) {
                    builder.distanceErrorPct(Double.parseDouble(fieldNode.toString()));
                }
            }
            return builder;
        }
    }

    private final SpatialStrategy spatialStrategy;

    public GeoShapeFieldMapper(FieldMapper.Names names, SpatialPrefixTree prefixTree, double distanceErrorPct,
                               FieldType fieldType, PostingsFormatProvider provider) {
        super(names, 1, fieldType, null, null, provider);
        this.spatialStrategy = new TermQueryPrefixTreeStrategy(names, prefixTree, distanceErrorPct);
    }

    @Override
    protected Field parseCreateField(ParseContext context) throws IOException {
        return spatialStrategy.createField(GeoJSONShapeParser.parse(context.parser()));
    }

    @Override
    protected void doXContentBody(XContentBuilder builder) throws IOException {
        builder.field("type", contentType());

        // TODO: Come up with a better way to get the name, maybe pass it from builder
        if (spatialStrategy.getPrefixTree() instanceof GeohashPrefixTree) {
            // Don't emit the tree name since GeohashPrefixTree is the default
            // Only emit the tree levels if it isn't the default value
            if (spatialStrategy.getPrefixTree().getMaxLevels() != Defaults.GEOHASH_LEVELS) {
                builder.field(Names.TREE_LEVELS, spatialStrategy.getPrefixTree().getMaxLevels());
            }
        } else {
            builder.field(Names.TREE, Names.QUADTREE);
            if (spatialStrategy.getPrefixTree().getMaxLevels() != Defaults.QUADTREE_LEVELS) {
                builder.field(Names.TREE_LEVELS, spatialStrategy.getPrefixTree().getMaxLevels());
            }
        }

        if (spatialStrategy.getDistanceErrorPct() != Defaults.DISTANCE_ERROR_PCT) {
            builder.field(Names.DISTANCE_ERROR_PCT, spatialStrategy.getDistanceErrorPct());
        }
    }

    @Override
    protected String contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public String value(Field field) {
        throw new UnsupportedOperationException("GeoShape fields cannot be converted to String values");
    }

    @Override
    public String valueFromString(String value) {
        throw new UnsupportedOperationException("GeoShape fields cannot be converted to String values");
    }

    @Override
    public String valueAsString(Field field) {
        throw new UnsupportedOperationException("GeoShape fields cannot be converted to String values");
    }

    public SpatialStrategy spatialStrategy() {
        return this.spatialStrategy;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1253.java