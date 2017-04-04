package com.srinnix.kindergarten.model;

/**
 * Created by anhtu on 4/4/2017.
 */

public class LoadingItem3State {
    public static final int STATE_IDLE = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_LOADING = 3;

    private int loadingState = STATE_IDLE;

    public int getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(int loadingState) {
        this.loadingState = loadingState;
    }

}
