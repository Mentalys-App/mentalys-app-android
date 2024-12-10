package com.mentalys.app.data.remote.request.payment

data class PaymentChargeRequest(
    val psychiatristId: String,
    val phoneNumber: String? = null
)