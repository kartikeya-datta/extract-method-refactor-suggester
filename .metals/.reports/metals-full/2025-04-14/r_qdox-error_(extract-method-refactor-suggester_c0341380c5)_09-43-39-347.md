error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1451.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1451.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1451.java
text:
```scala
t@@hrow new IllegalArgumentException("faulty slice operation: bra=" + bra + ",ket=" + ket + ",limit=" + limit);

/*

Copyright (c) 2001, Dr Martin Porter
Copyright (c) 2002, Richard Boulton
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
    * this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
    * notice, this list of conditions and the following disclaimer in the
    * documentation and/or other materials provided with the distribution.
    * Neither the name of the copyright holders nor the names of its contributors
    * may be used to endorse or promote products derived from this software
    * without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */


package org.tartarus.snowball;

import java.lang.reflect.InvocationTargetException;

import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.RamUsageEstimator;

/**
 * This is the rev 502 of the Snowball SVN trunk,
 * but modified:
 * made abstract and introduced abstract method stem to avoid expensive reflection in filter class.
 * refactored StringBuffers to StringBuilder
 * uses char[] as buffer instead of StringBuffer/StringBuilder
 * eq_s,eq_s_b,insert,replace_s take CharSequence like eq_v and eq_v_b
 * reflection calls (Lovins, etc) use EMPTY_ARGS/EMPTY_PARAMS
 */
public abstract class SnowballProgram {
    private static final Object[] EMPTY_ARGS = new Object[0];

    protected SnowballProgram()
    {
	current = new char[8];
	setCurrent("");
    }

    public abstract boolean stem();

    /**
     * Set the current string.
     */
    public void setCurrent(String value)
    {
	current = value.toCharArray();
	cursor = 0;
	limit = value.length();
	limit_backward = 0;
	bra = cursor;
	ket = limit;
    }

    /**
     * Get the current string.
     */
    public String getCurrent()
    {
      return new String(current, 0, limit);
    }
    
    /**
     * Set the current string.
     * @param text character array containing input
     * @param length valid length of text.
     */
    public void setCurrent(char text[], int length) {
      current = text;
      cursor = 0;
      limit = length;
      limit_backward = 0;
      bra = cursor;
      ket = limit;
    }

    /**
     * Get the current buffer containing the stem.
     * <p>
     * NOTE: this may be a reference to a different character array than the
     * one originally provided with setCurrent, in the exceptional case that 
     * stemming produced a longer intermediate or result string. 
     * </p>
     * <p>
     * It is necessary to use {@link #getCurrentBufferLength()} to determine
     * the valid length of the returned buffer. For example, many words are
     * stemmed simply by subtracting from the length to remove suffixes.
     * </p>
     * @see #getCurrentBufferLength()
     */
    public char[] getCurrentBuffer() {
      return current;
    }
    
    /**
     * Get the valid length of the character array in 
     * {@link #getCurrentBuffer()}. 
     * @return valid length of the array.
     */
    public int getCurrentBufferLength() {
      return limit;
    }

    // current string
    private char current[];

    protected int cursor;
    protected int limit;
    protected int limit_backward;
    protected int bra;
    protected int ket;

    protected void copy_from(SnowballProgram other)
    {
	current          = other.current;
	cursor           = other.cursor;
	limit            = other.limit;
	limit_backward   = other.limit_backward;
	bra              = other.bra;
	ket              = other.ket;
    }

    protected boolean in_grouping(char [] s, int min, int max)
    {
	if (cursor >= limit) return false;
	char ch = current[cursor];
	if (ch > max || ch < min) return false;
	ch -= min;
	if ((s[ch >> 3] & (0X1 << (ch & 0X7))) == 0) return false;
	cursor++;
	return true;
    }

    protected boolean in_grouping_b(char [] s, int min, int max)
    {
	if (cursor <= limit_backward) return false;
	char ch = current[cursor - 1];
	if (ch > max || ch < min) return false;
	ch -= min;
	if ((s[ch >> 3] & (0X1 << (ch & 0X7))) == 0) return false;
	cursor--;
	return true;
    }

    protected boolean out_grouping(char [] s, int min, int max)
    {
	if (cursor >= limit) return false;
	char ch = current[cursor];
	if (ch > max || ch < min) {
	    cursor++;
	    return true;
	}
	ch -= min;
	if ((s[ch >> 3] & (0X1 << (ch & 0X7))) == 0) {
	    cursor ++;
	    return true;
	}
	return false;
    }

    protected boolean out_grouping_b(char [] s, int min, int max)
    {
	if (cursor <= limit_backward) return false;
	char ch = current[cursor - 1];
	if (ch > max || ch < min) {
	    cursor--;
	    return true;
	}
	ch -= min;
	if ((s[ch >> 3] & (0X1 << (ch & 0X7))) == 0) {
	    cursor--;
	    return true;
	}
	return false;
    }

    protected boolean in_range(int min, int max)
    {
	if (cursor >= limit) return false;
	char ch = current[cursor];
	if (ch > max || ch < min) return false;
	cursor++;
	return true;
    }

    protected boolean in_range_b(int min, int max)
    {
	if (cursor <= limit_backward) return false;
	char ch = current[cursor - 1];
	if (ch > max || ch < min) return false;
	cursor--;
	return true;
    }

    protected boolean out_range(int min, int max)
    {
	if (cursor >= limit) return false;
	char ch = current[cursor];
	if (!(ch > max || ch < min)) return false;
	cursor++;
	return true;
    }

    protected boolean out_range_b(int min, int max)
    {
	if (cursor <= limit_backward) return false;
	char ch = current[cursor - 1];
	if(!(ch > max || ch < min)) return false;
	cursor--;
	return true;
    }

    protected boolean eq_s(int s_size, CharSequence s)
    {
	if (limit - cursor < s_size) return false;
	int i;
	for (i = 0; i != s_size; i++) {
	    if (current[cursor + i] != s.charAt(i)) return false;
	}
	cursor += s_size;
	return true;
    }

    protected boolean eq_s_b(int s_size, CharSequence s)
    {
	if (cursor - limit_backward < s_size) return false;
	int i;
	for (i = 0; i != s_size; i++) {
	    if (current[cursor - s_size + i] != s.charAt(i)) return false;
	}
	cursor -= s_size;
	return true;
    }

    protected boolean eq_v(CharSequence s)
    {
	return eq_s(s.length(), s);
    }

    protected boolean eq_v_b(CharSequence s)
    {   return eq_s_b(s.length(), s);
    }

    protected int find_among(Among v[], int v_size)
    {
	int i = 0;
	int j = v_size;

	int c = cursor;
	int l = limit;

	int common_i = 0;
	int common_j = 0;

	boolean first_key_inspected = false;

	while(true) {
	    int k = i + ((j - i) >> 1);
	    int diff = 0;
	    int common = common_i < common_j ? common_i : common_j; // smaller
	    Among w = v[k];
	    int i2;
	    for (i2 = common; i2 < w.s_size; i2++) {
		if (c + common == l) {
		    diff = -1;
		    break;
		}
		diff = current[c + common] - w.s[i2];
		if (diff != 0) break;
		common++;
	    }
	    if (diff < 0) {
		j = k;
		common_j = common;
	    } else {
		i = k;
		common_i = common;
	    }
	    if (j - i <= 1) {
		if (i > 0) break; // v->s has been inspected
		if (j == i) break; // only one item in v

		// - but now we need to go round once more to get
		// v->s inspected. This looks messy, but is actually
		// the optimal approach.

		if (first_key_inspected) break;
		first_key_inspected = true;
	    }
	}
	while(true) {
	    Among w = v[i];
	    if (common_i >= w.s_size) {
		cursor = c + w.s_size;
		if (w.method == null) return w.result;
		boolean res;
		try {
		    Object resobj = w.method.invoke(w.methodobject, EMPTY_ARGS);
		    res = resobj.toString().equals("true");
		} catch (InvocationTargetException e) {
		    res = false;
		    // FIXME - debug message
		} catch (IllegalAccessException e) {
		    res = false;
		    // FIXME - debug message
		}
		cursor = c + w.s_size;
		if (res) return w.result;
	    }
	    i = w.substring_i;
	    if (i < 0) return 0;
	}
    }

    // find_among_b is for backwards processing. Same comments apply
    protected int find_among_b(Among v[], int v_size)
    {
	int i = 0;
	int j = v_size;

	int c = cursor;
	int lb = limit_backward;

	int common_i = 0;
	int common_j = 0;

	boolean first_key_inspected = false;

	while(true) {
	    int k = i + ((j - i) >> 1);
	    int diff = 0;
	    int common = common_i < common_j ? common_i : common_j;
	    Among w = v[k];
	    int i2;
	    for (i2 = w.s_size - 1 - common; i2 >= 0; i2--) {
		if (c - common == lb) {
		    diff = -1;
		    break;
		}
		diff = current[c - 1 - common] - w.s[i2];
		if (diff != 0) break;
		common++;
	    }
	    if (diff < 0) {
		j = k;
		common_j = common;
	    } else {
		i = k;
		common_i = common;
	    }
	    if (j - i <= 1) {
		if (i > 0) break;
		if (j == i) break;
		if (first_key_inspected) break;
		first_key_inspected = true;
	    }
	}
	while(true) {
	    Among w = v[i];
	    if (common_i >= w.s_size) {
		cursor = c - w.s_size;
		if (w.method == null) return w.result;

		boolean res;
		try {
		    Object resobj = w.method.invoke(w.methodobject, EMPTY_ARGS);
		    res = resobj.toString().equals("true");
		} catch (InvocationTargetException e) {
		    res = false;
		    // FIXME - debug message
		} catch (IllegalAccessException e) {
		    res = false;
		    // FIXME - debug message
		}
		cursor = c - w.s_size;
		if (res) return w.result;
	    }
	    i = w.substring_i;
	    if (i < 0) return 0;
	}
    }

    /* to replace chars between c_bra and c_ket in current by the
     * chars in s.
     */
    protected int replace_s(int c_bra, int c_ket, CharSequence s)
    {
	final int adjustment = s.length() - (c_ket - c_bra);
	final int newLength = limit + adjustment;
	//resize if necessary
	if (newLength > current.length) {
	  char newBuffer[] = new char[ArrayUtil.oversize(newLength, RamUsageEstimator.NUM_BYTES_CHAR)];
	  System.arraycopy(current, 0, newBuffer, 0, limit);
	  current = newBuffer;
	}
	// if the substring being replaced is longer or shorter than the
	// replacement, need to shift things around
	if (adjustment != 0 && c_ket < limit) {
	  System.arraycopy(current, c_ket, current, c_bra + s.length(), 
	      limit - c_ket);
	}
	// insert the replacement text
	// Note, faster is s.getChars(0, s.length(), current, c_bra);
	// but would have to duplicate this method for both String and StringBuilder
	for (int i = 0; i < s.length(); i++)
	  current[c_bra + i] = s.charAt(i);
	
	limit += adjustment;
	if (cursor >= c_ket) cursor += adjustment;
	else if (cursor > c_bra) cursor = c_bra;
	return adjustment;
    }

    protected void slice_check()
    {
	if (bra < 0 ||
	    bra > ket ||
	    ket > limit)
	{
	    System.err.println("faulty slice operation");
	// FIXME: report error somehow.
	/*
	    fprintf(stderr, "faulty slice operation:\n");
	    debug(z, -1, 0);
	    exit(1);
	    */
	}
    }

    protected void slice_from(CharSequence s)
    {
	slice_check();
	replace_s(bra, ket, s);
    }
 
    protected void slice_del()
    {
	slice_from((CharSequence)"");
    }

    protected void insert(int c_bra, int c_ket, CharSequence s)
    {
	int adjustment = replace_s(c_bra, c_ket, s);
	if (c_bra <= bra) bra += adjustment;
	if (c_bra <= ket) ket += adjustment;
    }

    /* Copy the slice into the supplied StringBuffer */
    protected StringBuilder slice_to(StringBuilder s)
    {
	slice_check();
	int len = ket - bra;
	s.setLength(0);
	s.append(current, bra, len);
	return s;
    }

    protected StringBuilder assign_to(StringBuilder s)
    {
	s.setLength(0);
	s.append(current, 0, limit);
	return s;
    }

/*
extern void debug(struct SN_env * z, int number, int line_count)
{   int i;
    int limit = SIZE(z->p);
    //if (number >= 0) printf("%3d (line %4d): '", number, line_count);
    if (number >= 0) printf("%3d (line %4d): [%d]'", number, line_count,limit);
    for (i = 0; i <= limit; i++)
    {   if (z->lb == i) printf("{");
        if (z->bra == i) printf("[");
        if (z->c == i) printf("|");
        if (z->ket == i) printf("]");
        if (z->l == i) printf("}");
        if (i < limit)
        {   int ch = z->p[i];
            if (ch == 0) ch = '#';
            printf("%c", ch);
        }
    }
    printf("'\n");
}
*/

};


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1451.java