package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.mydomain.BalancoRaw
import com.zembrzuski.loader.filesystemloader.repository.filesystem.BalancesFilesystemRepository
import com.zembrzuski.loader.filesystemloader.service.CapitalSocialRetriever
import com.zembrzuski.loader.filesystemloader.util.TipoDemonstrativoConverter
import com.zembrzuski.loader.filesystemloader.util.XmlParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BalanceLoader @Autowired constructor(
        val balancesFilesystemRepository: BalancesFilesystemRepository,
        val xmlParser: XmlParser,
        val tipoDemonstrativoConverter: TipoDemonstrativoConverter,
        val capitalSocialRetriever: CapitalSocialRetriever) {

    fun loadBalance(numeroDocumento: Long, cvmCode: String): BalancoRaw {
        val xmls = balancesFilesystemRepository.openBalanco(cvmCode, numeroDocumento)

        val formularioCadastral =
                xmlParser.parseFormularioCadastral(xmlParser.getKey("outer-FormularioCadastral.xml", xmls))

        val infoFinanceiras = xmlParser.parseInfoFinaDFin(
                xmlParser.getKey(tipoDemonstrativoConverter.getInfoFinaDFinKey(xmls), xmls))

        val formularioDemonstrativo = xmlParser.parseFormularioDemonstracaoFinanceira(
                xmlParser.getKey(tipoDemonstrativoConverter.getFormularioDemonstracaoFinanceiraKey(xmls), xmls))

        val capitalSocial = xmlParser.parseComposicaoCapitalSocial(
                xmlParser.getKey(tipoDemonstrativoConverter.getCapitalSocialKey(xmls), xmls))

        return BalancoRaw(
                formularioCadastral.companhiaAberta.codigoCvm,
                formularioCadastral.companhiaAberta.nomeEmpresa,
                formularioDemonstrativo.documento.dataReferenciaDocumento,
                tipoDemonstrativoConverter.fromXmls(xmls),
                infoFinanceiras.infoFinanceiras,
                numeroDocumento,
                capitalSocialRetriever.getCapitalSocial(capitalSocial),
                formularioDemonstrativo.documento.codigoEscalaMoeda,
                formularioDemonstrativo.documento.codigoEscalaQuantidade,
                formularioCadastral.dataEntrega)
    }

}
