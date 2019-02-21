package com.gzq.lib_resource.dialog;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

public class DialogViewHolder {

    private SparseArray<View> views;
    private View convertView;

    private DialogViewHolder(View view) {
        convertView = view;
        views = new SparseArray<>();
    }

    public static DialogViewHolder create(View view) {
        return new DialogViewHolder(view);
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }

    public void setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
    }

    public void setText(int viewId, int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
    }

    public void setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
    }

    public void setTextSize(int viewId, int textSize) {
        TextView textView = getView(viewId);
        textView.setTextSize(textSize);
    }

    public void setTextGravity(int viewId, int gravity) {
        TextView textView = getView(viewId);
        textView.setGravity(gravity);
    }

    public void setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        view.setOnClickListener(clickListener);
    }

    public void setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
    }

    public void setBackgroundColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
    }
}
