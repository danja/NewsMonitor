${prefixes} 
WITH <${graph}> DELETE { 
?entry ?p ?o 
} WHERE { 
GRAPH <${graph}>{
     ?entry a schema:article ;
     dcterms:source <${feedUrl}> ;
     ?p ?o 
     }
  } ;
INSERT DATA { GRAPH <${graph}>{ ${body} }}