# Feed --------------------
<${feedUrl}> a rss:channel .
<${feedUrl}> dcterms:source <${feedUrl}> .
         
         # feed/admin bits
         <#if responseCode??><${feedUrl}> nm:responseCode "${responseCode?c}" .</#if>
         <#if format??><${feedUrl}> nm:format "${format}" .</#if>
         <#if contentType??><${feedUrl}> nm:contentType "${contentType}" .</#if>
         <#if lives??><${feedUrl}> nm:lives "${lives?c}" .</#if>
         <#if dead??><${feedUrl}> nm:dead "${dead?c}" .</#if> 
         <#if entryCount??><${feedUrl}> nm:entryCount "${entryCount?c}" .</#if> 
         <#if volatile??><${feedUrl}> nm:volatile "${volatile?c}" .</#if>
         <#if relevance??><${feedUrl}> nm:relevance "${relevance?c}" .</#if>
         <#if favourite??><${feedUrl}> nm:favourite "${favourite?c}" .</#if>
         <#if relevanceFactor??><${feedUrl}> nm:relevanceFactor "${relevanceFactor?c}" .</#if>                  
         <#if id??><${feedUrl}> dcterms:identifier "${id}" .</#if>
         <#if htmlUrl??><${feedUrl}> nm:htmlUrl <${htmlUrl}> .</#if>
         <#if title??><${feedUrl}> dcterms:title """${title}""" .</#if>
         <#if content??><${feedUrl}> schema:description """${content}""" .</#if> 
         
         # feed/author
         <#if author??>
         <${feedUrl}> 
         	dcterms:creator
         	[ a foaf:Person ;
         		<#if author.name??>foaf:name "${author.name}" ;</#if>
         		<#if author.email??>foaf:email <mailto:${author.email}> ;</#if>
         		<#if author.homepage??>foaf:homepage <${author.homepage}> ;</#if>
         	] .
         </#if>
         
         # feed/datestamp
         <#if datestamp??>
         	<#if datestamp.seen??><${feedUrl}> nm:seen "${datestamp.seen}" .</#if>
         	<#if datestamp.sortDate??><${feedUrl}> dcterms:date "${datestamp.sortDate}" .</#if>
         	<#if datestamp.published??><${feedUrl}> dcterms:published "${datestamp.published}" .</#if>
         	<#if datestamp.updated??><${feedUrl}> dcterms:updated "${datestamp.updated}" .</#if>
         	
         </#if> 
         
         # feed/links
         <#list links as link>
         	<#if link.href??><${feedUrl}> dcterms:references <${link.href}> .</#if>
         	<#if link.label??><${link.href}> schema:description """${link.label}""" .</#if>
         	<#if link.href??>
         	   <${url}> nm:hasLink [
         	   		a nm:Link ;
         	   		nm:href <${link.href}> ;
         	   		<#if link.origin??>nm:origin <${link.origin}> ;</#if>
         	   		<#if link.label??>nm:label """${link.label}""" ;</#if>
         	   		<#if link.rel??>nm:rel "${link.rel}" ;</#if>
         	   		<#if link.responseCode??>nm:responseCode "${link.responseCode}" ;</#if>
         	   		<#if link.contentType??>nm:contentType "${link.contentType}" ;</#if>
         	   		<#if link.format??>nm:format "${link.format}" ;</#if>   		
         	   		<#if link.explored??>nm:explored "${link.explored?c}" ;</#if>
         	   		<#if link.relevance??>nm:relevance "${link.relevance?c}" ;</#if>
         	   		<#if link.remote??>nm:remote "${link.remote?c}" ;</#if>
         	   ] .
         	</#if>
         </#list>     
         
         # feed/tags
         <#list tags as tag>
         	<#if tag.text??>
         	    <${feedUrl}> nm:tag [ 
         	    	a nm:Tag ;
         	        nm:tagText <${tag.text}> ;
         	        <#if tag.relevance??><${feedUrl}> nm:relevance <${tag.relevance}> .</#if>
         	        ] .
         	</#if>
         </#list>
    
<#list entries as entry>
# Entry ------------------------
<${entry.url}> a schema:article ;
         dcterms:source <${feedUrl}> .
         <#if entry.htmlUrl??><${entry.url}> nm:htmlUrl <${entry.htmlUrl}> .</#if>
         <#if entry.id??><${entry.url}> nm:id "${entry.id}" .</#if>
         <#if entry.wordcount??><${entry.url}> nm:wordcount "${entry.wordcount}" .</#if>
         <#if entry.title??><${entry.url}> dcterms:title "${entry.title}" .</#if>
         <#if entry.content??><${entry.url}> schema:articleBody """${entry.content}""" .</#if> 
         <#if entry.author??>
         
         # entry/creator
         <${entry.url}> dcterms:creator
         	[ a foaf:Person ;
         		<#if entry.author.name??>foaf:name "${entry.author.name}" ;</#if>
         		<#if entry.author.email??>foaf:email <mailto:${entry.author.email}> ;</#if>
         		<#if entry.author.homepage??>foaf:homepage <${entry.author.homepage}> ;</#if>
         	] .
         </#if>
         
         # entry/datestamp
         <#if entry.datestamp??>
     <#if entry.datestamp.seen??><${entry.url}> nm:seen "${entry.datestamp.seen}" .</#if>
     <#if entry.datestamp.sortDate??><${entry.url}> dcterms:date "${entry.datestamp.sortDate}" .</#if>
     <#if entry.datestamp.published??><${entry.url}> dcterms:published "${entry.datestamp.published}" .</#if>
     <#if entry.datestamp.updated??><${entry.url}> dcterms:updated "${entry.datestamp.updated}" .</#if>
         </#if>
         
             # entry/admin
         	 <#if entry.read??><${entry.url}> nm:read "${entry.read?c}" .</#if>
         	 <#if entry.relevance??><${entry.url}> nm:relevance "${entry.relevance?c}" .</#if>
         	 <#if entry.favourite??><${entry.url}> nm:favourite "${entry.favourite?c}" .</#if> 
         	 
         	 # entry/tags
         	 <#list entry.tags as tag>
         	    <#if tag.text??>
         	       <${feedUrl}> nm:tag [ 
         	    	   a nm:Tag ;
         	           nm:tagText <${tag.text}> ;
         	           <#if tag.relevance??><${feedUrl}> nm:relevance <${tag.relevance}> .</#if>
         	           ] .
         	   </#if>
            </#list>

         # entry/links
         <#list entry.links as link>
         	<#if link.href??>
         	   <${url}> dcterms:references <${link.href}> .
               <#if link.label??><${link.href}> schema:description """${link.label}""" .</#if>
         	   <${entry.url}> nm:hasLink [
         	   		a nm:Link ;
         	   		nm:href <${link.href}> ;
         	   		<#if link.origin??>nm:origin <${link.origin}> ;</#if>
         	   		<#if link.label??>nm:label """${link.label}""" ;</#if>
         	   		<#if link.rel??>nm:rel """${link.rel}""" ;</#if>
         	   		<#if link.responseCode??>nm:responseCode "${link.responseCode}" ;</#if>
         	   		<#if link.contentType??>nm:contentType "${link.contentType}" ;</#if>
         	   		<#if link.format??>nm:format "${link.format}" ;</#if>
         	   		<#if link.explored??>nm:explored "${link.explored?c}" ;</#if>
         	   		<#if link.relevance??>nm:relevance "${link.relevance?c}" ;</#if>
         	   		<#if link.remote??>nm:remote "${link.remote?c}" ;</#if>
         	   ] .
         	</#if>
        </#list>   
</#list>
# <#if entryCount == 0> .</#if>
#
