package com.myproject.outtake.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.HomeInfo;
import com.myproject.outtake.model.net.bean.Promotion;
import com.myproject.outtake.model.net.bean.Seller;
import com.myproject.outtake.ui.activity.BusinessActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/17.
 */
public class HomeRecycleViewAdapter extends RecyclerView.Adapter {
    private static final int ITEM_HEAD = 0;
    private static final int ITEM_SELLER = 1;
    private static final int ITEM_DIVIDED = 2;
    private final Activity activity;
    private HomeInfo homeinfo;

    public HomeRecycleViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEAD) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_title, null);
            return new HeadHolder(view);
        } else if (viewType == ITEM_SELLER) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_seller, null);
            return new SellerHolder(view);
        } else {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_division, null);
            return new DivHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position==0){

        }else if(homeinfo.getBody().get(position-1).getType()==0){
            setSellerData(holder,position-1);
            ((SellerHolder)holder).setPosition(position);
        }else{
            setDivData(holder,position-1);
        }
    }

    private void setDivData(RecyclerView.ViewHolder holder, int i) {
        ((DivHolder)holder).tv1.setText(homeinfo.getBody().get(i).getRecommendInfos().get(0));
        ((DivHolder)holder).tv2.setText(homeinfo.getBody().get(i).getRecommendInfos().get(1));
        ((DivHolder)holder).tv3.setText(homeinfo.getBody().get(i).getRecommendInfos().get(2));
        ((DivHolder)holder).tv4.setText(homeinfo.getBody().get(i).getRecommendInfos().get(3));
        ((DivHolder)holder).tv5.setText(homeinfo.getBody().get(i).getRecommendInfos().get(4));
        ((DivHolder)holder).tv6.setText(homeinfo.getBody().get(i).getRecommendInfos().get(5));
    }

    private void setSellerData(RecyclerView.ViewHolder holder, int i) {
        ((SellerHolder)holder).tvTitle.setText(homeinfo.getBody().get(i).getSeller().getName());


    }

    @Override
    public int getItemCount() {
        if(homeinfo!=null&&homeinfo.getBody()!=null&&homeinfo.getHead()!=null&&homeinfo.getBody().size()>0){
            return homeinfo.getBody().size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEAD;
        } else if (homeinfo.getBody().get(position - 1).getType() == 0) {

            return ITEM_SELLER;
        } else {
            return ITEM_DIVIDED;
        }

    }

    public void setData(HomeInfo homeinfo) {
        this.homeinfo = homeinfo;
        notifyDataSetChanged();
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.slider)
        SliderLayout slider;

        HeadHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            for (int i = 0; i < homeinfo.getHead().getPromotionList().size(); i++) {
                Promotion promotion = homeinfo.getHead().getPromotionList().get(i);
                String info = promotion.getInfo();
                String pic = promotion.getPic();

                //设置文本,设置图片
                TextSliderView textSliderView = new TextSliderView(activity);
                // initialize a SliderLayout
                textSliderView
                        .description(info)//给TextSliderView设置文本内容
                        .image(pic)//TextSliderView设置显示图片
                        .setScaleType(BaseSliderView.ScaleType.Fit);//ScaleType  centercrop fitxy
                //将包含了图片和文本的控件,放置到容器中
                //mDemoSlider---->viewPager
                //textSliderView---->ImageView+TextView
                slider.addSlider(textSliderView);
            }
            //指定容器在切换图片过程中的动画类型
            slider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
            //Indicator指示器所在位置
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            //让文字由下往上展示
            slider.setCustomAnimation(new DescriptionAnimation());
            //设置动画时长
            slider.setDuration(4000);
        }
    }

    class SellerHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tvCount)
        TextView tvCount;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        private int position;

        SellerHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, BusinessActivity.class);
                    Seller seller = homeinfo.getBody().get(position - 1).getSeller();
                    intent.putExtra("seller",seller);
                    activity.startActivity(intent);
                }
            });
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    class DivHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_division_title)
        TextView tvDivisionTitle;
        @Bind(R.id.tv1)
        TextView tv1;
        @Bind(R.id.tv2)
        TextView tv2;
        @Bind(R.id.tv3)
        TextView tv3;
        @Bind(R.id.tv4)
        TextView tv4;
        @Bind(R.id.tv5)
        TextView tv5;
        @Bind(R.id.tv6)
        TextView tv6;

        DivHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
