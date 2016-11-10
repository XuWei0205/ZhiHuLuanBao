package com.hanyu.zhihuluanbao.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hanyu.zhihuluanbao.R;
import com.hanyu.zhihuluanbao.models.OtherModel;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Dell on 2016/11/4.
 */
public class DrawerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;



    private ArrayList<OtherModel> mData = new ArrayList<>();

    public DrawerAdapter(Context context){
        this.mContext = context;
        inflater = LayoutInflater.from(context);

    }

    public void setData(ArrayList<OtherModel> mData){
        this.mData.clear();
        this.mData.addAll(mData);


    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public OtherModel getItem(int position) {

        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.theme_item,null);
            holder.textView = (TextView) convertView.findViewById(R.id.theme_tv);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        OtherModel otherModel = mData.get(position);
        holder.textView.setText(otherModel.name);
        return convertView;
    }


    static class Holder{
       public TextView textView;
    }

}

