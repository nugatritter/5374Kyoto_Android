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


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.kubotaku.android.code4kyoto5374.R;
import com.kubotaku.android.code4kyoto5374.data.GarbageType;
import com.kubotaku.android.code4kyoto5374.segregation.GarbageSegregationData;
import com.kubotaku.android.code4kyoto5374.segregation.GarbageSegregationType;
import com.kubotaku.android.code4kyoto5374.segregation.GarbageSegregator;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * ゴミの分別方法表示Fragment
 */
public class SegregationFragment extends Fragment {

    public static final String TAG = SegregationFragment.class.getSimpleName();

    public static SegregationFragment newInstance() {
        SegregationFragment fragment = new SegregationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SegregationFragment() {
        // Required empty public constructor
    }

    private OnFragmentTaskListener onFragmentTaskListener;

    private List<GarbageSegregationData> matchGarbageList = new ArrayList<>();

    private SegregationViewAdapter segregationViewAdapter;

    private EditText inputTarget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seregration, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentTaskListener) {
            onFragmentTaskListener = (OnFragmentTaskListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentTaskListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupViews();
    }

    private void setupViews() {
        View view = getView();

        GridView gridView = (GridView) view.findViewById(R.id.segregation_grid);
        segregationViewAdapter = new SegregationViewAdapter();
        gridView.setAdapter(segregationViewAdapter);

        inputTarget = (EditText) view.findViewById(R.id.segregation_edit_target);

        Button btnSearch = (Button) view.findViewById(R.id.segregation_btn_search);
        btnSearch.setOnClickListener(onClickSearchBtnListener);
    }


    private View.OnClickListener onClickSearchBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new SegregationTask().execute();
        }
    };

    private GarbageSegregator segregator;

    private class SegregationTask extends AsyncTask<Void, Void, List<GarbageSegregationData>> {

        private String inputText;

        public SegregationTask() {
            if (onFragmentTaskListener != null) {
                onFragmentTaskListener.onTaskStart();
            }
            inputText = inputTarget.getText().toString();
        }

        @Override
        protected List<GarbageSegregationData> doInBackground(Void... voids) {
            if (segregator == null) {
                segregator = new GarbageSegregator();
            }
            return segregator.findCandidates(inputText);
        }

        @Override
        protected void onPostExecute(List<GarbageSegregationData> candidates) {
            if (onFragmentTaskListener != null) {
                onFragmentTaskListener.onTaskStop();
            }

            matchGarbageList.clear();

            if (candidates != null) {
                matchGarbageList.addAll(candidates);
            } else {
                Toast toast = Toast.makeText(getContext(), R.string.message_missing_match_garbage, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            segregationViewAdapter.notifyDataSetChanged();
        }
    }

    // -------------------------------------

    private static class ViewHolder {
        TextView name;
        TextView note;
        TextView type1;
        TextView type2;
        TextView type3;
        TextView type4;
    }

    private class SegregationViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return matchGarbageList.size();
        }

        @Override
        public GarbageSegregationData getItem(int i) {
            if (i < matchGarbageList.size()) {
                return matchGarbageList.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view = inflater.inflate(R.layout.garbage_segregation_view, parent, false);

                holder.name = (TextView) view.findViewById(R.id.segregation_text_name);
                holder.note = (TextView) view.findViewById(R.id.segregation_text_note);
                holder.type1 = (TextView) view.findViewById(R.id.segregation_type_1);
                holder.type2 = (TextView) view.findViewById(R.id.segregation_type_2);
                holder.type3 = (TextView) view.findViewById(R.id.segregation_type_3);
                holder.type4 = (TextView) view.findViewById(R.id.segregation_type_4);

                convertView = view;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final GarbageSegregationData data = getItem(position);
            if (data != null) {

                holder.name.setText(data.name);
                holder.note.setText(data.note);

                Context context = getContext();

                holder.type1.setText(null);
                holder.type1.setBackgroundColor(Color.TRANSPARENT);
                GarbageSegregationType type1 = data.getType(0);
                if (type1 != null) {
                    holder.type1.setText(GarbageType.getTypeText(type1.type));
                    holder.type1.setTextColor(GarbageType.getViewTextColor(context, type1.type));
                    holder.type1.setBackgroundColor(GarbageType.getViewColor(context, type1.type));
                }

                holder.type2.setText(null);
                holder.type2.setBackgroundColor(Color.TRANSPARENT);
                GarbageSegregationType type2 = data.getType(1);
                if (type2 != null) {
                    holder.type2.setText(GarbageType.getTypeText(type2.type));
                    holder.type2.setTextColor(GarbageType.getViewTextColor(context, type2.type));
                    holder.type2.setBackgroundColor(GarbageType.getViewColor(context, type2.type));
                }

                holder.type3.setText(null);
                holder.type3.setBackgroundColor(Color.TRANSPARENT);
                holder.type3.setVisibility(View.GONE);
                GarbageSegregationType type3 = data.getType(2);
                if (type3 != null) {
                    holder.type3.setVisibility(View.VISIBLE);
                    holder.type3.setText(GarbageType.getTypeText(type3.type));
                    holder.type3.setTextColor(GarbageType.getViewTextColor(context, type3.type));
                    holder.type3.setBackgroundColor(GarbageType.getViewColor(context, type3.type));
                }

                holder.type4.setText(null);
                holder.type4.setBackgroundColor(Color.TRANSPARENT);
                holder.type4.setVisibility(View.GONE);
                GarbageSegregationType type4 = data.getType(3);
                if (type4 != null) {
                    holder.type4.setVisibility(View.VISIBLE);
                    holder.type4.setText(GarbageType.getTypeText(type4.type));
                    holder.type4.setTextColor(GarbageType.getViewTextColor(context, type4.type));
                    holder.type4.setBackgroundColor(GarbageType.getViewColor(context, type4.type));
                }
            }

            return convertView;
        }
    }
}
