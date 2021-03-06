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
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fbreader.common.FBReaderHelper;
import com.fbreader.common.IRefresh;
import com.fbreader.view.adapter.MarkersAdapter;

import org.geometerplus.fbreader.book.Book;
import org.geometerplus.fbreader.book.BookEvent;
import org.geometerplus.fbreader.book.Bookmark;
import org.geometerplus.fbreader.book.IBookCollection;
import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.ui.android.R;

import java.util.List;

public class MarkersFragment extends Fragment implements IRefresh, IBookCollection.Listener<Book> {

    private RecyclerView recyclerView;
    private MarkersAdapter adapter;

    private FBReaderHelper fbReaderHelper;

    public static MarkersFragment instance() {
        return new MarkersFragment();
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

        adapter = new MarkersAdapter(null);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                fbReaderHelper.gotoBookMark(getActivity(), (Bookmark) adapter.getItem(position));
                ZLApplication.Instance().runAction(ActionCode.HIDE_TOC);
            }
        });

        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                ZLApplication.Instance().runAction(ActionCode.SHOW_BOOKMARK, adapter.getItem(position));
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        fbReaderHelper = new FBReaderHelper(getActivity());
        fbReaderHelper.bindToService(new Runnable() {
            @Override
            public void run() {
                fbReaderHelper.addListener(MarkersFragment.this);
                refresh();
            }
        });
    }

    @Override
    public void refresh() {
        if (fbReaderHelper == null) return;
        List<Bookmark> bookmarks = fbReaderHelper.loadBookMarks(fbReaderHelper.getCurrentBook());
        adapter.setNewData(bookmarks);
    }

    @Override
    public void onBookEvent(BookEvent event, Book book) {
        switch (event) {
            default:
                break;
            case BookmarksUpdated:
                refresh();
                break;
        }
    }

    @Override
    public void onBuildEvent(IBookCollection.Status status) {

    }

}
