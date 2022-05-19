package com.example.myapplication.fragment.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.MypostAdapter;
import com.example.myapplication.utils.BasicInfo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DraftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DraftFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected MypostAdapter mypostAdapter;

    public DraftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DraftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DraftFragment newInstance(String param1, String param2) {
        DraftFragment fragment = new DraftFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_draft, container, false);
        if(BasicInfo.mDraftlist != null){
            RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
//            MypostAdapter mAdapter = new MypostAdapter(getActivity(), BasicInfo.mDraftlist);
//            mRecyclerView.setAdapter(mAdapter);
            mypostAdapter = new MypostAdapter(BasicInfo.mDraftlist,getContext());
            mypostAdapter.setRecyclerManager(mRecyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        }
        return root;
    }
}