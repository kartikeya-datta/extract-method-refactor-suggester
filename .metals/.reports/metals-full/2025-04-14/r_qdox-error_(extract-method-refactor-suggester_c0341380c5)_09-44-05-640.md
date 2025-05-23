error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4071.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4071.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4071.java
text:
```scala
n@@ew CSVParser((Reader) null, invalidFormat).close();

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

package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.LF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * CSVParserTest
 *
 * The test are organized in three different sections:
 * The 'setter/getter' section, the lexer section and finally the parser
 * section. In case a test fails, you should follow a top-down approach for
 * fixing a potential bug (its likely that the parser itself fails if the lexer
 * has problems...).
 *
 * @version $Id$
 */
public class CSVParserTest {

    private static final String CSVINPUT = "a,b,c,d\n"
                    + " a , b , 1 2 \n"
                    + "\"foo baar\", b,\n"
                    // + "   \"foo\n,,\n\"\",,\n\\\"\",d,e\n";
                    + "   \"foo\n,,\n\"\",,\n\"\"\",d,e\n";   // changed to use standard CSV escaping

    private static final String[][] RESULT = {
            {"a", "b", "c", "d"},
            {"a", "b", "1 2"},
            {"foo baar", "b", ""},
            {"foo\n,,\n\",,\n\"", "d", "e"}
    };

    @Test
    public void testGetLine() throws IOException {
        final CSVParser parser = CSVParser.parseString(CSVINPUT, CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true));
        for (final String[] re : RESULT) {
            assertArrayEquals(re, parser.nextRecord().values());
        }

        assertNull(parser.nextRecord());
    }

    @Test
    public void testGetRecords() throws IOException {
        final CSVParser parser = CSVParser.parseString(CSVINPUT, CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true));
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(RESULT.length, records.size());
        assertTrue(records.size() > 0);
        for (int i = 0; i < RESULT.length; i++) {
            assertArrayEquals(RESULT[i], records.get(i).values());
        }
    }

    @Test
    public void testExcelFormat1() throws IOException {
        final String code =
                "value1,value2,value3,value4\r\na,b,c,d\r\n  x,,,"
                        + "\r\n\r\n\"\"\"hello\"\"\",\"  \"\"world\"\"\",\"abc\ndef\",\r\n";
        final String[][] res = {
                {"value1", "value2", "value3", "value4"},
                {"a", "b", "c", "d"},
                {"  x", "", "", ""},
                {""},
                {"\"hello\"", "  \"world\"", "abc\ndef", ""}
        };
        final CSVParser parser = CSVParser.parseString(code, CSVFormat.EXCEL);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(res.length, records.size());
        assertTrue(records.size() > 0);
        for (int i = 0; i < res.length; i++) {
            assertArrayEquals(res[i], records.get(i).values());
        }
    }

    @Test
    public void testExcelFormat2() throws Exception {
        final String code = "foo,baar\r\n\r\nhello,\r\n\r\nworld,\r\n";
        final String[][] res = {
                {"foo", "baar"},
                {""},
                {"hello", ""},
                {""},
                {"world", ""}
        };
        final CSVParser parser = CSVParser.parseString(code, CSVFormat.EXCEL);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(res.length, records.size());
        assertTrue(records.size() > 0);
        for (int i = 0; i < res.length; i++) {
            assertArrayEquals(res[i], records.get(i).values());
        }
    }

    @Test
    public void testEndOfFileBehaviourExcel() throws Exception {
        final String[] codes = {
                "hello,\r\n\r\nworld,\r\n",
                "hello,\r\n\r\nworld,",
                "hello,\r\n\r\nworld,\"\"\r\n",
                "hello,\r\n\r\nworld,\"\"",
                "hello,\r\n\r\nworld,\n",
                "hello,\r\n\r\nworld,",
                "hello,\r\n\r\nworld,\"\"\n",
                "hello,\r\n\r\nworld,\"\""
        };
        final String[][] res = {
                {"hello", ""},
                {""},  // Excel format does not ignore empty lines
                {"world", ""}
        };

        for (final String code : codes) {
            final CSVParser parser = CSVParser.parseString(code, CSVFormat.EXCEL);
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(res.length, records.size());
            assertTrue(records.size() > 0);
            for (int i = 0; i < res.length; i++) {
                assertArrayEquals(res[i], records.get(i).values());
            }
        }
    }

    @Test
    public void testEndOfFileBehaviorCSV() throws Exception {
        final String[] codes = {
                "hello,\r\n\r\nworld,\r\n",
                "hello,\r\n\r\nworld,",
                "hello,\r\n\r\nworld,\"\"\r\n",
                "hello,\r\n\r\nworld,\"\"",
                "hello,\r\n\r\nworld,\n",
                "hello,\r\n\r\nworld,",
                "hello,\r\n\r\nworld,\"\"\n",
                "hello,\r\n\r\nworld,\"\""
        };
        final String[][] res = {
                {"hello", ""},  // CSV format ignores empty lines
                {"world", ""}
        };
        for (final String code : codes) {
            final CSVParser parser = CSVParser.parseString(code);
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(res.length, records.size());
            assertTrue(records.size() > 0);
            for (int i = 0; i < res.length; i++) {
                assertArrayEquals(res[i], records.get(i).values());
            }
        }
    }

    @Test
    public void testEmptyLineBehaviourExcel() throws Exception {
        final String[] codes = {
                "hello,\r\n\r\n\r\n",
                "hello,\n\n\n",
                "hello,\"\"\r\n\r\n\r\n",
                "hello,\"\"\n\n\n"
        };
        final String[][] res = {
                {"hello", ""},
                {""},  // Excel format does not ignore empty lines
                {""}
        };
        for (final String code : codes) {
            final CSVParser parser = CSVParser.parseString(code, CSVFormat.EXCEL);
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(res.length, records.size());
            assertTrue(records.size() > 0);
            for (int i = 0; i < res.length; i++) {
                assertArrayEquals(res[i], records.get(i).values());
            }
        }
    }

    @Test
    public void testEmptyLineBehaviourCSV() throws Exception {
        final String[] codes = {
                "hello,\r\n\r\n\r\n",
                "hello,\n\n\n",
                "hello,\"\"\r\n\r\n\r\n",
                "hello,\"\"\n\n\n"
        };
        final String[][] res = {
                {"hello", ""}  // CSV format ignores empty lines
        };
        for (final String code : codes) {
            final CSVParser parser = CSVParser.parseString(code, CSVFormat.DEFAULT);
            final List<CSVRecord> records = parser.getRecords();
            assertEquals(res.length, records.size());
            assertTrue(records.size() > 0);
            for (int i = 0; i < res.length; i++) {
                assertArrayEquals(res[i], records.get(i).values());
            }
        }
    }

    @Test
    public void testEmptyFile() throws Exception {
        final CSVParser parser = CSVParser.parseString("", CSVFormat.DEFAULT);
        assertNull(parser.nextRecord());
    }

    @Test
    public void testCSV57() throws Exception {
        final CSVParser parser = CSVParser.parseString("", CSVFormat.DEFAULT);
        final List<CSVRecord> list = parser.getRecords();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    @Ignore
    public void testBackslashEscapingOld() throws IOException {
        final String code =
                "one,two,three\n"
                        + "on\\\"e,two\n"
                        + "on\"e,two\n"
                        + "one,\"tw\\\"o\"\n"
                        + "one,\"t\\,wo\"\n"
                        + "one,two,\"th,ree\"\n"
                        + "\"a\\\\\"\n"
                        + "a\\,b\n"
                        + "\"a\\\\,b\"";
        final String[][] res = {
                {"one", "two", "three"},
                {"on\\\"e", "two"},
                {"on\"e", "two"},
                {"one", "tw\"o"},
                {"one", "t\\,wo"},  // backslash in quotes only escapes a delimiter (",")
                {"one", "two", "th,ree"},
                {"a\\\\"},     // backslash in quotes only escapes a delimiter (",")
                {"a\\", "b"},  // a backslash must be returnd
                {"a\\\\,b"}    // backslash in quotes only escapes a delimiter (",")
        };
        final CSVParser parser = CSVParser.parseString(code);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(res.length, records.size());
        assertTrue(records.size() > 0);
        for (int i = 0; i < res.length; i++) {
            assertArrayEquals(res[i], records.get(i).values());
        }
    }

    @Test
    public void testBackslashEscaping() throws IOException {

        // To avoid confusion over the need for escaping chars in java code,
        // We will test with a forward slash as the escape char, and a single
        // quote as the encapsulator.

        final String code =
                "one,two,three\n" // 0
                        + "'',''\n"       // 1) empty encapsulators
                        + "/',/'\n"       // 2) single encapsulators
                        + "'/'','/''\n"   // 3) single encapsulators encapsulated via escape
                        + "'''',''''\n"   // 4) single encapsulators encapsulated via doubling
                        + "/,,/,\n"       // 5) separator escaped
                        + "//,//\n"       // 6) escape escaped
                        + "'//','//'\n"   // 7) escape escaped in encapsulation
                        + "   8   ,   \"quoted \"\" /\" // string\"   \n"     // don't eat spaces
                        + "9,   /\n   \n"  // escaped newline
                        + "";
        final String[][] res = {
                {"one", "two", "three"}, // 0
                {"", ""},                // 1
                {"'", "'"},              // 2
                {"'", "'"},              // 3
                {"'", "'"},              // 4
                {",", ","},              // 5
                {"/", "/"},              // 6
                {"/", "/"},              // 7
                {"   8   ", "   \"quoted \"\" /\" / string\"   "},
                {"9", "   \n   "},
        };


        final CSVFormat format = CSVFormat.newFormat(',').withQuoteChar('\'')
                               .withRecordSeparator(CRLF).withEscape('/').withIgnoreEmptyLines(true);

        final CSVParser parser = CSVParser.parseString(code, format);
        final List<CSVRecord> records = parser.getRecords();
        assertTrue(records.size() > 0);

        Utils.compare("Records do not match expected result", res, records);
    }

    @Test
    public void testBackslashEscaping2() throws IOException {

        // To avoid confusion over the need for escaping chars in java code,
        // We will test with a forward slash as the escape char, and a single
        // quote as the encapsulator.

        final String code = ""
                + " , , \n"           // 1)
                + " \t ,  , \n"       // 2)
                + " // , /, , /,\n"   // 3)
                + "";
        final String[][] res = {
                {" ", " ", " "},         // 1
                {" \t ", "  ", " "},     // 2
                {" / ", " , ", " ,"},    // 3
        };


        final CSVFormat format = CSVFormat.newFormat(',')
                .withRecordSeparator(CRLF).withEscape('/').withIgnoreEmptyLines(true);

        final CSVParser parser = CSVParser.parseString(code, format);
        final List<CSVRecord> records = parser.getRecords();
        assertTrue(records.size() > 0);

        Utils.compare("", res, records);
    }

    @Test
    public void testDefaultFormat() throws IOException {
        final String code = ""
                + "a,b#\n"           // 1)
                + "\"\n\",\" \",#\n"   // 2)
                + "#,\"\"\n"         // 3)
                + "# Final comment\n"// 4)
                ;
        final String[][] res = {
                {"a", "b#"},
                {"\n", " ", "#"},
                {"#", ""},
                {"# Final comment"}
        };

        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isCommentingEnabled());

        CSVParser parser = CSVParser.parseString(code, format);
        List<CSVRecord> records = parser.getRecords();
        assertTrue(records.size() > 0);

        Utils.compare("Failed to parse without comments", res, records);

        final String[][] res_comments = {
                {"a", "b#"},
                {"\n", " ", "#"},
        };

        format = CSVFormat.DEFAULT.withCommentStart('#');
        parser = CSVParser.parseString(code, format);
        records = parser.getRecords();

        Utils.compare("Failed to parse with comments", res_comments, records);
    }

    @Test
    public void testCarriageReturnLineFeedEndings() throws IOException {
        final String code = "foo\r\nbaar,\r\nhello,world\r\n,kanu";
        final CSVParser parser = CSVParser.parseString(code, CSVFormat.DEFAULT);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());
    }

    @Test
    public void testClose() throws Exception {
        final Reader in = new StringReader("# comment\na,b,c\n1,2,3\nx,y,z");
        final CSVParser parser = CSVFormat.DEFAULT.withCommentStart('#').withHeader().parse(in);
        final Iterator<CSVRecord> records = parser.iterator();
        assertTrue(records.hasNext());
        parser.close();
        assertFalse(records.hasNext());
        assertNull(records.next());
        assertFalse(records.hasNext());
        assertNull(records.next());
    }

    @Test
    public void testCarriageReturnEndings() throws IOException {
        final String code = "foo\rbaar,\rhello,world\r,kanu";
        final CSVParser parser = CSVParser.parseString(code);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());
    }

    @Test
    public void testLineFeedEndings() throws IOException {
        final String code = "foo\nbaar,\nhello,world\n,kanu";
        final CSVParser parser = CSVParser.parseString(code);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(4, records.size());
    }

    @Test
    public void testIgnoreEmptyLines() throws IOException {
        final String code = "\nfoo,baar\n\r\n,\n\n,world\r\n\n";
        //String code = "world\r\n\n";
        //String code = "foo;baar\r\n\r\nhello;\r\n\r\nworld;\r\n";
        final CSVParser parser = CSVParser.parseString(code);
        final List<CSVRecord> records = parser.getRecords();
        assertEquals(3, records.size());
    }

    @Test
    public void testForEach() throws Exception {
        final List<CSVRecord> records = new ArrayList<CSVRecord>();

        final Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");

        for (final CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
            records.add(record);
        }

        assertEquals(3, records.size());
        assertArrayEquals(new String[]{"a", "b", "c"}, records.get(0).values());
        assertArrayEquals(new String[]{"1", "2", "3"}, records.get(1).values());
        assertArrayEquals(new String[]{"x", "y", "z"}, records.get(2).values());
    }

    @Test
    public void testRoundtrip() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        final String input = "a,b,c\r\n1,2,3\r\nx,y,z\r\n";
        for (final CSVRecord record : CSVParser.parseString(input, CSVFormat.DEFAULT)) {
            printer.printRecord(record);
        }
        assertEquals(input, out.toString());
        printer.close();
    }

    @Test
    public void testIterator() throws Exception {
        final Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");

        final Iterator<CSVRecord> iterator = CSVFormat.DEFAULT.parse(in).iterator();

        assertTrue(iterator.hasNext());
        try {
            iterator.remove();
            fail("expected UnsupportedOperationException");
        } catch (final UnsupportedOperationException expected) {
            // expected
        }
        assertArrayEquals(new String[]{"a", "b", "c"}, iterator.next().values());
        assertArrayEquals(new String[]{"1", "2", "3"}, iterator.next().values());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertArrayEquals(new String[]{"x", "y", "z"}, iterator.next().values());
        assertFalse(iterator.hasNext());

        try {
            iterator.next();
            fail("NoSuchElementException expected");
        } catch (final NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testHeader() throws Exception {
        final Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");

        final Iterator<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in).iterator();

        for (int i = 0; i < 2; i++) {
            assertTrue(records.hasNext());
            final CSVRecord record = records.next();
            assertEquals(record.get(0), record.get("a"));
            assertEquals(record.get(1), record.get("b"));
            assertEquals(record.get(2), record.get("c"));
        }

        assertFalse(records.hasNext());
    }

    @Test
    public void testSkipSetHeader() throws Exception {
        final Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");
        final Iterator<CSVRecord> records = CSVFormat.DEFAULT.withHeader("a", "b", "c").withSkipHeaderRecord(true)
                .parse(in).iterator();
        final CSVRecord record = records.next();
        assertEquals("1", record.get("a"));
        assertEquals("2", record.get("b"));
        assertEquals("3", record.get("c"));
    }

    @Test
    public void testSkipAutoHeader() throws Exception {
        final Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");
        final Iterator<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in).iterator();
        final CSVRecord record = records.next();
        assertEquals("1", record.get("a"));
        assertEquals("2", record.get("b"));
        assertEquals("3", record.get("c"));
    }

    @Test
    public void testHeaderComment() throws Exception {
        final Reader in = new StringReader("# comment\na,b,c\n1,2,3\nx,y,z");

        final Iterator<CSVRecord> records = CSVFormat.DEFAULT.withCommentStart('#').withHeader().parse(in).iterator();

        for (int i = 0; i < 2; i++) {
            assertTrue(records.hasNext());
            final CSVRecord record = records.next();
            assertEquals(record.get(0), record.get("a"));
            assertEquals(record.get(1), record.get("b"));
            assertEquals(record.get(2), record.get("c"));
        }

        assertFalse(records.hasNext());
    }

    @Test
    public void testProvidedHeader() throws Exception {
        final Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");

        final Iterator<CSVRecord> records = CSVFormat.DEFAULT.withHeader("A", "B", "C").parse(in).iterator();

        for (int i = 0; i < 3; i++) {
            assertTrue(records.hasNext());
            final CSVRecord record = records.next();
            assertTrue(record.isMapped("A"));
            assertTrue(record.isMapped("B"));
            assertTrue(record.isMapped("C"));
            assertFalse(record.isMapped("NOT MAPPED"));
            assertEquals(record.get(0), record.get("A"));
            assertEquals(record.get(1), record.get("B"));
            assertEquals(record.get(2), record.get("C"));
        }

        assertFalse(records.hasNext());
    }

    @Test
    public void testProvidedHeaderAuto() throws Exception {
        final Reader in = new StringReader("a,b,c\n1,2,3\nx,y,z");

        final Iterator<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in).iterator();

        for (int i = 0; i < 2; i++) {
            assertTrue(records.hasNext());
            final CSVRecord record = records.next();
            assertTrue(record.isMapped("a"));
            assertTrue(record.isMapped("b"));
            assertTrue(record.isMapped("c"));
            assertFalse(record.isMapped("NOT MAPPED"));
            assertEquals(record.get(0), record.get("a"));
            assertEquals(record.get(1), record.get("b"));
            assertEquals(record.get(2), record.get("c"));
        }

        assertFalse(records.hasNext());
    }

    @Test
    public void testMappedButNotSetAsOutlook2007ContactExport() throws Exception {
        final Reader in = new StringReader("a,b,c\n1,2\nx,y,z");
        final Iterator<CSVRecord> records = CSVFormat.DEFAULT.withHeader("A", "B", "C").withSkipHeaderRecord(true)
                .parse(in).iterator();
        CSVRecord record;

        // 1st record
        record = records.next();
        assertTrue(record.isMapped("A"));
        assertTrue(record.isMapped("B"));
        assertTrue(record.isMapped("C"));
        assertTrue(record.isSet("A"));
        assertTrue(record.isSet("B"));
        assertFalse(record.isSet("C"));
        assertEquals("1", record.get("A"));
        assertEquals("2", record.get("B"));
        assertFalse(record.isConsistent());

        // 2nd record
        record = records.next();
        assertTrue(record.isMapped("A"));
        assertTrue(record.isMapped("B"));
        assertTrue(record.isMapped("C"));
        assertTrue(record.isSet("A"));
        assertTrue(record.isSet("B"));
        assertTrue(record.isSet("C"));
        assertEquals("x", record.get("A"));
        assertEquals("y", record.get("B"));
        assertEquals("z", record.get("C"));
        assertTrue(record.isConsistent());

        assertFalse(records.hasNext());
    }

    @Test
    public void testGetHeaderMap() throws Exception {
        final CSVParser parser = CSVParser.parseString("a,b,c\n1,2,3\nx,y,z", CSVFormat.DEFAULT.withHeader("A", "B", "C"));
        final Map<String, Integer> headerMap = parser.getHeaderMap();
        final Iterator<String> columnNames = headerMap.keySet().iterator();
        // Headers are iterated in column order.
        Assert.assertEquals("A", columnNames.next());
        Assert.assertEquals("B", columnNames.next());
        Assert.assertEquals("C", columnNames.next());
        final Iterator<CSVRecord> records = parser.iterator();

        // Parse to make sure getHeaderMap did not have a side-effect.
        for (int i = 0; i < 3; i++) {
            assertTrue(records.hasNext());
            final CSVRecord record = records.next();
            assertEquals(record.get(0), record.get("A"));
            assertEquals(record.get(1), record.get("B"));
            assertEquals(record.get(2), record.get("C"));
        }

        assertFalse(records.hasNext());
    }

    @Test
    public void testGetLineNumberWithLF() throws Exception {
        this.validateLineNumbers(String.valueOf(LF));
    }

    @Test
    public void testGetLineNumberWithCRLF() throws Exception {
        this.validateLineNumbers(CRLF);
    }

    @Test
    public void testGetLineNumberWithCR() throws Exception {
        this.validateLineNumbers(String.valueOf(CR));
    }

    @Test
    public void testGetRecordNumberWithLF() throws Exception {
        this.validateRecordNumbers(String.valueOf(LF));
    }

    @Test
    public void testGetRecordWithMultiiLineValues() throws Exception {
        final CSVParser parser = CSVParser.parseString("\"a\r\n1\",\"a\r\n2\"" + CRLF + "\"b\r\n1\",\"b\r\n2\"" + CRLF + "\"c\r\n1\",\"c\r\n2\"",
                CSVFormat.DEFAULT.withRecordSeparator(CRLF));
        CSVRecord record;
        assertEquals(0, parser.getRecordNumber());
        assertEquals(0, parser.getCurrentLineNumber());
        assertNotNull(record = parser.nextRecord());
        assertEquals(3, parser.getCurrentLineNumber());
        assertEquals(1, record.getRecordNumber());
        assertEquals(1, parser.getRecordNumber());
        assertNotNull(record = parser.nextRecord());
        assertEquals(6, parser.getCurrentLineNumber());
        assertEquals(2, record.getRecordNumber());
        assertEquals(2, parser.getRecordNumber());
        assertNotNull(record = parser.nextRecord());
        assertEquals(8, parser.getCurrentLineNumber());
        assertEquals(3, record.getRecordNumber());
        assertEquals(3, parser.getRecordNumber());
        assertNull(record = parser.nextRecord());
        assertEquals(8, parser.getCurrentLineNumber());
        assertEquals(3, parser.getRecordNumber());
    }

    @Test
    public void testGetRecordNumberWithCRLF() throws Exception {
        this.validateRecordNumbers(CRLF);
    }

    @Test
    public void testGetRecordNumberWithCR() throws Exception {
        this.validateRecordNumbers(String.valueOf(CR));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws Exception {
        final CSVFormat invalidFormat = CSVFormat.DEFAULT.withDelimiter(CR);
        new CSVParser((Reader) null, invalidFormat);
    }

    private void validateRecordNumbers(final String lineSeparator) throws IOException {
        final CSVParser parser = CSVParser.parseString("a" + lineSeparator + "b" + lineSeparator + "c", CSVFormat.DEFAULT.withRecordSeparator(lineSeparator));
        CSVRecord record;
        assertEquals(0, parser.getRecordNumber());
        assertNotNull(record = parser.nextRecord());
        assertEquals(1, record.getRecordNumber());
        assertEquals(1, parser.getRecordNumber());
        assertNotNull(record = parser.nextRecord());
        assertEquals(2, record.getRecordNumber());
        assertEquals(2, parser.getRecordNumber());
        assertNotNull(record = parser.nextRecord());
        assertEquals(3, record.getRecordNumber());
        assertEquals(3, parser.getRecordNumber());
        assertNull(record = parser.nextRecord());
        assertEquals(3, parser.getRecordNumber());
    }

    private void validateLineNumbers(final String lineSeparator) throws IOException {
        final CSVParser parser = CSVParser.parseString("a" + lineSeparator + "b" + lineSeparator + "c", CSVFormat.DEFAULT.withRecordSeparator(lineSeparator));
        assertEquals(0, parser.getCurrentLineNumber());
        assertNotNull(parser.nextRecord());
        assertEquals(1, parser.getCurrentLineNumber());
        assertNotNull(parser.nextRecord());
        assertEquals(2, parser.getCurrentLineNumber());
        assertNotNull(parser.nextRecord());
        // Still 2 because the last line is does not have EOL chars
        assertEquals(2, parser.getCurrentLineNumber());
        assertNull(parser.nextRecord());
        // Still 2 because the last line is does not have EOL chars
        assertEquals(2, parser.getCurrentLineNumber());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4071.java