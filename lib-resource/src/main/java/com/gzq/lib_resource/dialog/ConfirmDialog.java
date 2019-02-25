package com.gzq.lib_resource.dialog;

import android.support.annotation.ColorRes;
import android.view.Gravity;
import android.view.View;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.R;

public class ConfirmDialog {
    private String confirmText = "确认";
    @ColorRes
    private int confirmTextColor = R.color.text_normal_color;
    private int confirmTextSize = 16;
    private DialogClickListener clickConfirmListener = new DialogClickListener() {
        @Override
        public void onClick(View v, FDialog dialog) {
            dialog.dismiss();
        }
    };

    private String contentText;
    private int contentTextGravity = Gravity.CENTER;

    @ColorRes
    private int contentTextColor = R.color.text_normal_color;
    private int contentTextSize = 16;
    private FDialog fDialog;

    public ConfirmDialog(FDialog fDialog) {
        this.fDialog = fDialog;
    }

    public ConfirmDialog setConfirmText(String text) {
        this.confirmText = text;
        return this;
    }

    public ConfirmDialog setConfirmTextColor(@ColorRes int confirmTextColor) {
        this.confirmTextColor = confirmTextColor;
        return this;
    }

    public ConfirmDialog setConfirmTextSize(int confirmTextSize) {
        this.confirmTextSize = confirmTextSize;
        return this;
    }

    public ConfirmDialog setOnClickConfirmListener(DialogClickListener clickListener) {
        this.clickConfirmListener = clickListener;
        return this;
    }

    public ConfirmDialog setContentText(String text) {
        this.contentText = text;
        return this;
    }

    public ConfirmDialog setContentTextColor(@ColorRes int contentTextColor) {
        this.contentTextColor = contentTextColor;
        return this;
    }

    public ConfirmDialog setContentTextSize(int contentTextSize) {
        this.contentTextSize = contentTextSize;
        return this;
    }

    public ConfirmDialog setContentTextGravity(int contentTextGravity) {
        this.contentTextGravity = contentTextGravity;
        return this;
    }

    public void show() {
        fDialog.setLayoutId(R.layout.dialog_layout_confirm)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, final FDialog dialog) {
                        holder.setText(R.id.tv_confirm, confirmText);
                        holder.setTextColor(R.id.tv_confirm, Box.getColor(confirmTextColor));
                        holder.setTextSize(R.id.tv_confirm, confirmTextSize);
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (clickConfirmListener != null) {
                                    clickConfirmListener.onClick(v, dialog);
                                }
                            }
                        });


                        holder.setText(R.id.tv_message, contentText);
                        holder.setTextColor(R.id.tv_message, Box.getColor(contentTextColor));
                        holder.setTextSize(R.id.tv_message, contentTextSize);
                        holder.setTextGravity(R.id.tv_message, contentTextGravity);
                    }
                })
                .show();
    }
}
