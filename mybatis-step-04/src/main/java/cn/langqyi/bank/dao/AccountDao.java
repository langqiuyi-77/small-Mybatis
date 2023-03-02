package cn.langqyi.bank.dao;

import cn.langqyi.bank.pojo.Account;

/**
 * 账号数据访问对象
 */
public interface AccountDao {

    /**
     * 根据账号获取账户信息
     * @param actno 账号
     * @return 账户信息
     */
    Account selectByActno(String actno);

    /**
     * 更新账户信息
     * @param act 账户信息
     * @return 1表示更新成功，其他值表示失败
     */
    int update(Account act);

}
