<?xml version="1.0"?>
<!--
	# http://purl.org/net/syndication/subscribe/feed-rss1.0-1.4.xsl
	# A transformation of any of the following formats to RSS 1.0:
	# - RSS 0.9
	# - RSS 0.9x/2.0
	# - RSS 1.0
	# - Atom 0.3

	# (c) 2002-2003 Morten Frederiksen
	# License: http://www.gnu.org/licenses/gpl
-->
<xsl:transform
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:simple="http://my.netscape.com/rdf/simple/0.9/"
	xmlns:rss="http://purl.org/rss/1.0/"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:dcterms="http://purl.org/dc/terms/"
	xmlns:content="http://purl.org/rss/1.0/modules/content/"
	xmlns:syn="http://purl.org/rss/1.0/modules/syndication/"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:atom="http://purl.org/atom/ns#"
	xmlns:foaf="http://xmlns.com/foaf/0.1/"
	xmlns:admin="http://webns.net/mvcb/"
	xmlns:icbm="http://postneo.com/icbm"
	xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#"
	exclude-result-prefixes="simple atom icbm"
	version="1.0">
<xsl:output
	indent="yes"
	omit-xml-declaration="yes"
	method="xml"/>
<xsl:namespace-alias
	result-prefix="rss"
	stylesheet-prefix="rss"/>

<xsl:param name="rss"/>

<xsl:template match="/">
	<xsl:apply-templates select="/rdf:RDF[rss:channel or simple:channel]"/>
	<xsl:apply-templates select="/rss[@version]/channel"/>
	<xsl:apply-templates select="/redirect/newLocation"/>
	<xsl:apply-templates select="/atom:feed[@version='0.3']"/>
</xsl:template>

<xsl:template match="/redirect/newLocation">
	<rdf:RDF>
		<xsl:apply-templates mode="version" select="."/>
		<rdf:Description rdf:about="{$rss}">
			<dc:isReplacedBy rdf:resource="{normalize-space(.)}"/>
		</rdf:Description>
	</rdf:RDF>
</xsl:template>

<xsl:template match="/atom:feed">
	<rdf:RDF>
		<xsl:copy-of select="@xml:lang"/>
		<xsl:apply-templates mode="version" select="."/>
		<rss:channel rdf:about="{$rss}">
			<xsl:apply-templates select="*"/>
			<rss:items>
				<rdf:Seq>
					<xsl:for-each select="atom:entry">
						<rdf:li rdf:resource="{atom:link[@rel='alternate']/@href}"/>
					</xsl:for-each>
				</rdf:Seq>
			</rss:items>
		</rss:channel>
		<xsl:apply-templates mode="item" select="atom:entry"/>
	</rdf:RDF>
</xsl:template>

<xsl:template mode="item" match="atom:entry">
	<rss:item rdf:about="{atom:link[@rel='alternate']/@href}">
		<xsl:copy-of select="@xml:lang"/>
		<xsl:apply-templates select="*"/>
	</rss:item>
</xsl:template>

<xsl:template match="/rdf:RDF">
	<rdf:RDF>
		<xsl:apply-templates mode="version" select="rss:channel|simple:channel"/>
		<xsl:apply-templates mode="rss" select="*"/>
		<xsl:apply-templates select="simple:*"/>
	</rdf:RDF>
</xsl:template>

<xsl:template mode="version" match="rss:channel|channel|simple:channel|newLocation|atom:feed">
	<xsl:comment>
		<xsl:value-of select="' version=&quot;'"/>
		<xsl:choose>
			<xsl:when test="namespace-uri()='http://purl.org/rss/1.0/'">
				<xsl:value-of select="'RSS 1.0'"/>
			</xsl:when>
			<xsl:when test="namespace-uri()='http://purl.org/atom/ns#'">
				<xsl:value-of select="'Atom '"/>
				<xsl:value-of select="@version"/>
			</xsl:when>
			<xsl:when test="../@version">
				<xsl:value-of select="'RSS '"/>
				<xsl:value-of select="../@version"/>
			</xsl:when>
			<xsl:when test="namespace-uri()='http://my.netscape.com/rdf/simple/0.9/'">
				<xsl:value-of select="'RSS 0.9'"/>
			</xsl:when>
			<xsl:when test="local-name()='newLocation' and normalize-space(.)!=''">
				<xsl:value-of select="'Redirected'"/>
			</xsl:when>
			<xsl:when test="local-name()='newLocation'">
				<xsl:value-of select="'Dead'"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'?'"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="'&quot; '"/>
	</xsl:comment>
</xsl:template>

<xsl:template mode="rss" match="rss:channel">
	<rss:channel>
		<xsl:copy-of select="@*"/>
		<xsl:apply-templates mode="rss" select="node()|text()|comment()"/>
	</rss:channel>
</xsl:template>

<xsl:template mode="rss" match="*">
	<xsl:copy>
		<xsl:copy-of select="@*"/>
		<xsl:apply-templates mode="rss" select="node()|text()|comment()"/>
	</xsl:copy>
</xsl:template>

<xsl:template mode="rss" match="rdf:li">
	<rdf:li rdf:resource="{concat(@resource,@rdf:resource)}"/>
</xsl:template>

<xsl:template match="simple:channel">
	<rss:channel rdf:about="{$rss}">
		<xsl:apply-templates select="simple:title|simple:description|simple:link"/>
		<rss:items>
			<rdf:Seq>
				<xsl:for-each select="../simple:item[normalize-space(simple:link)!='']">
					<rdf:li rdf:resource="{simple:link}"/>
				</xsl:for-each>
			</rdf:Seq>
		</rss:items>
		<xsl:if test="../simple:image[normalize-space(simple:url)!='']">
			<rss:image rdf:resource="{../simple:image[normalize-space(simple:url)!=''][1]/simple:url}"/>
		</xsl:if>
		<xsl:if test="../simple:textinput[normalize-space(simple:link)!='']">
			<rss:textinput rdf:resource="{../simple:textinput[normalize-space(simple:link)!=''][1]/simple:link}"/>
		</xsl:if>
	</rss:channel>
</xsl:template>

<xsl:template match="simple:item">
	<xsl:if test="normalize-space(simple:link)!=''">
		<rss:item rdf:about="{simple:link}">
			<xsl:apply-templates select="simple:title|simple:description|simple:link"/>
		</rss:item>
	</xsl:if>
</xsl:template>

<xsl:template match="simple:image">
	<xsl:if test="normalize-space(simple:url)!=''">
		<rss:image rdf:about="{simple:url}">
			<xsl:apply-templates select="simple:url|simple:link|simple:title|simple:description"/>
		</rss:image>
	</xsl:if>
</xsl:template>

<xsl:template match="simple:textinput">
	<xsl:if test="normalize-space(simple:link)!=''">
		<rss:textinput rdf:about="{simple:link}">
			<xsl:apply-templates select="simple:name|simple:link|simple:title|simple:description"/>
		</rss:textinput>
	</xsl:if>
</xsl:template>

<xsl:template match="icbm:latitude">
	<geo:lat>
		<xsl:value-of select="text()"/>
	</geo:lat>
</xsl:template>

<xsl:template match="icbm:longitude">
	<geo:long>
		<xsl:value-of select="text()"/>
	</geo:long>
</xsl:template>

<xsl:template match="dc:*|dcterms:*">
	<xsl:copy-of select="."/>
</xsl:template>

<xsl:template match="atom:title|atom:link/@title|simple:title|simple:description|simple:link|simple:url|simple:name">
	<xsl:element name="{concat('rss:',local-name(.))}">
		<xsl:value-of select="."/>
	</xsl:element>
</xsl:template>

<xsl:template match="atom:modified|atom:created|atom:issued">
	<xsl:element name="{concat('dcterms:',local-name(.))}">
		<xsl:value-of select="."/>
	</xsl:element>
</xsl:template>

<xsl:template match="atom:name">
	<xsl:element name="{concat('foaf:',local-name(.))}">
		<xsl:value-of select="."/>
	</xsl:element>
</xsl:template>

<xsl:template match="atom:url">
	<foaf:homepage rdf:resource="{.}"/>
</xsl:template>

<xsl:template match="atom:email">
	<foaf:mbox rdf:resource="mailto:{.}"/>
</xsl:template>

<xsl:template match="atom:link[@rel='alternate' and @type='text/html']">
	<rss:link>
		<xsl:value-of select="@href"/>
	</rss:link>
	<foaf:page rdf:resource="{@href}"/>
</xsl:template>

<xsl:template match="atom:link">
	<dcterms:references>
		<rdf:Description rdf:about="{@href}">
			<xsl:apply-templates select="@*"/>
		</rdf:Description>
	</dcterms:references>
</xsl:template>

<xsl:template match="atom:generator|atom:entry|atom:info|atom:*/@*">
</xsl:template>

<xsl:template match="atom:link/@type">
	<dc:format>
		<xsl:value-of select="."/>
	</dc:format>
</xsl:template>

<xsl:template match="atom:link/@rel|atom:id">
	<dc:identifier>
		<xsl:value-of select="."/>
	</dc:identifier>
</xsl:template>

<xsl:template match="atom:generator[@url and @version]">
	<admin:generatorAgent rdf:resource="{@url}?v={@version}"/>
</xsl:template>

<xsl:template match="atom:author|atom:contributor">
	<foaf:maker>
		<foaf:Person>
			<xsl:apply-templates select="*"/>
		</foaf:Person>
	</foaf:maker>
</xsl:template>

<xsl:template match="atom:tagline|atom:summary">
	<dc:description>
		<xsl:value-of select="."/>
	</dc:description>
</xsl:template>

<xsl:template match="atom:copyright">
	<dc:rights>
		<xsl:value-of select="."/>
	</dc:rights>
</xsl:template>

<xsl:template match="atom:content[@mode='escaped']">
	<content:encoded>
		<xsl:copy-of select="@xml:lang"/>
		<xsl:value-of select="."/>
	</content:encoded>
</xsl:template>

<xsl:template match="atom:content[not(@mode) or @mode='xml']">
	<content:encoded>
		<xsl:copy-of select="@xml:lang"/>
		<xsl:value-of select="."/>
	</content:encoded>
</xsl:template>

<xsl:template match="atom:content[@type='multipart/alternative']">
	<xsl:apply-templates select="*"/>
</xsl:template>

<xsl:template match="atom:content">
</xsl:template>

<xsl:template match="channel">
	<rdf:RDF>
		<xsl:apply-templates mode="version" select="."/>
		<rss:channel rdf:about="{$rss}">
			<xsl:apply-templates select="title|link|description|language|copyright|webMaster|webmaster|managingEditor|managingeditor|pubDate|pubdate|lastBuildDate|lastbuilddate|category[@domain='Syndic8']"/>
			<xsl:copy-of select="dc:*|dcterms:*|syn:*"/>
			<rss:items>
				<rdf:Seq>
					<xsl:for-each select="item">
						<rdf:li>
							<xsl:attribute name="rdf:resource">
								<xsl:apply-templates mode="link" select="."/>
							</xsl:attribute>
						</rdf:li>
					</xsl:for-each>
				</rdf:Seq>
			</rss:items>
			<xsl:if test="image[normalize-space(url)!='']">
				<rss:image rdf:resource="{image[normalize-space(url)!=''][1]/url}"/>
			</xsl:if>
			<xsl:if test="textinput[normalize-space(link)!='']">
				<rss:textinput rdf:resource="{textinput[normalize-space(link)!=''][1]/link}"/>
			</xsl:if>
		</rss:channel>
		<xsl:apply-templates select="item|image[normalize-space(url)!='']|textinput[normalize-space(link)!='']"/>
	</rdf:RDF>
</xsl:template>

<xsl:template match="item">
	<rss:item>
		<xsl:attribute name="rdf:about">
			<xsl:apply-templates mode="link" select="."/>
		</xsl:attribute>
		<xsl:apply-templates select="icbm:latitude|icbm:longitude|link|title|description|language|category|pubDate|pubdate|lastBuildDate|lastbuilddate"/>
		<xsl:if test="not(link) and normalize-space(guid[not(@isPermaLink='false')])!=''">
			<rss:link>
				<xsl:value-of select="guid[not(@isPermaLink='false')]"/>
			</rss:link>
		</xsl:if>
		<xsl:if test="not(title)">
			<rss:title/>
		</xsl:if>
		<xsl:copy-of select="dc:*|dcterms:*|content:*"/>
	</rss:item>
</xsl:template>

<xsl:template mode="link" match="item">
	<xsl:choose>
		<xsl:when test="normalize-space(guid[not(@isPermaLink='false')])!=''">
			<xsl:value-of select="guid[not(@isPermaLink='false')]"/>
		</xsl:when>
		<xsl:when test="normalize-space(link)!=''">
			<xsl:value-of select="link"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$rss"/>
			<xsl:value-of select="'#'"/>
			<xsl:value-of select="generate-id(.)"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template mode="link" match="channel|image|textinput">
	<xsl:value-of select="link[1]"/>
</xsl:template>

<xsl:template match="image">
	<xsl:if test="normalize-space(url)!=''">
		<rss:image rdf:about="{url}">
			<xsl:apply-templates select="url|link|title|description"/>
		</rss:image>
	</xsl:if>
</xsl:template>

<xsl:template match="textinput">
	<xsl:if test="normalize-space(link)!=''">
		<rss:textinput rdf:about="{link}">
			<xsl:apply-templates select="name|link|title|description"/>
		</rss:textinput>
	</xsl:if>
</xsl:template>

<xsl:template match="link">
	<xsl:element name="{concat('rss:',name(.))}">
		<xsl:apply-templates mode="link" select=".."/>
	</xsl:element>
</xsl:template>

<xsl:template match="name|url|title|description">
	<xsl:element name="{concat('rss:',name(.))}">
		<xsl:value-of select="normalize-space(.)"/>
	</xsl:element>
</xsl:template>

<xsl:template match="language">
	<xsl:element name="{concat('dc:',name(.))}">
		<xsl:value-of select="normalize-space(.)"/>
	</xsl:element>
</xsl:template>

<xsl:template match="copyright">
	<xsl:element name="dc:rights">
		<xsl:value-of select="normalize-space(.)"/>
	</xsl:element>
</xsl:template>

<xsl:template match="webMaster|webmaster">
	<xsl:element name="dc:publisher">
		<xsl:value-of select="normalize-space(.)"/>
	</xsl:element>
</xsl:template>

<xsl:template match="managingEditor|managingeditor">
	<xsl:element name="dc:creator">
		<xsl:value-of select="normalize-space(.)"/>
	</xsl:element>
</xsl:template>

<xsl:template match="category[@domain='Syndic8']">
	<xsl:element name="dcterms:isReferencedBy">
		<xsl:attribute name="rdf:resource">
			<xsl:value-of select="'http://www.syndic8.com/feedinfo.php?FeedID='"/>
			<xsl:value-of select="normalize-space(.)"/>
		</xsl:attribute>
	</xsl:element>
</xsl:template>

<xsl:template match="category">
	<xsl:element name="dc:subject">
		<xsl:value-of select="normalize-space(.)"/>
	</xsl:element>
</xsl:template>

<xsl:template match="pubDate|pubdate">
	<xsl:apply-templates mode="rfc2822-w3cdtf" select=".">
		<xsl:with-param name="name" select="'dcterms:created'"/>
	</xsl:apply-templates>
</xsl:template>

<xsl:template match="lastBuildDate|lastbuilddate">
	<xsl:apply-templates mode="rfc2822-w3cdtf" select=".">
		<xsl:with-param name="name" select="'dcterms:modified'"/>
	</xsl:apply-templates>
</xsl:template>

<xsl:template mode="rfc2822-w3cdtf" match="*">
	<xsl:param name="name" select="'dc:date'"/>
	<xsl:if test="contains(.,',') and string-length(normalize-space(substring-before(.,',')))=3">
		<xsl:variable name="dmyhisz" select="normalize-space(substring-after(.,','))"/>
		<!-- Fetch date of month. -->
		<xsl:if test="contains($dmyhisz,' ') and string-length(substring-before($dmyhisz,' '))&lt;=2">
			<xsl:variable name="d" select="substring-before($dmyhisz,' ')"/>
			<xsl:variable name="myhisz" select="normalize-space(substring-after($dmyhisz,' '))"/>
			<!-- Validate date of month, fetch and translate month name to month number. -->
			<xsl:if test="string-length(translate($d,'0123456789',''))=0 and contains($myhisz,' ') and string-length(substring-before($myhisz,' '))=3">
				<xsl:variable name="m-temp" select="translate(substring-before($myhisz,' '),'ACDEFGLMNOPRSTUY','acdefglmnoprstuy')"/>
				<xsl:variable name="yhisz" select="normalize-space(substring-after($myhisz,' '))"/>
				<xsl:variable name="m">
					<xsl:choose>
						<xsl:when test="$m-temp='jan'">
							<xsl:value-of select="'1'"/>
						</xsl:when>
						<xsl:when test="$m-temp='feb'">
							<xsl:value-of select="'2'"/>
						</xsl:when>
						<xsl:when test="$m-temp='mar'">
							<xsl:value-of select="'3'"/>
						</xsl:when>
						<xsl:when test="$m-temp='apr'">
							<xsl:value-of select="'4'"/>
						</xsl:when>
						<xsl:when test="$m-temp='may'">
							<xsl:value-of select="'5'"/>
						</xsl:when>
						<xsl:when test="$m-temp='jun'">
							<xsl:value-of select="'6'"/>
						</xsl:when>
						<xsl:when test="$m-temp='jul'">
							<xsl:value-of select="'7'"/>
						</xsl:when>
						<xsl:when test="$m-temp='aug'">
							<xsl:value-of select="'8'"/>
						</xsl:when>
						<xsl:when test="$m-temp='sep'">
							<xsl:value-of select="'9'"/>
						</xsl:when>
						<xsl:when test="$m-temp='oct'">
							<xsl:value-of select="'10'"/>
						</xsl:when>
						<xsl:when test="$m-temp='nov'">
							<xsl:value-of select="'11'"/>
						</xsl:when>
						<xsl:when test="$m-temp='dev'">
							<xsl:value-of select="'12'"/>
						</xsl:when>
					</xsl:choose>
				</xsl:variable>
				<!-- Validate month, fetch (possibly translating) year. -->
				<xsl:if test="string-length($m)!=0 and contains($yhisz,' ')">
					<xsl:variable name="y-temp" select="substring-before($yhisz,' ')"/>
					<xsl:variable name="hisz" select="normalize-space(substring-after($yhisz,' '))"/>
					<xsl:variable name="y">
						<xsl:choose>
							<xsl:when test="string-length(translate($y-temp,'0123456789',''))=0 and string-length($y-temp)=2 and $y-temp &lt; 70">
								<xsl:value-of select="concat('20',$y-temp)"/>
							</xsl:when>
							<xsl:when test="string-length(translate($y-temp,'0123456789',''))=0 and string-length($y-temp)=2 and $y-temp &gt;= 70">
								<xsl:value-of select="concat('19',$y-temp)"/>
							</xsl:when>
							<xsl:when test="string-length(translate($y-temp,'0123456789',''))=0 and string-length($y-temp)=4">
								<xsl:value-of select="$y-temp"/>
							</xsl:when>
						</xsl:choose>
					</xsl:variable>
					<!-- Validate year, fetch time, fetch and translate time zone. -->
					<xsl:if test="string-length($y)!=0 and contains($hisz,' ')">
						<xsl:variable name="his" select="substring-before($hisz,' ')"/>
						<xsl:variable name="z" select="normalize-space(substring-after($hisz,' '))"/>
						<xsl:variable name="offset">
							<xsl:choose>
								<xsl:when test="$z='GMT'">
									<xsl:value-of select="'Z'"/>
								</xsl:when>
							</xsl:choose>
						</xsl:variable>
						<!-- Validate time and time zone. -->
						<xsl:if test="string-length($his)=8 and string-length(translate($his,'0123456789',''))=2 and string-length(translate($his,':',''))=6 and string-length($offset)!=0">
							<xsl:element name="{$name}">
								<xsl:value-of select="concat($y,'-',format-number($m,'00'),'-',format-number($d,'00'),'T',$his,$offset)"/>
							</xsl:element>
							<xsl:comment>
								<xsl:value-of select="."/>
							</xsl:comment>
						</xsl:if>
					</xsl:if>
				</xsl:if>
			</xsl:if>
		</xsl:if>
	</xsl:if>
</xsl:template>

</xsl:transform>
