package com.gcml.module_mine.api;

import com.sjtu.yifei.annotation.Go;

public interface MineRouterApi {
    @Go("/mine/setup")
    boolean skipSetupActivity();

    @Go("/mine/feedback")
    boolean skipFeedbackActivity();

    @Go("/mine/about/us")
    boolean skipAboutUsActivity();
    @Go("/login/phone")
    boolean skipLoginActivity();

    @Go("/mine/my/information")
    boolean skipMyInformationActivity();

    @Go("/mine/my/service/history")
    boolean skipMyServiceHistoryActivity();
}
