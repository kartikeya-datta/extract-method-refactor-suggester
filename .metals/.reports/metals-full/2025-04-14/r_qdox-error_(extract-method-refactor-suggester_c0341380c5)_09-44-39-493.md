error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7370.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7370.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7370.java
text:
```scala
V@@aluesSourceConfig<NumericValuesSource> config = new ValuesSourceConfig<>(NumericValuesSource.class);

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
package org.elasticsearch.search.aggregations.bucket.histogram;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.collect.MapBuilder;
import org.elasticsearch.common.joda.DateMathParser;
import org.elasticsearch.common.rounding.DateTimeUnit;
import org.elasticsearch.common.rounding.TimeZoneRounding;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.fielddata.IndexFieldData;
import org.elasticsearch.index.mapper.FieldMapper;
import org.elasticsearch.index.mapper.core.DateFieldMapper;
import org.elasticsearch.script.SearchScript;
import org.elasticsearch.search.SearchParseException;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.AggregatorFactory;
import org.elasticsearch.search.aggregations.support.FieldContext;
import org.elasticsearch.search.aggregations.support.ValuesSourceConfig;
import org.elasticsearch.search.aggregations.support.numeric.NumericValuesSource;
import org.elasticsearch.search.aggregations.support.numeric.ValueFormatter;
import org.elasticsearch.search.aggregations.support.numeric.ValueParser;
import org.elasticsearch.search.internal.SearchContext;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class DateHistogramParser implements Aggregator.Parser {

    static final ParseField EXTENDED_BOUNDS = new ParseField("extended_bounds");

    private final ImmutableMap<String, DateTimeUnit> dateFieldUnits;

    public DateHistogramParser() {
        dateFieldUnits = MapBuilder.<String, DateTimeUnit>newMapBuilder()
                .put("year", DateTimeUnit.YEAR_OF_CENTURY)
                .put("1y", DateTimeUnit.YEAR_OF_CENTURY)
                .put("quarter", DateTimeUnit.QUARTER)
                .put("1q", DateTimeUnit.QUARTER)
                .put("month", DateTimeUnit.MONTH_OF_YEAR)
                .put("1M", DateTimeUnit.MONTH_OF_YEAR)
                .put("week", DateTimeUnit.WEEK_OF_WEEKYEAR)
                .put("1w", DateTimeUnit.WEEK_OF_WEEKYEAR)
                .put("day", DateTimeUnit.DAY_OF_MONTH)
                .put("1d", DateTimeUnit.DAY_OF_MONTH)
                .put("hour", DateTimeUnit.HOUR_OF_DAY)
                .put("1h", DateTimeUnit.HOUR_OF_DAY)
                .put("minute", DateTimeUnit.MINUTES_OF_HOUR)
                .put("1m", DateTimeUnit.MINUTES_OF_HOUR)
                .put("second", DateTimeUnit.SECOND_OF_MINUTE)
                .put("1s", DateTimeUnit.SECOND_OF_MINUTE)
                .immutableMap();
    }

    @Override
    public String type() {
        return InternalDateHistogram.TYPE.name();
    }

    @Override
    public AggregatorFactory parse(String aggregationName, XContentParser parser, SearchContext context) throws IOException {

        ValuesSourceConfig<NumericValuesSource> config = new ValuesSourceConfig<NumericValuesSource>(NumericValuesSource.class);

        String field = null;
        String script = null;
        String scriptLang = null;
        Map<String, Object> scriptParams = null;
        boolean keyed = false;
        long minDocCount = 1;
        ExtendedBounds extendedBounds = null;
        InternalOrder order = (InternalOrder) Histogram.Order.KEY_ASC;
        String interval = null;
        boolean preZoneAdjustLargeInterval = false;
        DateTimeZone preZone = DateTimeZone.UTC;
        DateTimeZone postZone = DateTimeZone.UTC;
        String format = null;
        long preOffset = 0;
        long postOffset = 0;
        boolean assumeSorted = false;

        XContentParser.Token token;
        String currentFieldName = null;
        while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token == XContentParser.Token.VALUE_STRING) {
                if ("field".equals(currentFieldName)) {
                    field = parser.text();
                } else if ("script".equals(currentFieldName)) {
                    script = parser.text();
                } else if ("lang".equals(currentFieldName)) {
                    scriptLang = parser.text();
                } else if ("time_zone".equals(currentFieldName) || "timeZone".equals(currentFieldName)) {
                    preZone = parseZone(parser.text());
                } else if ("pre_zone".equals(currentFieldName) || "preZone".equals(currentFieldName)) {
                    preZone = parseZone(parser.text());
                } else if ("post_zone".equals(currentFieldName) || "postZone".equals(currentFieldName)) {
                    postZone = parseZone(parser.text());
                } else if ("pre_offset".equals(currentFieldName) || "preOffset".equals(currentFieldName)) {
                    preOffset = parseOffset(parser.text());
                } else if ("post_offset".equals(currentFieldName) || "postOffset".equals(currentFieldName)) {
                    postOffset = parseOffset(parser.text());
                } else if ("interval".equals(currentFieldName)) {
                    interval = parser.text();
                } else if ("format".equals(currentFieldName)) {
                    format = parser.text();
                } else {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.VALUE_BOOLEAN) {
                if ("keyed".equals(currentFieldName)) {
                    keyed = parser.booleanValue();
                } else if ("script_values_sorted".equals(currentFieldName) || "scriptValuesSorted".equals(currentFieldName)) {
                    assumeSorted = parser.booleanValue();
                } else if ("pre_zone_adjust_large_interval".equals(currentFieldName) || "preZoneAdjustLargeInterval".equals(currentFieldName)) {
                    preZoneAdjustLargeInterval = parser.booleanValue();
                } else {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.VALUE_NUMBER) {
                if ("min_doc_count".equals(currentFieldName) || "minDocCount".equals(currentFieldName)) {
                    minDocCount = parser.longValue();
                } else if ("time_zone".equals(currentFieldName) || "timeZone".equals(currentFieldName)) {
                    preZone = DateTimeZone.forOffsetHours(parser.intValue());
                } else if ("pre_zone".equals(currentFieldName) || "preZone".equals(currentFieldName)) {
                    preZone = DateTimeZone.forOffsetHours(parser.intValue());
                } else if ("post_zone".equals(currentFieldName) || "postZone".equals(currentFieldName)) {
                    postZone = DateTimeZone.forOffsetHours(parser.intValue());
                } else {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else if (token == XContentParser.Token.START_OBJECT) {
                if ("params".equals(currentFieldName)) {
                    scriptParams = parser.map();
                } else if ("order".equals(currentFieldName)) {
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            currentFieldName = parser.currentName();
                        } else if (token == XContentParser.Token.VALUE_STRING) {
                            String dir = parser.text();
                            boolean asc = "asc".equals(dir);
                            order = resolveOrder(currentFieldName, asc);
                            //TODO should we throw an error if the value is not "asc" or "desc"???
                        }
                    }
                } else if (EXTENDED_BOUNDS.match(currentFieldName)) {
                    extendedBounds = new ExtendedBounds();
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            currentFieldName = parser.currentName();
                        } else if (token == XContentParser.Token.VALUE_STRING) {
                            if ("min".equals(currentFieldName)) {
                                extendedBounds.minAsStr = parser.text();
                            } else if ("max".equals(currentFieldName)) {
                                extendedBounds.maxAsStr = parser.text();
                            } else {
                                throw new SearchParseException(context, "Unknown extended_bounds key for a " + token + " in aggregation [" + aggregationName + "]: [" + currentFieldName + "].");
                            }
                        } else if (token == XContentParser.Token.VALUE_NUMBER) {
                            if ("min".equals(currentFieldName)) {
                                extendedBounds.min = parser.longValue();
                            } else if ("max".equals(currentFieldName)) {
                                extendedBounds.max = parser.longValue();
                            } else {
                                throw new SearchParseException(context, "Unknown extended_bounds key for a " + token + " in aggregation [" + aggregationName + "]: [" + currentFieldName + "].");
                            }
                        } else {
                            throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                        }
                    }

                } else {
                    throw new SearchParseException(context, "Unknown key for a " + token + " in [" + aggregationName + "]: [" + currentFieldName + "].");
                }
            } else {
                throw new SearchParseException(context, "Unexpected token " + token + " in [" + aggregationName + "].");
            }
        }

        if (interval == null) {
            throw new SearchParseException(context, "Missing required field [interval] for histogram aggregation [" + aggregationName + "]");
        }

        SearchScript searchScript = null;
        if (script != null) {
            searchScript = context.scriptService().search(context.lookup(), scriptLang, script, scriptParams);
            config.script(searchScript);
        }

        if (!assumeSorted) {
            // we need values to be sorted and unique for efficiency
            config.ensureSorted(true);
        }

        TimeZoneRounding.Builder tzRoundingBuilder;
        DateTimeUnit dateTimeUnit = dateFieldUnits.get(interval);
        if (dateTimeUnit != null) {
            tzRoundingBuilder = TimeZoneRounding.builder(dateTimeUnit);
        } else {
            // the interval is a time value?
            tzRoundingBuilder = TimeZoneRounding.builder(TimeValue.parseTimeValue(interval, null));
        }

        TimeZoneRounding rounding = tzRoundingBuilder
                .preZone(preZone).postZone(postZone)
                .preZoneAdjustLargeInterval(preZoneAdjustLargeInterval)
                .preOffset(preOffset).postOffset(postOffset)
                .build();

        if (format != null) {
            config.formatter(new ValueFormatter.DateTime(format));
        }

        if (field == null) {

            if (searchScript != null) {
                ValueParser valueParser = new ValueParser.DateMath(new DateMathParser(DateFieldMapper.Defaults.DATE_TIME_FORMATTER, DateFieldMapper.Defaults.TIME_UNIT));
                config.parser(valueParser);
                return new HistogramAggregator.Factory(aggregationName, config, rounding, order, keyed, minDocCount, extendedBounds, InternalDateHistogram.FACTORY);
            }

            // falling back on the get field data context
            return new HistogramAggregator.Factory(aggregationName, config, rounding, order, keyed, minDocCount, extendedBounds, InternalDateHistogram.FACTORY);
        }

        FieldMapper<?> mapper = context.smartNameFieldMapper(field);
        if (mapper == null) {
            config.unmapped(true);
            if (format == null) {
                config.formatter(new ValueFormatter.DateTime(DateFieldMapper.Defaults.DATE_TIME_FORMATTER));
            }
            config.parser(new ValueParser.DateMath(new DateMathParser(DateFieldMapper.Defaults.DATE_TIME_FORMATTER, DateFieldMapper.Defaults.TIME_UNIT)));
            return new HistogramAggregator.Factory(aggregationName, config, rounding, order, keyed, minDocCount, extendedBounds, InternalDateHistogram.FACTORY);
        }

        if (!(mapper instanceof DateFieldMapper)) {
            throw new SearchParseException(context, "date histogram can only be aggregated on date fields but [" + field + "] is not a date field");
        }

        IndexFieldData<?> indexFieldData = context.fieldData().getForField(mapper);
        config.fieldContext(new FieldContext(field, indexFieldData));
        if (format == null) {
            config.formatter(new ValueFormatter.DateTime(((DateFieldMapper) mapper).dateTimeFormatter()));
        }
        config.parser(new ValueParser.DateMath(new DateMathParser(((DateFieldMapper) mapper).dateTimeFormatter(), DateFieldMapper.Defaults.TIME_UNIT)));
        return new HistogramAggregator.Factory(aggregationName, config, rounding, order, keyed, minDocCount, extendedBounds, InternalDateHistogram.FACTORY);
    }

    private static InternalOrder resolveOrder(String key, boolean asc) {
        if ("_key".equals(key) || "_time".equals(key)) {
            return (InternalOrder) (asc ? InternalOrder.KEY_ASC : InternalOrder.KEY_DESC);
        }
        if ("_count".equals(key)) {
            return (InternalOrder) (asc ? InternalOrder.COUNT_ASC : InternalOrder.COUNT_DESC);
        }
        return new InternalOrder.Aggregation(key, asc);
    }

    private long parseOffset(String offset) throws IOException {
        if (offset.charAt(0) == '-') {
            return -TimeValue.parseTimeValue(offset.substring(1), null).millis();
        }
        int beginIndex = offset.charAt(0) == '+' ? 1 : 0;
        return TimeValue.parseTimeValue(offset.substring(beginIndex), null).millis();
    }

    private DateTimeZone parseZone(String text) throws IOException {
        int index = text.indexOf(':');
        if (index != -1) {
            int beginIndex = text.charAt(0) == '+' ? 1 : 0;
            // format like -02:30
            return DateTimeZone.forOffsetHoursMinutes(
                    Integer.parseInt(text.substring(beginIndex, index)),
                    Integer.parseInt(text.substring(index + 1))
            );
        } else {
            // id, listed here: http://joda-time.sourceforge.net/timezones.html
            return DateTimeZone.forID(text);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7370.java