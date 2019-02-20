package com.gzq.lib_resource.dialog;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.R;

import retrofit2.http.PUT;


public class FDialog extends BaseFDialog {
    private ViewConvertListener convertListener;

    public static FDialog build() {
        return new FDialog();
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseFDialog dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }

    public FDialog setSupportFM(FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    public FDialog setTheme(@StyleRes int theme) {
        this.theme = theme;
        return this;
    }

    public ConfirmDialog showConfirm() {
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        return confirmDialog;
    }

    public FDialog setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public FDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    public FDialog showConfirmCancelDialog() {
        if (this.layoutId != 0) {
            throw new IllegalArgumentException("layout id only be set once");
        }
        this.layoutId = R.layout.dialog_layout_confirm_cancel;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            convertListener = savedInstanceState.getParcelable("listener");
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("listener", convertListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        convertListener = null;
    }

    public static class ConfirmDialog {
        public String confirmText;
        @ColorInt
        public int confirmTextColor;
        private FDialog fDialog;

        public ConfirmDialog(FDialog fDialog) {
            this.fDialog = fDialog;
        }

        public ConfirmDialog setConfirmText(String text) {
            this.confirmText = text;
            return this;
        }

        public ConfirmDialog setConfirmTextColor(@ColorInt int confirmTextColor) {
            this.confirmTextColor = confirmTextColor;
            return this;
        }

        public void show() {
            fDialog.setLayoutId(R.layout.dialog_layout_confirm)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        protected void convertView(DialogViewHolder holder, BaseFDialog dialog) {
                            holder.setText(R.id.message, confirmText);
                            holder.setTextColor(R.id.message,confirmTextColor==0?Box.getColor( R.color.text_normal_color) :confirmTextColor);
                            holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fDialog.dismiss();
                                }
                            });
                        }
                    })
                    .show();
        }
    }
}
