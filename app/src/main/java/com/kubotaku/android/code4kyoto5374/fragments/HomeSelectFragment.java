/**
 * Copyright 2017 kubotaku1119 <kubotaku1119@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kubotaku.android.code4kyoto5374.fragments;


import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kubotaku.android.code4kyoto5374.OnPermissionRequestListener;
import com.kubotaku.android.code4kyoto5374.OnRequestPermissionResultListener;
import com.kubotaku.android.code4kyoto5374.R;
import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.AreaMaster;
import com.kubotaku.android.code4kyoto5374.data.HomePlace;
import com.kubotaku.android.code4kyoto5374.util.AreaUtil;
import com.kubotaku.android.code4kyoto5374.util.Prefs;
import com.kubotaku.android.code4kyoto5374.widget.GarbageCollectDayWidget;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ごみ収集日表示地点選択画面Fragment
 */
public class HomeSelectFragment extends Fragment implements OnRequestPermissionResultListener {

    public static final String TAG = HomeSelectFragment.class.getSimpleName();

    public static final int MODE_INITIALIZE = 0;

    public static final int MODE_RE_SELECT = 1;

    private static final int REQUEST_PLACE_PICKER = 1;

    private static final int REQUEST_PERMISSIONS = 2;

    private static final String PARAM_MODE = "param_mode";

    public static HomeSelectFragment newInstance(final int mode) {
        final HomeSelectFragment fragment = new HomeSelectFragment();

        final Bundle args = new Bundle();
        args.putInt(PARAM_MODE, mode);
        fragment.setArguments(args);

        return fragment;
    }

    public HomeSelectFragment() {
        // Required empty public constructor
    }

    private OnPermissionRequestListener onPermissionRequestListener;

    private OnCloseFragmentListener onCloseFragmentListener;

    private int mode;

    private Place pickedPlace;

    private HomePlace homePlace;

    private RealmResults<AreaDays> areaDaysResults;

    private AreaDays selectedAreaDays;

    private Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mode = getArguments().getInt(PARAM_MODE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPermissionRequestListener) {
            onPermissionRequestListener = (OnPermissionRequestListener) context;
        }

        if (context instanceof OnCloseFragmentListener) {
            onCloseFragmentListener = (OnCloseFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onPermissionRequestListener = null;
        onCloseFragmentListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_select, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        realm = Realm.getDefaultInstance();
        if (!checkPermissions()) {
            requestPermissions();
        }

        getActivity().setTitle("地域の設定");

        loadHomePlace();
        setupViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    private void setupViews() {
        final View view = getView();

        final Button btnSelectPlace = (Button) view.findViewById(R.id.button_select_place);
        btnSelectPlace.setOnClickListener(onClickSelectPlaceBtnListener);

        final Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(onClickOkBtnListener);

        final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(onClickCancelBtnListener);
        if (mode == MODE_INITIALIZE) {
            btnCancel.setVisibility(View.GONE);

            view.findViewById(R.id.space_bw_btn).setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                this.pickedPlace = PlacePicker.getPlace(getContext(), data);
                if (this.pickedPlace != null) {
                    String address = this.pickedPlace.getAddress().toString();

                    final String city = AreaUtil.parseCity(address);
                    final String ward = AreaUtil.parseWard(address);
                    final String town = AreaUtil.parseTown(address);
                    if (AreaUtil.isValidCity(city)) {

                        boolean existAreaDays = false;
                        final RealmResults<AreaMaster> areaMasterResult = realm.where(AreaMaster.class).equalTo("areaName", ward).findAll();
                        if (areaMasterResult.size() != 0) {

                            final AreaMaster areaMaster = areaMasterResult.first();

                            final RealmResults<AreaDays> areaDaysResult = realm.where(AreaDays.class)
                                    .equalTo("masterAreaID", areaMaster.areaID)
                                    .contains("areaName", town)
                                    .findAll();

                            if (areaDaysResult.size() > 0) {
                                showPlaceSelector(areaDaysResult);
                                this.areaDaysResults = areaDaysResult;
                                existAreaDays = true;
                            }
                        }

                        if (!existAreaDays) {
                            final TextView textAreaDays = (TextView) getView().findViewById(R.id.text_selected_area_days);
                            textAreaDays.setText("選択された地域の情報がありませんでした。選択し直して下さい");
                        }

                        address = city + ward + town;

                    } else {
                        // 対象地域以外のとき
                        final Toast toast = Toast.makeText(getContext(), "無効な地域です。選択し直して下さい", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    TextView textName = (TextView) getView().findViewById(R.id.text_selected_place);
                    textName.setText(address);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPermissionRequested(String[] permissions, int[] grantResults) {
        if (!checkPermissions()) {
            Toast.makeText(getContext(), R.string.message_need_access_location, Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPermissions() {
        final Activity activity = getActivity();
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private void requestPermissions() {
        if (onPermissionRequestListener != null) {
            onPermissionRequestListener.requestPermission(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS);
        }
    }


    private void callPlacePicker() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            if (this.pickedPlace != null) {
                builder.setLatLngBounds(new LatLngBounds(pickedPlace.getLatLng(), pickedPlace.getLatLng()));
            } else if ((this.selectedAreaDays != null) && (this.homePlace != null)) {
                final LatLng latLng = new LatLng(homePlace.latitude, homePlace.longitude);
                builder.setLatLngBounds(new LatLngBounds(latLng, latLng));
            }
            final Activity activity = getActivity();
            startActivityForResult(builder.build(activity), REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void showValidSelectedAreaDays(final AreaDays selectedAreaDays) {
        final TextView textAreaDays = (TextView) getView().findViewById(R.id.text_selected_area_days);
        textAreaDays.setText(selectedAreaDays.toInfoString());
        this.selectedAreaDays = selectedAreaDays;
    }

    private void showPlaceSelector(RealmResults<AreaDays> areaDaysResult) {

        final TextView textAlert = (TextView) getView().findViewById(R.id.text_place_alert);
        final Spinner spinner = (Spinner) getView().findViewById(R.id.spinner_select_town);

        if (areaDaysResult.size() <= 1) {
            textAlert.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);

            if (areaDaysResult.size() == 1) {
                showValidSelectedAreaDays(areaDaysResult.first());
            }

        } else {
            textAlert.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            for (AreaDays areaDays : areaDaysResult) {
                adapter.add(areaDays.areaName);
            }
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(onTownSelectedListener);
        }
    }

    private void loadHomePlace() {
        this.homePlace = Prefs.loadHomePlace(getContext());

        final RealmResults<AreaDays> areaDaysRealmResults = realm.where(AreaDays.class)
                .equalTo("masterAreaID", homePlace.areaMasterID)
                .equalTo("areaName", homePlace.areaName).findAll();
        if (areaDaysRealmResults.size() != 0) {
            selectedAreaDays = areaDaysRealmResults.first();
            showValidSelectedAreaDays(selectedAreaDays);

            TextView textName = (TextView) getView().findViewById(R.id.text_selected_place);
            textName.setText(homePlace.address);
        }
    }

    private void saveHomePlace() {
        if ((selectedAreaDays != null) && (pickedPlace != null)) {
            if (homePlace == null) {
                homePlace = new HomePlace();
            }

            homePlace.address = pickedPlace.getAddress().toString();
            homePlace.latitude = pickedPlace.getLatLng().latitude;
            homePlace.longitude = pickedPlace.getLatLng().longitude;

            homePlace.areaMasterID = selectedAreaDays.masterAreaID;
            homePlace.areaName = selectedAreaDays.areaName;

            Prefs.saveHomePlace(getContext(), homePlace);
        }
    }

    // -----------------------------

    private AdapterView.OnItemSelectedListener onTownSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final String areaName = (String) parent.getAdapter().getItem(position);
            final AreaDays selectedAreaDays = areaDaysResults.where().equalTo("areaName", areaName).findFirst();
            showValidSelectedAreaDays(selectedAreaDays);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    private View.OnClickListener onClickSelectPlaceBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callPlacePicker();
        }
    };

    private View.OnClickListener onClickOkBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            saveHomePlace();

            if (onCloseFragmentListener != null) {
                onCloseFragmentListener.onCloseFragment(HomeSelectFragment.this);
            }

            updateAppWidget();
        }
    };

    private View.OnClickListener onClickCancelBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (onCloseFragmentListener != null) {
                onCloseFragmentListener.onCloseFragment(HomeSelectFragment.this);
            }
        }
    };

    private void updateAppWidget() {
        Context context = getContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, GarbageCollectDayWidget.class);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.list_collect_days);
    }
}
