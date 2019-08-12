private fun handleMobilePayPayment() {
    val createPaymentParams = QPCreatePaymentParameters("DKK", createRandomOrderId())
    val createPaymentRequest = QPCreatePaymentRequest(createPaymentParams)

    createPaymentRequest.sendRequest(listener = { payment ->
        QuickPay.log("Create Payment Success - Id = ${payment.id}")
        currentPaymentId = payment.id

        val mobilePayParameters = MobilePayParameters("quickpayexample://mobilepayreturn", "dk", "https://quickpay.net/images/payment-methods/payment-methods.png")
        val createSessionParameters = QPCreatePaymentSessionParameters(100, mobilePayParameters)
        val createSessionRequest = QPCreatePaymentSessionRequest(payment.id, createSessionParameters)

        createSessionRequest.sendRequest(listener = { payment ->
            QuickPay.log("Create Session Success")
            QuickPay.instance.authorizeWithMobilePay(payment, this)
        }, errorListener = ::printError)
    }, errorListener = ::printError)
}


fun onPaymentButtonClicked(v: View) {
    if (selectedPaymentMethod != null) {
        progressBar?.visibility = View.VISIBLE
        checkoutButton?.isEnabled = false
    }

    if (selectedPaymentMethod == PaymentMethod.PAYMENTCARD) {
        handleCreditCardPayment()
    }
    else if (selectedPaymentMethod == PaymentMethod.MOBILEPAY) {
        handleMobilePayPayment()
    }
}


private fun onMobilePayReturn(intent: Intent) {
    if (intent.data == null) {
        return
    }

    if (intent.data?.scheme?.equals("quickpayexample") == true) {
        QuickPay.log("MainActivity id: $currentPaymentId")
        QuickPay.log("Intent: $intent")
    }
}
