package com.sociorich.app.facebook_grid.Assymetric;


import androidx.recyclerview.widget.RecyclerView;

public abstract class AGVRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  public abstract AsymmetricItem getItem(int position);
}
