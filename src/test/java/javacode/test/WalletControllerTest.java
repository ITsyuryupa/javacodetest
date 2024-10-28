package javacode.test;
import javacode.test.сontroller.WalletController;

import javacode.test.service.WalletService;
import javacode.test.util.WalletOperationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    private UUID walletId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletId = UUID.fromString("35372aa5-ef58-4c85-ba14-72ba4e8c8212");
    }

    @Test
    void testGetAndUpdateBalance() {
        // Начальный баланс
        BigDecimal initialBalance = BigDecimal.valueOf(100.00);
        // Сумма для пополнения
        BigDecimal amountToAdd = BigDecimal.valueOf(50.00);
        // Ожидаемый баланс после пополнения
        BigDecimal expectedBalance = initialBalance.add(amountToAdd);

        // Настройка моков
        when(walletService.getBalance(walletId)).thenReturn(initialBalance);

        // Получаем баланс
        ResponseEntity<BigDecimal> getBalanceResponse = walletController.getBalance(walletId);
        assertEquals(HttpStatus.OK, getBalanceResponse.getStatusCode());
        assertEquals(initialBalance, getBalanceResponse.getBody());

        // Пополняем баланс
        WalletOperationRequest request = new WalletOperationRequest(walletId, "DEPOSIT", amountToAdd);
        ResponseEntity<String> processOperationResponse = walletController.processOperation(request);
        assertEquals(HttpStatus.OK, processOperationResponse.getStatusCode());
        assertEquals("Operation successful", processOperationResponse.getBody());

        // Проверяем обновленный баланс
        when(walletService.getBalance(walletId)).thenReturn(expectedBalance);
        ResponseEntity<BigDecimal> updatedBalanceResponse = walletController.getBalance(walletId);
        assertEquals(HttpStatus.OK, updatedBalanceResponse.getStatusCode());
        assertEquals(expectedBalance, updatedBalanceResponse.getBody());
    }
}
