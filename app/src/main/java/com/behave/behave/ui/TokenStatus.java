package com.behave.behave.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.behave.behave.R;

import java.util.ArrayList;
import java.util.List;

public class TokenStatus extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private List<String> tokenList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_status);

        Intent intent = getIntent();
        String childName = intent.getStringExtra("childName");
        int tokenCount = 5;
        final TextView tvChildName = (TextView) findViewById(R.id.tvChildNameTokenStatus);
        final TextView tvTokenCount = (TextView) findViewById(R.id.tvTokenCount);
        final Button bTokenMinus = (Button) findViewById(R.id.bTokenStatusMinus);
        final Button bTokenAdd = (Button) findViewById(R.id.bTokenStatusAdd);
        final ListView lvTokenList = (ListView) findViewById(R.id.lvTokenStatusList);
        tvChildName.setText(childName + " Token Status:");

        //test
        tokenList.add("baaaad boy");
        tokenList.add("spankings boy");
        tokenList.add("lashing");
        adapter = new ArrayAdapter<String>(TokenStatus.this, android.R.layout.simple_list_item_1, tokenList);
        lvTokenList.setAdapter(adapter);      // arrayadapter filled with friends' name
        lvTokenList.setOnItemClickListener(TokenStatus.this);
        tvTokenCount.setText("Token Count: " + tokenCount);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
