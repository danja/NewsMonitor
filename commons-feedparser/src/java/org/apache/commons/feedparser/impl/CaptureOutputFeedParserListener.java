/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.feedparser.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Code which can run a parse but capture the output to a string to verify
 * certain methods were called via greping the output.  This is ONLY for unit
 * tests.
 * 
 * When done you can just call toString() to get the output of all the events
 * and then grep across it.
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: CaptureOutputFeedParserListener.java 373622 2006-01-30 22:53:00Z mvdb $
 */
public class CaptureOutputFeedParserListener
    extends DebugFeedParserListener {

    public CaptureOutputFeedParserListener() {
        super( new CapturePrintStream() );
    }

    public String toString() {

        CapturePrintStream cps = (CapturePrintStream)out;
        ByteArrayOutputStream bos = (ByteArrayOutputStream)cps.getOutputStream();

        return bos.toString();
        
    }
    
}

class CapturePrintStream extends PrintStream {

    public CapturePrintStream() {
        super( new ByteArrayOutputStream() );
    }

    public OutputStream getOutputStream() {
        return out;
    }
    
}