package com.example.ggq.gaoguoqing20180917.di;

public interface IContract {
    public interface IView{
        void showData();
    }
    public interface IPresenter<IView>{
        void attachView(IView iView);
        void detachView(IView iView);
        void requestInfo();
    }
    public interface IModel{
        public interface onCallback{
            void responseMsg();
        }
        void requestData(onCallback onCallback);
    }
}
