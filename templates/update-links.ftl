PREFIX rss: <http://purl.org/rss/1.0/>
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX schema: <http://schema.org/>
PREFIX nm: <http://purl.org/stuff/newsmonitor/>

WITH <${origin}> DELETE { 
?link ?p ?o 
} WHERE { 
GRAPH <${origin}> {
       	     ?link a nm:Link ;
         	   		nm:href <${href}>  .
         	   		}
  } ;
INSERT DATA {
GRAPH <${origin}> {
         	   	[	a nm:Link ;
         	   		nm:href <${href}> ;
         	   		nm:origin <${origin}> ;
         	   		<#if label??>nm:label """${label}""" ;</#if>
         	   		<#if rel??>nm:rel """${rel}""" ;</#if>
         	   		<#if format??>nm:format """${format}""" ;</#if>
         	   		<#if contentType??>nm:contentType """${contentType}""" ;</#if>
         	   		<#if responseCode??>nm:responseCode """${responseCode?c}""" ;</#if>
         	   		<#if explored??>nm:explored "${explored?c}" ;</#if>
         	   		<#if relevance??>nm:relevance "${relevance?c}" ;</#if>
         	   		]
         	   		}
 }
