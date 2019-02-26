package com.ml.module_shouhuan.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.app.AppStore;
import com.gzq.lib_resource.divider.LinearLayoutDividerItemDecoration;
import com.gzq.lib_resource.mvp.StateBaseFragment;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.data.TimeUtils;
import com.ml.module_shouhuan.R;
import com.ml.module_shouhuan.api.ShouhuanRouterApi;
import com.gzq.lib_resource.bean.MsgBean;
import com.ml.module_shouhuan.presenter.MsgTodoPresenter;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/shouhuan/msgTodo")
public class MsgToDoFragment extends StateBaseFragment {
    private MsgTodoPresenter msgTodoPresenter;
    private RecyclerView mRvMsgTodo;
    private BaseQuickAdapter<MsgBean, BaseViewHolder> adapter;

    @Override
    public void onResume() {
        super.onResume();
        msgTodoPresenter.preData();
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.fragment_msg_todo;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        mRvMsgTodo = (RecyclerView) view.findViewById(R.id.rv_msg_todo);
    }

    private void initRv(List<MsgBean> msgs) {
        mRvMsgTodo.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMsgTodo.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 24, Box.getColor(R.color.background_gray_f8f8f8)));
        mRvMsgTodo.setAdapter(adapter = new BaseQuickAdapter<MsgBean, BaseViewHolder>(R.layout.item_msg_todo, msgs) {
            @Override
            protected void convert(BaseViewHolder helper, MsgBean item) {
                Glide.with(Box.getApp())
                        .load(item.getUserPhoto())
                        .into(((CircleImageView) helper.getView(R.id.civ_head)));
                helper.setText(R.id.msg_title, item.getUserName() + " 发起紧急呼叫");
                helper.setText(R.id.msg_address, item.getWarningAddress());
                helper.setText(R.id.msg_time, TimeUtils.milliseconds2String(item.getWarningTime(), new SimpleDateFormat("yyyy.MM.dd HH:mm")));

                String dealStatus = item.getDealStatus();
                if (!TextUtils.isEmpty(dealStatus) && "1".equals(dealStatus)) {
                    //已处理该条信息
                    helper.getView(R.id.msg_unread).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.msg_unread).setVisibility(View.VISIBLE);
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Routerfit.register(ShouhuanRouterApi.class).skipResidentLocationDetailActivity(msgs.get(position));
            }
        });

    }

    @Override
    public IPresenter obtainPresenter() {
        msgTodoPresenter = new MsgTodoPresenter(this);
        return msgTodoPresenter;
    }

    @Override
    public void loadDataSuccess(Object... objects) {
        super.loadDataSuccess(objects);
        List<MsgBean> object = (List<MsgBean>) objects[0];
        //通知底部bar更新未读数字
        AppStore.sosDeal.postValue(object.size());
        initRv(object);
    }
}