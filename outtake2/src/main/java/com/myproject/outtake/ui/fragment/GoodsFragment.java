package com.myproject.outtake.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.GoodsInfo;
import com.myproject.outtake.model.net.bean.GoodsTypeInfo;
import com.myproject.outtake.model.net.bean.Seller;
import com.myproject.outtake.presenter.GoodsPresenter;
import com.myproject.outtake.ui.activity.BusinessActivity;
import com.myproject.outtake.ui.adapter.GoodsAdapter;
import com.myproject.outtake.ui.adapter.GoodsTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Administrator on 2017/2/18.
 */
public class GoodsFragment extends BaseFragment {
    @Bind(R.id.rv_goods_type)
    RecyclerView rvGoodsType;
    @Bind(R.id.slhlv)
    StickyListHeadersListView slhlv;
    private Seller seller;
    public GoodsAdapter goodsAdapter;
    public GoodsTypeAdapter goodsTypeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, null);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        seller = (Seller) bundle.getSerializable("seller");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvGoodsType.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        goodsTypeAdapter = new GoodsTypeAdapter(this,BusinessActivity.shopCardAdapter);
        rvGoodsType.setAdapter(goodsTypeAdapter);
        goodsAdapter = new GoodsAdapter(getActivity(),this, BusinessActivity.shopCardAdapter);
        slhlv.setAdapter(goodsAdapter);
        GoodsPresenter goodsPresenter = new GoodsPresenter(goodsTypeAdapter, goodsAdapter, seller);
        goodsPresenter.getGoodsData(seller);

        slhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                ArrayList<GoodsInfo> goodsInfo = goodsAdapter.getData();
                List<GoodsTypeInfo> goodsTypeInfos = goodsTypeAdapter.getData();
                if(goodsInfo!=null&&goodsInfo.size()!=0&&goodsTypeInfos!=null&&goodsTypeInfos.size()>0){
                    int typeId = goodsInfo.get(i).getTypeId();
                        int id = goodsTypeInfos.get(goodsTypeAdapter.currentPosition).getId();
                    if (id!=typeId){
                        for (int k = 0; k < goodsTypeInfos.size(); k++) {
                            if (typeId==goodsTypeInfos.get(k).getId()){
                                goodsTypeAdapter.currentPosition=k;
                                goodsTypeAdapter.notifyDataSetChanged();
                                rvGoodsType.smoothScrollToPosition(k);
                                break;
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
 public void switchGoodsInfo(int id){
     ArrayList<GoodsInfo> data = goodsAdapter.getData();
     for (int i = 0; i < data.size(); i++) {
         if (data.get(i).getTypeId()==id){
             slhlv.setSelection(i);
             break;
         }
     }
 }
}
