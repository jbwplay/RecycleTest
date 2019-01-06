package com.recycletest;

import android.os.Bundle;
import android.support.v4.util.Pools;
import android.support.v7.app.AppCompatActivity;

import com.recycletest.recycleview.AdapterHelper;
import com.recycletest.recycleview.DefaultItemAnimator;
import com.recycletest.recycleview.LinearLayoutManager;
import com.recycletest.recycleview.OpReorderer;
import com.recycletest.recycleview.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OpReorderer.Callback {

    RecyclerView rcy_id;
    RecycleAdapter mRecycleAdapter;
    List<Object> mObjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcy_id = findViewById(R.id.rcy_id);

        for (int i = 0; i < 30; i++) {
            mObjectList.add(new Object());
        }

        mRecycleAdapter = new RecycleAdapter(this, mObjectList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcy_id.setLayoutManager(linearLayoutManager);
        rcy_id.setAdapter(mRecycleAdapter);
        rcy_id.setItemAnimator(new DefaultItemAnimator());

        OpReorderer opReorderer = new OpReorderer(this);
        AdapterHelper.UpdateOp updateOp1 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.ADD, 0, 3, null);
        AdapterHelper.UpdateOp updateOp2 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.ADD, 2, 3, null);
        AdapterHelper.UpdateOp updateOp3 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.ADD, 4, 3, null);
        AdapterHelper.UpdateOp updateOp4 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.MOVE, 5, 3, null);
        AdapterHelper.UpdateOp updateOp5 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.MOVE, 3, 2, null);
        AdapterHelper.UpdateOp updateOp9 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.ADD, 7, 2, null);
        AdapterHelper.UpdateOp updateOp6 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.UPDATE, 2, 6, null);
        AdapterHelper.UpdateOp updateOp8 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.MOVE, 2, 7, null);
        AdapterHelper.UpdateOp updateOp7 = new AdapterHelper.UpdateOp(AdapterHelper.UpdateOp.REMOVE, 3, 2, null);
        mUpdates.add(updateOp1);
        mUpdates.add(updateOp2);
        mUpdates.add(updateOp3);
        mUpdates.add(updateOp4);
        mUpdates.add(updateOp5);
        mUpdates.add(updateOp9);
        mUpdates.add(updateOp6);
        mUpdates.add(updateOp8);
        mUpdates.add(updateOp7);
        opReorderer.reorderOps(mUpdates);

        rcy_id.mAdapterHelper.addUpdateOp(mUpdates.toArray(new AdapterHelper.UpdateOp[mUpdates
                .size()])).preProcess();

    }

    Pools.Pool<AdapterHelper.UpdateOp> mUpdateOpPool = new Pools.SimplePool<AdapterHelper.UpdateOp>(30);

    ArrayList<AdapterHelper.UpdateOp> mUpdates = new ArrayList<AdapterHelper.UpdateOp>();

    @Override
    public AdapterHelper.UpdateOp obtainUpdateOp(int cmd, int positionStart, int itemCount, Object payload) {
        AdapterHelper.UpdateOp op = mUpdateOpPool.acquire();
        if (op == null) {
            op = new AdapterHelper.UpdateOp(cmd, positionStart, itemCount, payload);
        } else {
            op.cmd = cmd;
            op.positionStart = positionStart;
            op.itemCount = itemCount;
            op.payload = payload;
        }
        return op;
    }

    @Override
    public void recycleUpdateOp(AdapterHelper.UpdateOp op) {
        op.payload = null;
        mUpdateOpPool.release(op);
    }

}
