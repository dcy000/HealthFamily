package com.ml.module_shouhuan.api;

import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.bean.MsgBean;
import com.sjtu.yifei.annotation.Extra;
import com.sjtu.yifei.annotation.Go;

public interface ShouhuanRouterApi {
    @Go("/shouhuan/msgAlreadyDone")
    StateBaseFragment getMsgAlreadyDoneFragment();

    @Go("/shouhuan/msgTodo")
    StateBaseFragment getMsgToDoFragment();

    @Go("/shouhuan/msgSystem")
    StateBaseFragment getMsgSystemFragment();

    @Go("/shouhuan/msgShowActivity")
    boolean skipMsgShowActivity();

    @Go("/guardianship/resident/location/detail")
    boolean skipResidentLocationDetailActivity(@Extra("data") MsgBean msgBean);

    @Go("/shouhuan/msg/alreadydo")
    boolean skipMsgAlreadyDoActivity(@Extra("msg") MsgBean msgBean);
}
