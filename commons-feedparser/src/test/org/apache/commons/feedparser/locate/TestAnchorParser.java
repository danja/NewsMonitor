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
package org.apache.commons.feedparser.locate;

import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.feedparser.network.ResourceRequest;
import org.apache.commons.feedparser.network.ResourceRequestFactory;

/**
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 */
public class TestAnchorParser extends TestCase {

    public TestAnchorParser( String name ) {
        super( name );
    }
    
    private void doTest( int linkCount, String resource ) throws Exception {

        ResourceRequest request = ResourceRequestFactory.getResourceRequest( resource );
        String content = request.getInputStreamAsString();

        TestAnchorParserListener listener = new TestAnchorParserListener();

        AnchorParser.parse( content, listener );

        System.out.println( "Found N anchors: " + listener.anchorCount );

        assertEquals( linkCount, listener.anchorCount );

    }
    
    private void assertAnchorCount( int linkCount, String content ) throws Exception {

        TestAnchorParserListener listener = new TestAnchorParserListener();

        AnchorParser.parse( content, listener );

        assertEquals( linkCount, listener.anchorCount );
        
    }

    public void testGetAttributes() {

        Map map = DiscoveryLocator.getAttributes( "<a rel=\"linux\" href=\"http://peerfear.org\" title=\"linux\" >\n\nadf</a>" );

        assertNotNull( map.get( "rel" ) );
        
    }
    
    public void testBasic() throws Exception {

        assertAnchorCount( 1, "<a rel=\"linux\" href=\"http://peerfearadsf.org/?thisisit\" title=\"linuxfoo\" >\n\nadf</a>" );

        assertAnchorCount( 1, "<a rel=\"linux\" href=\"http://peerfear.org\" title=\"linux\" >adf</a>" );

        assertAnchorCount( 1, "<a href=\"http://peerfear.org\" rel='linux' title='linux' >adf</a>" );

        assertAnchorCount( 1, "<a href='http://peerfear.org' rel='linux' title='linux' >adf</a>" );

        assertAnchorCount( 1,  "<a title=\"linux\" rel=\"linux\" href=\"http://peerfear.org\" >adf</a>" );

        assertAnchorCount( 1,  "<a href=\"http://peerfear.org\" rel=\"linux\" title=\"linux\" >adf</a>" );

        //FIXME: this won't work because it has an image
        assertAnchorCount( 1, "<a href='mailto:burton@rojo.com' rel='linux' title='linux' ><img src='' /></a>" );

        //FIXME: what about unit tests which have multiple lines ?

    }

    public void testContent() throws Exception {

        doTest( 1, "file:tests/anchor/anchor4.html" );
        doTest( 1, "file:tests/anchor/anchor1.html" );
        doTest( 1, "file:tests/anchor/anchor2.html" );
        doTest( 1, "file:tests/anchor/anchor3.html" );
        doTest( 418, "file:tests/anchor/anchor5.html" );

        //FIXME: don't find anchors in comments.
        //doTest( 0, "file:tests/anchor/anchor6.html" );
        
    }
    
    public static void main( String[] args ) throws Exception {

        //FIXME: won't work with <a />

        TestAnchorParser test = new TestAnchorParser( null );

        test.testBasic();
        test.testGetAttributes();
        test.testContent();
        
    }

}

class TestAnchorParserListener implements AnchorParserListener {

    public int anchorCount;
    
    public boolean onAnchor( String href, String rel, String title ) {
        
        ++anchorCount;

        System.out.println( "href: " + href );
        
        return true;
    }
    
    public Object getResult() { return null; }
    public void setContext( Object context ) {}
    
}
    
