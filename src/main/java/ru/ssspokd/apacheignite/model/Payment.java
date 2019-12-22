package ru.ssspokd.apacheignite.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {
    @QuerySqlField(index = true)
    private Long id;
    @QuerySqlField(index = true)
    private String accountUser;
    @QuerySqlField(index = true)
    private Long accountBalance;
    @QuerySqlField(index = true)
    private Date lastOperationDate;
    @QuerySqlField(index = true)
    private EnumOperation enumOperation;

    public Payment(Long id, String accountUser, Long accountBalance, Date lastOperationDate, EnumOperation enumOperation) {
        this.id = id;
        this.accountUser = accountUser;
        this.accountBalance = accountBalance;
        this.lastOperationDate = lastOperationDate;
        this.enumOperation = enumOperation;
    }

    public Payment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(String accountUser) {
        accountUser = accountUser;
    }

    public Long getBalanse() {
        return accountBalance;
    }

    public void setBalanse(Long accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Date getLastOperationDate() {
        return lastOperationDate;
    }

    public void setLastOperationDate(Date lastOperationDate) {
        this.lastOperationDate = lastOperationDate;
    }

    public EnumOperation getEnumOperation() {
        return enumOperation;
    }

    public void setEnumOperation(EnumOperation enumOperation) {
        this.enumOperation = enumOperation;
    }
}
