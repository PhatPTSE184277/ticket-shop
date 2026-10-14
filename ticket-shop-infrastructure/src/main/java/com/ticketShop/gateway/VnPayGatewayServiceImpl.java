package com.ticketShop.gateway;

import com.ticketShop.model.entity.PaymentTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class VnPayGatewayServiceImpl {

    @Value("${vnpay.tmn-code}")
    private String tmnCode;

    @Value("${vnpay.secret-key}")
    private String secretKey;

    @Value("${vnpay.payment-url}")
    private String paymentUrl;

    @Value("${vnpay.return-url}")
    private String returnUrl;

    @Value("${vnpay.currency-code:VND}")
    private String currencyCode;

    @Value("${vnpay.locale:vn}")
    private String locale;

    @Value("${vnpay.bank-code:NCB}")
    private String bankCode;

    public String createPaymentUrl(PaymentTransaction transaction)
            throws UnsupportedEncodingException {

        Map<String, String> params = new TreeMap<>();

        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount",
                transaction.getAmount()
                        .multiply(new java.math.BigDecimal("100"))
                        .toBigInteger()
                        .toString());
        params.put("vnp_CurrCode", currencyCode);
        params.put("vnp_BankCode", bankCode);

        String txnRef = transaction.getPaymentId()
                .substring(transaction.getPaymentId().length() - 12);

        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderType", "other");
        params.put("vnp_OrderInfo",
                "Thanh toan don hang: " + transaction.getOrderId());
        params.put("vnp_Locale", locale);
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_CreateDate",
                LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();

            if (fieldValue != null && !fieldValue.isEmpty()) {
                String encodedName = URLEncoder.encode(
                        fieldName, StandardCharsets.US_ASCII.toString());

                String encodedValue = URLEncoder.encode(
                                fieldValue, StandardCharsets.US_ASCII.toString())
                        .replace("+", "%20");

                hashData.append(encodedName)
                        .append("=")
                        .append(encodedValue)
                        .append("&");

                query.append(encodedName)
                        .append("=")
                        .append(encodedValue)
                        .append("&");
            }
        }

        hashData.deleteCharAt(hashData.length() - 1);
        query.deleteCharAt(query.length() - 1);

        String secureHash = hmacSHA512(secretKey, hashData.toString());

        return paymentUrl + "?" + query
                + "&vnp_SecureHash=" + secureHash;
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");

            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA512");

            hmac512.init(secretKeySpec);

            byte[] hashBytes = hmac512.doFinal(
                    data.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();

            for (byte b : hashBytes) {
                result.append(String.format("%02x", b & 0xff));
            }

            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo VNPAY signature", e);
        }
    }
}