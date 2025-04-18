error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8858.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8858.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8858.java
text:
```scala
public synchronized v@@oid close() throws IOException {

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
package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.testtools.FileBasedTestCase;
import org.junit.Assert;

/**
 * This is used to test IOUtils for correctness. The following checks are performed:
 * <ul>
 * <li>The return must not be null, must be the same type and equals() to the method's second arg</li>
 * <li>All bytes must have been read from the source (available() == 0)</li>
 * <li>The source and destination content must be identical (byte-wise comparison check)</li>
 * <li>The output stream must not have been closed (a byte/char is written to test this, and subsequent size checked)</li>
 * </ul>
 * Due to interdependencies in IOUtils and IOUtilsTestlet, one bug may cause multiple tests to fail.
 */
public class IOUtilsTestCase extends FileBasedTestCase {

    private static final int FILE_SIZE = 1024 * 4 + 1;

    /** Determine if this is windows. */
    private static final boolean WINDOWS = File.separatorChar == '\\';
    /*
     * Note: this is not particularly beautiful code. A better way to check for flush and close status would be to
     * implement "trojan horse" wrapper implementations of the various stream classes, which set a flag when relevant
     * methods are called. (JT)
     */

    private char[] carr = null;

    private byte[] iarr = null;

    private File m_testFile;

    public IOUtilsTestCase(final String name) {
        super(name);
    }

    /** Assert that the contents of two byte arrays are the same. */
    private void assertEqualContent(final byte[] b0, final byte[] b1) {
        assertTrue("Content not equal according to java.util.Arrays#equals()", Arrays.equals(b0, b1));
    }

    @Override
    public void setUp() {
        try {
            getTestDirectory().mkdirs();
            m_testFile = new File(getTestDirectory(), "file2-test.txt");

            createFile(m_testFile, FILE_SIZE);
        } catch (final IOException ioe) {
            throw new RuntimeException("Can't run this test because the environment could not be built: "
                    + ioe.getMessage());
        }
        // Create and init a byte array as input data
        iarr = new byte[200];
        Arrays.fill( iarr, (byte)-1);
        for( int i=0; i< 80; i++){
            iarr[i] = (byte) i;
        }
        carr = new char[200];
        Arrays.fill( carr, (char)-1);
        for( int i=0; i< 80; i++){
            carr[i] = (char) i;
        }
    }

    @Override
    public void tearDown() {
        carr = null;
        iarr = null;
        try {
            FileUtils.deleteDirectory(getTestDirectory());
        } catch (final IOException e) {
            throw new RuntimeException("Could not clear up " + getTestDirectory() + ": " + e);
        }
    }

    public void testCloseQuietly_CloseableIOException() {
        IOUtils.closeQuietly(new Closeable() {            
            public void close() throws IOException {
                throw new IOException();
            }
        });
    }

    public void testCloseQuietly_Selector() {
        Selector selector = null;
        try {
            selector = Selector.open();
        } catch (final IOException e) {
        } finally {
            IOUtils.closeQuietly(selector);
        }
    }

    public void testCloseQuietly_SelectorIOException() {
        final Selector selector = new SelectorAdapter() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(selector);
    }

    public void testCloseQuietly_SelectorNull() {
        final Selector selector = null;
        IOUtils.closeQuietly(selector);
    }

    public void testCloseQuietly_SelectorTwice() {
        Selector selector = null;
        try {
            selector = Selector.open();
        } catch (final IOException e) {
        } finally {
            IOUtils.closeQuietly(selector);
            IOUtils.closeQuietly(selector);
        }
    }

    public void testCloseQuietly_ServerSocket() throws IOException {
        IOUtils.closeQuietly((ServerSocket) null);
        IOUtils.closeQuietly(new ServerSocket());
    }

    public void testCloseQuietly_ServerSocketIOException() throws IOException {
        IOUtils.closeQuietly(new ServerSocket() {            
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        });
    }

    public void testCloseQuietly_Socket() {
        IOUtils.closeQuietly((Socket) null);
        IOUtils.closeQuietly(new Socket());
    }

    public void testCloseQuietly_SocketIOException() {
        IOUtils.closeQuietly(new Socket() {            
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        });
    }

    public void testConstants() throws Exception {
        assertEquals('/', IOUtils.DIR_SEPARATOR_UNIX);
        assertEquals('\\', IOUtils.DIR_SEPARATOR_WINDOWS);
        assertEquals("\n", IOUtils.LINE_SEPARATOR_UNIX);
        assertEquals("\r\n", IOUtils.LINE_SEPARATOR_WINDOWS);
        if (WINDOWS) {
            assertEquals('\\', IOUtils.DIR_SEPARATOR);
            assertEquals("\r\n", IOUtils.LINE_SEPARATOR);
        } else {
            assertEquals('/', IOUtils.DIR_SEPARATOR);
            assertEquals("\n", IOUtils.LINE_SEPARATOR);
        }
    }

    public void testContentEquals_InputStream_InputStream() throws Exception {
        {
            final ByteArrayInputStream input1 = new ByteArrayInputStream("".getBytes(Charsets.UTF_8));
            assertTrue(IOUtils.contentEquals(input1, input1));
        }
        {
            final ByteArrayInputStream input1 = new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8));
            assertTrue(IOUtils.contentEquals(input1, input1));
        }
        assertTrue(IOUtils
                .contentEquals(new ByteArrayInputStream("".getBytes(Charsets.UTF_8)), new ByteArrayInputStream("".getBytes(Charsets.UTF_8))));
        assertTrue(IOUtils.contentEquals(new BufferedInputStream(new ByteArrayInputStream("".getBytes(Charsets.UTF_8))), new BufferedInputStream(
                new ByteArrayInputStream("".getBytes(Charsets.UTF_8)))));
        assertTrue(IOUtils.contentEquals(new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8)),
                new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8))));
        assertFalse(IOUtils.contentEquals(new ByteArrayInputStream("ABCD".getBytes(Charsets.UTF_8)),
                new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8))));
        assertFalse(IOUtils.contentEquals(new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8)),
                new ByteArrayInputStream("ABCD".getBytes(Charsets.UTF_8))));
    }

    public void testContentEquals_Reader_Reader() throws Exception {
        {
            final StringReader input1 = new StringReader("");
            assertTrue(IOUtils.contentEquals(input1, input1));
        }
        {
            final StringReader input1 = new StringReader("ABC");
            assertTrue(IOUtils.contentEquals(input1, input1));
        }
        assertTrue(IOUtils.contentEquals(new StringReader(""), new StringReader("")));
        assertTrue(IOUtils.contentEquals(new BufferedReader(new StringReader("")), new BufferedReader(new StringReader(""))));
        assertTrue(IOUtils.contentEquals(new StringReader("ABC"), new StringReader("ABC")));
        assertFalse(IOUtils.contentEquals(new StringReader("ABCD"), new StringReader("ABC")));
        assertFalse(IOUtils.contentEquals(new StringReader("ABC"), new StringReader("ABCD")));
    }

    public void testContentEqualsIgnoreEOL() throws Exception {
        {
            final Reader input1 = new CharArrayReader("".toCharArray());
            assertTrue(IOUtils.contentEqualsIgnoreEOL(input1, input1));
        }
        {
            final Reader input1 =  new CharArrayReader("321\r\n".toCharArray());
            assertTrue(IOUtils.contentEqualsIgnoreEOL(input1, input1));
        }

        Reader r1;
        Reader r2;

        r1 = new CharArrayReader("".toCharArray());
        r2 = new CharArrayReader("".toCharArray());
        assertTrue(IOUtils.contentEqualsIgnoreEOL(r1, r2));

        r1 = new CharArrayReader("1".toCharArray());
        r2 = new CharArrayReader("1".toCharArray());
        assertTrue(IOUtils.contentEqualsIgnoreEOL(r1, r2));

        r1 = new CharArrayReader("1".toCharArray());
        r2 = new CharArrayReader("2".toCharArray());
        assertFalse(IOUtils.contentEqualsIgnoreEOL(r1, r2));

        r1 = new CharArrayReader("123\rabc".toCharArray());
        r2 = new CharArrayReader("123\nabc".toCharArray());
        assertTrue(IOUtils.contentEqualsIgnoreEOL(r1, r2));

        r1 = new CharArrayReader("321".toCharArray());
        r2 = new CharArrayReader("321\r\n".toCharArray());
        assertTrue(IOUtils.contentEqualsIgnoreEOL(r1, r2));
    }

    @SuppressWarnings("deprecation")
    // testing deprecated method
    public void testCopy_ByteArray_OutputStream() throws Exception {
        final File destination = newFile("copy8.txt");
        final FileInputStream fin = new FileInputStream(m_testFile);
        byte[] in;
        try {
            // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
            in = IOUtils.toByteArray(fin);
        } finally {
            fin.close();
        }

        final FileOutputStream fout = new FileOutputStream(destination);
        try {
            CopyUtils.copy(in, fout);

            fout.flush();

            checkFile(destination, m_testFile);
            checkWrite(fout);
        } finally {
            fout.close();
        }
        deleteFile(destination);
    }
    
    @SuppressWarnings("deprecation")
    // testing deprecated method
    public void testCopy_ByteArray_Writer() throws Exception {
        final File destination = newFile("copy7.txt");
        final FileInputStream fin = new FileInputStream(m_testFile);
        byte[] in;
        try {
            // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
            in = IOUtils.toByteArray(fin);
        } finally {
            fin.close();
        }

        final FileWriter fout = new FileWriter(destination);
        try {
            CopyUtils.copy(in, fout);
            fout.flush();
            checkFile(destination, m_testFile);
            checkWrite(fout);
        } finally {
            fout.close();
        }
        deleteFile(destination);
    }

    @SuppressWarnings("deprecation")
    // testing deprecated method
    public void testCopy_String_Writer() throws Exception {
        final File destination = newFile("copy6.txt");
        final FileReader fin = new FileReader(m_testFile);
        String str;
        try {
            // Create our String. Rely on testReaderToString() to make sure this is valid.
            str = IOUtils.toString(fin);
        } finally {
            fin.close();
        }

        final FileWriter fout = new FileWriter(destination);
        try {
            CopyUtils.copy(str, fout);
            fout.flush();

            checkFile(destination, m_testFile);
            checkWrite(fout);
        } finally {
            fout.close();
        }
        deleteFile(destination);
    }

    public void testCopyLarge_CharExtraLength() throws IOException {
        CharArrayReader is = null;
        CharArrayWriter os = null;
        try {
            // Create streams
            is = new CharArrayReader(carr);
            os = new CharArrayWriter();

            // Test our copy method
            // for extra length, it reads till EOF
            assertEquals(200, IOUtils.copyLarge(is, os, 0, 2000));
            final char[] oarr = os.toCharArray();

            // check that output length is correct
            assertEquals(200, oarr.length);
            // check that output data corresponds to input data
            assertEquals(1, oarr[1]);
            assertEquals(79, oarr[79]);
            assertEquals((char) -1, oarr[80]);

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_CharFullLength() throws IOException {
        CharArrayReader is = null;
        CharArrayWriter os = null;
        try {
            // Create streams
            is = new CharArrayReader(carr);
            os = new CharArrayWriter();

            // Test our copy method
            assertEquals(200, IOUtils.copyLarge(is, os, 0, -1));
            final char[] oarr = os.toCharArray();

            // check that output length is correct
            assertEquals(200, oarr.length);
            // check that output data corresponds to input data
            assertEquals(1, oarr[1]);
            assertEquals(79, oarr[79]);
            assertEquals((char) -1, oarr[80]);

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_CharNoSkip() throws IOException {
        CharArrayReader is = null;
        CharArrayWriter os = null;
        try {
            // Create streams
            is = new CharArrayReader(carr);
            os = new CharArrayWriter();

            // Test our copy method
            assertEquals(100, IOUtils.copyLarge(is, os, 0, 100));
            final char[] oarr = os.toCharArray();

            // check that output length is correct
            assertEquals(100, oarr.length);
            // check that output data corresponds to input data
            assertEquals(1, oarr[1]);
            assertEquals(79, oarr[79]);
            assertEquals((char) -1, oarr[80]);

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_CharSkip() throws IOException {
        CharArrayReader is = null;
        CharArrayWriter os = null;
        try {
            // Create streams
            is = new CharArrayReader(carr);
            os = new CharArrayWriter();

            // Test our copy method
            assertEquals(100, IOUtils.copyLarge(is, os, 10, 100));
            final char[] oarr = os.toCharArray();

            // check that output length is correct
            assertEquals(100, oarr.length);
            // check that output data corresponds to input data
            assertEquals(11, oarr[1]);
            assertEquals(79, oarr[69]);
            assertEquals((char) -1, oarr[70]);

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_CharSkipInvalid() throws IOException {
        CharArrayReader is = null;
        CharArrayWriter os = null;
        try {
            // Create streams
            is = new CharArrayReader(carr);
            os = new CharArrayWriter();

            // Test our copy method
            IOUtils.copyLarge(is, os, 1000, 100);
            fail("Should have thrown EOFException");
        } catch (final EOFException eofe) {
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_ExtraLength() throws IOException {
        ByteArrayInputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            // Create streams
            is = new ByteArrayInputStream(iarr);
            os = new ByteArrayOutputStream();

            // Test our copy method
            // for extra length, it reads till EOF
            assertEquals(200, IOUtils.copyLarge(is, os, 0, 2000));
            final byte[] oarr = os.toByteArray();

            // check that output length is correct
            assertEquals(200, oarr.length);
            // check that output data corresponds to input data
            assertEquals(1, oarr[1]);
            assertEquals(79, oarr[79]);
            assertEquals(-1, oarr[80]);

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_FullLength() throws IOException {
        ByteArrayInputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            // Create streams
            is = new ByteArrayInputStream(iarr);
            os = new ByteArrayOutputStream();

            // Test our copy method
            assertEquals(200, IOUtils.copyLarge(is, os, 0, -1));
            final byte[] oarr = os.toByteArray();

            // check that output length is correct
            assertEquals(200, oarr.length);
            // check that output data corresponds to input data
            assertEquals(1, oarr[1]);
            assertEquals(79, oarr[79]);
            assertEquals(-1, oarr[80]);

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_NoSkip() throws IOException {
        ByteArrayInputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            // Create streams
            is = new ByteArrayInputStream(iarr);
            os = new ByteArrayOutputStream();

            // Test our copy method
            assertEquals(100, IOUtils.copyLarge(is, os, 0, 100));
            final byte[] oarr = os.toByteArray();

            // check that output length is correct
            assertEquals(100, oarr.length);
            // check that output data corresponds to input data
            assertEquals(1, oarr[1]);
            assertEquals(79, oarr[79]);
            assertEquals(-1, oarr[80]);

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_Skip() throws IOException {
        ByteArrayInputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            // Create streams
            is = new ByteArrayInputStream(iarr);
            os = new ByteArrayOutputStream();

            // Test our copy method
            assertEquals(100, IOUtils.copyLarge(is, os, 10, 100));
            final byte[] oarr = os.toByteArray();

            // check that output length is correct
            assertEquals(100, oarr.length);
            // check that output data corresponds to input data
            assertEquals(11, oarr[1]);
            assertEquals(79, oarr[69]);
            assertEquals(-1, oarr[70]);

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testCopyLarge_SkipInvalid() throws IOException {
        ByteArrayInputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            // Create streams
            is = new ByteArrayInputStream(iarr);
            os = new ByteArrayOutputStream();

            // Test our copy method
            IOUtils.copyLarge(is, os, 1000, 100);
            fail("Should have thrown EOFException");
        } catch (final EOFException eofe) {
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    public void testRead_ReadableByteChannel() throws Exception {
        final ByteBuffer buffer = ByteBuffer.allocate(FILE_SIZE);
        final FileInputStream fileInputStream = new FileInputStream(m_testFile);
        final FileChannel input = fileInputStream.getChannel();
        try {
            assertEquals(FILE_SIZE, IOUtils.read(input, buffer));
            assertEquals(0, IOUtils.read(input, buffer));
            assertEquals(0, buffer.remaining());
            assertEquals(0, input.read(buffer));
            buffer.clear();
            try {
                IOUtils.readFully(input, buffer);
                fail("Should have failed with EOFxception");
            } catch (final EOFException expected) {
                // expected
            }
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fileInputStream);
        }}

    public void testReadFully_InputStream_ByteArray() throws Exception {
        final int size = 1027;

        final byte[] buffer = new byte[size];

        final InputStream input = new ByteArrayInputStream(new byte[size]);
        try {
            IOUtils.readFully(input, buffer, 0, -1);
            fail("Should have failed with IllegalArgumentException");
        } catch (final IllegalArgumentException expected) {
            // expected
        }
        IOUtils.readFully(input, buffer, 0, 0);
        IOUtils.readFully(input, buffer, 0, size - 1);
        try {
            IOUtils.readFully(input, buffer, 0, 2);
            fail("Should have failed with EOFxception");
        } catch (final EOFException expected) {
            // expected
        }
        IOUtils.closeQuietly(input);

    }

    public void testReadFully_InputStream_Offset() throws Exception {
        final byte[] bytes = "abcd1234".getBytes("UTF-8");
        final ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        final byte[] buffer = "wx00000000".getBytes("UTF-8");
        IOUtils.readFully(stream, buffer, 2, 8);
        assertEquals("wxabcd1234", new String(buffer, 0, buffer.length, "UTF-8"));
        IOUtils.closeQuietly(stream);
    }

    public void testReadFully_ReadableByteChannel() throws Exception {
        final ByteBuffer buffer = ByteBuffer.allocate(FILE_SIZE);
        final FileInputStream fileInputStream = new FileInputStream(m_testFile);
        final FileChannel input = fileInputStream.getChannel();
        try {
            IOUtils.readFully(input, buffer);
            assertEquals(FILE_SIZE, buffer.position());
            assertEquals(0, buffer.remaining());
            assertEquals(0, input.read(buffer));
            IOUtils.readFully(input, buffer);
            assertEquals(FILE_SIZE, buffer.position());
            assertEquals(0, buffer.remaining());
            assertEquals(0, input.read(buffer));
            IOUtils.readFully(input, buffer);
            buffer.clear();
            try {
                IOUtils.readFully(input, buffer);
                fail("Should have failed with EOFxception");
            } catch (final EOFException expected) {
                // expected
            }
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public void testReadFully_Reader() throws Exception {
        final int size = 1027;

        final char[] buffer = new char[size];

        final Reader input = new CharArrayReader(new char[size]);
        IOUtils.readFully(input, buffer, 0, 0);
        IOUtils.readFully(input, buffer, 0, size - 3);
        try {
            IOUtils.readFully(input, buffer, 0, -1);
            fail("Should have failed with IllegalArgumentException");
        } catch (final IllegalArgumentException expected) {
            // expected
        }
        try {
            IOUtils.readFully(input, buffer, 0, 5);
            fail("Should have failed with EOFException");
        } catch (final EOFException expected) {
            // expected
        }
        IOUtils.closeQuietly(input);
    }

    public void testReadFully_Reader_Offset() throws Exception {
        final Reader reader = new StringReader("abcd1234");
        final char[] buffer = "wx00000000".toCharArray();
        IOUtils.readFully(reader, buffer, 2, 8);
        assertEquals("wxabcd1234", new String(buffer));
        IOUtils.closeQuietly(reader);
    }

    public void testReadLines_InputStream() throws Exception {
        final File file = newFile("lines.txt");
        InputStream in = null;
        try {
            final String[] data = new String[] { "hello", "world", "", "this is", "some text" };
            createLineBasedFile(file, data);

            in = new FileInputStream(file);
            final List<String> lines = IOUtils.readLines(in);
            assertEquals(Arrays.asList(data), lines);
            assertEquals(-1, in.read());
        } finally {
            IOUtils.closeQuietly(in);
            deleteFile(file);
        }
    }

    public void testReadLines_InputStream_String() throws Exception {
        final File file = newFile("lines.txt");
        InputStream in = null;
        try {
            final String[] data = new String[] { "hello", "/u1234", "", "this is", "some text" };
            createLineBasedFile(file, data);

            in = new FileInputStream(file);
            final List<String> lines = IOUtils.readLines(in, "UTF-8");
            assertEquals(Arrays.asList(data), lines);
            assertEquals(-1, in.read());
        } finally {
            IOUtils.closeQuietly(in);
            deleteFile(file);
        }
    }

    public void testReadLines_Reader() throws Exception {
        final File file = newFile("lines.txt");
        Reader in = null;
        try {
            final String[] data = new String[] { "hello", "/u1234", "", "this is", "some text" };
            createLineBasedFile(file, data);

            in = new InputStreamReader(new FileInputStream(file));
            final List<String> lines = IOUtils.readLines(in);
            assertEquals(Arrays.asList(data), lines);
            assertEquals(-1, in.read());
        } finally {
            IOUtils.closeQuietly(in);
            deleteFile(file);
        }
    }

    public void testSkip_FileReader() throws Exception {
        final FileReader in = new FileReader(m_testFile);
        try {
            assertEquals(FILE_SIZE - 10, IOUtils.skip(in, FILE_SIZE - 10));
            assertEquals(10, IOUtils.skip(in, 20));
            assertEquals(0, IOUtils.skip(in, 10));
        } finally {
            in.close();
        }
    }

    public void testSkip_InputStream() throws Exception {
        final InputStream in = new FileInputStream(m_testFile);
        try {
            assertEquals(FILE_SIZE - 10, IOUtils.skip(in, FILE_SIZE - 10));
            assertEquals(10, IOUtils.skip(in, 20));
            assertEquals(0, IOUtils.skip(in, 10));
        } finally {
            in.close();
        }
    }

    public void testSkip_ReadableByteChannel() throws Exception {
        final FileInputStream fileInputStream = new FileInputStream(m_testFile);
        final FileChannel fileChannel = fileInputStream.getChannel();
        try {
            assertEquals(FILE_SIZE - 10, IOUtils.skip(fileChannel, FILE_SIZE - 10));
            assertEquals(10, IOUtils.skip(fileChannel, 20));
            assertEquals(0, IOUtils.skip(fileChannel, 10));
        } finally {
            IOUtils.closeQuietly(fileChannel);
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public void testSkipFully_InputStream() throws Exception {
        final int size = 1027;

        final InputStream input = new ByteArrayInputStream(new byte[size]);
        try {
            IOUtils.skipFully(input, -1);
            fail("Should have failed with IllegalArgumentException");
        } catch (final IllegalArgumentException expected) {
            // expected
        }
        IOUtils.skipFully(input, 0);
        IOUtils.skipFully(input, size - 1);
        try {
            IOUtils.skipFully(input, 2);
            fail("Should have failed with IOException");
        } catch (final IOException expected) {
            // expected
        }
        IOUtils.closeQuietly(input);

    }

    public void testSkipFully_ReadableByteChannel() throws Exception {
        final FileInputStream fileInputStream = new FileInputStream(m_testFile);
        final FileChannel fileChannel = fileInputStream.getChannel();
        try {
            try {
                IOUtils.skipFully(fileChannel, -1);
                fail("Should have failed with IllegalArgumentException");
            } catch (final IllegalArgumentException expected) {
                // expected
            }
            IOUtils.skipFully(fileChannel, 0);
            IOUtils.skipFully(fileChannel, FILE_SIZE - 1);
            try {
                IOUtils.skipFully(fileChannel, 2);
                fail("Should have failed with IOException");
            } catch (final IOException expected) {
                // expected
            }
        } finally {
            IOUtils.closeQuietly(fileChannel);
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public void testSkipFully_Reader() throws Exception {
        final int size = 1027;

        final Reader input = new CharArrayReader(new char[size]);
        IOUtils.skipFully(input, 0);
        IOUtils.skipFully(input, size - 3);
        try {
            IOUtils.skipFully(input, -1);
            fail("Should have failed with IllegalArgumentException");
        } catch (final IllegalArgumentException expected) {
            // expected
        }
        try {
            IOUtils.skipFully(input, 5);
            fail("Should have failed with IOException");
        } catch (final IOException expected) {
            // expected
        }
        IOUtils.closeQuietly(input);
    }

    @SuppressWarnings("deprecation")
    // testing deprecated method
    public void testStringToOutputStream() throws Exception {
        final File destination = newFile("copy5.txt");
        final FileReader fin = new FileReader(m_testFile);
        String str;
        try {
            // Create our String. Rely on testReaderToString() to make sure this is valid.
            str = IOUtils.toString(fin);
        } finally {
            fin.close();
        }

        final FileOutputStream fout = new FileOutputStream(destination);
        try {
            CopyUtils.copy(str, fout);
            // Note: this method *does* flush. It is equivalent to:
            // OutputStreamWriter _out = new OutputStreamWriter(fout);
            // CopyUtils.copy( str, _out, 4096 ); // copy( Reader, Writer, int );
            // _out.flush();
            // out = fout;
            // note: we don't flush here; this IOUtils method does it for us

            checkFile(destination, m_testFile);
            checkWrite(fout);
        } finally {
            fout.close();
        }
        deleteFile(destination);
    }

    public void testToBufferedInputStream_InputStream() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);
        try {
            final InputStream in = IOUtils.toBufferedInputStream(fin);
            final byte[] out = IOUtils.toByteArray(in);
            assertNotNull(out);
            assertEquals("Not all bytes were read", 0, fin.available());
            assertEquals("Wrong output size", FILE_SIZE, out.length);
            assertEqualContent(out, m_testFile);
        } finally {
            fin.close();
        }
    }

    public void testToByteArray_InputStream() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);
        try {
            final byte[] out = IOUtils.toByteArray(fin);
            assertNotNull(out);
            assertEquals("Not all bytes were read", 0, fin.available());
            assertEquals("Wrong output size", FILE_SIZE, out.length);
            assertEqualContent(out, m_testFile);
        } finally {
            fin.close();
        }
    }

    public void testToByteArray_InputStream_NegativeSize() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);

        try {
            IOUtils.toByteArray(fin, -1);
            fail("IllegalArgumentException excepted");
        } catch (final IllegalArgumentException exc) {
            assertTrue("Exception message does not start with \"Size must be equal or greater than zero\"", exc
                    .getMessage().startsWith("Size must be equal or greater than zero"));
        } finally {
            fin.close();
        }

    }

    public void testToByteArray_InputStream_Size() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);
        try {
            final byte[] out = IOUtils.toByteArray(fin, m_testFile.length());
            assertNotNull(out);
            assertEquals("Not all bytes were read", 0, fin.available());
            assertEquals("Wrong output size: out.length=" + out.length + "!=" + FILE_SIZE, FILE_SIZE, out.length);
            assertEqualContent(out, m_testFile);
        } finally {
            fin.close();
        }
    }

    public void testToByteArray_InputStream_SizeIllegal() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);

        try {
            IOUtils.toByteArray(fin, m_testFile.length() + 1);
            fail("IOException excepted");
        } catch (final IOException exc) {
            assertTrue("Exception message does not start with \"Unexpected readed size\"",
                    exc.getMessage().startsWith("Unexpected readed size"));
        } finally {
            fin.close();
        }

    }

    public void testToByteArray_InputStream_SizeLong() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);

        try {
            IOUtils.toByteArray(fin, (long) Integer.MAX_VALUE + 1);
            fail("IOException excepted");
        } catch (final IllegalArgumentException exc) {
            assertTrue("Exception message does not start with \"Size cannot be greater than Integer max value\"", exc
                    .getMessage().startsWith("Size cannot be greater than Integer max value"));
        } finally {
            fin.close();
        }

    }

    public void testToByteArray_InputStream_SizeZero() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);

        try {
            final byte[] out = IOUtils.toByteArray(fin, 0);
            assertNotNull("Out cannot be null", out);
            assertEquals("Out length must be 0", 0, out.length);
        } finally {
            fin.close();
        }
    }

    public void testToByteArray_Reader() throws IOException {
        final String charsetName = "UTF-8";
        final byte[] expecteds = charsetName.getBytes(charsetName);
        byte[] actuals = IOUtils.toByteArray(new InputStreamReader(new ByteArrayInputStream(expecteds)));
        Assert.assertArrayEquals(expecteds, actuals);
        actuals = IOUtils.toByteArray(new InputStreamReader(new ByteArrayInputStream(expecteds)), charsetName);
        Assert.assertArrayEquals(expecteds, actuals);
    }

    @SuppressWarnings("deprecation")
    // testing deprecated method
    public void testToByteArray_String() throws Exception {
        final FileReader fin = new FileReader(m_testFile);
        try {
            // Create our String. Rely on testReaderToString() to make sure this is valid.
            final String str = IOUtils.toString(fin);

            final byte[] out = IOUtils.toByteArray(str);
            assertEqualContent(str.getBytes(), out);
        } finally {
            fin.close();
        }
    }

    public void testToByteArray_URI() throws Exception {
        final URI url = m_testFile.toURI();
        final byte[] actual = IOUtils.toByteArray(url);
        Assert.assertEquals(FILE_SIZE, actual.length);
    }

    public void testToByteArray_URL() throws Exception {
        final URL url = m_testFile.toURI().toURL();
        final byte[] actual = IOUtils.toByteArray(url);
        Assert.assertEquals(FILE_SIZE, actual.length);
    }

    public void testToByteArray_URLConnection() throws Exception {
        final URLConnection urlConn = m_testFile.toURI().toURL().openConnection();
        byte[] actual;
        try {
            actual = IOUtils.toByteArray(urlConn);
        } finally {
            IOUtils.close(urlConn);
        }
        Assert.assertEquals(FILE_SIZE, actual.length);
    }

    public void testToCharArray_InputStream() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);
        try {
            final char[] out = IOUtils.toCharArray(fin);
            assertNotNull(out);
            assertEquals("Not all chars were read", 0, fin.available());
            assertEquals("Wrong output size", FILE_SIZE, out.length);
            assertEqualContent(out, m_testFile);
        } finally {
            fin.close();
        }
    }

    public void testToCharArray_InputStream_CharsetName() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);
        try {
            final char[] out = IOUtils.toCharArray(fin, "UTF-8");
            assertNotNull(out);
            assertEquals("Not all chars were read", 0, fin.available());
            assertEquals("Wrong output size", FILE_SIZE, out.length);
            assertEqualContent(out, m_testFile);
        } finally {
            fin.close();
        }
    }
    
    public void testToCharArray_Reader() throws Exception {
        final FileReader fr = new FileReader(m_testFile);
        try {
            final char[] out = IOUtils.toCharArray(fr);
            assertNotNull(out);
            assertEquals("Wrong output size", FILE_SIZE, out.length);
            assertEqualContent(out, m_testFile);
        } finally {
            fr.close();
        }
    }
    
    /**
     * Test for {@link IOUtils#toInputStream(CharSequence)} and {@link IOUtils#toInputStream(CharSequence, String)}.
     * Note, this test utilizes on {@link IOUtils#toByteArray(java.io.InputStream)} and so relies on
     * {@link #testToByteArray_InputStream()} to ensure this method functions correctly.
     * 
     * @throws Exception
     *             on error
     */
    public void testToInputStream_CharSequence() throws Exception {
        final CharSequence csq = new StringBuilder("Abc123Xyz!");
        InputStream inStream = IOUtils.toInputStream(csq);
        byte[] bytes = IOUtils.toByteArray(inStream);
        assertEqualContent(csq.toString().getBytes(), bytes);
        inStream = IOUtils.toInputStream(csq, (String) null);
        bytes = IOUtils.toByteArray(inStream);
        assertEqualContent(csq.toString().getBytes(), bytes);
        inStream = IOUtils.toInputStream(csq, "UTF-8");
        bytes = IOUtils.toByteArray(inStream);
        assertEqualContent(csq.toString().getBytes("UTF-8"), bytes);
    }

    // Tests from IO-305
    
    /**
     * Test for {@link IOUtils#toInputStream(String)} and {@link IOUtils#toInputStream(String, String)}. Note, this test
     * utilizes on {@link IOUtils#toByteArray(java.io.InputStream)} and so relies on
     * {@link #testToByteArray_InputStream()} to ensure this method functions correctly.
     * 
     * @throws Exception
     *             on error
     */
    public void testToInputStream_String() throws Exception {
        final String str = "Abc123Xyz!";
        InputStream inStream = IOUtils.toInputStream(str);
        byte[] bytes = IOUtils.toByteArray(inStream);
        assertEqualContent(str.getBytes(), bytes);
        inStream = IOUtils.toInputStream(str, (String) null);
        bytes = IOUtils.toByteArray(inStream);
        assertEqualContent(str.getBytes(), bytes);
        inStream = IOUtils.toInputStream(str, "UTF-8");
        bytes = IOUtils.toByteArray(inStream);
        assertEqualContent(str.getBytes("UTF-8"), bytes);
    }
    
    @SuppressWarnings("deprecation")
    // testing deprecated method
    public void testToString_ByteArray() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);
        try {
            final byte[] in = IOUtils.toByteArray(fin);
            // Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
            final String str = IOUtils.toString(in);
            assertEqualContent(in, str.getBytes());
        } finally {
            fin.close();
        }
    }

    public void testToString_InputStream() throws Exception {
        final FileInputStream fin = new FileInputStream(m_testFile);
        try {
            final String out = IOUtils.toString(fin);
            assertNotNull(out);
            assertEquals("Not all bytes were read", 0, fin.available());
            assertEquals("Wrong output size", FILE_SIZE, out.length());
        } finally {
            fin.close();
        }
    }

    public void testToString_Reader() throws Exception {
        final FileReader fin = new FileReader(m_testFile);
        try {
            final String out = IOUtils.toString(fin);
            assertNotNull(out);
            assertEquals("Wrong output size", FILE_SIZE, out.length());
        } finally {
            fin.close();
        }
    }

    public void testToString_URI() throws Exception {
        final URI url = m_testFile.toURI();
        final String out = IOUtils.toString(url);
        assertNotNull(out);
        assertEquals("Wrong output size", FILE_SIZE, out.length());
    }

    private void testToString_URI(final String encoding) throws Exception {
        final URI uri = m_testFile.toURI();
        final String out = IOUtils.toString(uri, encoding);
        assertNotNull(out);
        assertEquals("Wrong output size", FILE_SIZE, out.length());
    }

    public void testToString_URI_CharsetName() throws Exception {
        testToString_URI("US-ASCII");
    }

    public void testToString_URI_CharsetNameNull() throws Exception {
        testToString_URI(null);
    }

    public void testToString_URL() throws Exception {
        final URL url = m_testFile.toURI().toURL();
        final String out = IOUtils.toString(url);
        assertNotNull(out);
        assertEquals("Wrong output size", FILE_SIZE, out.length());
    }

    private void testToString_URL(final String encoding) throws Exception {
        final URL url = m_testFile.toURI().toURL();
        final String out = IOUtils.toString(url, encoding);
        assertNotNull(out);
        assertEquals("Wrong output size", FILE_SIZE, out.length());
    }

    public void testToString_URL_CharsetName() throws Exception {
        testToString_URL("US-ASCII");
    }

    public void testToString_URL_CharsetNameNull() throws Exception {
        testToString_URL(null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8858.java