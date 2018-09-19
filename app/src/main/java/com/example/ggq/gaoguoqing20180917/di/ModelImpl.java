package com.example.ggq.gaoguoqing20180917.di;

public class ModelImpl implements IContract.IModel{
    @Override
    public void requestData(onCallback onCallback) {
        onCallback.responseMsg();
    }
}
