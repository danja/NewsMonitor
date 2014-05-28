<${url}> a schema:article ;
         dcterms:title "${title}" ;
         <#if date??>dcterms:date "${date}" ;</#if>
         dcterms:source "${url}" ;
         schema:articleBody """${content}""" .
         
         # 
         