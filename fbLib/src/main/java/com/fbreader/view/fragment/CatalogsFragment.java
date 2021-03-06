package com.fbreader.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fbreader.view.adapter.CatalogsAdapter;
import com.fbreader.common.FBReaderHelper;
import com.fbreader.util.FragmentUtils;
import com.fbreader.common.IRefresh;

import org.geometerplus.fbreader.bookmodel.TOCTree;
import org.geometerplus.zlibrary.ui.android.R;
import java.util.List;

public class CatalogsFragment extends Fragment implements IRefresh {

    private RecyclerView recyclerView;
    private CatalogsAdapter adapter;

    private FBReaderHelper fbReaderHelper;

    public static CatalogsFragment instance() {
        return new CatalogsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_catalogs, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new CatalogsAdapter(null);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                TOCTree tocTree = (TOCTree) adapter.getItem(position);
                fbReaderHelper.openBookTo(tocTree);
                FragmentUtils.hide(getParentFragment());

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        fbReaderHelper = new FBReaderHelper(getActivity());
        fbReaderHelper.bindToService(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    @Override
    public void refresh() {
        if (fbReaderHelper == null) return;
        TOCTree all = fbReaderHelper.getBookTOCTree();
        List<TOCTree> alls = all.subtrees();
        if (alls != null && alls.size() > 0 &&adapter.getData().size()<=0)
            adapter.setNewData(alls);
        adapter.setSelectItem(fbReaderHelper.getCurTOCTree());
    }

}
