package com.gzq.lib_resource.api;

import com.sjtu.yifei.annotation.Go;

public interface CommonRouterApi {
    @Go("/call/video/activity")
    ICallService getCallServiceImp();
}
