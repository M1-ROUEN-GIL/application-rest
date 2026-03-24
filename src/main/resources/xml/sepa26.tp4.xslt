<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:p="http://univ.fr/sepa26"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                exclude-result-prefixes="p xs fn">

  <xsl:output method="html" version="5" encoding="UTF-8" indent="yes"/>
  <xsl:variable name="nbTransactions" select="count(//p:DrctDbtTxInf)"/>
  <xsl:template match="/">
    <html lang="fr">
      <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <title>TP4 - Flux SEPA 26</title>
        <link rel="stylesheet" href="sepa26.css"/>
      </head>
      <body>

        <header>
          <h1>TP4 - Flux SEPA 26</h1>
          <p class="date-emission">
            <xsl:text>Le </xsl:text>
            <xsl:call-template name="formatDate">
              <xsl:with-param name="date" select="string(current-date())"/>
            </xsl:call-template>
          </p>
          <xsl:call-template name="listeTransactions"/>
        </header>

        <main>
          <xsl:apply-templates select="//p:DrctDbtTxInf"/>
        </main>

        <footer>
          <p><em>Document émis par Nom Prénom</em></p>
        </footer>

      </body>
    </html>
  </xsl:template>
  <xsl:template name="listeTransactions">
    <nav class="sommaire">
      <h2>Liste des transactions</h2>
      <ol>
        <xsl:for-each select="//p:DrctDbtTxInf">
          <xsl:sort select="xs:decimal(p:InstdAmt)" order="descending"/>
          <li>
            <span class="liste-montant">
              <xsl:text>montant = </xsl:text>
              <xsl:value-of select="p:InstdAmt"/>
              <xsl:text> </xsl:text>
              <xsl:value-of select="p:InstdAmt/@Ccy"/>
            </span>
            <xsl:text>  référence : </xsl:text>
            <span class="liste-ref">
              <xsl:value-of select="p:PmtId"/>
            </span>
          </li>
        </xsl:for-each>
      </ol>
    </nav>
  </xsl:template>
  <xsl:template match="p:DrctDbtTxInf">
    <xsl:variable name="classe">
      <xsl:choose>
        <xsl:when test="position() mod 2 = 1">transaction impair</xsl:when>
        <xsl:otherwise>transaction pair</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <section class="{$classe}">
      <h2 id="{generate-id(p:PmtId)}">
        <xsl:text>Transaction </xsl:text>
        <xsl:value-of select="position()"/>
        <xsl:text>/</xsl:text>
        <xsl:value-of select="$nbTransactions"/>
        <xsl:text> : </xsl:text>
        <xsl:value-of select="p:PmtId"/>
      </h2>

      <div class="transaction-corps">
        <xsl:call-template name="infoPrincipales"/>
        <xsl:call-template name="infoDebiteur"/>
        <xsl:if test="p:RmtInf">
          <xsl:call-template name="infoCommentaire"/>
        </xsl:if>
      </div>
    </section>
  </xsl:template>

  <xsl:template name="infoPrincipales">
    <table class="tableau-info">
      <tr>
        <td class="label">Montant</td>
        <td>
          <span class="montant">
            <xsl:value-of select="p:InstdAmt"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="p:InstdAmt/@Ccy"/>
          </span>
        </td>
      </tr>
      <tr>
        <td class="label">Date</td>
        <td>
          <xsl:call-template name="formatDate">
            <xsl:with-param name="date" select="p:DrctDbtTx/p:MndtRltdInf/p:DtOfSgntr"/>
          </xsl:call-template>
        </td>
      </tr>
      <tr>
        <td class="label">Mandat</td>
        <td>
          <xsl:value-of select="p:DrctDbtTx/p:MndtRltdInf/p:MndtId"/>
        </td>
      </tr>
    </table>
  </xsl:template>

  <xsl:template name="infoDebiteur">
    <p class="sous-titre">Débiteur</p>
    <table class="tableau-info">
      <tr>
        <td class="label">Nom</td>
        <td><xsl:value-of select="p:Dbtr/p:Nm"/></td>
      </tr>

      <xsl:choose>
        <xsl:when test="p:DbtrAgt/p:FinInstnId/p:BIC">
          <xsl:variable name="bic" select="p:DbtrAgt/p:FinInstnId/p:BIC"/>
          <tr>
            <td class="label">BIC</td>
            <td>
              <xsl:choose>
                <xsl:when test="string-length($bic) = 8 or string-length($bic) = 11">
                  <xsl:value-of select="$bic"/>
                </xsl:when>
                <xsl:otherwise>
                  <span class="non-normalise"><xsl:value-of select="$bic"/></span>
                </xsl:otherwise>
              </xsl:choose>
            </td>
          </tr>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="agentId" select="p:DbtrAgt/p:FinInstnId/p:Id"/>
          <tr>
            <td class="label">Agent</td>
            <td>
              <xsl:choose>
                <xsl:when test="$agentId = 'NOTPROVIDED' or string-length($agentId) = 0">
                  <span class="non-normalise"><xsl:value-of select="$agentId"/></span>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="$agentId"/>
                </xsl:otherwise>
              </xsl:choose>
            </td>
          </tr>
        </xsl:otherwise>
      </xsl:choose>

      <xsl:choose>
        <xsl:when test="p:DbtrAcct/p:Id/p:IBAN">
          <xsl:variable name="iban" select="p:DbtrAcct/p:Id/p:IBAN"/>
          <tr>
            <td class="label">IBAN</td>
            <td>
              <xsl:choose>
                <xsl:when test="matches($iban, '^[A-Z]{2}[0-9]{2}[A-Z0-9]{11,30}$')">
                  <xsl:value-of select="$iban"/>
                </xsl:when>
                <xsl:otherwise>
                  <span class="non-normalise"><xsl:value-of select="$iban"/></span>
                </xsl:otherwise>
              </xsl:choose>
            </td>
          </tr>
        </xsl:when>
        <xsl:otherwise>
          <tr>
            <td class="label">ID</td>
            <td>
              <span class="non-normalise">
                <xsl:value-of select="p:DbtrAcct/p:Id/p:PrvtId"/>
              </span>
            </td>
          </tr>
        </xsl:otherwise>
      </xsl:choose>

    </table>
  </xsl:template>

  <xsl:template name="infoCommentaire">
    <p class="sous-titre">Commentaire</p>
    <p class="commentaire"><xsl:value-of select="p:RmtInf"/></p>
  </xsl:template>

  <xsl:template name="formatDate">
    <xsl:param name="date"/>

    <xsl:variable name="annee"    select="substring($date, 1, 4)"/>
    <xsl:variable name="moisNum"  select="substring($date, 6, 2)"/>
    <xsl:variable name="jourBrut" select="substring($date, 9, 2)"/>

    <xsl:variable name="jour">
      <xsl:choose>
        <xsl:when test="starts-with($jourBrut, '0')">
          <xsl:value-of select="substring($jourBrut, 2)"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$jourBrut"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="mois">
      <xsl:choose>
        <xsl:when test="$moisNum = '01'">janv</xsl:when>
        <xsl:when test="$moisNum = '02'">févr</xsl:when>
        <xsl:when test="$moisNum = '03'">mars</xsl:when>
        <xsl:when test="$moisNum = '04'">avr</xsl:when>
        <xsl:when test="$moisNum = '05'">mai</xsl:when>
        <xsl:when test="$moisNum = '06'">juin</xsl:when>
        <xsl:when test="$moisNum = '07'">juil</xsl:when>
        <xsl:when test="$moisNum = '08'">août</xsl:when>
        <xsl:when test="$moisNum = '09'">sept</xsl:when>
        <xsl:when test="$moisNum = '10'">oct</xsl:when>
        <xsl:when test="$moisNum = '11'">nov</xsl:when>
        <xsl:when test="$moisNum = '12'">déc</xsl:when>
        <xsl:otherwise><xsl:value-of select="$moisNum"/></xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:value-of select="$jour"/>
    <xsl:text>-</xsl:text>
    <xsl:value-of select="$mois"/>
    <xsl:text>-</xsl:text>
    <xsl:value-of select="$annee"/>
  </xsl:template>

</xsl:stylesheet>
