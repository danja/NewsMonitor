/*
 * Copyright 2018 danny.
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
package it.danja.newsmonitor.interpreters;

import it.danja.newsmonitor.model.Link;
import it.danja.newsmonitor.model.impl.LinkImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author danny
 * 
 * total fudge
 */
public class GenericHandler extends FeedHandlerBase {

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
       Link link = new LinkImpl();
       link.setHref(uri);
       this.getFeed().addLink(link);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
