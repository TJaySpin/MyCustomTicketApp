package com.example.mytaobaounion.UI.Fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytaobaounion.Adapter.HomePagerContentAdapter;
import com.example.mytaobaounion.Base.BaseApplication;
import com.example.mytaobaounion.Base.BaseFragment;
import com.example.mytaobaounion.Model.Domain.Histories;
import com.example.mytaobaounion.Model.Domain.SearchContent;
import com.example.mytaobaounion.Model.Domain.SearchRecommend;
import com.example.mytaobaounion.Presenter.IItemInfo;
import com.example.mytaobaounion.Presenter.ISearchPresenter;
import com.example.mytaobaounion.R;
import com.example.mytaobaounion.UI.Custom.TextFlowLayout;
import com.example.mytaobaounion.Utils.KeyBoardUtils;
import com.example.mytaobaounion.Utils.LogUtils;
import com.example.mytaobaounion.Utils.PresenterManager;
import com.example.mytaobaounion.Utils.SizeUtils;
import com.example.mytaobaounion.Utils.TicketUtils;
import com.example.mytaobaounion.Utils.ToastUtils;
import com.example.mytaobaounion.View.ISearchCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchCallback, HomePagerContentAdapter.OnListItemClickListener, TextFlowLayout.onFlowTextClickListener {

    private ISearchPresenter searchPresenter;

    @BindView(R.id.search_history_textflow)
    public TextFlowLayout textFlowHistory;

    @BindView(R.id.search_recommend_textflow)
    public TextFlowLayout textFlowRecommend;

    @BindView(R.id.search_history_container)
    public LinearLayout historyContainer;

    @BindView(R.id.search_recommend_container)
    public LinearLayout recommendContainer;

    @BindView(R.id.search_history_delete)
    public ImageView deleteImg;

    @BindView(R.id.search_result_list)
    public RecyclerView searchResult;

    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout searchContainer;

    @BindView(R.id.search_et)
    public EditText searchEditText;

    @BindView(R.id.cancelImg)
    public ImageView cancelImg;

    @BindView(R.id.cancelBtn)
    public TextView cancelTextView;

    private HomePagerContentAdapter searchResultAdapter;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_search_fragment_layout,container,false);
    }


    @Override
    protected void initView(View rootView) {
        searchResult.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultAdapter = new HomePagerContentAdapter();
        searchResult.setAdapter(searchResultAdapter);
        searchResult.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = SizeUtils.dip2px(getContext(),2f);
                outRect.bottom = SizeUtils.dip2px(getContext(),2f);
            }
        });

        searchContainer.setEnableRefresh(false);
        searchContainer.setEnableLoadmore(true);
        searchContainer.setEnableOverScroll(true);
    }


    @Override
    protected void initPresenter() {
        searchPresenter = PresenterManager.getInstance().getmSearchPresenter();
        searchPresenter.registerViewCallback(this);
    }


    @Override
    protected void initListener() {
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchPresenter!=null){
                    searchPresenter.removeHistory();
                }
            }
        });

        searchContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if(searchPresenter!=null){
                    searchPresenter.loadMore();
                }
            }
        });


        searchResultAdapter.setListItemClickListener(this);


        textFlowHistory.setFlowTextClickListener(this);
        textFlowRecommend.setFlowTextClickListener(this);


        //给EditText设置监听，调用editSearch进行搜索
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    String searchWords = v.getText().toString().trim();
                    if(!TextUtils.isEmpty(searchWords)&&searchPresenter!=null){
                        editSearch(searchWords);
                    }
                }
                return false;
            }
        });


        //根据搜索框中有没有内容，从而调整按钮是“取消”还是“搜索”
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchWords = s.toString().trim();
                cancelTextView.setText(TextUtils.isEmpty(searchWords)?"取消":"搜索");
                cancelImg.setVisibility(searchWords.length()>0?View.VISIBLE:View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.getText().clear();

                //隐藏键盘，自己写了一个工具类
                KeyBoardUtils.hideKB(getContext(),v);
                switchToHistoryPage();
            }
        });


        //这里是设置取消/搜索按键，上面是取消图标
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SearchOrCancel = cancelTextView.getText().toString();
                if(SearchOrCancel.equals("搜索")){
                    KeyBoardUtils.hideKB(getContext(),v);
                    String searchWords = searchEditText.getText().toString().trim();
                    editSearch(searchWords);
                }
                else if(SearchOrCancel.equals("取消")){
                    KeyBoardUtils.hideKB(getContext(),v);
                    switchToHistoryPage();
                }
            }
        });
    }

    //当点击取消按钮和取消图标时，自动跳到历史和推荐页面，注意要隐藏下面的内容（tkrefreshlayout以及里面的rv）
    private void switchToHistoryPage() {
        if(searchPresenter!=null){
            searchPresenter.getHistory();
        }

        if(textFlowRecommend.getContentSize()!=0){
            recommendContainer.setVisibility(View.VISIBLE);
        }
        else{
            recommendContainer.setVisibility(View.GONE);
        }

        searchContainer.setVisibility(View.GONE);

    }




    //因此一打开界面就会先去获取历史和推荐数据
    @Override
    protected void loadData() {
        searchPresenter.onSearch("键盘");
        searchPresenter.getHistory();
        searchPresenter.getRecommendWords();
    }



    //在onSearch搜索之前进行一些显示相关的操作，View层的搜索都是用editSearch
    private void editSearch(String searchWords) {
        LogUtils.i(SearchFragment.class,"进入editSearch");
        if(searchPresenter!=null){
            searchResult.scrollToPosition(0);
            searchEditText.setText(searchWords);
            searchEditText.setFocusable(true);
            searchEditText.requestFocus();

            //将光标移动输入内容至末尾
            searchEditText.setSelection(searchWords.length(),searchWords.length());
            searchPresenter.onSearch(searchWords);

            KeyBoardUtils.hideKB(getContext(),this.getView());
        }
    }



    @Override
    protected void release() {
        if(searchPresenter!=null){
            searchPresenter.unregistViewCallback(this);
        }
    }

    @Override
    public int getResId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }


    @Override
    public void onLoadHistory(Histories history) {
        setUpState(State.SUCCESS);
        if(history==null||history.getHistoriesList().size()==0){
            LogUtils.i(SearchFragment.class,"历史数据为空");
            historyContainer.setVisibility(View.GONE);
        }
        else{
            LogUtils.i(SearchFragment.class,"进入读取历史");
            textFlowHistory.setTextList(history.getHistoriesList());
            historyContainer.setVisibility(View.VISIBLE);
        }
    }



    /**
     * 这里之所以是getHistory，是因为在点击deleteImg删除后，先调用逻辑层的removeHistory把缓存里历史记录全删除了；
     * removeHistory中会来到该方法，再次调用逻辑层的getHistory，继续走到view层onLoadHistory；
     * 因为之前把缓存全部删了，因此history是null，那么historyContainer会自动隐藏，从而达到了删除历史记录的效果
     */
    @Override
    public void onRemoveHistory() {
        if(searchPresenter!=null){
            searchPresenter.getHistory();
        }
    }

    @Override
    public void onLoadSearchSuccess(SearchContent searchContent) {
        setUpState(State.SUCCESS);
        historyContainer.setVisibility(View.GONE);
        recommendContainer.setVisibility(View.GONE);
        //searchResult.setVisibility(View.VISIBLE);
        searchContainer.setVisibility(View.VISIBLE);
        //LogUtils.i(SearchFragment.class,searchContent.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().get(0).getCover());
        searchResultAdapter.setData(searchContent.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
    }

    @Override
    public void onContentLoadMore(SearchContent searchContent) {
        if(searchContainer!=null){
            searchContainer.finishLoadmore();
        }
        List<SearchContent.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> data = searchContent.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        searchResultAdapter.setLoadMoreData(data);
        ToastUtils.showToast(getContext(),"加载了"+data.size()+"件宝贝");
    }

    @Override
    public void onLoadMoreError() {
        if(searchContainer!=null){
            searchContainer.finishLoadmore();
        }
        ToastUtils.showToast(getContext(),"网络出错，请稍后重试");
    }

    @Override
    public void onLoadMoreEmpty() {
        if(searchContainer!=null){
            searchContainer.finishLoadmore();
        }
        ToastUtils.showToast(getContext(),"没有更多数据了");
    }



    @Override
    public void onLoadRecommendWords(List<SearchRecommend.DataDTO> dataDTO) {
        //这里设置显示的都是同一个界面，每个方法都加了但是好像并不需要都加，暂时忘了为什么
        setUpState(State.SUCCESS);

        //显示推荐条目，因此用一个列表保存拿到的推荐数据名称（getKeyWord）
        List<String> recommends=new ArrayList<>();

        if(dataDTO==null||dataDTO.size()==0){
            recommendContainer.setVisibility(View.GONE);
        }
        else{
            for(SearchRecommend.DataDTO data:dataDTO){
                recommends.add(data.getKeyword());
            }
            //自定义view显示推荐和历史条目
            textFlowRecommend.setTextList(recommends);
            recommendContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnItemClick(IItemInfo ItemData) {
        TicketUtils.toTicketPage(getContext(),ItemData);
    }

    @Override
    protected void onRetryClick() {
        if(searchPresenter!=null){
            searchPresenter.searchRetry();
        }
    }

    @Override
    public void onFlowTextClick(String text) {                                                      //这里text是在setText里面拿到的
       editSearch(text);
    }
}