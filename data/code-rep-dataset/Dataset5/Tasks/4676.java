public static final int sizeof = COM.LICINFO_sizeof ();

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public final class LICINFO {
	public int cbLicInfo;
	public boolean fRuntimeKeyAvail;
	public boolean fLicVerified;
	public static final int sizeof = 12;
}