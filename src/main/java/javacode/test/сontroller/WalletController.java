package javacode.test.сontroller;


import javacode.test.service.WalletService;
import javacode.test.util.WalletOperationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID walletId) {
        BigDecimal balance = walletService.getBalance(walletId);
        return ResponseEntity.ok(balance);

    }

    @PostMapping
    public ResponseEntity<String> processOperation(@RequestBody WalletOperationRequest request) {
        walletService.processOperation(request.getWalletId(), request.getOperationType(), request.getAmount());
        return ResponseEntity.ok("Operation successful");

    }


}
