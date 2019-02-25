package com.ml.module_shouhuan.api;

import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.sjtu.yifei.annotation.Go;

public interface AppRouterApi {
    @Go("/shouhuan/msgAlreadyDone")
    StateBaseFragment getMsgAlreadyDoneFragment();

    @Go("/shouhuan/msgTodo")
    StateBaseFragment getMsgToDoFragment();

    @Go("/shouhuan/msgSystem")
    StateBaseFragment getMsgSystemFragment();

    @Go("/shouhuan/msgShowActivity")
    boolean skipMsgShowActivity();
}
