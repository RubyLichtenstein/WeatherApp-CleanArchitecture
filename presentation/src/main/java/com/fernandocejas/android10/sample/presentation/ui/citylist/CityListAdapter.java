/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.ui.citylist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.model.CityModel;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

/**
 * Adaptar that manages a collection of {@link CityModel}.
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityViewHolder> {

  private PublishSubject<CityModel> onItemClickSubject = PublishSubject.create();

  private List<CityModel> citiesCollection;
  private final LayoutInflater layoutInflater;

  @Inject CityListAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.citiesCollection = new ArrayList<>();
  }

  @Override public int getItemCount() {
    return (this.citiesCollection != null) ? this.citiesCollection.size() : 0;
  }

  @Override public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.city_list_row, parent, false);
    return new CityViewHolder(view);
  }

  @Override public void onBindViewHolder(CityViewHolder holder, final int position) {
    final CityModel cityModel = this.citiesCollection.get(position);
    holder.textViewTitle.setText(cityModel.getName());
    RxView.clicks(holder.itemView).subscribe(
        o -> CityListAdapter.this.onItemClickSubject.onNext(cityModel));
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void setCitiesCollection(Collection<CityModel> citiesCollection) {
    this.validateCitiesCollection(citiesCollection);
    this.citiesCollection = (List<CityModel>) citiesCollection;
    this.notifyDataSetChanged();
  }

  public void addCityModel(CityModel cityModel) {
    //todo validate ?
    this.citiesCollection.add(cityModel);
    this.notifyItemInserted(getItemCount() - 1);
  }

  private void validateCitiesCollection(Collection<CityModel> cityModelCollection) {
    if (cityModelCollection == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }

  static class CityViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.title) TextView textViewTitle;

    CityViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public Observable<CityModel> getCityClickObs() {
    return onItemClickSubject;
  }
}
