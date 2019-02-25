package com.gzq.lib_resource.dialog;

import android.support.annotation.ColorRes;
import android.view.Gravity;
import android.view.View;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.R;

public class ConfirmCancelDialog {
    private FDialog fDialog;
    private String contentText;
    @ColorRes
    private int contentTextColor = R.color.text_normal_color;
    private int contentTextSize = 16;
    private int contentTextGravity = Gravity.CENTER;

    private String leftButtonText = "取消";
    private int leftButtonTextSize = 16;
    @ColorRes
    private int leftButtonTextColor = R.color.text_normal_color;
    private DialogClickListener leftButtonClickListen;

    private String rightButtonText = "确定";
    private int rightButtonTextSize = 16;
    @ColorRes
    private int rightButtonTextColor = R.color.text_normal_color;
    private DialogClickListener rightButtonClickListener;

    public ConfirmCancelDialog(FDialog fDialog) {
        this.fDialog = fDialog;
    }

    public ConfirmCancelDialog setContentText(String contentText) {
        this.contentText = contentText;
        return this;
    }

    public ConfirmCancelDialog setContentTextGravity(int contentTextGravity) {
        this.contentTextGravity = contentTextGravity;
        return this;
    }

    public ConfirmCancelDialog setContentTextColor(int contentTextColor) {
        this.contentTextColor = contentTextColor;
        return this;
    }

    public ConfirmCancelDialog setContentTextSize(int contentTextSize) {
        this.contentTextSize = contentTextSize;
        return this;
    }

    public ConfirmCancelDialog setLeftButtonText(String leftButtonText) {
        this.leftButtonText = leftButtonText;
        return this;
    }

    public ConfirmCancelDialog setLeftButtonTextSize(int leftButtonTextSize) {
        this.leftButtonTextSize = leftButtonTextSize;
        return this;
    }

    public ConfirmCancelDialog setLeftButtonTextColor(int leftButtonTextColor) {
        this.leftButtonTextColor = leftButtonTextColor;
        return this;
    }

    public ConfirmCancelDialog setLeftButtonClickListen(DialogClickListener leftButtonClickListen) {
        this.leftButtonClickListen = leftButtonClickListen;
        return this;
    }

    public ConfirmCancelDialog setRightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
        return this;
    }

    public ConfirmCancelDialog setRightButtonTextSize(int rightButtonTextSize) {
        this.rightButtonTextSize = rightButtonTextSize;
        return this;
    }

    public ConfirmCancelDialog setRightButtonTextColor(int rightButtonTextColor) {
        this.rightButtonTextColor = rightButtonTextColor;
        return this;
    }

    public ConfirmCancelDialog setRightButtonClickListener(DialogClickListener rightButtonClickListener) {
        this.rightButtonClickListener = rightButtonClickListener;
        return this;
    }

    public void show() {
        fDialog.setLayoutId(R.layout.dialog_layout_confirm_cancel)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    protected void convertView(DialogViewHolder holder, final FDialog dialog) {
                        holder.setText(R.id.tv_message, contentText);
                        holder.setTextColor(R.id.tv_message, Box.getColor(contentTextColor));
                        holder.setTextSize(R.id.tv_message, contentTextSize);
                        holder.setTextGravity(R.id.tv_message, contentTextGravity);

                        holder.setText(R.id.tv_cancel, leftButtonText);
                        holder.setTextColor(R.id.tv_cancel, Box.getColor(leftButtonTextColor));
                        holder.setTextSize(R.id.tv_cancel, leftButtonTextSize);
                        holder.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (leftButtonClickListen != null) {
                                    leftButtonClickListen.onClick(v, dialog);
                                }
                            }
                        });

                        holder.setText(R.id.tv_confirm, rightButtonText);
                        holder.setTextColor(R.id.tv_confirm, Box.getColor(rightButtonTextColor));
                        holder.setTextSize(R.id.tv_confirm, rightButtonTextSize);
                        holder.setOnClickListener(R.id.tv_confirm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (rightButtonClickListener != null) {
                                    rightButtonClickListener.onClick(v, dialog);
                                }
                            }
                        });
                    }
                }).show();
    }
}
