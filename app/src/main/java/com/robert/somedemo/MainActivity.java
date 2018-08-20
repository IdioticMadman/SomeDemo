package com.robert.somedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import robert.somedemo.R;
import robert.somedemo.UserDao;

public class MainActivity extends AppCompatActivity {


    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void initData() {
        insertUsers();
        setData();
    }

    private void setData() {
        List<String> users = getUsers();
        if (mArrayAdapter == null) {
            mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.test_list_item, android.R.id.text1, users);
        }else {
            mArrayAdapter.clear();
            mArrayAdapter.addAll(users);
        }

        mListView.setAdapter(mArrayAdapter);
    }

    private List<String> getUsers() {
        List<robert.somedemo.User> users = mUserDao.loadAll();
        List<String> tempUsers = new ArrayList<>();
        for (robert.somedemo.User user : users) {
            tempUsers.add(user.toString());
        }
        return tempUsers;
    }


    private void insertUsers() {
        mUserDao = robert.somedemo.MyApplication.getDaoSession().getUserDao();
        List<robert.somedemo.User> users = new ArrayList<>();
        robert.somedemo.User user = new robert.somedemo.User("1", "张三", "开发");
        robert.somedemo.User user1 = new robert.somedemo.User("2", "李四", "运维");
        robert.somedemo.User user2 = new robert.somedemo.User("3", "王五", "测试");
        users.add(user);
        users.add(user1);
        users.add(user2);
        mUserDao.insertOrReplaceInTx(users);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv);
    }

    public void add(View view) {
        mUserDao.insertOrReplace(new robert.somedemo.User("4","熊大","产品"));
        setData();
    }

    public void refresh(View view) {
        mUserDao.update(new robert.somedemo.User("4","熊二","经理"));
        setData();
    }
}
