package cn.langqyi.bank.exception;

/**
 * 余额不足异常
 */
public class MoneyNotEnoughException extends Exception {
    public MoneyNotEnoughException(){}
    public MoneyNotEnoughException(String msg){ super(msg); }
}
