package com.zembrzuski.loader.filesystemloader.job

import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformationEnum
import com.zembrzuski.loader.filesystemloader.service.GeneralCompanyInformationExtractor
import junit.framework.TestCase.assertEquals
import org.junit.Test


internal class GeneralCompanyInformationExtractorTest {

    private val generalCompanyInformationExtractor = GeneralCompanyInformationExtractor()

    @Test
    fun extractClassificacaoSetorialFromHering() {
        // given
        val input = heringInput

        // when
        val retrievedClassificaoSetorial = generalCompanyInformationExtractor.extract(
                GeneralCompanyInformationEnum.SECTOR, input, "14761", 2)

        // then
        assertEquals(retrievedClassificaoSetorial, "Consumo Cíclico / Tecidos. Vestuário e Calçados / Vestuário")
    }

    @Test
    fun extractClassificacaoSetorialFromDimed() {
        // given
        val input = dimedInput

        // when
        val retrievedClassificaoSetorial = generalCompanyInformationExtractor.extract(
                GeneralCompanyInformationEnum.SECTOR, input, "9342", 2)

        // then
        assertEquals(retrievedClassificaoSetorial, "Saúde / Comércio e Distribuição / Medicamentos e Outros Produtos")
    }

    @Test
    fun extractSiteFromHering() {
        // given
        val input = heringInput

        // when
        val retrievedSite = generalCompanyInformationExtractor.extract(
                GeneralCompanyInformationEnum.SITE, input, "14761", 2)

        // then
        assertEquals(retrievedSite, "www.ciahering.com.br")
    }

    @Test
    fun extractSiteFromDimed() {
        // given
        val input = dimedInput

        // when
        val retrievedSite = generalCompanyInformationExtractor.extract(
                GeneralCompanyInformationEnum.SITE, input, "14761", 2)

        // then
        assertEquals(retrievedSite, "www.panvel.com.br")
    }

    @Test
    fun extractPapeisPnvl() {
        // given
        val input = dimedInput

        // when
        val retrievedCodes = generalCompanyInformationExtractor.extractNegotiationCode(
                GeneralCompanyInformationEnum.NEGOCIATION_CODE, input, "14761")

        // then
        assertEquals(retrievedCodes, listOf("PNVL3", "PNVL4"))
    }

    @Test
    fun extractPapeisHering() {
        // given
        val input = heringInput

        // when
        val retrievedCodes = generalCompanyInformationExtractor.extractNegotiationCode(
                GeneralCompanyInformationEnum.NEGOCIATION_CODE, input, "14761")

        // then
        assertEquals(retrievedCodes, listOf("HGTX3"))
    }

    @Test
    fun extractAtividadePrincipalFromDimed() {
        // given
        val input = dimedInput

        // when
        val retrievedSite = generalCompanyInformationExtractor.extract(
                GeneralCompanyInformationEnum.MAIN_ACTIVITY, input, "14761", 2)

        // then
        assertEquals(retrievedSite, "Comercio Atacadista E Varejista de Medicamentos E Produtos de Perfumaria E Toucador")
    }

    @Test
    fun extractAtividadePrincipalFromHering() {
        // given
        val input = heringInput

        // when
        val retrievedSite = generalCompanyInformationExtractor.extract(
                GeneralCompanyInformationEnum.MAIN_ACTIVITY, input, "14761", 2)

        // then
        assertEquals(retrievedSite, "Confecção de Artigos Do Vestuário E Acessórios")
    }

    @Test
    fun extractNomePregaoFromHering() {
        // given
        val input = heringInput

        // when
        val retrievedSite = generalCompanyInformationExtractor.extract(
                GeneralCompanyInformationEnum.NEGOCIATION_NAME, input, "14761", 2)

        // then
        assertEquals(retrievedSite, "CIA HERING")
    }

    @Test
    fun extractNomePregaoFromDimed() {
        // given
        val input = dimedInput

        // when
        val retrievedSite = generalCompanyInformationExtractor.extract(
                GeneralCompanyInformationEnum.NEGOCIATION_NAME, input, "14761", 2)

        // then
        assertEquals(retrievedSite, "DIMED")
    }

    private val heringInput =
            "<body onload=\"parent.resizeFrame(parent.document.getElementById('ctl00_contentPlaceHolderConteudo_iframeCarregadorPaginaExterna'))\">\n" +
            "<!-- Google Tag Manager -->\n" +
            "<noscript><iframe src=\"//www.googletagmanager.com/ns.html?id=GTM-KPF8G3\"\n" +
            "height=\"0\" width=\"0\" style=\"display:none;visibility:hidden\"></iframe></noscript>\n" +
            "<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':\n" +
            "new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],\n" +
            "j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=\n" +
            "'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);\n" +
            "})(window,document,'script','dataLayer','GTM-KPF8G3');</script>\n" +
            "<!-- End Google Tag Manager -->  \n" +
            "\n" +
            "<div class=\"row\">\n" +
            "<div class=\"large-9 columns\">\n" +
            "        <p class=\"legenda\">Atualizado em 02/09/2019, às 00h44</p>\n" +
            "    </div>              \n" +
            "\n" +
            "<div class=\"large-3 columns text-right\">\n" +
            "        <div id=\"divImgSegmento\">\n" +
            "            \n" +
            "            <p><img id=\"Img1\" src=\"/aspnet_client/system_web/2_0_50727/Themes/SiteBmfBovespa/img/InfEmpLogoNovoMercado.png\" /></p>\n" +
            "            \n" +
            "        </div>\n" +
            "    </div>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "<div class=\"row\">                \n" +
            "<div  class=\"large-8 columns\">\n" +
            "        <ul class=\"accordion\" data-accordion>\n" +
            "            <li class=\"accordion-navigation\">\n" +
            "            \n" +
            "            <a onclick=\"\$('#accordionDados').toggle();\">Dados da Companhia</a>\n" +
            "            \n" +
            "            <div id=\"accordionDados\" class=\"content active\">\n" +
            "            <table class=\"ficha responsive\">\n" +
            "            <tr>\n" +
            "                <td>Nome de Pregão:</td>\n" +
            "                <td>CIA HERING</td>\n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>Códigos de Negociação:</td>\n" +
            "                <td>\n" +
            "                <a onclick=\"\$('#divCodigosOculto').toggle()\"><strong>Mais Códigos</strong></a><br />\n" +
            "                <a href='javascript:;' class=LinkCodNeg onclick=javascript:VerificaCotacao('HGTX3','0');>HGTX3</a>\n" +
            "                <span id=\"spnCodigosOculto\"></span>\n" +
            "                <div id=\"divCodigosOculto\" class=\"hide\">\n" +
            "                \n" +
            "                <a href=\"javascript: void(0);\" class=\"LinkCodNeg\" onclick=\"VerificaCotacao('HGTX3', '0');\">HGTX3</a>\n" +
            "                \n" +
            "                <br />\n" +
            "                <strong>Códigos ISIN:</strong><br />\n" +
            "                BRHGTXACNOR9<br />\n" +
            "                <!-- Não mostra o CVM para Cias Dispensadas -->\n" +
            "                \n" +
            "                <br />\n" +
            "                <strong>Códigos CVM:</strong><br />\n" +
            "                14761<br />\n" +
            "                \n" +
            "                </div>\n" +
            "                \n" +
            "                </td>\n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>CNPJ:</td>\n" +
            "                <td>78.876.950/0001-71</td>\n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>Atividade Principal:</td>\n" +
            "                <td>Confecção de Artigos Do Vestuário E Acessórios</td>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <td>Classificação Setorial:</td>\n" +
            "                \n" +
            "                <td>Consumo Cíclico / Tecidos. Vestuário e Calçados / Vestuário</td>\n" +
            "                \n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>Site:</td>\n" +
            "                <td><a target=\"_blank\" href=\"http://www.ciahering.com.br\">www.ciahering.com.br</a></td>\n" +
            "            </tr>\n" +
            "            \n" +
            "            </table>\n" +
            "            </div>\n" +
            "            </li>\n" +
            "                <!-- FIM TAB DADOS DA COMPANIA -->\n" +
            "            <li class=\"accordion-navigation\">\n" +
            "                <a onclick=\"\$('#divContatos').toggle()\">Contatos</a>\n" +
            "                    <div id=\"divContatos\" class=\"content\">\n" +
            "\t\t\t\t\t \n" +
            "                        <div>\n" +
            "                            <table class=\"ficha responsive\">\n" +
            "                                <caption>Escriturador (Departamento de Acionistas)</caption>\n" +
            "                                \n" +
            "                                <tr>\n" +
            "                                    <td>Instituição:</td>\n" +
            "                                    <td>\n" +
            "                                        ITAU CORRETORA ACOES\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                                \n" +
            "                            </table>\n" +
            "                        </div>\n" +
            "                        \n" +
            "\t\t\t\t\t\t</br>\n" +
            "                        \t\t\t\t\t\t\n" +
            "                    </div>\n" +
            "\t\t\t\t\t</li>\n" +
            "                </ul>\n" +
            "                                    \n" +
            "\t\t\t    \n" +
            "                <!-- BOVESPA MAIS  -->\n" +
            "                \n" +
            "                <!-- FIM BOVESPA MAIS -->\n" +
            "\t\t\t\t\n" +
            "<script type=\"text/javascript\">iframe_resize();</script>\n" +
            "</body>\n" +
            "</html>\n"

    private val dimedInput =
            "<body onload=\"parent.resizeFrame(parent.document.getElementById('ctl00_contentPlaceHolderConteudo_iframeCarregadorPaginaExterna'))\">\n" +
            "<!-- Google Tag Manager -->\n" +
            "<noscript><iframe src=\"//www.googletagmanager.com/ns.html?id=GTM-KPF8G3\"\n" +
            "height=\"0\" width=\"0\" style=\"display:none;visibility:hidden\"></iframe></noscript>\n" +
            "<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':\n" +
            "new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],\n" +
            "j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=\n" +
            "'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);\n" +
            "})(window,document,'script','dataLayer','GTM-KPF8G3');</script>\n" +
            "<!-- End Google Tag Manager -->  \n" +
            "\n" +
            "<div class=\"row\">\n" +
            "<div class=\"large-9 columns\">\n" +
            "        <p class=\"legenda\">Atualizado em 02/09/2019, às 00h46</p>\n" +
            "    </div>              \n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "<div class=\"row\">                \n" +
            "<div  class=\"large-8 columns\">\n" +
            "        <ul class=\"accordion\" data-accordion>\n" +
            "            <li class=\"accordion-navigation\">\n" +
            "            \n" +
            "            <a onclick=\"\$('#accordionDados').toggle();\">Dados da Companhia</a>\n" +
            "            \n" +
            "            <div id=\"accordionDados\" class=\"content active\">\n" +
            "            <table class=\"ficha responsive\">\n" +
            "            <tr>\n" +
            "                <td>Nome de Pregão:</td>\n" +
            "                <td>DIMED</td>\n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>Códigos de Negociação:</td>\n" +
            "                <td>\n" +
            "                <a onclick=\"\$('#divCodigosOculto').toggle()\"><strong>Mais Códigos</strong></a><br />\n" +
            "                <a href='javascript:;' class=LinkCodNeg onclick=javascript:VerificaCotacao('PNVL3','0');>PNVL3</a>; <a href='javascript:;' class=LinkCodNeg onclick=javascript:VerificaCotacao('PNVL4','0');>PNVL4</a>\n" +
            "                <span id=\"spnCodigosOculto\"></span>\n" +
            "                <div id=\"divCodigosOculto\" class=\"hide\">\n" +
            "                \n" +
            "                <a href=\"javascript:void(0);\" class=\"LinkCodNeg\" onclick=\"VerificaCotacao('PNVL3', '0');\">PNVL3</a>;\n" +
            "                \n" +
            "                <br />\n" +
            "                <strong>Códigos ISIN:</strong><br />\n" +
            "                BRPNVLACNOR9,  BRPNVLACNPR6<br />\n" +
            "                <!-- Não mostra o CVM para Cias Dispensadas -->\n" +
            "                \n" +
            "                <br />\n" +
            "                <strong>Códigos CVM:</strong><br />\n" +
            "                9342<br />\n" +
            "                \n" +
            "                </div>\n" +
            "                \n" +
            "                </td>\n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>CNPJ:</td>\n" +
            "                <td>92.665.611/0001-77</td>\n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>Atividade Principal:</td>\n" +
            "                <td>Comercio Atacadista E Varejista de Medicamentos E Produtos de Perfumaria E Toucador</td>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <td>Classificação Setorial:</td>\n" +
            "                \n" +
            "                <td>Sa&uacute;de / Com&eacute;rcio e Distribuição / Medicamentos e Outros Produtos</td>\n" +
            "                \n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>Site:</td>\n" +
            "                <td><a target=\"_blank\" href=\"http://www.panvel.com.br\">www.panvel.com.br</a></td>\n" +
            "            </tr>\n" +
            "            \n" +
            "            </table>\n" +
            "            </div>\n" +
            "            </li>\n" +
            "                <!-- FIM TAB DADOS DA COMPANIA -->\n" +
            "            <li class=\"accordion-navigation\">\n" +
            "                <a onclick=\"\$('#divContatos').toggle()\">Contatos</a>\n" +
            "                    <div id=\"divContatos\" class=\"content\">\n" +
            "\t\t\t\t\t \n" +
            "                        <div>\n" +
            "                            <table class=\"ficha responsive\">\n" +
            "                                <caption>Escriturador (Departamento de Acionistas)</caption>\n" +
            "                                \n" +
            "                                <tr>\n" +
            "                                    <td>Instituição:</td>\n" +
            "                                    <td>\n" +
            "                                        BRADESCO\n" +
            "                                    </td>\n" +
            "                                </tr>\n" +
            "                                \n" +
            "                            </table>\n" +
            "                        </div>\n" +
            "                        \n" +
            "\t\t\t\t\t\t</br>\n" +
            "                        \t\t\t\t\t\t\n" +
            "                    </div>\n" +
            "\t\t\t\t\t</li>\n" +
            "                </ul>\n" +
            "                                    \n" +
            "\t\t\t    \n" +
            "                <!-- BOVESPA MAIS  -->\n" +
            "                \n" +
            "                <!-- FIM BOVESPA MAIS -->\n" +
            "</body>\n" +
            "</html>\n"
}
