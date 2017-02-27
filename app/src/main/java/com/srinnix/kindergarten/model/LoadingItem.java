package com.srinnix.kindergarten.model;

/**
 * Created by anhtu on 2/11/2017.
 */

public class LoadingItem {
    public static final int STATE_IDLE_AND_LOADING = 1;
    public static final int STATE_ERROR = 2;

    private int loadingState = STATE_IDLE_AND_LOADING;


    public int getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(int loadingState) {
        this.loadingState = loadingState;
    }
}
