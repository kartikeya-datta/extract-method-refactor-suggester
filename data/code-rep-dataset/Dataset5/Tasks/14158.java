public final class DTDImpl extends XMLEventImpl implements DTD {

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

package org.apache.xerces.stax.events;

import java.util.List;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.DTD;

/**
 * 
 * @author Lucian Holland
 *
 * @version $Id$
 */
public class DTDImpl extends XMLEventImpl implements DTD {

    private final String fDtd;
    
    /**
     * Constructor.
     */
    public DTDImpl(final String dtd, final Location location) {
        super(XMLStreamConstants.DTD, location);
        fDtd = dtd;
    }
    
    /**
     * @see javax.xml.stream.events.DTD#getDocumentTypeDeclaration()
     */
    public String getDocumentTypeDeclaration() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see javax.xml.stream.events.DTD#getProcessedDTD()
     */
    public Object getProcessedDTD() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see javax.xml.stream.events.DTD#getNotations()
     */
    public List getNotations() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see javax.xml.stream.events.DTD#getEntities()
     */
    public List getEntities() {
        // TODO Auto-generated method stub
        return null;
    }

}