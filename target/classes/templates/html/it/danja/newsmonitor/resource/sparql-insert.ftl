${prefixes} 
  
WITH <${feedUrl}> DELETE { 
?entry ?p ?o 
} WHERE { 
GRAPH <${feedUrl}>{
     ?entry a schema:article ;
     dcterms:source <${feedUrl}> ;
     ?p ?o 
     }
  } ;
INSERT DATA { GRAPH <${feedUrl}>{ ${body} }}