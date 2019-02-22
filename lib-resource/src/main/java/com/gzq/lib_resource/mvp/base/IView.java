package com.gzq.lib_resource.mvp.base;

public interface IView {
    void loadDataSuccess(Object... objects);

    void loadDataError(Object... objects);
    void loadDataEmpty();
    void onNetworkError();
}
