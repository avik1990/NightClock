package com.app.nightclock.alarm;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.nightclock.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.tomergoldst.timekeeper.BuildConfig;
import com.tomergoldst.timekeeper.core.TimeKeeper;
import com.tomergoldst.timekeeper.model.Alarm;

import java.util.List;

public class AlarmActivity extends AppCompatActivity implements
        AlarmsAdapter.OnAdapterInteractionListener,
        UserDecisionFragment.OnDialogInteractionListener{

    private static final String TAG = AlarmActivity.class.getSimpleName();

    public static final String ARG_ALARM_ID = "alarmUid";

    private AlarmsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyViewTxv;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setTitle(getString(R.string.app_name));*/

        // Ask Storage permission for logging information into file
        if (BuildConfig.DEBUG) {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new BasePermissionListener())
                    .check();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AlarmActivity.this, CreateAlarmActivity.class);
                startActivity(intent);
            }
        });

        mEmptyViewTxv = findViewById(R.id.empty);

        mSwipeRefreshLayout = findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refresh();
                    }
                }
        );

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int verticalDividerSize = DimensionUtils.convertDpToPixels(this, 8);
        int horizontalDividerSize = DimensionUtils.convertDpToPixels(this, 16);
        mRecyclerView.addItemDecoration(new SimpleItemDecoration(verticalDividerSize, horizontalDividerSize));

        mAdapter = new AlarmsAdapter(this);
        mAdapter.addAll(getAlarms());
        mRecyclerView.setAdapter(mAdapter);

    }

    private void updateViewStatus() {
        if (mAdapter.getItemCount() == 0){
            mEmptyViewTxv.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyViewTxv.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private List<Alarm> getAlarms() {
        return TimeKeeper.getAlarms();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_refresh:
                mSwipeRefreshLayout.setRefreshing(true);
                refresh();
                return true;
            case R.id.menu_clear:
                TimeKeeper.clear();
                refresh();
                return true;
            case R.id.menu_remove_persisted:
                TimeKeeper.removePastPersistAlarms();
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void refresh(){
        mAdapter.clear();
        mAdapter.addAll(getAlarms());
        mSwipeRefreshLayout.setRefreshing(false);
        updateViewStatus();
    }

    @Override
    public void onItemClicked(Alarm alarm) {
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_ALARM_ID, alarm.getId());

        UserDecisionFragment.Builder builder = new UserDecisionFragment.Builder()
                .setMessage(getString(R.string.cancel_alarm_confirmation_text))
                .setCancelable(true)
                .setPositiveButtonText(getString(R.string.cancel))
                .setNegativeButtonText(getString(R.string.discard))
                .setBundle(bundle);

        DialogFragment userDecisionFragment = UserDecisionFragment.newInstance(builder);
        userDecisionFragment.show(getSupportFragmentManager(), "userDecisionFragment");

    }

    @Override
    public void onPositiveDecision(String tag, Bundle bundle) {
        long alarmId = bundle.getLong(ARG_ALARM_ID);
        TimeKeeper.cancelAlarm(alarmId);
        mSwipeRefreshLayout.setRefreshing(true);
        refresh();
    }

    @Override
    public void onNegativeDecision(String tag, Bundle bundle) {

    }

    @Override
    public void onCancelDecision(String tag, Bundle bundle) {

    }
}
