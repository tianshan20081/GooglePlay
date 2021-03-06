package com.lipan.ui.fragment;

import java.util.List;

import com.lipan.bean.AppInfo;
import com.lipan.http.protocol.AppProtocol;
import com.lipan.http.protocol.GameProtocol;
import com.lipan.ui.adapter.ListBaseAdapter;
import com.lipan.ui.holder.MoreHolder;
import com.lipan.ui.widget.LoadingPage.LoadResult;

import android.view.View;
import android.widget.AbsListView;

import com.lipan.ui.widget.BaseListView;
import com.lipan.utils.UIUtils;

public class GameFragment extends BaseFragment {
	private BaseListView mListView;
	private GameAdapter mAdapter;
	private List<AppInfo> mDatas;

	@Override
	protected LoadResult load() {
		GameProtocol protocol = new GameProtocol();
		mDatas = protocol.load(0);
		return check(mDatas);
	}

	/** 可见时，需要启动监听，以便随时根据下载状态刷新页面 */
	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) {
			mAdapter.startObserver();
			mAdapter.notifyDataSetChanged();
		}
	}

	/** 不可见时，需要关闭监听 */
	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.stopObserver();
		}
	}

	@Override
	protected View createLoadedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mAdapter = new GameAdapter(mListView, mDatas);
		mAdapter.startObserver();
		mListView.setAdapter(mAdapter);
		return mListView;
	}

	class GameAdapter extends ListBaseAdapter {

		public GameAdapter(AbsListView listView, List<AppInfo> datas) {
			super(listView, datas);
		}

		/** 加载更多 */
		@Override
		public List<AppInfo> onLoadMore() {
			GameProtocol protocol = new GameProtocol();
			return protocol.load(getData().size());
		}
	}
}
