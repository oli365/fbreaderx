package com.fbreader.view.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.geometerplus.fbreader.book.Bookmark;
import org.geometerplus.zlibrary.ui.android.R;

import java.util.List;

public class MarkersAdapter extends BaseQuickAdapter<Bookmark, BaseViewHolder> {


    public MarkersAdapter(@Nullable List<Bookmark> data) {
        super(R.layout.holder_marker_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bookmark item) {
        helper.setText(R.id.txtMarker, item.getText())
                .setText(R.id.txtContent, item.getOriginalText());
//                .addOnClickListener(R.id.edtToEdit);
    }

}
