error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2441.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2441.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2441.java
text:
```scala
public D@@istance(double value, DistanceUnit unit) {

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.elasticsearch.common.unit;

import org.elasticsearch.ElasticSearchIllegalArgumentException;
import org.elasticsearch.common.geo.GeoUtils;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

/**
 * The DistanceUnit enumerates several units for measuring distances. These units 
 * provide methods for converting strings and methods to convert units among each
 * others. Some methods like {@link DistanceUnit#getEarthCircumference} refer to
 * the earth ellipsoid defined in {@link GeoUtils}.
 */
public enum DistanceUnit {
    INCH(0.0254, "in", "inch"),
    YARD(0.9144, "yd", "yards"),
    MILES(1609.344, "mi", "miles"),
    KILOMETERS(1000.0, "km", "kilometers"),
    MILLIMETERS(0.001, "mm", "millimeters"),
    CENTIMETERS(0.01, "cm", "centimeters"),
    
    // since 'm' is suffix of other unit
    // it must be the last entry of unit
    // names ending with 'm'. otherwise
    // parsing would fail
    METERS(1, "m", "meters");   

    private double meters; 
    private final String[] names;
    
    DistanceUnit(double meters, String...names) {
        this.meters = meters;
        this.names = names;
    }

    /**
     * Measures the circumference of earth in this unit
     * 
     * @return length of earth circumference in this unit
     */
    public double getEarthCircumference() {
        return GeoUtils.EARTH_EQUATOR / meters;
    }

    /**
     * Measures the radius of earth in this unit
     * 
     * @return length of earth radius in this unit
     */
    public double getEarthRadius() {
        return GeoUtils.EARTH_SEMI_MAJOR_AXIS / meters;
    }

    /**
     * Measures a longitude in this unit
     * 
     * @return length of a longitude degree in this unit
     */
    public double getDistancePerDegree() {
        return GeoUtils.EARTH_EQUATOR / (360.0 * meters);
    }

    /**
     * Convert a value into miles
     * 
     * @param distance distance in this unit
     * @return value in miles
     */
    public double toMiles(double distance) {
        return convert(distance, this, DistanceUnit.MILES);
    }

    /**
     * Convert a value into kilometers
     * 
     * @param distance distance in this unit
     * @return value in kilometers
     */
    public double toKilometers(double distance) {
        return convert(distance, this, DistanceUnit.KILOMETERS);
    }

    /**
     * Convert a value into meters
     * 
     * @param distance distance in this unit
     * @return value in meters
     */
    public double toMeters(double distance) {
        return convert(distance, this, DistanceUnit.METERS);
    }

    /**
     * Convert a value given in meters to a value of this unit
     * 
     * @param distance distance in meters
     * @return value in this unit
     */
    public double fromMeters(double distance) {
        return convert(distance, DistanceUnit.METERS, this);
    }

    /** 
     * Convert a given value into another unit
     * 
     * @param distance value in this unit
     * @param unit target unit
     * @return value of the target unit
     */
    public double convert(double distance, DistanceUnit unit) {
        return convert(distance, this, unit);
    }

    /**
     * Convert a value to a distance string
     * 
     * @param distance value to convert
     * @return String representation of the distance 
     */
    public String toString(double distance) {
        return distance + toString();
    }
    
    @Override
    public String toString() {
        return names[0];
    }

    /**
     * Converts the given distance from the given DistanceUnit, to the given DistanceUnit
     *
     * @param distance Distance to convert
     * @param from     Unit to convert the distance from
     * @param to       Unit of distance to convert to
     * @return Given distance converted to the distance in the given unit
     */
    public static double convert(double distance, DistanceUnit from, DistanceUnit to) {
        if (from == to) {
            return distance;
        } else {
            return distance * from.meters / to.meters;
        }
    }

    /**
     * Parses a given distance and converts it to the specified unit.
     * 
     * @param distance String defining a distance (value and unit)
     * @param defaultUnit unit assumed if none is defined
     * @param to unit of result
     * @return parsed distance
     */
    public static double parse(String distance, DistanceUnit defaultUnit, DistanceUnit to) {
        Distance dist = Distance.parseDistance(distance, defaultUnit);
        return convert(dist.value, dist.unit, to);
    }

    /**
     * Convert a String to a {@link DistanceUnit}
     * 
     * @param unit name of the unit
     * @return unit matching the given name
     * @throws ElasticSearchIllegalArgumentException if no unit matches the given name
     */
    public static DistanceUnit fromString(String unit) {
        for (DistanceUnit dunit : values()) {
            for (String name : dunit.names) {
                if(name.equals(unit)) {
                    return dunit;
                }
            }
        }
        throw new ElasticSearchIllegalArgumentException("No distance unit match [" + unit + "]");
    }

    /**
     * Parses the suffix of a given distance string and return the corresponding {@link DistanceUnit}
     * 
     * @param distance string representing a distance
     * @param defaultUnit default unit to use, if no unit is provided by the string
     * @return unit of the given distance
     */
    public static DistanceUnit parseUnit(String distance, DistanceUnit defaultUnit) {
        for (DistanceUnit unit : values()) {
            for (String name : unit.names) {
                if(distance.endsWith(name)) {
                    return unit;
                }
            }
        }
        return defaultUnit;
    }

    /**
     * Write a {@link DistanceUnit} to a {@link StreamOutput}
     * 
     * @param out {@link StreamOutput} to write to
     * @param unit {@link DistanceUnit} to write 
     * @throws IOException
     */
    public static void writeDistanceUnit(StreamOutput out, DistanceUnit unit) throws IOException {
        out.writeByte((byte) unit.ordinal());
    }

    /**
     * Read a {@link DistanceUnit} from a {@link StreamInput} 
     * 
     * @param in {@link StreamInput} to read the {@link DistanceUnit} from
     * @return {@link DistanceUnit} read from the {@link StreamInput}
     * @throws IOException if no unit can be read from the {@link StreamInput}
     * @thrown ElasticSearchIllegalArgumentException if no matching {@link DistanceUnit} can be found
     */
    public static DistanceUnit readDistanceUnit(StreamInput in) throws IOException {
        byte b = in.readByte();

        if(b<0 || b>=values().length) {
            throw new ElasticSearchIllegalArgumentException("No type for distance unit matching [" + b + "]");
        } else {
            return values()[b];
        }
    }

    /**
     * This class implements a value+unit tuple.
     */
    public static class Distance implements Comparable<Distance> {
        public final double value;
        public final DistanceUnit unit;

        private Distance(double value, DistanceUnit unit) {
            super();
            this.value = value;
            this.unit = unit;
        }

        /**
         * Converts a {@link Distance} value given in a specific {@link DistanceUnit} into
         * a value equal to the specified value but in a other {@link DistanceUnit}.
         * 
         * @param unit unit of the result
         * @return converted distance
         */
        public Distance convert(DistanceUnit unit) {
            if(this.unit == unit) {
                return this;
            } else {
                return new Distance(DistanceUnit.convert(value, this.unit, unit), unit);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) {
                return false;
            } else if (obj instanceof Distance) {
                Distance other = (Distance) obj;
                return DistanceUnit.convert(value, unit, other.unit) == other.value;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Double.valueOf(value * unit.meters).hashCode();
        }

        @Override
        public int compareTo(Distance o) {
            return Double.compare(value, DistanceUnit.convert(o.value, o.unit, unit));
        }

        @Override
        public String toString() {
            return unit.toString(value);
        }

        /**
         * Parse a {@link Distance} from a given String
         * 
         * @param distance String defining a {@link Distance} 
         * @param defaultUnit {@link DistanceUnit} to be assumed
         *          if not unit is provided in the first argument  
         * @return parsed {@link Distance}
         */
        public static Distance parseDistance(String distance, DistanceUnit defaultUnit) {
            for (DistanceUnit unit : values()) {
                for (String name : unit.names) {
                    if(distance.endsWith(name)) {
                        return new Distance(Double.parseDouble(distance.substring(0, distance.length() - name.length())), unit);
                    }
                }
            }
            return new Distance(Double.parseDouble(distance), defaultUnit);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2441.java