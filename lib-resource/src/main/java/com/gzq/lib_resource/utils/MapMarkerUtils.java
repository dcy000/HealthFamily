package com.gzq.lib_resource.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapMarkerUtils {
    private static MapMarkerUtils mapMarkerUtils;
    private Context context;

    public MapMarkerUtils(Context context) {
        this.context=context;
    }

    public static MapMarkerUtils instance(Context context){
        if (mapMarkerUtils==null){
            mapMarkerUtils=new MapMarkerUtils(context);
        }
        return mapMarkerUtils;
    }

    public void customizeMarkerIcon(String url, final OnMarkerIconLoadListener listener) {
        final View markerView = LayoutInflater.from(context).inflate(R.layout.bg_map_marker, null);
        final CircleImageView icon = (CircleImageView) markerView.findViewById(R.id.civ_location_head);

        Glide.with(Box.getApp())
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().centerCrop())
                .thumbnail(0.2f)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        //待图片加载完毕后再设置bitmapDes
                        icon.setImageBitmap(bitmap);
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(markerView));
                        listener.markerIconLoadingFinished(markerView,bitmapDescriptor);
                    }
                });

    }

    private static Bitmap convertViewToBitmap(View view) {

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        return bitmap;

    }

    public interface OnMarkerIconLoadListener {
        void markerIconLoadingFinished(View view, BitmapDescriptor bitmapDescriptor);
    }
}
