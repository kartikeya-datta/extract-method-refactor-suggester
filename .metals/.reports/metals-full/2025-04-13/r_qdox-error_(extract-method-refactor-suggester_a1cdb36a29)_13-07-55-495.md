error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7776.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7776.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7776.java
text:
```scala
r@@eturn Character.valueOf(getChar(value));

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.jorphan.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * Converter utilities for TestBeans
 */
public class Converter {

    /**
     * Convert the given value object to an object of the given type
     *
     * @param value
     * @param toType
     * @return Object
     */
    public static Object convert(Object value, Class<?> toType) {
        if (value == null) {
            value = "";
        } else if (toType.isAssignableFrom(value.getClass())) {
            return value;
        } else if (toType.equals(float.class) || toType.equals(Float.class)) {
            return new Float(getFloat(value));
        } else if (toType.equals(double.class) || toType.equals(Double.class)) {
            return new Double(getDouble(value));
        } else if (toType.equals(String.class)) {
            return getString(value);
        } else if (toType.equals(int.class) || toType.equals(Integer.class)) {
            return Integer.valueOf(getInt(value));
        } else if (toType.equals(char.class) || toType.equals(Character.class)) {
            return new Character(getChar(value));
        } else if (toType.equals(long.class) || toType.equals(Long.class)) {
            return Long.valueOf(getLong(value));
        } else if (toType.equals(boolean.class) || toType.equals(Boolean.class)) {
            return  Boolean.valueOf(getBoolean(value));
        } else if (toType.equals(java.util.Date.class)) {
            return getDate(value);
        } else if (toType.equals(Calendar.class)) {
            return getCalendar(value);
        } else if (toType.equals(Class.class)) {
            try {
                return Class.forName(value.toString());
            } catch (Exception e) {
                // don't do anything
            }
        }
        return value;
    }

    /**
     * Converts the given object to a calendar object. Defaults to the current
     * date if the given object can't be converted.
     *
     * @param date
     * @return Calendar
     */
    public static Calendar getCalendar(Object date, Calendar defaultValue) {
        Calendar cal = new GregorianCalendar();
        if (date != null && date instanceof java.util.Date) {
            cal.setTime((java.util.Date) date);
            return cal;
        } else if (date != null) {
            DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
            java.util.Date d = null;
            try {
                d = formatter.parse(date.toString());
            } catch (ParseException e) {
                formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
                try {
                    d = formatter.parse((String) date);
                } catch (ParseException e1) {
                    formatter = DateFormat.getDateInstance(DateFormat.LONG);
                    try {
                        d = formatter.parse((String) date);
                    } catch (ParseException e2) {
                        formatter = DateFormat.getDateInstance(DateFormat.FULL);
                        try {
                            d = formatter.parse((String) date);
                        } catch (ParseException e3) {
                            return defaultValue;
                        }
                    }
                }
            }
            cal.setTime(d);
        } else {
            cal = defaultValue;
        }
        return cal;
    }

    public static Calendar getCalendar(Object o) {
        return getCalendar(o, new GregorianCalendar());
    }

    public static Date getDate(Object date) {
        return getDate(date, Calendar.getInstance().getTime());
    }

    public static Date getDate(Object date, Date defaultValue) {
        Date val = null;
        if (date != null && date instanceof java.util.Date) {
            return (Date) date;
        } else if (date != null) {
            DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
            // java.util.Date d = null;
            try {
                val = formatter.parse(date.toString());
            } catch (ParseException e) {
                formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
                try {
                    val = formatter.parse((String) date);
                } catch (ParseException e1) {
                    formatter = DateFormat.getDateInstance(DateFormat.LONG);
                    try {
                        val = formatter.parse((String) date);
                    } catch (ParseException e2) {
                        formatter = DateFormat.getDateInstance(DateFormat.FULL);
                        try {
                            val = formatter.parse((String) date);
                        } catch (ParseException e3) {
                            return defaultValue;
                        }
                    }
                }
            }
        } else {
            return defaultValue;
        }
        return val;
    }

    public static float getFloat(Object o, float defaultValue) {
        try {
            if (o == null) {
                return defaultValue;
            }
            if (o instanceof Number) {
                return ((Number) o).floatValue();
            }
            return Float.parseFloat(o.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float getFloat(Object o) {
        return getFloat(o, 0);
    }

    public static double getDouble(Object o, double defaultValue) {
        try {
            if (o == null) {
                return defaultValue;
            }
            if (o instanceof Number) {
                return ((Number) o).doubleValue();
            }
            return Double.parseDouble(o.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static double getDouble(Object o) {
        return getDouble(o, 0);
    }

    public static boolean getBoolean(Object o) {
        return getBoolean(o, false);
    }

    public static boolean getBoolean(Object o, boolean defaultValue) {
        if (o == null) {
            return defaultValue;
        } else if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }
        return Boolean.valueOf(o.toString()).booleanValue();
    }

    /**
     * Convert object to integer, return defaultValue if object is not
     * convertible or is null.
     *
     * @param o
     * @param defaultValue
     * @return int
     */
    public static int getInt(Object o, int defaultValue) {
        try {
            if (o == null) {
                return defaultValue;
            }
            if (o instanceof Number) {
                return ((Number) o).intValue();
            }
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static char getChar(Object o) {
        return getChar(o, ' ');
    }

    public static char getChar(Object o, char defaultValue) {
        try {
            if (o == null) {
                return defaultValue;
            }
            if (o instanceof Character) {
                return ((Character) o).charValue();
            } else if (o instanceof Byte) {
                return (char) ((Byte) o).byteValue();
            } else if (o instanceof Integer) {
                return (char) ((Integer) o).intValue();
            } else {
                String s = o.toString();
                if (s.length() > 0) {
                    return o.toString().charAt(0);
                }
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Converts object to an integer, defaults to 0 if object is not convertible
     * or is null.
     *
     * @param o
     * @return int
     */
    public static int getInt(Object o) {
        return getInt(o, 0);
    }

    /**
     * Converts object to a long, return defaultValue if object is not
     * convertible or is null.
     *
     * @param o
     * @param defaultValue
     * @return long
     */
    public static long getLong(Object o, long defaultValue) {
        try {
            if (o == null) {
                return defaultValue;
            }
            if (o instanceof Number) {
                return ((Number) o).longValue();
            }
            return Long.parseLong(o.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Converts object to a long, defaults to 0 if object is not convertible or
     * is null
     *
     * @param o
     * @return long
     */
    public static long getLong(Object o) {
        return getLong(o, 0);
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String formatDate(java.sql.Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String formatDate(String date, String pattern) {
        return formatDate(getCalendar(date, null), pattern);
    }

    public static String formatDate(Calendar date, String pattern) {
        return formatCalendar(date, pattern);
    }

    public static String formatCalendar(Calendar date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date.getTime());
    }

    /**
     * Converts object to a String, return defaultValue if object is null.
     *
     * @param o
     * @param defaultValue
     * @return String
     */
    public static String getString(Object o, String defaultValue) {
        if (o == null) {
            return defaultValue;
        }
        return o.toString();
    }

    public static String insertLineBreaks(String v, String insertion) {
        if (v == null) {
            return "";
        }
        StringBuilder replacement = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(v, "\n", true);
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (token.compareTo("\n") == 0) {
                replacement.append(insertion);
            } else {
                replacement.append(token);
            }
        }
        return replacement.toString();
    }

    /**
     * Converts object to a String, defaults to empty string if object is null.
     *
     * @param o
     * @return String
     */
    public static String getString(Object o) {
        return getString(o, "");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7776.java