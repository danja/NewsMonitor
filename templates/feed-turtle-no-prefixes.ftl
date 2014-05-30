# Feed --------------------
<${url}> a nm:feed ;
         dcterms:source "${url}" ;
         <#if id??>dcterms:identifier "${id}" ;</#if>
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
         	<#if datestamp.published??>dcterms:published "${datestamp.published}" ;</#if>
         	<#if datestamp.updated??>dcterms:updated "${datestamp.updated}" ;</#if>
         </#if>
         <#if htmlUrl??>nm:htmlUrl <${htmlUrl}> ;</#if> 
         <#if dead??>nm:dead "${dead?c}" ;</#if> 
         .
         <#list links as link>
         	<#if link.href??><${url}> dcterms:references <${link.href}> .</#if>
         	<#if link.label??><${link.href}> schema:description """${link.label}""" .</#if>
         </#list>        
    
<#list entries as entry>
# Entry ------------------------
<${entry.url}> a schema:article ;
         dcterms:source "${url}" ;
         <#if entry.htmlUrl??>nm:htmlUrl "${entry.htmlUrl}" ;</#if>
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
     <#if entry.datestamp.published??>dcterms:published "${entry.datestamp.published}" ;</#if>
     <#if entry.datestamp.updated??>dcterms:updated "${entry.datestamp.updated}" ;</#if>
         </#if>
         .
         <#list entry.links as link>
<#if link.href??><${url}> dcterms:references <${link.href}> .</#if>
<#if link.label??><${link.href}> schema:description """${link.label}""" .</#if>
         </#list>   
</#list>
<#if entryCount == 0> .</#if>