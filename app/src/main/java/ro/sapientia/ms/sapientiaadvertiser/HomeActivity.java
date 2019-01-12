package ro.sapientia.ms.sapientiaadvertiser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Data> lsData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.activity_home,container,false);
        mRecyclerView = fragmentView.findViewById(R.id.recycler_view_home);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(),lsData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lsData = new ArrayList<>();
        lsData.add(new Data("Karácsonyi ünnepély",
                "December 18-án a Hallgatói Önkormányzat egy meghitt karácsonyi ünnepéllyel ajándékozta meg az egyetemi közösséget.",
                R.drawable.ic_karacsony));
    }
}
