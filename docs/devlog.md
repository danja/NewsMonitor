**2023-08-04**

PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>

SELECT DISTINCT ?class ?label ?description
WHERE {
  ?s rdf:type ?class .
  OPTIONAL { ?class rdfs:label ?label}
  OPTIONAL { ?class rdfs:comment ?description}
}
LIMIT 25


