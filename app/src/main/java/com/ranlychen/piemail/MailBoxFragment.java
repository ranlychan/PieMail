package com.ranlychen.piemail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MailBoxFragment extends Fragment implements OnClickListener {

    private List<MailItem> mailList = new ArrayList<>();
    private MailBoxAdapter mailboxAdapter;
    private RecyclerView recyclerView;
    private View headerView;
    private ImageButton composeNewMail;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mail_box, container, false);

        initView(view);
        //initMailData();
        return view;
    }

    private void initView(View view){

        recyclerView = view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        headerView = getLayoutInflater().inflate(R.layout.mailbox_header,null,false);
        composeNewMail = headerView.findViewById(R.id.newMailButton);
        composeNewMail.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        mailboxAdapter = new MailBoxAdapter(R.layout.item_mail,mailList);
        mailboxAdapter.addHeaderView(headerView);
        mailboxAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                Intent intent = new Intent(getContext(),MailDetailActivity.class);
                intent.putExtra("MailItem",mailList.get(i));
                startActivity(intent);
                //点击进入邮件详情
            }
        });
        mailboxAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (true) {
                    mailboxAdapter.getLoadMoreModule().loadMoreEnd();
                } else {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            configLoadMoreData();
                        }
                    }, 2000);

                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {//下拉刷新监听
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                initMailData();
            }
        });
        recyclerView.setAdapter(mailboxAdapter);
    }

    private void initMailData(){

        mailList = new SQLite().select().from(MailItem.class).queryList();//.where(MailItem_Table.belongToAccount.eq(""))
        if(mailList.isEmpty()){
            Log.d("saveNewMail","mailList of SQL is empty");
        }else{
            Log.d("saveNewMail","mailList of SQL is not empty,size:"+mailList.size());
            for(int j=0;j<mailList.size();j++){
                mailboxAdapter.setNewData(mailList);
            }
        }

        //mailboxAdapter.addData(mailList.get(0));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newMailButton:
                //mailboxAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void refreshData(){

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtils mailUtils = new MailUtils();
                try {
                    List<MailItem> filteredList = filterNewMails(mailUtils.getMessageFromServer());
                    if(!filteredList.isEmpty()){
                        Log.d("refreshData","filteredList is not empty");
                        mailList.addAll(filteredList);
                        for(int i=0;i<filteredList.size();i++){
                            filteredList.get(i).save();
                        }
                    }else{
                        //no more new data
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mailboxAdapter.notifyDataSetChanged();
    }

    private void configLoadMoreData(){
        //refreshData();
        //mailboxAdapter.notifyDataSetChanged();
    }

    private List<MailItem> filterNewMails(List<MailItem> list){
        List<MailItem> newMailList = null;

        if(!list.isEmpty()){
            Log.d("filterNewMail","mailList is not empty,size:"+list.size());
            newMailList = new ArrayList<>();

            for(int k = 0;k<list.size();k++){
                if(new SQLite().select()
                        .from(MailItem.class)
                        .where(MailItem_Table.id.eq(list.get(k).getId()))
                        .querySingle()
                        ==null){
                    newMailList.add(list.get(k));
                }
            }
        }

        return newMailList;
    }


    class MailBoxAdapter extends BaseQuickAdapter<MailItem, BaseViewHolder> implements LoadMoreModule {

        public MailBoxAdapter(int layoutResId, @Nullable List<MailItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, MailItem mailItem) {
            try {
                baseViewHolder.setText(R.id.item_mail_title,mailItem.getSubject())
                        .setText(R.id.item_mail_from,mailItem.getFromName() + mailItem.getFromAddress())
                        .setText(R.id.item_mail_time,mailItem.getSendTime())
                        .setText(R.id.item_mail_content,mailItem.getBodyText())
                        .setImageResource(R.id.item_mail_logo,mailItem.getLogoResId());

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
