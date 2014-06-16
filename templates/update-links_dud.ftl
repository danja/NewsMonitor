PREFIX rss: <http://purl.org/rss/1.0/>
PREFIX dcterms: <http://purl.org/dc/terms/>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX schema: <http://schema.org/>
PREFIX nm: <http://purl.org/stuff/newsmonitor/>

WITH ?g DELETE { 
?link ?p ?o 
} WHERE { 
GRAPH ?g {
       	     ?link a nm:Link ;
         	   		nm:href <${link.href}> ;
         	   		?p ?o .
     }
  } ;
INSERT DATA { GRAPH ?g { 
         	   		a nm:Link ;
         	   		nm:href <${link.href}> ;
         	   		<#if link.label??>nm:label """${link.label}""" ;</#if>
         	   		<#if link.rel??>nm:rel """${link.rel}""" ;</#if>
         	   		<#if link.format??>nm:format """${link.format}""" ;</#if>
         	   		<#if link.contentType??>nm:contentType """${link.contentType}""" ;</#if>
         	   		<#if link.explored??>nm:explored "${link.explored?c}" ;</#if>
         	   		<#if link.relevance??>nm:relevance "${link.relevance?c}" ;</#if>
 }}


		         
         