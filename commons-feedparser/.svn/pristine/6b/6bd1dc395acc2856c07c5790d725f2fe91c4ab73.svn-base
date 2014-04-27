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

package org.apache.commons.feedparser.network;

import java.util.EventListener;

/**
 * An AggregationEventListener provides event dispatch from the core Aggregation
 * class to implementors of the AggregationEventListener interface.
 * 
 * Note that all NetworkEventListeners should be threadsafe.
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id$
 */
public interface NetworkEventListener extends EventListener {

    /**
     * Called once per request.
     *
     * 
     */
    public void init( DataEvent event );
    
    public void dataEvent( DataEvent event );

    /**
     * Called when this stream is closed.
     */
    public void onClosed();
    
}
