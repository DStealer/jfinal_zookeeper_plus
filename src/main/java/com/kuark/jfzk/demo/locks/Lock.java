package com.kuark.jfzk.demo.locks;

/**
 * 使用jk实现的分布式锁
 */
public interface Lock {
    /**
     * 阻塞式的获取锁
     *
     * @throws Exception
     */
    void lock() throws Exception;

    /**
     * 尝试获取锁,立即返回
     *
     * @return true 获取成功 false 失败
     * @throws Exception
     */
    boolean tryLock() throws Exception;

    /**
     * 解除锁定
     */
    void unlock();

    /**
     * 获取锁的信息
     *
     * @return
     */
    String getLockId();
}
