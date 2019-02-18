package com.gcml.healthfamily;

import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.sjtu.yifei.annotation.Go;

/**
 * Created by gzq on 19-2-3.
 */

public interface AppRouterApi {

    /**
     * 第一个主页面
     *
     * @return
     */
    @Go("/guardianship/main")
    StateBaseFragment getFirstFragment();

    /**
     * 获取第二个页面
     *
     * @return
     */
    @Go("/sosdeal/main")
    StateBaseFragment getSecondFragment();

    /**
     * 获取第三个页面
     *
     * @return
     */
    @Go("/healthmanager/main")
    StateBaseFragment getThirdFragment();

    /**
     * 获取第四个页面
     *
     * @return
     */
    @Go("/mine/main")
    StateBaseFragment getFourthFragment();
}
