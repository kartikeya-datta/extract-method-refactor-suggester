error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9873.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9873.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9873.java
text:
```scala
w@@hile(getNextCharAsJavaIdentifierPart()){/*empty*/}

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.codeassist.complete;

/*
 * Scanner aware of a cursor location so as to discard trailing portions of identifiers
 * containing the cursor location.
 *
 * Cursor location denotes the position of the last character behind which completion
 * got requested:
 *  -1 means completion at the very beginning of the source
 *	0  means completion behind the first character
 *  n  means completion behind the n-th character
 */
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.internal.compiler.parser.Scanner;

public class CompletionScanner extends Scanner {

	public char[] completionIdentifier;
	public int cursorLocation;
	public int endOfEmptyToken = -1;
		
	/* Source positions of the completedIdentifier
	 * if inside actual identifier, end goes to the actual identifier 
	 * end, in other words, beyond cursor location
	 */
	public int completedIdentifierStart = 0;
	public int completedIdentifierEnd = -1;

	public static final char[] EmptyCompletionIdentifier = {};
public CompletionScanner(long sourceLevel) {
	super(
		false /*comment*/, 
		false /*whitespace*/, 
		false /*nls*/, 
		sourceLevel, 
		null /*taskTags*/, 
		null/*taskPriorities*/,
		true/*taskCaseSensitive*/);
}
/* 
 * Truncate the current identifier if it is containing the cursor location. Since completion is performed
 * on an identifier prefix.
 *
 */
public char[] getCurrentIdentifierSource() {

	if (this.completionIdentifier == null){
		if (this.cursorLocation < this.startPosition && this.currentPosition == this.startPosition){ // fake empty identifier got issued
			// remember actual identifier positions
			this.completedIdentifierStart = this.startPosition;
			this.completedIdentifierEnd = this.completedIdentifierStart - 1;			
			return this.completionIdentifier = EmptyCompletionIdentifier;					
		}
		if (this.cursorLocation+1 >= this.startPosition && this.cursorLocation < this.currentPosition){
			// remember actual identifier positions
			this.completedIdentifierStart = this.startPosition;
			this.completedIdentifierEnd = this.currentPosition - 1;
			if (this.withoutUnicodePtr != 0){			// check unicode scenario
				System.arraycopy(this.withoutUnicodeBuffer, 1, this.completionIdentifier = new char[this.withoutUnicodePtr], 0, this.withoutUnicodePtr);
			} else {
				int length = this.cursorLocation + 1 - this.startPosition;
				// no char[] sharing around completionIdentifier, we want it to be unique so as to use identity checks	
				System.arraycopy(this.source, this.startPosition, (this.completionIdentifier = new char[length]), 0, length);
			}
			return this.completionIdentifier;
		}
	}
	return super.getCurrentIdentifierSource();
}
/* 
 * Identifier splitting for unicodes.
 * Only store the current unicode if we did not pass the cursor location.
 * Note: this does not handle cases where the cursor is in the middle of a unicode
 */
public boolean getNextCharAsJavaIdentifierPart() {

	int temp = this.currentPosition;
	try {
		if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
			&& (this.source[this.currentPosition] == 'u')) {
			//-------------unicode traitement ------------
			int c1, c2, c3, c4;
			int unicodeSize = 6;
			this.currentPosition++;
			while (this.source[this.currentPosition] == 'u') {
				this.currentPosition++;
				unicodeSize++;
			}

			if (((c1 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c1 < 0)
 ((c2 = Character.getNumericValue(this.source[this.currentPosition++])) > 15 || c2 < 0)
 ((c3 = Character.getNumericValue(this.source[this.currentPosition++])) > 15 || c3 < 0)
 ((c4 = Character.getNumericValue(this.source[this.currentPosition++])) > 15 || c4 < 0)) {
				this.currentPosition = temp;
				return false;
			}

			this.currentCharacter = (char) (((c1 * 16 + c2) * 16 + c3) * 16 + c4);
			if (!Character.isJavaIdentifierPart(this.currentCharacter)) {
				this.currentPosition = temp;
				return false;
			}

			//need the unicode buffer
			if (this.withoutUnicodePtr == 0) {
				//buffer all the entries that have been left aside....
				this.withoutUnicodePtr = this.currentPosition - unicodeSize - this.startPosition;
				System.arraycopy(
					this.source, 
					this.startPosition, 
					this.withoutUnicodeBuffer, 
					1, 
					this.withoutUnicodePtr); 
			}
			if (temp < this.cursorLocation && this.cursorLocation < this.currentPosition-1){
				throw new InvalidCursorLocation(InvalidCursorLocation.NO_COMPLETION_INSIDE_UNICODE);
			}
			// store the current unicode, only if we did not pass the cursor location
			// Note: this does not handle cases where the cursor is in the middle of a unicode
			if ((this.completionIdentifier != null)
 (this.startPosition <= this.cursorLocation+1 && this.cursorLocation >= this.currentPosition-1)){
				this.withoutUnicodeBuffer[++this.withoutUnicodePtr] = this.currentCharacter;
			}
			return true;
		} //-------------end unicode traitement--------------
		else {
			if (!Character.isJavaIdentifierPart(this.currentCharacter)) {
				this.currentPosition = temp;
				return false;
			}

			if (this.withoutUnicodePtr != 0){
				// store the current unicode, only if we did not pass the cursor location
				// Note: this does not handle cases where the cursor is in the middle of a unicode
				if ((this.completionIdentifier != null)
 (this.startPosition <= this.cursorLocation+1 && this.cursorLocation >= this.currentPosition-1)){
					this.withoutUnicodeBuffer[++this.withoutUnicodePtr] = this.currentCharacter;
				}
			}
			return true;
		}
	} catch (IndexOutOfBoundsException e) {
		this.currentPosition = temp;
		return false;
	}
}
public int getNextToken() throws InvalidInputException {

	this.wasAcr = false;
	if (this.diet) {
		jumpOverMethodBody();
		this.diet = false;
		return this.currentPosition > this.source.length ? TokenNameEOF : TokenNameRBRACE;
	}
	try {
		while (true) { //loop for jumping over comments
			this.withoutUnicodePtr = 0;
			//start with a new token (even comment written with unicode )

			// ---------Consume white space and handles start position---------
			int whiteStart = this.currentPosition;
			boolean isWhiteSpace;
			do {
				this.startPosition = this.currentPosition;
				if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
					&& (this.source[this.currentPosition] == 'u')) {
					isWhiteSpace = jumpOverUnicodeWhiteSpace();
				} else {
					if (this.recordLineSeparator
						&& ((this.currentCharacter == '\r') || (this.currentCharacter == '\n')))
						pushLineSeparator();
					isWhiteSpace = 
						(this.currentCharacter == ' ') || CharOperation.isWhitespace(this.currentCharacter); 
				}
				/* completion requesting strictly inside blanks */
				if ((whiteStart != this.currentPosition)
					//&& (previousToken == TokenNameDOT)
					&& (this.completionIdentifier == null)
					&& (whiteStart <= this.cursorLocation+1)
					&& (this.cursorLocation < this.startPosition)
					&& !Character.isJavaIdentifierStart(this.currentCharacter)){
					this.currentPosition = this.startPosition; // for next token read
					return TokenNameIdentifier;
				}
			} while (isWhiteSpace);
			if (this.tokenizeWhiteSpace && (whiteStart != this.currentPosition - 1)) {
				// reposition scanner in case we are interested by spaces as tokens
				this.currentPosition--;
				this.startPosition = whiteStart;
				return TokenNameWHITESPACE;
			}
			//little trick to get out in the middle of a source computation
			if (this.currentPosition > this.eofPosition){
				/* might be completing at eof (e.g. behind a dot) */
				if (this.completionIdentifier == null && 
					this.startPosition == this.cursorLocation + 1){
					// compute end of empty identifier.
					// if the empty identifier is at the start of a next token the end of
					// empty identifier is the end of the next token (eg. "<empty token>next").
				 	while(getNextCharAsJavaIdentifierPart());
				 	this.endOfEmptyToken = this.currentPosition - 1;
					this.currentPosition = this.startPosition; // for being detected as empty free identifier
					return TokenNameIdentifier;
				}				
				return TokenNameEOF;
			}

			// ---------Identify the next token-------------

			switch (this.currentCharacter) {
				case '(' :
					return TokenNameLPAREN;
				case ')' :
					return TokenNameRPAREN;
				case '{' :
					return TokenNameLBRACE;
				case '}' :
					return TokenNameRBRACE;
				case '[' :
					return TokenNameLBRACKET;
				case ']' :
					return TokenNameRBRACKET;
				case ';' :
					return TokenNameSEMICOLON;
				case ',' :
					return TokenNameCOMMA;
				case '.' :
					if (this.startPosition <= this.cursorLocation 
					    && this.cursorLocation < this.currentPosition){
					    	return TokenNameDOT; // completion inside .<|>12
				    }
					if (getNextCharAsDigit())
						return scanNumber(true);
					return TokenNameDOT;
				case '+' :
					{
						int test;
						if ((test = getNextChar('+', '=')) == 0)
							return TokenNamePLUS_PLUS;
						if (test > 0)
							return TokenNamePLUS_EQUAL;
						return TokenNamePLUS;
					}
				case '-' :
					{
						int test;
						if ((test = getNextChar('-', '=')) == 0)
							return TokenNameMINUS_MINUS;
						if (test > 0)
							return TokenNameMINUS_EQUAL;
						return TokenNameMINUS;
					}
				case '~' :
					return TokenNameTWIDDLE;
				case '!' :
					if (getNextChar('='))
						return TokenNameNOT_EQUAL;
					return TokenNameNOT;
				case '*' :
					if (getNextChar('='))
						return TokenNameMULTIPLY_EQUAL;
					return TokenNameMULTIPLY;
				case '%' :
					if (getNextChar('='))
						return TokenNameREMAINDER_EQUAL;
					return TokenNameREMAINDER;
				case '<' :
					{
						int test;
						if ((test = getNextChar('=', '<')) == 0)
							return TokenNameLESS_EQUAL;
						if (test > 0) {
							if (getNextChar('='))
								return TokenNameLEFT_SHIFT_EQUAL;
							return TokenNameLEFT_SHIFT;
						}
						return TokenNameLESS;
					}
				case '>' :
					{
						int test;
						if ((test = getNextChar('=', '>')) == 0)
							return TokenNameGREATER_EQUAL;
						if (test > 0) {
							if ((test = getNextChar('=', '>')) == 0)
								return TokenNameRIGHT_SHIFT_EQUAL;
							if (test > 0) {
								if (getNextChar('='))
									return TokenNameUNSIGNED_RIGHT_SHIFT_EQUAL;
								return TokenNameUNSIGNED_RIGHT_SHIFT;
							}
							return TokenNameRIGHT_SHIFT;
						}
						return TokenNameGREATER;
					}
				case '=' :
					if (getNextChar('='))
						return TokenNameEQUAL_EQUAL;
					return TokenNameEQUAL;
				case '&' :
					{
						int test;
						if ((test = getNextChar('&', '=')) == 0)
							return TokenNameAND_AND;
						if (test > 0)
							return TokenNameAND_EQUAL;
						return TokenNameAND;
					}
				case '|' :
					{
						int test;
						if ((test = getNextChar('|', '=')) == 0)
							return TokenNameOR_OR;
						if (test > 0)
							return TokenNameOR_EQUAL;
						return TokenNameOR;
					}
				case '^' :
					if (getNextChar('='))
						return TokenNameXOR_EQUAL;
					return TokenNameXOR;
				case '?' :
					return TokenNameQUESTION;
				case ':' :
					return TokenNameCOLON;
				case '\'' :
					{
						int test;
						if ((test = getNextChar('\n', '\r')) == 0) {
							throw new InvalidInputException(INVALID_CHARACTER_CONSTANT);
						}
						if (test > 0) {
							// relocate if finding another quote fairly close: thus unicode '/u000D' will be fully consumed
							for (int lookAhead = 0; lookAhead < 3; lookAhead++) {
								if (this.currentPosition + lookAhead == this.source.length)
									break;
								if (this.source[this.currentPosition + lookAhead] == '\n')
									break;
								if (this.source[this.currentPosition + lookAhead] == '\'') {
									this.currentPosition += lookAhead + 1;
									break;
								}
							}
							throw new InvalidInputException(INVALID_CHARACTER_CONSTANT);
						}
					}
					if (getNextChar('\'')) {
						// relocate if finding another quote fairly close: thus unicode '/u000D' will be fully consumed
						for (int lookAhead = 0; lookAhead < 3; lookAhead++) {
							if (this.currentPosition + lookAhead == this.source.length)
								break;
							if (this.source[this.currentPosition + lookAhead] == '\n')
								break;
							if (this.source[this.currentPosition + lookAhead] == '\'') {
								this.currentPosition += lookAhead + 1;
								break;
							}
						}
						throw new InvalidInputException(INVALID_CHARACTER_CONSTANT);
					}
					if (getNextChar('\\'))
						scanEscapeCharacter();
					else { // consume next character
						this.unicodeAsBackSlash = false;
						if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
							&& (this.source[this.currentPosition] == 'u')) {
							getNextUnicodeChar();
						} else {
							if (this.withoutUnicodePtr != 0) {
								this.withoutUnicodeBuffer[++this.withoutUnicodePtr] = this.currentCharacter;
							}
						}
					}
					if (getNextChar('\''))
						return TokenNameCharacterLiteral;
					// relocate if finding another quote fairly close: thus unicode '/u000D' will be fully consumed
					for (int lookAhead = 0; lookAhead < 20; lookAhead++) {
						if (this.currentPosition + lookAhead == this.source.length)
							break;
						if (this.source[this.currentPosition + lookAhead] == '\n')
							break;
						if (this.source[this.currentPosition + lookAhead] == '\'') {
							this.currentPosition += lookAhead + 1;
							break;
						}
					}
					throw new InvalidInputException(INVALID_CHARACTER_CONSTANT);
				case '"' :
					try {
						// consume next character
						this.unicodeAsBackSlash = false;
						if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
							&& (this.source[this.currentPosition] == 'u')) {
							getNextUnicodeChar();
						} else {
							if (this.withoutUnicodePtr != 0) {
								this.withoutUnicodeBuffer[++this.withoutUnicodePtr] = this.currentCharacter;
							}
						}

						while (this.currentCharacter != '"') {
							/**** \r and \n are not valid in string literals ****/
							if ((this.currentCharacter == '\n') || (this.currentCharacter == '\r')) {
								// relocate if finding another quote fairly close: thus unicode '/u000D' will be fully consumed
								for (int lookAhead = 0; lookAhead < 50; lookAhead++) {
									if (this.currentPosition + lookAhead == this.source.length)
										break;
									if (this.source[this.currentPosition + lookAhead] == '\n')
										break;
									if (this.source[this.currentPosition + lookAhead] == '\"') {
										this.currentPosition += lookAhead + 1;
										break;
									}
								}
								throw new InvalidInputException(INVALID_CHAR_IN_STRING);
							}
							if (this.currentCharacter == '\\') {
								int escapeSize = this.currentPosition;
								boolean backSlashAsUnicodeInString = this.unicodeAsBackSlash;
								//scanEscapeCharacter make a side effect on this value and we need the previous value few lines down this one
								scanEscapeCharacter();
								escapeSize = this.currentPosition - escapeSize;
								if (this.withoutUnicodePtr == 0) {
									//buffer all the entries that have been left aside....
									this.withoutUnicodePtr = this.currentPosition - escapeSize - 1 - this.startPosition;
									System.arraycopy(
										this.source, 
										this.startPosition, 
										this.withoutUnicodeBuffer, 
										1, 
										this.withoutUnicodePtr); 
									this.withoutUnicodeBuffer[++this.withoutUnicodePtr] = this.currentCharacter;
								} else { //overwrite the / in the buffer
									this.withoutUnicodeBuffer[this.withoutUnicodePtr] = this.currentCharacter;
									if (backSlashAsUnicodeInString) { //there are TWO \ in the stream where only one is correct
										this.withoutUnicodePtr--;
									}
								}
							}
							// consume next character
							this.unicodeAsBackSlash = false;
							if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
								&& (this.source[this.currentPosition] == 'u')) {
								getNextUnicodeChar();
							} else {
								if (this.withoutUnicodePtr != 0) {
									this.withoutUnicodeBuffer[++this.withoutUnicodePtr] = this.currentCharacter;
								}
							}

						}
					} catch (IndexOutOfBoundsException e) {
						throw new InvalidInputException(UNTERMINATED_STRING);
					} catch (InvalidInputException e) {
						if (e.getMessage().equals(INVALID_ESCAPE)) {
							// relocate if finding another quote fairly close: thus unicode '/u000D' will be fully consumed
							for (int lookAhead = 0; lookAhead < 50; lookAhead++) {
								if (this.currentPosition + lookAhead == this.source.length)
									break;
								if (this.source[this.currentPosition + lookAhead] == '\n')
									break;
								if (this.source[this.currentPosition + lookAhead] == '\"') {
									this.currentPosition += lookAhead + 1;
									break;
								}
							}

						}
						throw e; // rethrow
					}
					if (this.startPosition <= this.cursorLocation && this.cursorLocation <= this.currentPosition-1){
						throw new InvalidCursorLocation(InvalidCursorLocation.NO_COMPLETION_INSIDE_STRING);
					}
					return TokenNameStringLiteral;
				case '/' :
					{
						int test;
						if ((test = getNextChar('/', '*')) == 0) { //line comment 
							try {
								//get the next char 
								this.lastCommentLinePosition = this.currentPosition;
								if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
									&& (this.source[this.currentPosition] == 'u')) {
									//-------------unicode traitement ------------
									int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
									this.currentPosition++;
									while (this.source[this.currentPosition] == 'u') {
										this.currentPosition++;
									}
									if ((c1 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c1 < 0
 (c2 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c2 < 0
 (c3 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c3 < 0
 (c4 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c4 < 0) {
										throw new InvalidInputException(INVALID_UNICODE_ESCAPE);
									} else {
										this.currentCharacter = (char) (((c1 * 16 + c2) * 16 + c3) * 16 + c4);
									}
								}

								//handle the \\u case manually into comment
								if (this.currentCharacter == '\\') {
									if (this.source[this.currentPosition] == '\\')
										this.currentPosition++;
								} //jump over the \\
								while (this.currentCharacter != '\r' && this.currentCharacter != '\n') {
									//get the next char 
									this.lastCommentLinePosition = this.currentPosition;
									if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
										&& (this.source[this.currentPosition] == 'u')) {
										//-------------unicode traitement ------------
										int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
										this.currentPosition++;
										while (this.source[this.currentPosition] == 'u') {
											this.currentPosition++;
										}
										if ((c1 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c1 < 0
 (c2 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c2 < 0
 (c3 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c3 < 0
 (c4 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c4 < 0) {
											throw new InvalidInputException(INVALID_UNICODE_ESCAPE);
										} else {
											this.currentCharacter = (char) (((c1 * 16 + c2) * 16 + c3) * 16 + c4);
										}
									}
									//handle the \\u case manually into comment
									if (this.currentCharacter == '\\') {
										if (this.source[this.currentPosition] == '\\')
											this.currentPosition++;
									} //jump over the \\
								}
								recordComment(TokenNameCOMMENT_LINE);
								if (this.startPosition <= this.cursorLocation && this.cursorLocation < this.currentPosition-1){
									throw new InvalidCursorLocation(InvalidCursorLocation.NO_COMPLETION_INSIDE_COMMENT);
								}
								if (this.recordLineSeparator && ((this.currentCharacter == '\r') || (this.currentCharacter == '\n'))) {
									pushLineSeparator();
								}
								if (this.tokenizeComments) {
									this.currentPosition--; // reset one character behind
									return TokenNameCOMMENT_LINE;
								}
							} catch (IndexOutOfBoundsException e) {
								recordComment(TokenNameCOMMENT_LINE);
								if (this.taskTags != null) checkTaskTag(this.startPosition, this.currentPosition-1);
								if (this.tokenizeComments) {
									this.currentPosition--; // reset one character behind
									return TokenNameCOMMENT_LINE;
								}
							}
							break;
						}
						if (test > 0) { //traditional and javadoc comment
							boolean isJavadoc = false, star = false;
							// consume next character
							this.unicodeAsBackSlash = false;
							if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
								&& (this.source[this.currentPosition] == 'u')) {
								getNextUnicodeChar();
							} else {
								if (this.withoutUnicodePtr != 0) {
									this.withoutUnicodeBuffer[++this.withoutUnicodePtr] = this.currentCharacter;
								}
							}

							if (this.currentCharacter == '*') {
								isJavadoc = true;
								star = true;
							}
							if (this.recordLineSeparator
								&& ((this.currentCharacter == '\r') || (this.currentCharacter == '\n')))
								pushLineSeparator();
							try { //get the next char 
								if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
									&& (this.source[this.currentPosition] == 'u')) {
									//-------------unicode traitement ------------
									int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
									this.currentPosition++;
									while (this.source[this.currentPosition] == 'u') {
										this.currentPosition++;
									}
									if ((c1 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c1 < 0
 (c2 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c2 < 0
 (c3 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c3 < 0
 (c4 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c4 < 0) {
										throw new InvalidInputException(INVALID_UNICODE_ESCAPE);
									} else {
										this.currentCharacter = (char) (((c1 * 16 + c2) * 16 + c3) * 16 + c4);
									}
								}
								//handle the \\u case manually into comment
								if (this.currentCharacter == '\\') {
									if (this.source[this.currentPosition] == '\\')
										this.currentPosition++;
								} //jump over the \\
								// empty comment is not a javadoc /**/
								if (this.currentCharacter == '/') { 
									isJavadoc = false;
								}
								//loop until end of comment */ 
								while ((this.currentCharacter != '/') || (!star)) {
									if (this.recordLineSeparator
										&& ((this.currentCharacter == '\r') || (this.currentCharacter == '\n')))
										pushLineSeparator();
									star = this.currentCharacter == '*';
									//get next char
									if (((this.currentCharacter = this.source[this.currentPosition++]) == '\\')
										&& (this.source[this.currentPosition] == 'u')) {
										//-------------unicode traitement ------------
										int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
										this.currentPosition++;
										while (this.source[this.currentPosition] == 'u') {
											this.currentPosition++;
										}
										if ((c1 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c1 < 0
 (c2 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c2 < 0
 (c3 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c3 < 0
 (c4 = Character.getNumericValue(this.source[this.currentPosition++])) > 15
 c4 < 0) {
											throw new InvalidInputException(INVALID_UNICODE_ESCAPE);
										} else {
											this.currentCharacter = (char) (((c1 * 16 + c2) * 16 + c3) * 16 + c4);
										}
									}
									//handle the \\u case manually into comment
									if (this.currentCharacter == '\\') {
										if (this.source[this.currentPosition] == '\\')
											this.currentPosition++;
									} //jump over the \\
								}
								int token = isJavadoc ? TokenNameCOMMENT_JAVADOC : TokenNameCOMMENT_BLOCK;
								recordComment(token);
								if (this.startPosition <= this.cursorLocation && this.cursorLocation < this.currentPosition-1){
									throw new InvalidCursorLocation(InvalidCursorLocation.NO_COMPLETION_INSIDE_COMMENT);
								}
								if (this.tokenizeComments) {
									/*
									if (isJavadoc)
										return TokenNameCOMMENT_JAVADOC;
									return TokenNameCOMMENT_BLOCK;
									*/
									return token;
								}
							} catch (IndexOutOfBoundsException e) {
								throw new InvalidInputException(UNTERMINATED_COMMENT);
							}
							break;
						}
						if (getNextChar('='))
							return TokenNameDIVIDE_EQUAL;
						return TokenNameDIVIDE;
					}
				case '\u001a' :
					if (atEnd())
						return TokenNameEOF;
					//the atEnd may not be <this.currentPosition == this.source.length> if source is only some part of a real (external) stream
					throw new InvalidInputException("Ctrl-Z"); //$NON-NLS-1$

				default :
					if (Character.isJavaIdentifierStart(this.currentCharacter))
						return scanIdentifierOrKeyword();
					if (Character.isDigit(this.currentCharacter))
						return scanNumber(false);
					return TokenNameERROR;
			}
		}
	} //-----------------end switch while try--------------------
	catch (IndexOutOfBoundsException e) {
		// eof reached
	}
	/* might be completing at very end of file (e.g. behind a dot) */
	if (this.completionIdentifier == null && 
		this.startPosition == this.cursorLocation + 1){
		this.currentPosition = this.startPosition; // for being detected as empty free identifier
		return TokenNameIdentifier;
	}
	return TokenNameEOF;
}
/*
 * In case we actually read a keyword, but the cursor is located inside,
 * we pretend we read an identifier.
 */
public int scanIdentifierOrKeyword() {

	int id = super.scanIdentifierOrKeyword();

	// convert completed keyword into an identifier
	if (id != TokenNameIdentifier
		&& this.startPosition <= this.cursorLocation+1 
		&& this.cursorLocation < this.currentPosition){
		return TokenNameIdentifier;
	}
	return id;
}
public int scanNumber(boolean dotPrefix) throws InvalidInputException {
	
	int token = super.scanNumber(dotPrefix);

	// consider completion just before a number to be ok, will insert before it
	if (this.startPosition <= this.cursorLocation && this.cursorLocation < this.currentPosition){  
		throw new InvalidCursorLocation(InvalidCursorLocation.NO_COMPLETION_INSIDE_NUMBER);
	}
	return token;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9873.java