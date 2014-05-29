${prefixes} 
WITH <${graph}> DELETE { ?s ?p ?o } WHERE {  ?s ?p ?o } ; 
INSERT DATA { GRAPH <${graph}>{ ${body} }}