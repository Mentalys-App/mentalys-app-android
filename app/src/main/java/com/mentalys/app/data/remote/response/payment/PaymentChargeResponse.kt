package com.mentalys.app.data.remote.response.payment

data class PaymentChargeResponse(
    val message: String? = null,
    val token: String? = null,
    val redirect_url: String? = null
)