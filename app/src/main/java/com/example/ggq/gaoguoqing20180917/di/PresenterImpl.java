package com.example.ggq.gaoguoqing20180917.di;

import java.lang.ref.WeakReference;

public class PresenterImpl implements IContract.IPresenter<IContract.IView>{
    private IContract.IView iView;
    private ModelImpl modelImpl;
    private WeakReference<IContract.IView> iViewWeakReference;
    private WeakReference<IContract.IModel> iModelWeakReference;

    @Override
    public void attachView(IContract.IView iView) {
        this.iView = iView;
        modelImpl = new ModelImpl();
        iViewWeakReference = new WeakReference<>(iView);
        iModelWeakReference = new WeakReference<IContract.IModel>(modelImpl);
    }

    @Override
    public void detachView(IContract.IView iView) {
        iViewWeakReference.clear();
        iModelWeakReference.clear();
    }

    @Override
    public void requestInfo() {
        iView.showData();
    }
}
