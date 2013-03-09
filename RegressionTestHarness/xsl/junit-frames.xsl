<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- import the default stylesheet -->
  <xsl:import href="jar:file:lib/ant-junit.jar!/org/apache/tools/ant/taskdefs/optional/junit/xsl/junit-frames.xsl"/>

<!-- class header -->
<xsl:template name="testsuite.test.header">
    <tr valign="top">
        <th width="80%">Name</th>
        <th>Tests</th>
        <th>Errors</th>
        <th>Failures</th>
        <th nowrap="nowrap">Time(s)</th>
        <th nowrap="nowrap">Time Stamp</th>
        <th>Host</th>
        <!-- ADDED -->
        <th>Screenshots</th>
    </tr>
</xsl:template>

<!-- class information -->
<xsl:template match="testsuite" mode="print.test">
    <tr valign="top">
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="@errors[.&gt; 0]">Error</xsl:when>
                <xsl:when test="@failures[.&gt; 0]">Failure</xsl:when>
                <xsl:otherwise>Pass</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <td><a title="Display all tests" href="{@id}_{@name}.html"><xsl:value-of select="@name"/></a></td>
        <td><a title="Display all tests" href="{@id}_{@name}.html"><xsl:apply-templates select="@tests"/></a></td>
        <td>
	    <xsl:choose>
		<xsl:when test="@errors != 0">
		    <a title="Display only errors" href="{@id}_{@name}-errors.html"><xsl:apply-templates select="@errors"/></a>
		</xsl:when>
		<xsl:otherwise>
		    <xsl:apply-templates select="@errors"/>
		</xsl:otherwise>
	    </xsl:choose>
	</td>
        <td>
	    <xsl:choose>
		<xsl:when test="@failures != 0">
		    <a title="Display only failures" href="{@id}_{@name}-fails.html"><xsl:apply-templates select="@failures"/></a>
		</xsl:when>
		<xsl:otherwise>
		    <xsl:apply-templates select="@failures"/>
		</xsl:otherwise>
	    </xsl:choose>
	</td>
        <td><xsl:call-template name="display-time">
                <xsl:with-param name="value" select="@time"/>
            </xsl:call-template>
        </td>
        <td><xsl:apply-templates select="@timestamp"/></td>
        <td><xsl:apply-templates select="@hostname"/></td>
        <!-- ADDED -->
      <xsl:variable name="screenshot.dir">
        <xsl:value-of select="concat('../images', '/',@name)"/>
      </xsl:variable>
        <td> <a href="{$screenshot.dir}/index.html" target="_blank">View</a></td>
    </tr>
</xsl:template>
</xsl:stylesheet>