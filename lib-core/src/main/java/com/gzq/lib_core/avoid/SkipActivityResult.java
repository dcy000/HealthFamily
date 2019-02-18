package com.gzq.lib_core.avoid;

import android.content.Intent;

public interface SkipActivityResult {
    void onSuccess();

    void onCancel();

    void onState(int resultCode, Intent loginActivtyReturnData);
}
