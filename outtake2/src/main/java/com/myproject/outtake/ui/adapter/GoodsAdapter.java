package com.myproject.outtake.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.myproject.outtake.MyApp;
import com.myproject.outtake.R;
import com.myproject.outtake.model.net.bean.GoodsInfo;
import com.myproject.outtake.ui.activity.BusinessActivity;
import com.myproject.outtake.ui.fragment.GoodsFragment;
import com.myproject.outtake.utils.CountPriceFormater;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Administrator on 2017/2/18.
 */
public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private final FragmentActivity activity;
    private final GoodsFragment goodFragment;
    private final ShopCardAdapter shopCardAdapter
            ;
    private ArrayList<GoodsInfo> goodsInfoArrayList;

    public GoodsAdapter(FragmentActivity activity, GoodsFragment goodsFragment, ShopCardAdapter shopCardAdapter) {
        this.activity = activity;
        this.goodFragment = goodsFragment;
        this.shopCardAdapter = shopCardAdapter;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(parent.getContext(), R.layout.item_type_header, null);
        textView.setText(goodsInfoArrayList.get(position).getTypeName());
        return textView;
    }

    @Override
    public long getHeaderId(int position) {
        return goodsInfoArrayList.get(position).getTypeId();
    }

    @Override
    public int getCount() {

        if (goodsInfoArrayList != null && goodsInfoArrayList.size() > 0) {
            return goodsInfoArrayList.size();
        }
        return 0;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();

        }
         if(goodsInfoArrayList.get(i).getCount()==0){
             viewHolder.tvCount.setVisibility(View.GONE);
             viewHolder. ibMinus.setVisibility(View.GONE);
         }
        viewHolder.tvCount.setText(goodsInfoArrayList.get(i).getCount() +"");
        viewHolder.tvName.setText(goodsInfoArrayList.get(i).getName());
        viewHolder.tvNewprice.setText(CountPriceFormater.format((int) goodsInfoArrayList.get(i).getNewPrice()));
        viewHolder.tvOldprice.setText(CountPriceFormater.format((int) goodsInfoArrayList.get(i).getOldPrice()));
        Picasso.with(viewGroup.getContext()).load(goodsInfoArrayList.get(i).getIcon()).into(viewHolder.ivIcon);
        viewHolder.getPosition(i);
        return view;
    }

    public void setData(ArrayList<GoodsInfo> goodsInfoArrayList) {
        this.goodsInfoArrayList = goodsInfoArrayList;
    }

    public ArrayList<GoodsInfo> getData() {
        return goodsInfoArrayList;
    }

    class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_zucheng)
        TextView tvZucheng;
        @Bind(R.id.tv_yueshaoshounum)
        TextView tvYueshaoshounum;
        @Bind(R.id.tv_newprice)
        TextView tvNewprice;
        @Bind(R.id.tv_oldprice)
        TextView tvOldprice;
        @Bind(R.id.ib_minus)
        ImageButton ibMinus;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.ib_add)
        ImageButton ibAdd;
        private int position;
    int operate = 0;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.ib_add, R.id.ib_minus})
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_add:
                    operate = 1;
                    v.setEnabled(false);
                    AddGoods(v,position);
                    if (shopCardAdapter!=null){
                        shopCardAdapter.notifyDataSetChanged();
                    }

                    break;
                case R.id.ib_minus:
                    operate=0;
                    deleteGoods(v,position);
                    if (shopCardAdapter!=null){
                        shopCardAdapter.notifyDataSetChanged();
                    }
                    break;
            }
            int typeId = goodsInfoArrayList.get(position).getTypeId();
            goodFragment.goodsTypeAdapter.refreGoodtypeInfo(operate,typeId);
            BusinessActivity  activity= (BusinessActivity) goodFragment.getActivity();
            activity.updateShopCart();
        }

        private void deleteGoods(final View v, final int position) {
            if (goodsInfoArrayList.get(position).getCount()==1){
                //旋转
                RotateAnimation rotateAnimation = new RotateAnimation(720,0,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                //透明度变化
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                //平移
                TranslateAnimation translateAnimation = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 2,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                //将动画放置在一个set中一起运行
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(500);
                //减号执行动画
                v.startAnimation(animationSet);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setEnabled(true);
                        v.setVisibility(View.GONE);
                        tvCount.setVisibility(View.GONE);
                        goodsInfoArrayList.get(position).setCount(0);
                        BusinessActivity  activity= (BusinessActivity) goodFragment.getActivity();
                        activity.updateShopCart();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }else if(goodsInfoArrayList.get(position).getCount()>1){
                v.setEnabled(true);
                int i = goodsInfoArrayList.get(position).getCount() - 1;
                goodsInfoArrayList.get(position).setCount(i);
                notifyDataSetChanged();
            }

        }

        private void AddGoods(View v,int position) {
            if (goodsInfoArrayList.get(position).getCount()==0){
                RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2, Animation.RELATIVE_TO_SELF, 0
                        , Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(alphaAnimation);
                animationSet.addAnimation(translateAnimation);
                animationSet.setDuration(500);
                ibMinus.startAnimation(animationSet);
                ibMinus.setVisibility(View.VISIBLE);
                tvCount.setVisibility(View.VISIBLE);
            }



            //平抛动画
            ImageView imageView = new ImageView(v.getContext());
            imageView.setImageResource(R.drawable.button_add);
            int[] sourceLocation = new int[2];
            v.getLocationInWindow(sourceLocation);
            imageView.setX(sourceLocation[0]);
            imageView.setY(sourceLocation[1] - MyApp.statusBarHeight);
            ((BusinessActivity) activity).addFlyView(imageView, v.getWidth(), v.getHeight());

            int[] startSourLocation = new int[2];
            int[] endSourLocat = new int[2];
            imageView.getLocationInWindow(startSourLocation);
            endSourLocat = ((BusinessActivity) activity).getLocationResource();

            //平抛飞行动画
            fly(imageView, startSourLocation, endSourLocat, v);
            int count = goodsInfoArrayList.get(position).getCount() + 1;
            goodsInfoArrayList.get(position).setCount(count);
            notifyDataSetChanged();

        }

        private void fly(final ImageView imageView, int[] startSourLocation, int[] endSourLocat, final View v) {
            int startX = startSourLocation[0];
            int startY = startSourLocation[1];
            int endX = endSourLocat[0];
            int endY = endSourLocat[1];
            TranslateAnimation translateAnimationX = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, endX - startX, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
            TranslateAnimation translateAnimationY = new TranslateAnimation(
                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0, Animation.ABSOLUTE, endY - startY);
            translateAnimationX.setInterpolator(new LinearInterpolator());
            translateAnimationY.setInterpolator(new AccelerateInterpolator());
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(translateAnimationX);
            animationSet.addAnimation(translateAnimationY);
            animationSet.setDuration(500);
            imageView.setAnimation(animationSet);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setEnabled(true);
                    imageView.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }

        public void getPosition(int i) {
            this.position =i;
        }
    }


}
