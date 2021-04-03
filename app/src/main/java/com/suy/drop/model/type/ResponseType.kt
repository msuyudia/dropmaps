package com.suy.drop.model.type

enum class ResponseType(val message: String) {
    OK("Success"), ZERO_RESULTS("Data tidak ditemukan"),
    OVER_DAILY_LIMIT("Pemakaian harian aplikasi sudah mencapai batas, silahkan gunakan lagi besok"),
    OVER_QUERY_LIMIT("Pemakaian harian fitur ini sudah mencapai batas, silahkan gunakan lagi besok"),
    REQUEST_DENIED("Maaf, permintaan ditolak, silahkan hubungi developer"),
    INVALID_REQUEST("Maaf, permintaan tidak valid, silahkan hubungi developer"),
    UNKNOWN_ERROR("Maaf, server sedang ada masalah"),
    NOT_FOUND("Maaf, alamat sudah tidak dipindahkan atau hilang dari server"),
    NULL_ERROR("Maaf, sesuatu error terjadi, silahkan hubungi developer")
}