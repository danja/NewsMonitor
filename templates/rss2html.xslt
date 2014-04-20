<xsl:stylesheet version="1.0"  
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"> 

<xsl:output method="xml" indent="yes"/>  

<xsl:template match="rss">
  <xsl:text disable-output-escaping="yes">
    &lt;!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"&gt;
  </xsl:text>
  <html>
    <xsl:apply-templates />
  </html>
</xsl:template>
   
<xsl:template match="channel">
  <head>
    <title>
      <xsl:value-of select="title" />
    </title>
  </head>
  <body xml:lang="{language}" lang="{language}">
    <xsl:apply-templates />
  </body>
</xsl:template>

<!-- new for Planet -->
<xsl:template match="item">
<div class="item">
  <h3>
        <xsl:element name="a">
         <xsl:attribute name="href">
            <xsl:value-of select="link" />
         </xsl:attribute> 
         <xsl:value-of select="title" />
    </xsl:element>
    </h3>
  <p><xsl:value-of select="description" /></p>
  <p>
      <xsl:element name="a">
         <xsl:attribute name="href">
            <xsl:value-of select="source/@url" />
         </xsl:attribute> 
         <xsl:value-of select="source" />
    </xsl:element>
<br/>
    <xsl:value-of select="pubDate" /></p>
</div>

</xsl:template>



<xsl:template match="text()" />

</xsl:stylesheet>

