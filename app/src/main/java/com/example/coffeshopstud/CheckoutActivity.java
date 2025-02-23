package com.example.coffeshopstud;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Элементы интерфейса
    private TextView totalPriceTextView;
    private TextView deliveryCostTextView;
    private TextView totalWithDeliveryTextView;
    private TextView finalTotalTextView;
    private EditText cardNumber;
    private EditText expiryDate;
    private EditText cvv;
    private EditText cardHolderName;
    private Button confirmPaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Инициализация элементов интерфейса
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        deliveryCostTextView = findViewById(R.id.deliveryCostTextView);
        totalWithDeliveryTextView = findViewById(R.id.totalWithDeliveryTextView);
        finalTotalTextView = findViewById(R.id.finalTotalTextView);
        cardNumber = findViewById(R.id.cardNumber);
        expiryDate = findViewById(R.id.expiryDate);
        cvv = findViewById(R.id.cvv);
        cardHolderName = findViewById(R.id.cardHolderName);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);

        // Получаем сумму заказа из Intent
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Устанавливаем значения текстовых полей
        totalPriceTextView.setText(String.format("₽%.2f", totalPrice));
        deliveryCostTextView.setText(String.format("₽%.2f", 200.0));
        double totalWithDelivery = totalPrice + 200.0;
        totalWithDeliveryTextView.setText(String.format("₽%.2f", totalWithDelivery));
        finalTotalTextView.setText(String.format("₽%.2f", totalWithDelivery));

        // Настройка форматирования номера карты
        cardNumber.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;

                String cleanInput = s.toString().replaceAll("\\s+", ""); // Удаляем пробелы
                String formattedCardNumber = formatCardNumber(cleanInput);

                isUpdating = true;
                cardNumber.setText(formattedCardNumber);
                cardNumber.setSelection(formattedCardNumber.length());
                isUpdating = false;
            }
        });

        // Настройка форматирования срока действия
        expiryDate.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.equals(current)) {
                    if (input.length() == 2 && current.length() == 1) {
                        input += "/";
                    }
                    if (input.length() > 5) {
                        input = input.substring(0, 5); // Ограничиваем длину до 5 символов
                    }
                    current = input;
                    expiryDate.setText(input);
                    expiryDate.setSelection(input.length());
                }
            }
        });

        // Обработка нажатия кнопки подтверждения оплаты
        confirmPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    // Создание объекта заказа
                    Order order = new Order(
                            totalPrice,
                            200.0,
                            totalWithDelivery,
                            cardNumber.getText().toString().replaceAll("\\s+", ""), // Удаляем пробелы
                            expiryDate.getText().toString(),
                            cvv.getText().toString(),
                            cardHolderName.getText().toString()
                    );
                    // Отправка заказа в Firebase
                    sendOrderToFirebase(order);
                }
            }
        });
    }

    // Метод для форматирования номера карты
    private String formatCardNumber(String cardNumber) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" "); // Добавляем пробел каждые 4 цифры
            }
            formatted.append(cardNumber.charAt(i));
        }
        return formatted.toString();
    }

    // Метод для валидации полей формы
    private boolean validateFields() {
        if (cardNumber.getText().toString().replaceAll("\\s+", "").isEmpty() || cardNumber.getText().toString().replaceAll("\\s+", "").length() != 16) {
            cardNumber.setError("Введите корректный номер карты (16 цифр)");
            return false;
        }

        String expiry = expiryDate.getText().toString();
        if (expiry.isEmpty() || expiry.length() != 5 || !expiry.matches("\\d{2}/\\d{2}")) {
            expiryDate.setError("Введите срок действия в формате MM/YY");
            return false;
        }

        try {
            int month = Integer.parseInt(expiry.split("/")[0]);
            int year = Integer.parseInt(expiry.split("/")[1]);
            if (month < 1 || month > 12 || year < 23 || year > 99) { // Проверяем актуальность даты
                expiryDate.setError("Неверная дата");
                return false;
            }
        } catch (Exception e) {
            expiryDate.setError("Неверный формат даты");
            return false;
        }

        if (cvv.getText().toString().isEmpty() || cvv.getText().toString().length() != 3) {
            cvv.setError("Введите CVV (3 цифры)");
            return false;
        }

        if (cardHolderName.getText().toString().isEmpty()) {
            cardHolderName.setError("Введите имя держателя карты");
            return false;
        }

        return true;
    }

    // Метод для отправки заказа в Firebase
    private void sendOrderToFirebase(Order order) {
        DocumentReference orderRef = db.collection("orders").document();
        orderRef.set(order)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Заказ успешно сохранен в Firestore");
                    Toast.makeText(this, "Оплата прошла успешно", Toast.LENGTH_SHORT).show();
                    // Переход на страницу успешной оплаты
                    Intent intent = new Intent(CheckoutActivity.this, succsesful_pay.class);
                    startActivity(intent);
                    finish(); // Закрываем текущую активность
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Ошибка при сохранении заказа", e);
                    Toast.makeText(this, "Произошла ошибка при оплате", Toast.LENGTH_SHORT).show();
                });
    }
}

// Класс для представления заказа
class Order {
    private double totalPrice;
    private double deliveryCost;
    private double totalWithDelivery;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String cardHolderName;

    public Order() {}

    public Order(double totalPrice, double deliveryCost, double totalWithDelivery, String cardNumber, String expiryDate, String cvv, String cardHolderName) {
        this.totalPrice = totalPrice;
        this.deliveryCost = deliveryCost;
        this.totalWithDelivery = totalWithDelivery;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.cardHolderName = cardHolderName;
    }

    // Геттеры и сеттеры
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public double getTotalWithDelivery() {
        return totalWithDelivery;
    }

    public void setTotalWithDelivery(double totalWithDelivery) {
        this.totalWithDelivery = totalWithDelivery;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
}