error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4039.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4039.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4039.java
text:
```scala
r@@eturn datatypeFactory.newDuration(sign == 1,

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

/**
 * Validator for &lt;duration&gt; datatype (W3C Schema Datatypes)
 *
 * @xerces.internal 
 *
 * @author Elena Litani
 * @author Gopal Sharma, SUN Microsystem Inc.
 * @version $Id$
 */
public class DurationDV extends AbstractDateTimeDV {

	public static final int DURATION_TYPE = 0;
	public static final int YEARMONTHDURATION_TYPE = 1;
	public static final int DAYTIMEDURATION_TYPE = 2;
    // order-relation on duration is a partial order. The dates below are used to
    // for comparison of 2 durations, based on the fact that
    // duration x and y is x<=y iff s+x<=s+y
    // see 3.2.6 duration W3C schema datatype specs
    //
    // the dates are in format: {CCYY,MM,DD, H, S, M, MS, timezone}
    private final static DateTimeData[] DATETIMES= {
        new DateTimeData(1696, 9, 1, 0, 0, 0, 'Z', null, true, null),
        new DateTimeData(1697, 2, 1, 0, 0, 0, 'Z', null, true, null),
        new DateTimeData(1903, 3, 1, 0, 0, 0, 'Z', null, true, null),
        new DateTimeData(1903, 7, 1, 0, 0, 0, 'Z', null, true, null)};

    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException{
        try{
            return parse(content, DURATION_TYPE);
        } catch (Exception ex) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "duration"});
        }
    }

    /**
     * Parses, validates and computes normalized version of duration object
     *
     * @param str    The lexical representation of duration object PnYn MnDTnH nMnS
     * @param durationType
     * @return normalized date representation
     * @exception SchemaDateTimeException Invalid lexical representation
     */
    protected DateTimeData parse(String str, int durationType) throws SchemaDateTimeException{
        int len = str.length();
        DateTimeData date= new DateTimeData(str, this);
        
        int start = 0;
        char c=str.charAt(start++);
        if ( c!='P' && c!='-' ) {
            throw new SchemaDateTimeException();
        }
        else {
            date.utc=(c=='-')?'-':0;
            if ( c=='-' && str.charAt(start++)!='P' ) {
                throw new SchemaDateTimeException();
            }
        }
        
        int negate = 1;
        //negative duration
        if ( date.utc=='-' ) {
            negate = -1;
            
        }
        //at least one number and designator must be seen after P
        boolean designator = false;
        
        int endDate = indexOf (str, start, len, 'T');
        if ( endDate == -1 ) {
            endDate = len;
        }
        else if (durationType == YEARMONTHDURATION_TYPE) {
            throw new SchemaDateTimeException();
        }
        
        //find 'Y'
        int end = indexOf (str, start, endDate, 'Y');
        if ( end!=-1 ) {
            
            if (durationType == DAYTIMEDURATION_TYPE) {
                throw new SchemaDateTimeException();
            }
            
            //scan year
            date.year=negate * parseInt(str,start,end);
            start = end+1;
            designator = true;
        }
        
        end = indexOf (str, start, endDate, 'M');
        if ( end!=-1 ) {
            
            if (durationType == DAYTIMEDURATION_TYPE) {
                throw new SchemaDateTimeException();
            }
            
            //scan month
            date.month=negate * parseInt(str,start,end);
            start = end+1;
            designator = true;
        }
        
        end = indexOf (str, start, endDate, 'D');
        if ( end!=-1 ) {
            
            if(durationType == YEARMONTHDURATION_TYPE) {
                throw new SchemaDateTimeException();
            }
            
            //scan day
            date.day=negate * parseInt(str,start,end);
            start = end+1;
            designator = true;
        }
        
        if ( len == endDate && start!=len ) {
            throw new SchemaDateTimeException();
        }
        if ( len !=endDate ) {
            
            //scan hours, minutes, seconds
            //REVISIT: can any item include a decimal fraction or only seconds?
            //
            
            end = indexOf (str, ++start, len, 'H');
            if ( end!=-1 ) {
                //scan hours
                date.hour=negate * parseInt(str,start,end);
                start=end+1;
                designator = true;
            }
            
            end = indexOf (str, start, len, 'M');
            if ( end!=-1 ) {
                //scan min
                date.minute=negate * parseInt(str,start,end);
                start=end+1;
                designator = true;
            }
            
            end = indexOf (str, start, len, 'S');
            if ( end!=-1 ) {
                //scan seconds
                date.second = negate * parseSecond(str, start, end);
                start=end+1;
                designator = true;
            }
            // no additional data shouls appear after last item
            // P1Y1M1DT is illigal value as well
            if ( start != len || str.charAt(--start)=='T' ) {
                throw new SchemaDateTimeException();
            }
        }
        
        if ( !designator ) {
            throw new SchemaDateTimeException();
        }
        
        return date;
    }

    /**
     * Compares 2 given durations. (refer to W3C Schema Datatypes "3.2.6 duration")
     *
     * @param date1  Unnormalized duration
     * @param date2  Unnormalized duration
     * @param strict (min/max)Exclusive strict == true ( LESS_THAN ) or ( GREATER_THAN )
     *               (min/max)Inclusive strict == false (LESS_EQUAL) or (GREATER_EQUAL)
     * @return INDETERMINATE if the order relationship between date1 and date2 is indeterminate.
     * EQUAL if the order relation between date1 and date2 is EQUAL.
     * If the strict parameter is true, return LESS_THAN if date1 is less than date2 and
     * return GREATER_THAN if date1 is greater than date2.
     * If the strict parameter is false, return LESS_THAN if date1 is less than OR equal to date2 and
     * return GREATER_THAN if date1 is greater than OR equal to date2
     */
    protected  short compareDates(DateTimeData date1, DateTimeData date2, boolean strict) {

        //REVISIT: this is unoptimazed vs of comparing 2 durations
        //         Algorithm is described in 3.2.6.2 W3C Schema Datatype specs
        //

        //add constA to both durations
        short resultA, resultB= INDETERMINATE;
        //try and see if the objects are equal
        resultA = compareOrder (date1, date2);
        if ( resultA == 0 ) {
            return 0;
        }

        DateTimeData[] result = new DateTimeData[2];
        result[0] = new DateTimeData(null, this);
        result[1] = new DateTimeData(null, this);

        //long comparison algorithm is required
        DateTimeData tempA = addDuration (date1, DATETIMES[0], result[0]);
        DateTimeData tempB = addDuration (date2, DATETIMES[0], result[1]);
        resultA =  compareOrder(tempA, tempB);
        if ( resultA == INDETERMINATE ) {
            return INDETERMINATE;
        }

        tempA = addDuration(date1, DATETIMES[1], result[0]);
        tempB = addDuration(date2, DATETIMES[1], result[1]);
        resultB = compareOrder(tempA, tempB);
        resultA = compareResults(resultA, resultB, strict);
        if (resultA == INDETERMINATE) {
            return INDETERMINATE;
        }

        tempA = addDuration(date1, DATETIMES[2], result[0]);
        tempB = addDuration(date2, DATETIMES[2], result[1]);
        resultB = compareOrder(tempA, tempB);
        resultA = compareResults(resultA, resultB, strict);
        if (resultA == INDETERMINATE) {
            return INDETERMINATE;
        }

        tempA = addDuration(date1, DATETIMES[3], result[0]);
        tempB = addDuration(date2, DATETIMES[3], result[1]);
        resultB = compareOrder(tempA, tempB);
        resultA = compareResults(resultA, resultB, strict);

        return resultA;
    }

    private short compareResults(short resultA, short resultB, boolean strict){

      if ( resultB == INDETERMINATE ) {
            return INDETERMINATE;
        }
        else if ( resultA!=resultB && strict ) {
            return INDETERMINATE;
        }
        else if ( resultA!=resultB && !strict ) {
            if ( resultA!=0 && resultB!=0 ) {
                return INDETERMINATE;
            }
            else {
                return (resultA!=0)?resultA:resultB;
            }
        }
        return resultA;
    }

    private DateTimeData addDuration(DateTimeData date, DateTimeData addto, DateTimeData duration) {

        //REVISIT: some code could be shared between normalize() and this method,
        //         however is it worth moving it? The structures are different...
        //

        resetDateObj(duration);
        //add months (may be modified additionaly below)
        int temp = addto.month + date.month;
        duration.month = modulo (temp, 1, 13);
        int carry = fQuotient (temp, 1, 13);

        //add years (may be modified additionaly below)
        duration.year=addto.year + date.year + carry;

        //add seconds
        double dtemp = addto.second + date.second;
        carry = (int)Math.floor(dtemp/60);
        duration.second = dtemp - carry*60;

        //add minutes
        temp = addto.minute +date.minute + carry;
        carry = fQuotient (temp, 60);
        duration.minute= mod(temp, 60, carry);

        //add hours
        temp = addto.hour + date.hour + carry;
        carry = fQuotient(temp, 24);
        duration.hour = mod(temp, 24, carry);


        duration.day=addto.day + date.day + carry;

        while ( true ) {

            temp=maxDayInMonthFor(duration.year, duration.month);
            if ( duration.day < 1 ) { //original duration was negative
                duration.day = duration.day + maxDayInMonthFor(duration.year, duration.month-1);
                carry=-1;
            }
            else if ( duration.day > temp ) {
                duration.day = duration.day - temp;
                carry=1;
            }
            else {
                break;
            }
            temp = duration.month+carry;
            duration.month = modulo(temp, 1, 13);
            duration.year = duration.year+fQuotient(temp, 1, 13);
        }

        duration.utc='Z';
        return duration;
    }

    protected double parseSecond(String buffer, int start, int end)
        throws NumberFormatException {
        int dot = -1;
        for (int i = start; i < end; i++) {
            char ch = buffer.charAt(i);
            if (ch == '.')
                dot = i;
            else if (ch > '9' || ch < '0')
                throw new NumberFormatException("'" + buffer + "' has wrong format");
        }
        if (dot+1 == end) {
            throw new NumberFormatException("'" + buffer + "' has wrong format");
        }
        double value = Double.parseDouble(buffer.substring(start, end));
        if (value == Double.POSITIVE_INFINITY) {
            throw new NumberFormatException("'" + buffer + "' has wrong format");
        }
        return value;
    }

    protected String dateToString(DateTimeData date) {
        StringBuffer message = new StringBuffer(30);
        if ( date.year<0 || date.month<0 || date.day<0
 date.hour<0 || date.minute<0 || date.second<0) {
            message.append('-');
        }
        message.append('P');
        message.append((date.year < 0?-1:1) * date.year);
        message.append('Y');
        message.append((date.month < 0?-1:1) * date.month);
        message.append('M');
        message.append((date.day < 0?-1:1) * date.day);
        message.append('D');
        message.append('T');
        message.append((date.hour < 0?-1:1) * date.hour);
        message.append('H');
        message.append((date.minute < 0?-1:1) * date.minute);
        message.append('M');
        append2(message, (date.second < 0?-1:1) * date.second);
        message.append('S');

        return message.toString();
    }
    
    protected Duration getDuration(DateTimeData date) {
        int sign = 1;
        if ( date.year<0 || date.month<0 || date.day<0
 date.hour<0 || date.minute<0 || date.second<0) {
            sign = -1;
        }
        return factory.newDuration(sign == 1, 
                date.year != DatatypeConstants.FIELD_UNDEFINED?BigInteger.valueOf(sign*date.year):null, 
                date.month != DatatypeConstants.FIELD_UNDEFINED?BigInteger.valueOf(sign*date.month):null, 
                date.day != DatatypeConstants.FIELD_UNDEFINED?BigInteger.valueOf(sign*date.day):null, 
                date.hour != DatatypeConstants.FIELD_UNDEFINED?BigInteger.valueOf(sign*date.hour):null, 
                date.minute != DatatypeConstants.FIELD_UNDEFINED?BigInteger.valueOf(sign*date.minute):null, 
                date.second != DatatypeConstants.FIELD_UNDEFINED?new BigDecimal(String.valueOf(sign*date.second)):null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4039.java