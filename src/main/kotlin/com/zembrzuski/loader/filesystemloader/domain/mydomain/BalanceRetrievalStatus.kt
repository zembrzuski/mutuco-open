package com.zembrzuski.loader.filesystemloader.domain.mydomain

data class BalanceRetrievalStatus(
        val fileStatus: FileStatus,
        val cvmCode: String? = null,
        val balanceId: Long
)

enum class FileStatus {

    FILE_IS_A_BALANCE,
    FILE_IS_NOT_A_BALANCE,
    FILE_NOT_FOUND

}