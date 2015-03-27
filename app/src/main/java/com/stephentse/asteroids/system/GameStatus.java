package com.stephentse.asteroids.system;

public class GameStatus {

    private int _tickCount;
    private boolean _isPaused;


    public GameStatus() {
        _tickCount = 0;
        _isPaused = false;
    }

    public synchronized void setTickCount(byte tickCount) {
        _tickCount = tickCount;
    }

    public synchronized int getTickCount() {
        return _tickCount;
    }

    public synchronized void incrementTickCount() {
        _tickCount = _tickCount + 1 < 0 ? 0 : (_tickCount + 1);
    }

    public synchronized void pauseGame() {
        _isPaused = true;
    }

    public synchronized void resumeGame() {
        _isPaused = false;
    }

    public synchronized boolean isPaused() {
        return _isPaused;
    }
}
