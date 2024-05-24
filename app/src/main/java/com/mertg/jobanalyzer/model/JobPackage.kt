package com.mertg.jobanalyzer.model

import java.util.Date

data class JobPackage(
    val isEmriKod: String,
    val islemMerkeziKod: String,
    val yapilanIslem: String,
    val calisanNo: String,
    val startTime: Date
)
