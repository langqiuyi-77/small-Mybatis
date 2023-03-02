package cn.langqyi.bank.service;

import cn.langqyi.bank.exception.AppException;
import cn.langqyi.bank.exception.MoneyNotEnoughException;

public interface AccountService {

    /**
     * 银行账户转账
     * @param fromActno 转出账号
     * @param toActno 转入账号
     * @param money 转账金额
     * @throws MoneyNotEnoughException 余额不足异常
     * @throws AppException App发生异常
     */
    void transfer(String fromActno, String toActno, double money) throws MoneyNotEnoughException, AppException;
}
