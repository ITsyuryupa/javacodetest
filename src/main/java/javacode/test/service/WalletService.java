package javacode.test.service;

import jakarta.persistence.OptimisticLockException;
import javacode.test.model.Wallet;
import javacode.test.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        return wallet.getBalance();
    }


    @Transactional
    public void processOperation(UUID walletId, String operationType, BigDecimal amount) {
        boolean success = false;
        int retries = 3;

        while (!success && retries > 0) {
            try {
                Wallet wallet = walletRepository.findById(walletId)
                        .orElseThrow(() -> new RuntimeException("Wallet not found"));

                if ("DEPOSIT".equalsIgnoreCase(operationType)) {
                    wallet.setBalance(wallet.getBalance().add(amount));
                } else if ("WITHDRAW".equalsIgnoreCase(operationType)) {
                    if (wallet.getBalance().compareTo(amount) < 0) {
                        throw new RuntimeException("Insufficient funds");
                    }
                    wallet.setBalance(wallet.getBalance().subtract(amount));
                } else {
                    throw new RuntimeException("Invalid operation type");
                }

                walletRepository.save(wallet);
                success = true;

            } catch (OptimisticLockException e) {
                retries--;
                if (retries == 0) {
                    throw new RuntimeException("Operation failed due to high contention. Try again later.", e);
                }
            }
        }
    }
}

