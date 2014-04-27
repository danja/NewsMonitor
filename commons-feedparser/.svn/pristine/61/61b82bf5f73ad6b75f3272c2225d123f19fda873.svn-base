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

package org.apache.commons.feedparser.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.TestCase;

import org.apache.commons.feedparser.FeedList;
import org.apache.commons.feedparser.locate.BlogServiceDiscovery;
import org.apache.commons.feedparser.locate.FeedLocator;
import org.apache.commons.feedparser.locate.FeedReference;
import org.apache.commons.feedparser.locate.ProbeLocator;
import org.apache.commons.feedparser.locate.blogservice.AOLJournal;
import org.apache.commons.feedparser.locate.blogservice.BlogService;
import org.apache.commons.feedparser.locate.blogservice.Blogger;
import org.apache.commons.feedparser.locate.blogservice.Blosxom;
import org.apache.commons.feedparser.locate.blogservice.GreyMatter;
import org.apache.commons.feedparser.locate.blogservice.LiveJournal;
import org.apache.commons.feedparser.locate.blogservice.PMachine;
import org.apache.commons.feedparser.locate.blogservice.RadioUserland;
import org.apache.commons.feedparser.locate.blogservice.TextAmerica;
import org.apache.commons.feedparser.locate.blogservice.TextPattern;
import org.apache.commons.feedparser.locate.blogservice.Typepad;
import org.apache.commons.feedparser.locate.blogservice.Unknown;
import org.apache.commons.feedparser.locate.blogservice.WordPress;
import org.apache.commons.feedparser.locate.blogservice.Xanga;

/**
 *
 * @author <a href="mailto:bkn3@columbia.edu">Brad Neuberg</a>
 * @version $Id$
 */
public class TestProbeLocator extends TestCase {
    public static boolean NO_ATOM_FEED = false;
    public static boolean HAS_ATOM_FEED = true;
    
    public static boolean NO_RSS_FEED = false;
    public static boolean HAS_RSS_FEED = true;
    
    public TestProbeLocator(String name) throws Exception {
        super(name);
        ProbeLocator.AGGRESIVE_PROBING_ENABLED = true;
        ProbeLocator.BLOG_SERVICE_PROBING_ENABLED = true;
    }
    
    public static void main( String[] args ) throws Exception {
        TestProbeLocator test = new TestProbeLocator( null );

        test.testBlogger();
        test.testLiveJournal();
        test.testDiaryLand();
        test.testMovableType();
        test.testXanga();
        test.testWordPress();
        test.testAOLJournal();
        test.testTypePad();
        test.testGreyMatter();
        test.testPMachine();
        //test.testBlosxom();
        test.testRadioUserland();
        test.testTextPattern();
        test.testTextAmerica();
    }

    public void testBlogger() throws Exception {   
        System.out.println("\nTesting Blogger...");
        testSite( "http://edpro.blogspot.com/",
                  new Blogger(),
                  1, 
                  new String[] { FeedReference.ATOM_MEDIA_TYPE },
                  new String[] { "http://edpro.blogspot.com/atom.xml" },
                  HAS_ATOM_FEED,
                  "http://edpro.blogspot.com/atom.xml",
                  NO_RSS_FEED,
                  null );
        
        testSite("http://carolinascl.blogspot.com/", new Blogger(), 1, 
                 new String[] { FeedReference.ATOM_MEDIA_TYPE },
                 new String[] { "http://carolinascl.blogspot.com/atom.xml" },
                 HAS_ATOM_FEED, "http://carolinascl.blogspot.com/atom.xml", 
                 NO_RSS_FEED, null);
        
        testSite("http://azizindia.blogspot.com/", new Blogger(), 1, 
                 new String[] { FeedReference.ATOM_MEDIA_TYPE },
                 new String[] { "http://azizindia.blogspot.com/atom.xml" },
                 HAS_ATOM_FEED, "http://azizindia.blogspot.com/atom.xml", 
                 NO_RSS_FEED, null);
        
        // This site has no blogs
        testSite("http://davebarry.blogspot.com/", new Blogger(), 0, 
                 new String[] { },
                 new String[] { },
                 NO_ATOM_FEED, null, 
                 NO_RSS_FEED, null);
    }

    public void testLiveJournal() throws Exception {
       System.out.println("\nTesting LiveJournal...");
       testSite("http://www.livejournal.com/community/indiexiankids/", 
                new LiveJournal(), 2, 
                new String[] { 
                               FeedReference.ATOM_MEDIA_TYPE, 
                               FeedReference.RSS_MEDIA_TYPE 
                             },
                new String[] { 
                               "http://www.livejournal.com/community/indiexiankids/data/atom",
                               "http://www.livejournal.com/community/indiexiankids/data/rss"
                             },
                HAS_ATOM_FEED, "http://www.livejournal.com/community/indiexiankids/data/atom",
                HAS_RSS_FEED, "http://www.livejournal.com/community/indiexiankids/data/rss");

       testSite("http://www.livejournal.com/community/ajoyforever/", 
                new LiveJournal(), 2, 
                new String[] { 
                               FeedReference.ATOM_MEDIA_TYPE, 
                               FeedReference.RSS_MEDIA_TYPE 
                             },
                new String[] { 
                               "http://www.livejournal.com/community/ajoyforever/data/atom",
                               "http://www.livejournal.com/community/ajoyforever/data/rss"
                             },
                HAS_ATOM_FEED, "http://www.livejournal.com/community/ajoyforever/data/atom",
                HAS_RSS_FEED, "http://www.livejournal.com/community/ajoyforever/data/rss");

        testSite("http://www.livejournal.com/users/_jb_/12332.html", 
                 new LiveJournal(), 2, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE, 
                                FeedReference.RSS_MEDIA_TYPE 
                              },
                 new String[] { 
                                "http://www.livejournal.com/users/_jb_/data/atom",
                                "http://www.livejournal.com/users/_jb_/data/rss"
                              },
                 HAS_ATOM_FEED, "http://www.livejournal.com/users/_jb_/data/atom",
                 HAS_RSS_FEED, "http://www.livejournal.com/users/_jb_/data/rss");
    }

    public void testDiaryLand() throws Exception {
        System.out.println("\nTesting DiaryLand... No tests currently");
        // FIXME: Test this
    }

    public void testMovableType() throws Exception {
        System.out.println("\nTesting MovableType... No tests currently");
        // FIXME: Test this
    }

    public void testXanga() throws Exception {
        System.out.println("\nTesting Xanga...");
        testSite("http://www.xanga.com/home.aspx?user=lithium98", 
                 new Xanga(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://www.xanga.com/rss.aspx?user=lithium98" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://www.xanga.com/rss.aspx?user=lithium98");

        testSite("http://www.xanga.com/home.aspx?user=ChUnSA_86", 
                 new Xanga(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://www.xanga.com/rss.aspx?user=ChUnSA_86" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://www.xanga.com/rss.aspx?user=ChUnSA_86");
        
        testSite("http://www.xanga.com/home.aspx?user=wdfphillz", 
                 new Xanga(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://www.xanga.com/rss.aspx?user=wdfphillz" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://www.xanga.com/rss.aspx?user=wdfphillz");

        // FIXME: We should be able to pass this test when we
        // expand resources inside of the Feed Parser; we don't
        // currently do this yet, Brad Neuberg, bkn3@columbia.edu
        /*testSite("http://xanga.com/home.aspx?user=joe", 
                 new Xanga(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://www.xanga.com/rss.aspx?user=joe" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://www.xanga.com/rss.aspx?user=joe");*/
    }

    public void testWordPress() throws Exception {
        System.out.println("\nTesting WordPress...");
        // This site went down
        /*testSite("http://zh.yazzy.org/blog/index.php", 
                 new WordPress(), 3, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE, 
                                FeedReference.RSS_MEDIA_TYPE,
                                FeedReference.RSS_MEDIA_TYPE 
                              },
                 new String[] { 
                                "http://synflood.at/blog/wp-atom.php",
                                "http://synflood.at/blog/wp-rss2.php",
                                "http://synflood.at/blog/wp-rss.php"
                              },
                 HAS_ATOM_FEED, "http://synflood.at/blog/wp-atom.php",
                 HAS_RSS_FEED, "http://synflood.at/blog/wp-rss2.php");
        */
        // We need to firm up our autodiscovery regular expressions before
        // this will pass; what happens is it has autodiscovery, but those
        // point to a different location than the usual ones aggresive
        // discovery would find (which are also there).  We get back different
        // results at different times
        /*testSite("http://zh.yazzy.org/blog/index.php", 
                 new WordPress(), 3, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE, 
                                FeedReference.RSS_MEDIA_TYPE,
                                FeedReference.RSS_MEDIA_TYPE 
                              },
                 new String[] { 
                                "http://zh.yazzy.org/blog/wp-atom.php",
                                "http://zh.yazzy.org/blog/wp-rss2.php",
                                "http://zh.yazzy.org/blog/wp-rss.php"
                              },
                 HAS_ATOM_FEED, "http://zh.yazzy.org/blog/wp-atom.php",
                 HAS_RSS_FEED, "http://zh.yazzy.org/blog/wp-rss2.php");
        */
        testSite("http://holmes.hgen.pitt.edu/~dweeks/wordpress/", 
                 new WordPress(), 3, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE, 
                                FeedReference.RSS_MEDIA_TYPE,
                                FeedReference.RSS_MEDIA_TYPE 
                              },
                 new String[] { 
                                "http://holmes.hgen.pitt.edu/~dweeks/wordpress/wp-atom.php",
                                "http://holmes.hgen.pitt.edu/~dweeks/wordpress/wp-rss2.php",
                                "http://holmes.hgen.pitt.edu/~dweeks/wordpress/wp-rss.php"
                              },
                 HAS_ATOM_FEED, "http://holmes.hgen.pitt.edu/~dweeks/wordpress/wp-atom.php",
                 HAS_RSS_FEED, "http://holmes.hgen.pitt.edu/~dweeks/wordpress/wp-rss2.php");
    }

    public void testAOLJournal() throws Exception {
        System.out.println("\nTesting AOL Journal...");
        /* AOL recently turned on experimental Atom support, but it is still 
           buggy; it turns out we can "see" it through autodiscovery but not
           through aggresive link probing, since their server returns a 500
           HTTP internal server error if we do a HEAD request.  For this
           reason we have to divide our testing between the probe locator
           and feed locator because they give different results for this
           kind of blog service currently.
        */
        testProbeLocator(
                 "http://journals.aol.com/redhdka/BrandNewDay/", 
                 new AOLJournal(), 1, 
                 new String[] { 
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                     "http://journals.aol.com/redhdka/BrandNewDay/rss.xml" 
                              },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://journals.aol.com/redhdka/BrandNewDay/rss.xml");
        testFeedLocator(
                 "http://journals.aol.com/redhdka/BrandNewDay/", 
                 new AOLJournal(), 2, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE,
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://journals.aol.com/redhdka/BrandNewDay/atom.xml",
                                "http://journals.aol.com/redhdka/BrandNewDay/rss.xml" 
                              },
                 HAS_ATOM_FEED, "http://journals.aol.com/redhdka/BrandNewDay/atom.xml",
                 HAS_RSS_FEED, "http://journals.aol.com/redhdka/BrandNewDay/rss.xml");
        
        testProbeLocator(
                 "http://journals.aol.com/goldenchildnc/GCS/", 
                 new AOLJournal(), 1, 
                 new String[] { 
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://journals.aol.com/goldenchildnc/GCS/rss.xml" 
                              },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://journals.aol.com/goldenchildnc/GCS/rss.xml");
        testFeedLocator(
                 "http://journals.aol.com/goldenchildnc/GCS/", 
                 new AOLJournal(), 2, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE,
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://journals.aol.com/goldenchildnc/GCS/atom.xml",
                                "http://journals.aol.com/goldenchildnc/GCS/rss.xml" 
                              },
                 HAS_ATOM_FEED, "http://journals.aol.com/goldenchildnc/GCS/atom.xml",
                 HAS_RSS_FEED, "http://journals.aol.com/goldenchildnc/GCS/rss.xml");

        
        testProbeLocator(
                 "http://journals.aol.com/mkgninja/MelissasMisunderstandingsofLife/", 
                 new AOLJournal(), 1, 
                 new String[] { 
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://journals.aol.com/mkgninja/MelissasMisunderstandingsofLife/rss.xml" 
                              },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://journals.aol.com/mkgninja/MelissasMisunderstandingsofLife/rss.xml");
        testFeedLocator(
                 "http://journals.aol.com/mkgninja/MelissasMisunderstandingsofLife/", 
                 new AOLJournal(), 2, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE,
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://journals.aol.com/mkgninja/MelissasMisunderstandingsofLife/atom.xml",
                                "http://journals.aol.com/mkgninja/MelissasMisunderstandingsofLife/rss.xml" 
                              },
                 HAS_ATOM_FEED, "http://journals.aol.com/mkgninja/MelissasMisunderstandingsofLife/atom.xml",
                 HAS_RSS_FEED, "http://journals.aol.com/mkgninja/MelissasMisunderstandingsofLife/rss.xml");
    }

    public void testTypePad() throws Exception {
        System.out.println("\nTesting TypePad...");
        // This site has no feed that we can link probe for (it's in a different
        // location then usual).
        // However, we get a feed when we go through the FeedParser since
        // the site has autodiscovery
        testSite("http://lynikers.typepad.com/", 
                 new Typepad(), 0, 
                 new String[] { },
                 new String[] { },
                 HAS_ATOM_FEED, "http://lynikers.typepad.com/on_buck_lake/atom.xml",
                 HAS_RSS_FEED, "http://lynikers.typepad.com/on_buck_lake/index.rdf");

        // This site has no feed that we can link probe for (it's in a different
        // location then usual).
        testSite("http://emmeke.typepad.com/", 
                 new Typepad(), 0, 
                 new String[] { },
                 new String[] { },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://emmeke.typepad.com/blog/index.rdf");

        testSite("http://www.prettypolitical.com/", 
                 new Typepad(), 2, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE, 
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://www.prettypolitical.com/atom.xml",
                                "http://www.prettypolitical.com/index.rdf"
                              },
                 HAS_ATOM_FEED, "http://www.prettypolitical.com/atom.xml",
                 HAS_RSS_FEED, "http://www.prettypolitical.com/index.rdf");
    }

    public void testGreyMatter() throws Exception {
        System.out.println("\nTesting GreyMatter...");
        // No feeds supported
        testSite("http://www.chattbike.com/gilligan/", 
                 new GreyMatter(), 0, 
                 new String[] { },
                 new String[] { },
                 NO_ATOM_FEED, null,
                 NO_RSS_FEED, null);

        // No feeds supported
        testSite("http://www.electricedge.com/greymatter/", 
                 new GreyMatter(), 0, 
                 new String[] { },
                 new String[] { },
                 NO_ATOM_FEED, null,
                 NO_RSS_FEED, null);
    }

    public void testPMachine() throws Exception {
        System.out.println("\nTesting PMachine...");
        testSite("http://bamph.com", 
                 new Unknown(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://bamph.com/index.xml" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://bamph.com/index.xml");
        
        testSite("http://bucsfishingreport.com/pMachine/weblog.php", 
                 new PMachine(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://bucsfishingreport.com/pMachine/index.xml" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://bucsfishingreport.com/pMachine/index.xml");
        
        testSite("http://www.simplekindoflife.com/pMachine/weblog.php", 
                 new PMachine(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://www.simplekindoflife.com/pMachine/index.xml" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://www.simplekindoflife.com/pMachine/index.xml");
        
        testSite("http://www.mondfish.net/pmachine/weblog.php", 
                 new PMachine(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://www.mondfish.net/pmachine/index.xml" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://www.mondfish.net/pmachine/index.xml");
    }

    public void testBlosxom() throws Exception {
        System.out.println("\nTesting Blosxom...");
        testSite("http://mikemason.ca/", 
                 new Blosxom(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://mikemason.ca/index.rss" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://mikemason.ca/index.rss");

        // Wed Mar 02 2005 06:07 PM (burton1@rojo.com): note... this no longer
        // looks like a Bloxsom blog.  It has autodiscovery though.
        //  
        //
        //         testSite("http://www.foobargeek.com/", 
        //                  new Blosxom(), 1, 
        //                  new String[] { FeedReference.RSS_MEDIA_TYPE },
        //                  new String[] { "http://www.foobargeek.com/index.rss" },
        //                  NO_ATOM_FEED, null,
        //                  HAS_RSS_FEED, "http://www.foobargeek.com/index.rss");
        
        // The FeedParser gets a different location for the XML file then
        // through the aggresive prober for this feed

        //         testSite("http://www.pipetree.com/qmacro/", 
        //                  new Blosxom(), 1, 
        //                  new String[] { FeedReference.RSS_MEDIA_TYPE },
        //                  new String[] { "http://www.pipetree.com/qmacro/index.rss" },
        //                  NO_ATOM_FEED, null,
        //                  HAS_RSS_FEED, "http://www.pipetree.com/qmacro/xml");

        testSite("http://www.bitbucketheaven.com/cgi-bin/blosxom.cgi", 
                 new Blosxom(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://www.bitbucketheaven.com/cgi-bin/blosxom.cgi/index.rss" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://www.bitbucketheaven.com/cgi-bin/blosxom.cgi/index.rss");
    }
    
    public void testRadioUserland() throws Exception {
        System.out.println("\nTesting Radio Userland...");
        testSite("http://radio.weblogs.com/0131722/", 
                 new RadioUserland(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://radio.weblogs.com/0131722/rss.xml" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://radio.weblogs.com/0131722/rss.xml");
        
        testSite("http://radio.weblogs.com/0131724/", 
                 new RadioUserland(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://radio.weblogs.com/0131724/rss.xml" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://radio.weblogs.com/0131724/rss.xml");
        
        testSite("http://radio.weblogs.com/0131734/", 
                 new RadioUserland(), 1, 
                 new String[] { FeedReference.RSS_MEDIA_TYPE },
                 new String[] { "http://radio.weblogs.com/0131734/rss.xml" },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://radio.weblogs.com/0131734/rss.xml");
    }
    
    public void testTextPattern() throws Exception {
        System.out.println("\nTesting TextPattern...");
        testSite("http://www.digitalmediaminute.com/", 
                 new TextPattern(), 2, 
                 new String[] { 
                                FeedReference.ATOM_MEDIA_TYPE, 
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://www.digitalmediaminute.com/?atom=1",
                                "http://www.digitalmediaminute.com/?rss=1"
                              },
                 HAS_ATOM_FEED, "http://www.digitalmediaminute.com/?atom=1",
                 HAS_RSS_FEED, "http://www.digitalmediaminute.com/?rss=1");
    }
    
    public void testTextAmerica() throws Exception {
        System.out.println("\nTesting TextAmerica...");
        testSite("http://morganwebb.textamerica.com/", 
                 new TextAmerica(), 1, 
                 new String[] { 
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://morganwebb.textamerica.com/rss.aspx"
                              },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://morganwebb.textamerica.com/rss.aspx");
        
        testSite("http://northlan.textamerica.com/", 
                 new TextAmerica(), 1, 
                 new String[] { 
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://northlan.textamerica.com/rss.aspx"
                              },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://northlan.textamerica.com/rss.aspx");
        
        testSite("http://mycamphone.textamerica.com/", 
                 new TextAmerica(), 1, 
                 new String[] { 
                                FeedReference.RSS_MEDIA_TYPE
                              },
                 new String[] { 
                                "http://mycamphone.textamerica.com/rss.aspx"
                              },
                 NO_ATOM_FEED, null,
                 HAS_RSS_FEED, "http://mycamphone.textamerica.com/rss.aspx");
    }

    /** Grabs all the content for a weblog for testing purposes. */
    protected String getContent(String resource) throws Exception {

        //FIXME: use the IO package from NewsMonster for this.

        URL resourceURL = new URL(resource);
        URLConnection con = resourceURL.openConnection();
        con.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(con
                                        .getInputStream()));
        StringBuffer results = new StringBuffer();
        String line = null;

        while ((line = in.readLine()) != null) {
            results.append(line);
        }

        return results.toString();
    }
    
    private void testSite(String resource, BlogService correctBlogService,
                          int numberOfFeeds, String feedType[],
                          String feedURL[], boolean hasAtomFeed,
                          String atomFeedURL, boolean hasRSSFeed,
                          String rssFeedURL) throws Exception {
        System.out.println("Testing " + resource + "...");
        /* Test through the probe locator */
        testProbeLocator(resource, correctBlogService, numberOfFeeds, 
                         feedType, feedURL, hasAtomFeed,
                         atomFeedURL, hasRSSFeed, rssFeedURL);
        
        /* Test through the FeedLocator */
        testFeedLocator(resource, correctBlogService, numberOfFeeds, 
                        feedType, feedURL, hasAtomFeed,
                        atomFeedURL, hasRSSFeed, rssFeedURL);
    }
    
    private FeedList testProbeLocator(String resource, BlogService correctBlogService,
                                      int numberOfFeeds, String feedType[],
                                      String feedURL[], boolean hasAtomFeed,
                                      String atomFeedURL, boolean hasRSSFeed,
                                      String rssFeedURL) throws Exception {
        String content = getContent(resource);
        assertNotNull(content);
        
        BlogService blogService = BlogServiceDiscovery.discover(resource, content);
        assertEquals(correctBlogService, blogService);
        
        FeedList list = new FeedList();
        ProbeLocator.locate(resource, content, list);
        assertEquals(numberOfFeeds, list.size());

        FeedReference[] feeds = (FeedReference[])list.toArray(new FeedReference[list.size()]);
        assertEquals(numberOfFeeds, feeds.length);
        
        for (int i = 0; i < feeds.length; i++) {
            assertEquals(FeedReference.METHOD_PROBE_DISCOVERY, feeds[i].method);
            assertNull(null, feeds[i].title);
            assertEquals(feedType[i], feeds[i].type);
            assertEquals(feedURL[i], feeds[i].resource);
        }
        
        return list;
    }
    
    private FeedList testFeedLocator( String resource,
                                      BlogService correctBlogService,
                                      int numberOfFeeds,
                                      String feedType[],
                                      String feedURL[],
                                      boolean hasAtomFeed,
                                      String atomFeedURL,
                                      boolean hasRSSFeed,
                                      String rssFeedURL ) throws Exception {

        FeedList list = FeedLocator.locate( resource );
        FeedReference atomFeed = list.getAdAtomFeed();
        FeedReference rssFeed = list.getAdRSSFeed();
        if (hasAtomFeed) {
            assertNotNull(atomFeed);
            assertEquals(FeedReference.ATOM_MEDIA_TYPE, atomFeed.type);
            assertEquals(atomFeedURL, atomFeed.resource);
        }
        else
            assertNull(atomFeed);
        
        if (hasRSSFeed) {
            assertNotNull(rssFeed);
            assertEquals(FeedReference.RSS_MEDIA_TYPE, rssFeed.type);
            assertEquals(rssFeedURL, rssFeed.resource);
        }
        else
            assertNull(rssFeed);
        
        return list;
    }
}
