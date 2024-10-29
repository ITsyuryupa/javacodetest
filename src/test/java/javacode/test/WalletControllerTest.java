package javacode.test;

import javacode.test.сontroller.WalletController;
import javacode.test.service.WalletService;
import javacode.test.util.WalletOperationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("testGetBalance")
    void testGetBalance() {
        BigDecimal initialBalance = BigDecimal.valueOf(100.00);
        when(walletService.getBalance(walletId)).thenReturn(initialBalance);

        ResponseEntity<BigDecimal> getBalanceResponse = walletController.getBalance(walletId);

        assertEquals(HttpStatus.OK, getBalanceResponse.getStatusCode());
        assertEquals(initialBalance, getBalanceResponse.getBody());

        verify(walletService, times(1)).getBalance(walletId);
    }

    @Test
    @DisplayName("testDepositBalance")
    void testDepositBalance() {
        BigDecimal amountToAdd = BigDecimal.valueOf(50.00);

        WalletOperationRequest request = new WalletOperationRequest(walletId, "DEPOSIT", amountToAdd);

        doNothing().when(walletService).processOperation(walletId, "DEPOSIT", amountToAdd);
        ResponseEntity<String> processOperationResponse = walletController.processOperation(request);

        assertEquals(HttpStatus.OK, processOperationResponse.getStatusCode());
        assertEquals("Operation successful", processOperationResponse.getBody());

        verify(walletService, times(1)).processOperation(walletId, "DEPOSIT", amountToAdd);
    }

    @Test
    @DisplayName("testWithdrawBalance")
    void testWithdrawBalance() {
        BigDecimal amountToWithdraw = BigDecimal.valueOf(50.00);

        WalletOperationRequest request = new WalletOperationRequest(walletId, "WITHDRAW", amountToWithdraw);

        doNothing().when(walletService).processOperation(walletId, "WITHDRAW", amountToWithdraw);
        ResponseEntity<String> processOperationResponse = walletController.processOperation(request);

        assertEquals(HttpStatus.OK, processOperationResponse.getStatusCode());
        assertEquals("Operation successful", processOperationResponse.getBody());

        verify(walletService, times(1)).processOperation(walletId, "WITHDRAW", amountToWithdraw);
    }
}
