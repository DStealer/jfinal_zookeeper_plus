package com.kuark.jfzk.demo.locks;

public interface LockListener {
    /**
     * call back called when the lock
     * is acquired
     */
    void lockAcquired();

    /**
     * call back called when the lock is
     * released.
     */
    void lockReleased();
}
