package com.mertg.jobanalyzer.model

import java.util.Date

data class JobPackage(
    val isemriKod: String = "",
    val islemMerkeziKod: String = "",
    val yapilanIslem: String = "",
    val calisanNo: String = "",
    val baslangicTarihi: Date = Date(),
    val bitisTarihi: Date? = null,
    val durdurulmaSuresi: String = "",
    val gecirilenTumZaman: String = "",
    val calismaSuresi: String = "",
    val hataNedenleri: String = "",
    val fireSayisi: String = "",
    val uretilenMiktar: String = ""
)
