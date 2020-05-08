package com.aman.sandwichclub;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.aman.sandwichclub.Adapter.SandwichAdapter;
import com.aman.sandwichclub.model.Sandwich;
import com.aman.sandwichclub.utils.JsonUtils;
import com.aman.sandwichclub.utils.SharedPrefsUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<Sandwich> mSandwichList;
    private RecyclerView mRecyclerView;
    private SandwichAdapter mSandwichAdapter;
    private Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mRecyclerView = findViewById(R.id.sandwichRv);
        mRecyclerView.setHasFixedSize(true);
        preparSandwichItem();
        mSandwichAdapter = new SandwichAdapter(mSandwichList);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        int defaultLayout = SharedPrefsUtils.getItemType(this);
        if (defaultLayout == SharedPrefsUtils.GRID_LAYOUT_ID) {
            setGridLayout();
        } else {

            setListLayout();
        }
        mRecyclerView.setAdapter(mSandwichAdapter);
        runLayoutAnimation();

    }

    private void runLayoutAnimation() {
        final Context context = mRecyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        mRecyclerView.setLayoutAnimation(controller);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        updateMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_list_item:
                setListLayout();
                break;
            case R.id.action_grid_item:
                setGridLayout();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListLayout() {
        SharedPrefsUtils.saveItemType(this, SharedPrefsUtils.LIST_LAYOUT_ID);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        updateMenu();
    }

    private void setGridLayout() {
        SharedPrefsUtils.saveItemType(this, SharedPrefsUtils.GRID_LAYOUT_ID);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        updateMenu();
    }

    private void updateMenu() {
        if (mMenu == null) {
            return;
        }
        int defaultLayout = SharedPrefsUtils.getItemType(this);
        if (defaultLayout == SharedPrefsUtils.GRID_LAYOUT_ID) {
            mMenu.findItem(R.id.action_grid_item).setVisible(false);
            mMenu.findItem(R.id.action_list_item).setVisible(true);
        } else {
            mMenu.findItem(R.id.action_grid_item).setVisible(true);
            mMenu.findItem(R.id.action_list_item).setVisible(false);
        }
    }

    private void preparSandwichItem() {
        String[] sandwichList = getResources().getStringArray(R.array.sandwich_details);
        String[] sandwiches = getResources().getStringArray(R.array.sandwich_names);
        mSandwichList = new ArrayList<>();
        for (int i = 0; i < sandwiches.length; i++) {
            String json = sandwichList[i];

            Sandwich sandwich = JsonUtils.parsesandwichJson(json);
            mSandwichList.add(sandwich);
        }
    }
}
