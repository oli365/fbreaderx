package com.fbreader.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fbreader.common.FBReaderHelper;
import com.fbreader.view.adapter.FragmentViewPagerAdapter;
import com.fbreader.common.IRefresh;
import com.google.android.material.tabs.TabLayout;

import org.geometerplus.fbreader.fbreader.ActionCode;
import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.ui.android.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TOCFragment extends Fragment implements IRefresh {

    private View layoutEmpty;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentViewPagerAdapter adapter;

    private FBReaderHelper fbReaderHelper;

    public static TOCFragment newInstance() {
        return new TOCFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_toc, container, false);
        layoutEmpty = root.findViewById(R.id.layoutEmpty);
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEmpty();
        initViewPager();
        fbReaderHelper=new FBReaderHelper(getActivity());
        fbReaderHelper.bindToService(null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) refresh();
    }

    @Override
    public void refresh() {
        if (adapter != null)
            for (int i = 0, size = adapter.getFragments().size(); i < size; i++) {
                IRefresh iRefresh = (IRefresh) adapter.getFragments().get(i);
                iRefresh.refresh();
            }
    }

    public void initEmpty() {
        layoutEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZLApplication.Instance().runAction(ActionCode.HIDE_TOC);
            }
        });
    }

    private void initViewPager() {
        String[] titles = {"章节", "笔记"};
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CatalogsFragment());
        fragments.add(new MarkersFragment());
        adapter = new FragmentViewPagerAdapter(getChildFragmentManager());
        adapter.setTitles(Arrays.asList(titles));
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        viewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 10 + 0.5f));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

}
