error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8693.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8693.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8693.java
text:
```scala
i@@f (lineLength > 0 && pos > 0) {

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

package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Provides Base64 encoding and decoding as defined by RFC 2045.
 * 
 * <p>
 * This class implements section <cite>6.8. Base64 Content-Transfer-Encoding</cite> from RFC 2045 <cite>Multipurpose
 * Internet Mail Extensions (MIME) Part One: Format of Internet Message Bodies</cite> by Freed and Borenstein.
 * </p>
 * 
 * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>
 * @author Apache Software Foundation
 * @since 1.0-dev
 * @version $Id$
 */
public class Base64 implements BinaryEncoder, BinaryDecoder {
    /**
     * Chunk size per RFC 2045 section 6.8.
     * 
     * <p>
     * The {@value} character limit does not count the trailing CRLF, but counts all other characters, including any
     * equal signs.
     * </p>
     * 
     * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045 section 6.8</a>
     */
    static final int CHUNK_SIZE = 76;

    /**
     * Chunk separator per RFC 2045 section 2.1.
     * 
     * @see <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045 section 2.1</a>
     */
    static final byte[] CHUNK_SEPARATOR = {'\r','\n'};

    /**
     * This array is a lookup table that translates 6-bit positive integer
     * index values into their "Base64 Alphabet" equivalents as specified
     * in Table 1 of RFC 2045.
     *
     * Thanks to "commons" project in ws.apache.org for this code. 
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     */
    private static final byte[] STANDARD_ENCODE_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };

    /**
     * This is a copy of the STANDARD_ENCODE_TABLE above, but with + and /
     * changed to - and _ to make the encoded Base64 results more URL-SAFE.
     * This table is only used when the Base64's mode is set to URL-SAFE.
     */    
    private static final byte[] URL_SAFE_ENCODE_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };

    /**
     * Byte used to pad output.
     */
    private static final byte PAD = '=';

    /**
     * This array is a lookup table that translates Unicode characters
     * drawn from the "Base64 Alphabet" (as specified in Table 1 of RFC 2045)
     * into their 6-bit positive integer equivalents.  Characters that
     * are not in the Base64 alphabet but fall within the bounds of the
     * array are translated to -1.
     *
     * Note:  '+' and '-' both decode to 62.  '/' and '_' both decode to 63.
     * This means decoder seamlessly handles both URL_SAFE and STANDARD base64.
     * (The encoder, on the other hand, needs to know ahead of time what to emit).
     *
     * Thanks to "commons" project in ws.apache.org for this code.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/ 
     */
    private static final byte[] DECODE_TABLE = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54,
            55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4,
            5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
            24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34,
            35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
    };

    /** Mask used to extract 6 bits, used when encoding */
    private static final int MASK_6BITS = 0x3f;

    /** Mask used to extract 8 bits, used in decoding base64 bytes */
    private static final int MASK_8BITS = 0xff;

    // The static final fields above are used for the original static byte[] methods on Base64.
    // The private member fields below are used with the new streaming approach, which requires
    // some state be preserved between calls of encode() and decode().

    /**
     * Encode table to use:  either STANDARD or URL_SAFE.  Note:  the DECODE_TABLE above remains static
     * because it is able to decode both STANDARD and URL_SAFE streams, but the encodeTable must be a member
     * variable so we can switch between the two modes.
     */
    private final byte[] encodeTable;

    /**
     * Line length for encoding.  Not used when decoding.  A value of zero or less implies
     * no chunking of the base64 encoded data.
     */
    private final int lineLength;

    /**
     * Line separator for encoding.  Not used when decoding.  Only used if lineLength > 0.
     */
    private final byte[] lineSeparator;

    /**
     * Convenience variable to help us determine when our buffer is going to run out of
     * room and needs resizing.  <code>decodeSize = 3 + lineSeparator.length;</code>
     */
    private final int decodeSize;

    /**
     * Convenience variable to help us determine when our buffer is going to run out of
     * room and needs resizing.  <code>encodeSize = 4 + lineSeparator.length;</code>
     */
    private final int encodeSize;

    /**
     * Buffer for streaming. 
     */
    private byte[] buf;

    /**
     * Position where next character should be written in the buffer.
     */
    private int pos;

    /**
     * Position where next character should be read from the buffer.
     */
    private int readPos;

    /**
     * Variable tracks how many characters have been written to the current line.
     * Only used when encoding.  We use it to make sure each encoded line never
     * goes beyond lineLength (if lineLength > 0).
     */
    private int currentLinePos;

    /**
     * Writes to the buffer only occur after every 3 reads when encoding, an
     * every 4 reads when decoding.  This variable helps track that.
     */
    private int modulus;

    /**
     * Boolean flag to indicate the EOF has been reached.  Once EOF has been
     * reached, this Base64 object becomes useless, and must be thrown away.
     */
    private boolean eof;

    /**
     * Place holder for the 3 bytes we're dealing with for our base64 logic.
     * Bitwise operations store and extract the base64 encoding or decoding from
     * this variable.
     */
    private int x;

    /**
     * Sets state for decoding and encoding.
     * <p>
     * When encoding the line length is 76, the line separator is CRLF, and we use the STANDARD_ENCODE_TABLE.
     * </p>
     * 
     * <p>
     * When decoding all variants can be decoded.
     * </p>
     */
    public Base64() {
        this(false);
    }

    /**
     * Same as default constructor (line length is 76, line separator is CRLF), but URL-SAFE mode for encoding is
     * supplied.
     * 
     * When decoding: all variants can be decoded.
     * 
     * @param urlSafe
     *            true if URL-SAFE encoding should be performed. In most situations this should be set to false.
     */
    public Base64(boolean urlSafe) {
        this(CHUNK_SIZE, CHUNK_SEPARATOR, urlSafe);
    }

    /**
     * <p>
     * Sets the line length when encoding (line separator is still CRLF). All forms of data can be decoded.
     * </p>
     * <p>
     * Note: line lengths that aren't multiples of 4 will still essentially end up being multiples of 4 in the encoded
     * data.
     * </p>
     * 
     * @param lineLength
     *            each line of encoded data will be at most this long (rounded up to nearest multiple of 4). If
     *            lineLength <= 0, then the output will not be divided into lines (chunks). Ignored when decoding.
     */
    public Base64(int lineLength) {
        this(lineLength, CHUNK_SEPARATOR);
    }

    /**
     * <p>
     * Sets the line length and line separator when encoding. All forms of data can be decoded.
     * </p>
     * <p>
     * Note: line lengths that aren't multiples of 4 will still essentially end up being multiples of 4 in the encoded
     * data.
     * </p>
     * 
     * @param lineLength
     *            Each line of encoded data will be at most this long (rounded up to nearest multiple of 4). Ignored
     *            when decoding. If <= 0, then output will not be divided into lines (chunks).
     * @param lineSeparator
     *            Each line of encoded data will end with this sequence of bytes.
     * @throws IllegalArgumentException
     *             The provided lineSeparator included some base64 characters. That's not going to work!
     */
    public Base64(int lineLength, byte[] lineSeparator) {
        this(lineLength, lineSeparator, false);
    }

    /**
     * <p>
     * Consumer can use this constructor to choose a different lineLength,
     * lineSeparator, and whether to use URL-SAFE mode when encoding.
     * All forms of data can be decoded.
     * </p><p>
     * Note:  lineLengths that aren't multiples of 4 will still essentially
     * end up being multiples of 4 in the encoded data.
     * </p>
     * @param lineLength    Each line of encoded data will be at most this long
     *                      (rounded up to nearest multiple of 4).  Ignored when decoding.
     *                      If <= 0, then output will not be divided into lines (chunks).
     * @param lineSeparator Each line of encoded data will end with this
     *                      sequence of bytes.
     *                      If lineLength <= 0, then the lineSeparator is not used.
     * @param urlSafe       Instead of emitting '+' and '/' we emit '-' and '_' respectively.
     *                      urlSafe is only applied to "encode" operations.  Decoding seamlessly
     *                      handles both modes.
     *
     * @throws IllegalArgumentException The provided lineSeparator included
     *                                  some base64 characters.  That's not going to work!
     */
    public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe) {
        this.lineLength = lineLength;
        this.lineSeparator = new byte[lineSeparator.length];
        System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
        if (lineLength > 0) {
            this.encodeSize = 4 + lineSeparator.length;
        } else {
            this.encodeSize = 4;
        }
        this.decodeSize = this.encodeSize - 1;
        if (containsBase64Byte(lineSeparator)) {
            String sep;
            try {
                sep = new String(lineSeparator, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                sep = new String(lineSeparator);
            }
            throw new IllegalArgumentException("lineSeperator must not contain base64 characters: [" + sep + "]");
        }
        this.encodeTable = urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
    }

    /**
     * Returns our current encode mode. True if we're URL-SAFE, false otherwise.
     * 
     * @return true if we're in URL-SAFE mode, false otherwise.
     */
    public boolean isUrlSafe() {
        return this.encodeTable == URL_SAFE_ENCODE_TABLE;
    }

    /**
     * Returns true if this Base64 object has buffered data for reading.
     * 
     * @return true if there is Base64 object still available for reading.
     */
    boolean hasData() {
        return this.buf != null;
    }

    /**
     * Returns the amount of buffered data available for reading.
     * 
     * @return The amount of buffered data available for reading.
     */
    int avail() {
        return buf != null ? pos - readPos : 0;
    }

    /** Doubles our buffer. */
    private void resizeBuf() {
        if (buf == null) {
            buf = new byte[8192];
            pos = 0;
            readPos = 0;
        } else {
            byte[] b = new byte[buf.length * 2];
            System.arraycopy(buf, 0, b, 0, buf.length);
            buf = b;
        }
    }

    /**
     * Extracts buffered data into the provided byte[] array, starting
     * at position bPos, up to a maximum of bAvail bytes.  Returns how
     * many bytes were actually extracted.
     *
     * @param b      byte[] array to extract the buffered data into.
     * @param bPos   position in byte[] array to start extraction at.
     * @param bAvail amount of bytes we're allowed to extract.  We may extract
     *               fewer (if fewer are available).
     * @return The number of bytes successfully extracted into the provided
     *         byte[] array.
     */
    int readResults(byte[] b, int bPos, int bAvail) {
        if (buf != null) {
            int len = Math.min(avail(), bAvail);
            if (buf != b) {
                System.arraycopy(buf, readPos, b, bPos, len);
                readPos += len;
                if (readPos >= pos) {
                    buf = null;
                }
            } else {
                // Re-using the original consumer's output array is only
                // allowed for one round.
                buf = null;
            }
            return len;
        }
        return eof ? -1 : 0;
    }

    /**
     * Sets the streaming buffer. This is a small optimization where we try to buffer directly to the consumer's output
     * array for one round (if the consumer calls this method first) instead of starting our own buffer.
     * 
     * @param out byte[] array to buffer directly to.
     * @param outPos Position to start buffering into.
     * @param outAvail Amount of bytes available for direct buffering.
     */
    void setInitialBuffer(byte[] out, int outPos, int outAvail) {
        // We can re-use consumer's original output array under
        // special circumstances, saving on some System.arraycopy().
        if (out != null && out.length == outAvail) {
            buf = out;
            pos = outPos;
            readPos = outPos;
        }
    }

    /**
     * <p>
     * Encodes all of the provided data, starting at inPos, for inAvail bytes.
     * Must be called at least twice:  once with the data to encode, and once
     * with inAvail set to "-1" to alert encoder that EOF has been reached,
     * so flush last remaining bytes (if not multiple of 3).
     * </p><p>
     * Thanks to "commons" project in ws.apache.org for the bitwise operations,
     * and general approach.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     * </p>
     *
     * @param in byte[] array of binary data to base64 encode.
     * @param inPos Position to start reading data from.
     * @param inAvail Amount of bytes available from input for encoding.
     */
    void encode(byte[] in, int inPos, int inAvail) {
        if (eof) {
            return;
        }
        // inAvail < 0 is how we're informed of EOF in the underlying data we're
        // encoding.
        if (inAvail < 0) {
            eof = true;
            if (buf == null || buf.length - pos < encodeSize) {
                resizeBuf();
            }
            switch (modulus) {
                case 1:
                    buf[pos++] = encodeTable[(x >> 2) & MASK_6BITS];
                    buf[pos++] = encodeTable[(x << 4) & MASK_6BITS];
                    // URL-SAFE skips the padding to further reduce size.
                    if (encodeTable == STANDARD_ENCODE_TABLE) {
                        buf[pos++] = PAD;
                        buf[pos++] = PAD;
                    }
                    break;

                case 2:
                    buf[pos++] = encodeTable[(x >> 10) & MASK_6BITS];
                    buf[pos++] = encodeTable[(x >> 4) & MASK_6BITS];
                    buf[pos++] = encodeTable[(x << 2) & MASK_6BITS];
                    // URL-SAFE skips the padding to further reduce size.
                    if (encodeTable == STANDARD_ENCODE_TABLE) {
                        buf[pos++] = PAD;
                    }
                    break;
            }
            if (lineLength > 0) {
                System.arraycopy(lineSeparator, 0, buf, pos, lineSeparator.length);
                pos += lineSeparator.length;
            }
        } else {
            for (int i = 0; i < inAvail; i++) {
                if (buf == null || buf.length - pos < encodeSize) {
                    resizeBuf();
                }
                modulus = (++modulus) % 3;
                int b = in[inPos++];
                if (b < 0) { b += 256; }
                x = (x << 8) + b;
                if (0 == modulus) {
                    buf[pos++] = encodeTable[(x >> 18) & MASK_6BITS];
                    buf[pos++] = encodeTable[(x >> 12) & MASK_6BITS];
                    buf[pos++] = encodeTable[(x >> 6) & MASK_6BITS];
                    buf[pos++] = encodeTable[x & MASK_6BITS];
                    currentLinePos += 4;
                    if (lineLength > 0 && lineLength <= currentLinePos) {
                        System.arraycopy(lineSeparator, 0, buf, pos, lineSeparator.length);
                        pos += lineSeparator.length;
                        currentLinePos = 0;
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Decodes all of the provided data, starting at inPos, for inAvail bytes.
     * Should be called at least twice:  once with the data to decode, and once
     * with inAvail set to "-1" to alert decoder that EOF has been reached.
     * The "-1" call is not necessary when decoding, but it doesn't hurt, either.
     * </p><p>
     * Ignores all non-base64 characters.  This is how chunked (e.g. 76 character)
     * data is handled, since CR and LF are silently ignored, but has implications
     * for other bytes, too.  This method subscribes to the garbage-in, garbage-out
     * philosophy:  it will not check the provided data for validity.
     * </p><p>
     * Thanks to "commons" project in ws.apache.org for the bitwise operations,
     * and general approach.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     * </p>

     * @param in byte[] array of ascii data to base64 decode.
     * @param inPos Position to start reading data from.
     * @param inAvail Amount of bytes available from input for encoding.
     */    
    void decode(byte[] in, int inPos, int inAvail) {
        if (eof) {
            return;
        }
        if (inAvail < 0) {
            eof = true;
        }
        for (int i = 0; i < inAvail; i++) {
            if (buf == null || buf.length - pos < decodeSize) {
                resizeBuf();
            }
            byte b = in[inPos++];
            if (b == PAD) {
                // WE'RE DONE!!!!
                eof = true;
                break;
            } else {
                if (b >= 0 && b < DECODE_TABLE.length) {
                    int result = DECODE_TABLE[b];
                    if (result >= 0) {
                        modulus = (++modulus) % 4;
                        x = (x << 6) + result;
                        if (modulus == 0) {
                            buf[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                            buf[pos++] = (byte) ((x >> 8) & MASK_8BITS);
                            buf[pos++] = (byte) (x & MASK_8BITS);
                        }
                    }
                }
            }
        }

        // Two forms of EOF as far as base64 decoder is concerned:  actual
        // EOF (-1) and first time '=' character is encountered in stream.
        // This approach makes the '=' padding characters completely optional.
        if (eof && modulus != 0) {
            x = x << 6;
            switch (modulus) {
                case 2:
                    x = x << 6;
                    buf[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                    break;
                case 3:
                    buf[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                    buf[pos++] = (byte) ((x >> 8) & MASK_8BITS);
                    break;
            }
        }
    }

    /**
     * Returns whether or not the <code>octet</code> is in the base 64 alphabet.
     * 
     * @param octet
     *            The value to test
     * @return <code>true</code> if the value is defined in the the base 64 alphabet, <code>false</code> otherwise.
     */
    public static boolean isBase64(byte octet) {
        return octet == PAD || (octet >= 0 && octet < DECODE_TABLE.length && DECODE_TABLE[octet] != -1);
    }

    /**
     * Tests a given byte array to see if it contains only valid characters within the Base64 alphabet.
     * Currently the method treats whitespace as valid.
     * 
     * @param arrayOctet
     *            byte array to test
     * @return <code>true</code> if all bytes are valid characters in the Base64 alphabet or if the byte array is
     *         empty; false, otherwise
     */
    public static boolean isArrayByteBase64(byte[] arrayOctet) {
        for (int i = 0; i < arrayOctet.length; i++) {
            if (!isBase64(arrayOctet[i]) && !isWhiteSpace(arrayOctet[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests a given byte array to see if it contains only valid characters within the Base64 alphabet.
     * 
     * @param arrayOctet
     *            byte array to test
     * @return <code>true</code> if any byte is a valid character in the Base64 alphabet; false herwise
     */
    private static boolean containsBase64Byte(byte[] arrayOctet) {
        for (int i = 0; i < arrayOctet.length; i++) {
            if (isBase64(arrayOctet[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Encodes binary data using the base64 algorithm but does not chunk the output.
     * 
     * @param binaryData
     *            binary data to encode
     * @return Base64 characters
     */
    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    /**
     * Encodes binary data using a url-safe variation of the base64 algorithm but does not chunk the output.
     * The url-safe variation emits - and _ instead of + and / characters.
     *
     * @param binaryData
     *            binary data to encode
     * @return Base64 characters
     */
    public static byte[] encodeBase64URLSafe(byte[] binaryData) {
        return encodeBase64(binaryData, false, true);
    }
    

    /**
     * Encodes binary data using the base64 algorithm and chunks the encoded output into 76 character blocks
     * 
     * @param binaryData
     *            binary data to encode
     * @return Base64 characters chunked in 76 character blocks
     */
    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return encodeBase64(binaryData, true);
    }

    /**
     * Decodes an Object using the base64 algorithm. This method is provided in order to satisfy the requirements of the
     * Decoder interface, and will throw a DecoderException if the supplied object is not of type byte[].
     * 
     * @param pObject
     *            Object to decode
     * @return An object (of type byte[]) containing the binary data which corresponds to the byte[] supplied.
     * @throws DecoderException
     *             if the parameter supplied is not of type byte[]
     */
    public Object decode(Object pObject) throws DecoderException {
        if (!(pObject instanceof byte[])) {
            throw new DecoderException("Parameter supplied to Base64 decode is not a byte[]");
        }
        return decode((byte[]) pObject);
    }

    /**
     * Decodes a byte[] containing containing characters in the Base64 alphabet.
     * 
     * @param pArray
     *            A byte array containing Base64 character data
     * @return a byte array containing binary data
     */
    public byte[] decode(byte[] pArray) {
        return decodeBase64(pArray);
    }

    /**
     * Encodes binary data using the base64 algorithm, optionally chunking the output into 76 character blocks.
     * 
     * @param binaryData
     *            Array containing binary data to encode.
     * @param isChunked
     *            if <code>true</code> this encoder will chunk the base64 output into 76 character blocks
     * @return Base64-encoded data.
     * @throws IllegalArgumentException
     *             Thrown when the input array needs an output array bigger than {@link Integer#MAX_VALUE}
     */
    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        return encodeBase64(binaryData, isChunked, false);
    }

    /**
     * Encodes binary data using the base64 algorithm, optionally chunking the output into 76 character blocks.
     *
     * @param binaryData
     *            Array containing binary data to encode.
     * @param isChunked
     *            if <code>true</code> this encoder will chunk the base64 output into 76 character blocks
     * @param urlSafe
     *            if <code>true</code> this encoder will emit - and _ instead of the usual + and / characters.
     * @return Base64-encoded data.
     * @throws IllegalArgumentException
     *             Thrown when the input array needs an output array bigger than {@link Integer#MAX_VALUE}
     */
    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
        if (binaryData == null || binaryData.length == 0) {
            return binaryData;
        }
        Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
        long len = (binaryData.length * 4) / 3;
        long mod = len % 4;
        if (mod != 0) {
            len += 4 - mod;
        }
        if (isChunked) {
            len += (1 + (len / CHUNK_SIZE)) * CHUNK_SEPARATOR.length;
        }
        if (len > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "Input array too big, output array would be bigger than Integer.MAX_VALUE=" + Integer.MAX_VALUE);
        }
        byte[] buf = new byte[(int) len];
        b64.setInitialBuffer(buf, 0, buf.length);
        b64.encode(binaryData, 0, binaryData.length);
        b64.encode(binaryData, 0, -1); // Notify encoder of EOF.
        // Encoder might have resized, even though it was unnecessary.
        if (b64.buf != buf) {
            b64.readResults(buf, 0, buf.length);
        }
        // In URL-SAFE mode we skip the padding characters, so sometimes our
        // final length is a bit smaller.
        if (urlSafe && b64.pos < buf.length) {
            byte[] smallerBuf = new byte[b64.pos];
            System.arraycopy(buf, 0, smallerBuf, 0, b64.pos);
            buf = smallerBuf;
        }
        return buf;
    }

    /**
     * Decodes Base64 data into octets
     *
     * @param base64Data Byte array containing Base64 data
     * @return Array containing decoded data.
     */
    public static byte[] decodeBase64(byte[] base64Data) {
        if (base64Data == null || base64Data.length == 0) {
            return base64Data;
        }
        Base64 b64 = new Base64();
        long len = (base64Data.length * 3) / 4;
        byte[] buf = new byte[(int) len];
        b64.setInitialBuffer(buf, 0, buf.length);
        b64.decode(base64Data, 0, base64Data.length);
        b64.decode(base64Data, 0, -1); // Notify decoder of EOF.
        // We have no idea what the line-length was, so we
        // cannot know how much of our array wasn't used.
        byte[] result = new byte[b64.pos];
        b64.readResults(result, 0, result.length);
        return result;
    }

    /**
     * Discards any whitespace from a base-64 encoded block.
     * 
     * @param data
     *            The base-64 encoded data to discard the whitespace from.
     * @return The data, less whitespace (see RFC 2045).
     * @deprecated This method is no longer needed
     */
    static byte[] discardWhitespace(byte[] data) {
        byte groomedData[] = new byte[data.length];
        int bytesCopied = 0;
        for (int i = 0; i < data.length; i++) {
            switch (data[i]) {
                case ' ' :
                case '\n' :
                case '\r' :
                case '\t' :
                    break;
                default :
                    groomedData[bytesCopied++] = data[i];
            }
        }
        byte packedData[] = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }


    /**
     * Checks if a byte value is whitespace or not.
     * 
     * @param byteToCheck
     *            the byte to check
     * @return true if byte is whitespace, false otherwise
     */
    private static boolean isWhiteSpace(byte byteToCheck) {
        switch (byteToCheck) {
            case ' ' :
            case '\n' :
            case '\r' :
            case '\t' :
                return true;
            default :
                return false;
        }
    }

    /**
     * Discards any characters outside of the base64 alphabet, per the requirements on page 25 of RFC 2045 - "Any
     * characters outside of the base64 alphabet are to be ignored in base64 encoded data."
     * 
     * @param data
     *            The base-64 encoded data to groom
     * @return The data, less non-base64 characters (see RFC 2045).
     */
    static byte[] discardNonBase64(byte[] data) {
        byte groomedData[] = new byte[data.length];
        int bytesCopied = 0;
        for (int i = 0; i < data.length; i++) {
            if (isBase64(data[i])) {
                groomedData[bytesCopied++] = data[i];
            }
        }
        byte packedData[] = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    // Implementation of the Encoder Interface

    /**
     * Encodes an Object using the base64 algorithm. This method is provided in order to satisfy the requirements of the
     * Encoder interface, and will throw an EncoderException if the supplied object is not of type byte[].
     * 
     * @param pObject
     *            Object to encode
     * @return An object (of type byte[]) containing the base64 encoded data which corresponds to the byte[] supplied.
     * @throws EncoderException
     *             if the parameter supplied is not of type byte[]
     */
    public Object encode(Object pObject) throws EncoderException {
        if (!(pObject instanceof byte[])) {
            throw new EncoderException("Parameter supplied to Base64 encode is not a byte[]");
        }
        return encode((byte[]) pObject);
    }

    /**
     * Encodes a byte[] containing binary data, into a byte[] containing characters in the Base64 alphabet.
     * 
     * @param pArray
     *            a byte array containing binary data
     * @return A byte array containing only Base64 character data
     */
    public byte[] encode(byte[] pArray) {
        return encodeBase64(pArray, false, isUrlSafe());
    }

    // Implementation of integer encoding used for crypto
    /**
     * Decodes a byte64-encoded integer according to crypto
     * standards such as W3C's XML-Signature
     * 
     * @param pArray a byte array containing base64 character data
     * @return A BigInteger
     */
    public static BigInteger decodeInteger(byte[] pArray) {
        return new BigInteger(1, decodeBase64(pArray));
    }

    /**
     * Encodes to a byte64-encoded integer according to crypto
     * standards such as W3C's XML-Signature
     * 
     * @param bigInt a BigInteger
     * @return A byte array containing base64 character data
     * @throws NullPointerException if null is passed in
     */
    public static byte[] encodeInteger(BigInteger bigInt) {
        if(bigInt == null)  {
            throw new NullPointerException("encodeInteger called with null parameter");
        }
        return encodeBase64(toIntegerBytes(bigInt), false);
    }

    /**
     * Returns a byte-array representation of a <code>BigInteger</code>
     * without sign bit.
     *
     * @param bigInt <code>BigInteger</code> to be converted
     * @return a byte array representation of the BigInteger parameter
     */
     static byte[] toIntegerBytes(BigInteger bigInt) {
        int bitlen = bigInt.bitLength();
        // round bitlen
        bitlen = ((bitlen + 7) >> 3) << 3;
        byte[] bigBytes = bigInt.toByteArray();

        if(((bigInt.bitLength() % 8) != 0) &&
            (((bigInt.bitLength() / 8) + 1) == (bitlen / 8))) {
            return bigBytes;
        }
        // set up params for copying everything but sign bit
        int startSrc = 0;
        int len = bigBytes.length;

        // if bigInt is exactly byte-aligned, just skip signbit in copy
        if((bigInt.bitLength() % 8) == 0) {
            startSrc = 1;
            len--;
        }
        int startDst = bitlen / 8 - len; // to pad w/ nulls as per spec
        byte[] resizedBytes = new byte[bitlen / 8];
        System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
        return resizedBytes;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8693.java