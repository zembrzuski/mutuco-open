package com.zembrzuski.loader.filesystemloader.domain.exception

/**
 * Exceções de negócios que não devem travar o processamento.
 */

class InvalidCompanyException(message: String) : RuntimeException(message)

