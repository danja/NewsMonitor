# Feed --------------------
<${feedUrl}> a rss:channel ;
         dcterms:source <${feedUrl}> ;
         
         # admin bits
         <#if lives??>nm:lives "${lives?c}" ;</#if>
         <#if dead??>nm:dead "${dead?c}" ;</#if> 
         <#if volatile??>nm:volatile "${volatile?c}" ;</#if>
         <#if relevance??>nm:relevance "${relevance?c}" ;</#if>
         <#if favourite??>nm:favourite "${favourite?c}" ;</#if>
                  
         <#if id??>dcterms:identifier "${id}" ;</#if>
         <#if htmlUrl??>nm:htmlUrl <${htmlUrl}> ;</#if>
         <#if title??>dcterms:title """${title}""" ;</#if>
         <#if content??>schema:description """${content}""" ;</#if> 
         <#if author??>
         	dcterms:creator
         	[ a foaf:Person ;
         		<#if author.name??>foaf:name "${author.name}" ;</#if>
         		<#if author.email??>foaf:email <mailto:${author.email}> ;</#if>
         		<#if author.homepage??>foaf:homepage <${author.homepage}> ;</#if>
         	] ;
         </#if>
         <#if datestamp??>
         	<#if datestamp.seen??>nm:seen "${datestamp.seen}" ;</#if>
         	<#if datestamp.sortDate??>dcterms:date "${datestamp.sortDate}" ;</#if>
         	<#if datestamp.published??>dcterms:published "${datestamp.published}" ;</#if>
         	<#if datestamp.updated??>dcterms:updated "${datestamp.updated}" ;</#if>
         </#if> 
         
         .
         <#list links as link>
         	<#if link.href??><${feedUrl}> dcterms:references <${link.href}> .</#if>
         	<#if link.label??><${link.href}> schema:description """${link.label}""" .</#if>
         	<#if link.href??>
         	   <${url}> nm:hasLink [
         	   		a nm:Link ;
         	   		nm:href <${link.href}> ;
         	   		<#if link.label??>nm:label """${link.label}""" ;</#if>
         	   		<#if link.rel??>nm:rel """${link.rel}""" ;</#if>
         	   		<#if link.type??>nm:type """${link.type}""" ;</#if>
         	   ] .
         	</#if>
         </#list>     
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
         dcterms:source <${feedUrl}> ;
         <#if entry.htmlUrl??>nm:htmlUrl <${entry.htmlUrl}> ;</#if>
         <#if entry.id??>nm:id "${entry.id}" ;</#if>
         <#if entry.title??>dcterms:title "${entry.title}" ;</#if>
         <#if entry.content??>schema:articleBody """${entry.content}""" ;</#if> 
         <#if entry.author??>
         dcterms:creator
         	[ a foaf:Person ;
         		<#if entry.author.name??>foaf:name "${entry.author.name}" ;</#if>
         		<#if entry.author.email??>foaf:email <mailto:${entry.author.email}> ;</#if>
         		<#if entry.author.homepage??>foaf:homepage <${entry.author.homepage}> ;</#if>
         	];
         </#if>
         <#if entry.datestamp??>
     <#if entry.datestamp.seen??>nm:seen "${entry.datestamp.seen}" ;</#if>
     <#if entry.datestamp.sortDate??>dcterms:date "${entry.datestamp.sortDate}" ;</#if>
     <#if entry.datestamp.published??>dcterms:published "${entry.datestamp.published}" ;</#if>
     <#if entry.datestamp.updated??>dcterms:updated "${entry.datestamp.updated}" ;</#if>
         </#if>
         
                  	# admin
         	 <#if entry.read??>nm:read "${entry.read?c}" ;</#if>
         	 <#if entry.relevance??>nm:relevance "${entry.relevance?c}" ;</#if>
         	 <#if entry.favourite??>nm:favourite "${entry.favourite?c}" ;</#if> 
         	 <#list entry.tags as tag>
         	    <#if tag.text??>
         	       <${feedUrl}> nm:tag [ 
         	    	   a nm:Tag ;
         	           nm:tagText <${tag.text}> ;
         	           <#if tag.relevance??><${feedUrl}> nm:relevance <${tag.relevance}> .</#if>
         	           ] .
         	   </#if>
            </#list>
         .
         <#list entry.links as link>
         	<#if link.href??>
         	   <${url}> dcterms:references <${link.href}> .
               <#if link.label??><${link.href}> schema:description """${link.label}""" .</#if>
         	   <${entry.url}> nm:hasLink [
         	   		a nm:Link ;
         	   		nm:href <${link.href}> ;
         	   		<#if link.label??>nm:label """${link.label}""" ;</#if>
         	   		<#if link.rel??>nm:rel """${link.rel}""" ;</#if>
         	   		<#if link.type??>nm:type """${link.type}""" ;</#if>
         	   ] .
         	</#if>
        </#list>   
</#list>
<#if entryCount == 0> .</#if>