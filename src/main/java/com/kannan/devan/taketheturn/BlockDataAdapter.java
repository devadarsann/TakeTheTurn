package com.kannan.devan.taketheturn;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by devan on 25/1/17.
 */

public class BlockDataAdapter extends RecyclerView.Adapter<BlockDataAdapter.ViewHolder> implements OnMapReadyCallback {

    List<BlockData> mBlockData;
    Context mContext;
    BlockData mBdata;
    ViewHolder mHolder;

    public BlockDataAdapter(List<BlockData> mBlockDatas, Context mContext) {
        this.mBlockData=mBlockDatas;
        this.mContext=mContext;
    }

    @Override
    public BlockDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_data_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlockDataAdapter.ViewHolder holder, int position) {
        mBdata=mBlockData.get(position);
        holder.mBlockDateView.setText(mBdata.getBlockCause());
        SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM");
//        String UpdateDate=dateFormat.p
        holder.mBlockCauseView.setText(mBdata.getUpdateDate());
        Geocoder mGeocoder=new Geocoder(this.mContext, Locale.getDefault());
        try {
            String StreetAddress=String.format("%s,\n%s",mGeocoder.getFromLocation(Double.parseDouble(mBdata.getBlockLatitude()),Double.parseDouble(mBdata.getBlockLongitude()),1).get(0).getAddressLine(0),mGeocoder.getFromLocation(Double.parseDouble(mBdata.getBlockLatitude()),Double.parseDouble(mBdata.getBlockLongitude()),1).get(0).getLocality());
            holder.mBlockAddressView.setText(StreetAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //holder.setMapLocation(mBdata);
//        holder.mMapView.onCreate(null);
//        holder.mMapView.onResume();
//        holder.mMapView.getMapAsync(this);

    }

    @Override
    public int getItemCount() {
        return mBlockData.size();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(mContext);
        GoogleMap map=googleMap;
        //mHolder.setMapLocation(mBdata,map);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.395515, 76.323207),13f));
//        map.addMarker(new MarkerOptions()
//        .position(new LatLng(10.395515, 76.323207)));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        public TextView mBlockCauseView,mBlockDateView,mBlockAddressView;
        public MapView mMapView;
        GoogleMap map;
        public ViewHolder(View itemView) {
            super(itemView);
            mBlockCauseView= (TextView) itemView.findViewById(R.id.block_cause);
            mBlockDateView= (TextView) itemView.findViewById(R.id.block_update_date);
            mBlockAddressView= (TextView) itemView.findViewById(R.id.block_address);
            mMapView= (MapView) itemView.findViewById(R.id.mapView);
            if (mMapView!=null){
                mMapView.onCreate(null);
                mMapView.onResume();
                mMapView.getMapAsync(this);
            }

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(mContext);
            int position=getAdapterPosition();
            map=googleMap;
            if (map!=null){
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(mBlockData.get(position).getBlockLatitude()),Double.parseDouble(mBlockData.get(position).getBlockLongitude())),13f));
                map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(mBlockData.get(position).getBlockLatitude()),Double.parseDouble(mBlockData.get(position).getBlockLongitude()))));
            }
        }
    }
}
