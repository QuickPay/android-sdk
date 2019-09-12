# QuickPay SDK

The QuickPay SDK wraps the [QuickPay API](https://learn.quickpay.net/tech-talk/api/services/#services "QuickPay API") and provides the necessary functionality and convenience to add native payments to your app.


## Installation

You can find the newest version of the QuickPay SDK on the GitHub repo. We are working on releasing it through a package manager in the future.


### AndroidManifest

You will need to add an activity to your AndroidManifest.xml in order to show the payment window.
```xml
<activity android:name="net.quickpay.quickpaysdk.QuickPayActivity" />
```


### API key

In order for the SDK to communicate with QuickPay, you will need an API key. You can create one by logging in to your QuickPay account and navigate to Settings -> Users.


## Usage

This guide will take you through the steps needed to integrate the QuickPay SDK with your code and demonstrate how to make basic payments with the different payment methods the SDK supports.


### Initialization

In your MainActivity, you need to initialize the SDK with your API key and Context.
```kotlin
QuickPay.init(apiKey: String, context: Context)
```


### Payment flow

To make a payment and authorize it you need to follow these four steps
1. Create a payment
2. Create a payment session
3. Authorize the payment
4. Check the payment status to see if the authorization went well

All payments need to go through these four steps but some services, like the payment window, will handle multiple of these steps in one request.


### Payment Window

The payment windows are the easiest and quickest way to get payments up and running, it is currently also the only way you can accept payments with credit cards through the QuickPay SDK. The payment window handles step 2 and 3 of the payment flow for you, so the order of operations looks like this.

1. Create payment
2. Generate a payment URL and display the payment window
3. Check the payment status

To create a payment you first need to specify some parameters which are wrapped in the `QPCreatePaymentParameters` class. Afterward, you pass the parameters to the constructor of a `QPCreatePaymentRequest`. Last you need to send the request to QuickPay, this is done with the `sendRequest` function on the request itself which requires a success and failure handler.

```kotlin
val params = QPCreatePaymentParameters(currency: "DKK", order_id: "SomeOrderId")
val request = QPCreatePaymentRequest(params)

request.sendRequest(listener = { payment ->
    // Handle the payment
}, errorListener = { statusCode, message, error ->
    // Handle the failure
})
```

If this succeeds a `QPPayment` will be given to you in the success listener. The next step is to generate a payment URL that you will need in order to display the web-based payment window. The needed parameters for this request are wrapped in `QPCreatePaymentLinkParameters` and is needed in the constructor of a `QPCreatePaymentLinkRequest`. The parameters need a payment id and the amount it needs to authorize. Send the request and wait for the response.

```kotlin
val linkParams = QPCreatePaymentLinkParameters(payment.id, 100.0)
val linkRequest = QPCreatePaymentLinkRequest(linkParams)

linkRequest.sendRequest(listener = { paymentLink ->
    // Handle the paymentLink
}, errorListener = { statusCode, message, error ->
    // Handle the failure
})
```

The last step is to use the `QPPaymentLink` to open the payment window. This is done by passing the paymentLink to the QuickPay class and then wait for the activity result.

```kotlin
QuickPayActivity.openQuickPayPaymentWindow(this, paymentLink)
```

Now you override the onActivityResult function and check if the requestCode matches the QuickPayActivity and then use the data in the intent to figure out if the payment went through.

```kotlin
public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
  if (requestCode == QuickPayActivity.QUICKPAY_INTENT_CODE) {
     if (resultCode == Activity.RESULT_OK) {
          val returnedResult = intent?.data?.toString() ?: ""

          if (returnedResult == QuickPayActivity.SUCCESS_RESULT) {
            // Handle the success
          }
          else if (returnedResult == QuickPayActivity.FAILURE_RESULT) {
            // Handle the failure
          }
      }
      else if (resultCode == Activity.RESULT_CANCELED) {
        // Handle the cancel
      }
  }
}
```

If the result is a success the payment has been handled but we do not yet know if the payment has actually been authorized. For that, we need to check the status of the payment which is done with the `QPGetPaymentRequest`.

```kotlin
QPGetPaymentRequest(paymentId).sendRequest(listener = { payment ->
    if (payment.accepted) {
        // The payment has been authorized 👍
    }
}, errorListener = { statusCode, message, error ->
    // Handle the failure
})
```
