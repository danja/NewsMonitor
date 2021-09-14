# devlog 2021

**2021-09-14**

_Having a look after a very long time, hoping to deploy on hyperdata.it_

Had to do a bunch of Maven updates to get it to build (the standalone version).

Now seems to be some divergence from the Fuseki server setup I had last time and the current version.

When doing the initial insert, to add list of feeds, server is giving :

401 Unauthorised

Then when reading the list of feeds (via SPARQL) getting an XML parser error -

[Fatal Error] :1:1: Content is not allowed in prolog.

- which suggests it ain't getting XML

hee hee :

SparqlConnector.java, line 128

    	 if(statusCode == 401) {
    		 request.addHeader("Authorization", "Basic YWRtaW46YWRtaW4=");

Heh, the config wasn't consistent with notes, it needed a dataset called newsmonitor.

A bit closer, it was spinning tyres until crash so added a

    	try {
    		TimeUnit.SECONDS.sleep(1);
    	} catch (InterruptedException e2) {
    		// TODO Auto-generated catch block
    		e2.printStackTrace();
    	}

into SparqlConnector.

Now it's not seeing any feeds on the list.

Ah, ok, seems to be a permissions thing on Fuseki, INSERT
enough for today.
