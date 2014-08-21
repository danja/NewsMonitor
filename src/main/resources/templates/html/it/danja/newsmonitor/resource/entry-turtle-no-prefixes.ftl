<${url}> a schema:article ;
         dcterms:source "${url}" ;
         <#if id??>nm:id "${id}" ;</#if>
         <#if title??>dcterms:title "${title}" ;</#if>
         <#if content??>schema:articleBody """${content}""" ;</#if> 
         <#if author??>
         	[ a foaf:Person ;
         		<#if author.name??>foaf:name "${author.name}" ;</#if>
         		<#if author.email??>foaf:email <mailto:${author.email}> ;</#if>
         		<#if author.homepage??>foaf:homepage <${author.homepage}> ;</#if>
         	]
         </#if>
         <#if datestamp??>
         	<#if datestamp.seen??>nm:seen "${datestamp.seen}" ;</#if>
         	<#if datestamp.published??>dcterms:published "${datestamp.published}" ;</#if>
         	<#if datestamp.updated??>dcterms:updated "${datestamp.updated}" ;</#if>
         </#if>
.
         
# url, id, title, date, author, links
    
         