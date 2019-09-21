package ru.ao.app.access;

import ru.ao.app.access.db.DataBaseFactory;
import ru.ao.app.access.model.Account;
import ru.ao.app.business.exception.BusinessException;
import ru.ao.app.business.exception.StatusCodeEnum;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Singleton
public class AccountAccessServiceImpl implements AccountAccessService {

    private static final String GET_ALL_QUERY =
            "SELECT ID, NAME, BALANCE FROM ACCOUNT";

    private static final String GET_BY_ID_QUERY =
            "SELECT ID, NAME, BALANCE FROM ACCOUNT WHERE ID=?";

    private static final String INSERT_QUERY =
            "INSERT INTO ACCOUNT (NAME, BALANCE) VALUES (?,?)";

    private static final String LOCK_ACCOUNT_BY_ID_QUERY =
            "SELECT * FROM ACCOUNT WHERE ID=? FOR UPDATE";

    private static final String UPDATE_ACCOUNT_QUERY =
            "UPDATE ACCOUNT SET NAME=?,BALANCE=? WHERE ID=?";

    private static final String DELETE_ACCOUNT_BY_ID_QUERY =
            "DELETE FROM ACCOUNT WHERE ID=?";

    @Override
    public List<Account> getAll() {
        List<Account> allAccounts = new ArrayList<>();
        try (Connection conn = DataBaseFactory.getDataBase().getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Account account = getAccountFromResultSet(rs);
                allAccounts.add(account);
            }
            return allAccounts;
        } catch (SQLException e) {
            throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR, e);
        }
    }

    @Override
    public Account getById(long id) {
        Account account = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try (Connection conn = DataBaseFactory.getDataBase().getConnection()) {
            stmt = conn.prepareStatement(GET_BY_ID_QUERY);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                account = getAccountFromResultSet(rs);
            }
            return account;
        } catch (SQLException e) {
            throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR, e);
        } finally {
            close(stmt, rs);
        }
    }

    @Override
    public Account create(Account account) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try (Connection conn = DataBaseFactory.getDataBase().getConnection()) {
            stmt = conn.prepareStatement(INSERT_QUERY);
            stmt.setString(1, account.getName());
            stmt.setBigDecimal(2, account.getBalance());
            if (stmt.executeUpdate() == 0) {
                throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR,
                        "Creating Account failed");
            }
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                account.setId(rs.getLong(1));
                return account;
            } else {
                throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR,
                        "Creating Account failed, no ID obtained");
            }
        } catch (SQLException e) {
            throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR, e);
        } finally {
            close(stmt, rs);
        }
    }

    @Override
    public Account update(Account account) {
        Connection conn = null;
        PreparedStatement lockStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        Account old = null;
        try {
            conn = DataBaseFactory.getDataBase().getConnection();
            conn.setAutoCommit(false);
            lockStmt = conn.prepareStatement(LOCK_ACCOUNT_BY_ID_QUERY);
            lockStmt.setLong(1, account.getId());
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                old = getAccountFromResultSet(rs);
            }
            if (old == null) {
                throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR,
                        "Fail to lock Account by ID: " + account.getId());
            }
            updateStmt = conn.prepareStatement(UPDATE_ACCOUNT_QUERY);
            updateStmt.setString(1, account.getName());
            updateStmt.setBigDecimal(2, account.getBalance());
            updateStmt.setLong(3, old.getId());
            conn.commit();
            return account;
        } catch (SQLException se) {
            try {
                if (nonNull(conn)) {
                    conn.rollback();
                }
            } catch (SQLException ignored) {
            }
            throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR,
                    "Fail to update Account by ID: " + account.getId());
        } finally {
            close(lockStmt, updateStmt, rs);
        }
    }

    @Override
    public void delete(long id) {
        PreparedStatement stmt = null;
        try (Connection conn = DataBaseFactory.getDataBase().getConnection()) {
            stmt = conn.prepareStatement(DELETE_ACCOUNT_BY_ID_QUERY);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR,
                    "Fail to delete Account by ID: " + id);
        } finally {
            close(stmt);
        }
    }

    @Override
    public void transfer(long fromId, long toId, BigDecimal amount) {
        Connection conn = null;
        PreparedStatement lockStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        try {
            Account fromAccount = null;
            Account toAccount = null;
            conn = DataBaseFactory.getDataBase().getConnection();
            conn.setAutoCommit(false);
            lockStmt = conn.prepareStatement(LOCK_ACCOUNT_BY_ID_QUERY);
            lockStmt.setLong(1, fromId);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                fromAccount = getAccountFromResultSet(rs);
            }
            lockStmt = conn.prepareStatement(LOCK_ACCOUNT_BY_ID_QUERY);
            lockStmt.setLong(1, toId);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                toAccount = getAccountFromResultSet(rs);
            }
            if (isNull(fromAccount) || isNull(toAccount)) {
                throw new BusinessException(StatusCodeEnum.ACCESS_SERVICE_ERROR,
                        "Fail to lock both Accounts");
            }
            BigDecimal accFromNewBalance = fromAccount.getBalance().subtract(amount);
            BigDecimal accToNewBalance = toAccount.getBalance().add(amount);
            updateStmt = conn.prepareStatement(UPDATE_ACCOUNT_QUERY);

            updateStmt.setString(1, fromAccount.getName());
            updateStmt.setBigDecimal(2, accFromNewBalance);
            updateStmt.setLong(3, fromAccount.getId());

            updateStmt.addBatch();

            updateStmt.setString(1, toAccount.getName());
            updateStmt.setBigDecimal(2, accToNewBalance);
            updateStmt.setLong(3, toAccount.getId());
            updateStmt.addBatch();

            updateStmt.executeBatch();
            conn.commit();
        } catch (SQLException se) {
            try {
                if (nonNull(conn))
                    conn.rollback();
            } catch (SQLException ignored) {
            }
        } finally {
            close(lockStmt, updateStmt, rs);
        }
    }

    private void close(AutoCloseable... closeables) {
        for (AutoCloseable closeable : closeables) {
            if (nonNull(closeable)) {
                try {
                    closeable.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static Account getAccountFromResultSet(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("ID"));
        account.setName(rs.getString("NAME"));
        account.setBalance(rs.getBigDecimal("BALANCE"));
        return account;
    }

}
