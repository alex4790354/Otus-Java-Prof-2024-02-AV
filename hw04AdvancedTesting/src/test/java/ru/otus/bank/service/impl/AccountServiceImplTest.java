package ru.otus.bank.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bank.dao.AccountDao;
import ru.otus.bank.entity.Account;
import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.exception.AccountException;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    AccountDao accountDao;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Test
    public void testTransfer() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(100));

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        assertEquals(new BigDecimal(90), sourceAccount.getAmount());
        assertEquals(new BigDecimal(20), destinationAccount.getAmount());
    }

    @Test
    public void testSourceNotFound() {
        when(accountDao.findById(any())).thenReturn(Optional.empty());

        AccountException result = assertThrows(AccountException.class, () -> accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10)));
        assertEquals("No source account", result.getLocalizedMessage());
    }

    @Test
    public void testTransferWithVerify() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(100));
        sourceAccount.setId(1L);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));
        destinationAccount.setId(2L);

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        verify(accountDao).save(argThat(argument -> argument.getId().equals(1L) && argument.getAmount().equals(new BigDecimal(90))));
        verify(accountDao).save(argThat(argument -> argument.getId().equals(2L) && argument.getAmount().equals(new BigDecimal(20))));
    }

    @Test
    public void testAddAccount() {
        Agreement agreement = new Agreement();
        agreement.setId(1L);

        Account account = new Account();
        account.setAgreementId(1L);
        account.setNumber("acc1");
        account.setType(0);
        account.setAmount(new BigDecimal(1000));

        when(accountDao.save(any(Account.class))).thenReturn(account);

        Account result = accountServiceImpl.addAccount(agreement, "acc1", 0, new BigDecimal(1000));

        assertNotNull(result);
        assertEquals(account.getAgreementId(), result.getAgreementId());
        assertEquals(account.getNumber(), result.getNumber());
        assertEquals(account.getType(), result.getType());
        assertEquals(account.getAmount(), result.getAmount());
    }

    @Test
    public void testGetAccounts() {
        Account account = new Account();
        account.setId(1L);

        when(accountDao.findAll()).thenReturn(java.util.List.of(account));

        assertEquals(1, accountServiceImpl.getAccounts().size());
    }

    @Test
    public void testCharge() {
        Account account = new Account();
        account.setId(1L);
        account.setAmount(new BigDecimal(1000));

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(account));

        accountServiceImpl.charge(1L, new BigDecimal(100));

        assertEquals(new BigDecimal(900), account.getAmount());
        verify(accountDao).save(any(Account.class));
    }
}
