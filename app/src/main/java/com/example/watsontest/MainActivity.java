package com.example.watsontest;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageContextMetadata;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageInput;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.CreateSessionOptions;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText textView2;
    ListView listView;
    //TextViewAdapter adapter;
    ImageTextAdapter adapter;

    Context watsonContext;
    private Assistant watsonAssistant;

    private IamOptions iamOptions;
    private MessageResponse response=null;
    private MessageOptions.Builder builder;

    private static final String endPoint = "https://gateway-wdc.watsonplatform.net/assistant/api";
    private static final String APIkey = "Qw4Cqhs0eQnOOOSgCTYNtVdl8CI2i_omti8sXCPjGm6S";
    private static final String WORKPLACE = "9aa06906-8f89-4ce7-ac1e-8a8e26f14609";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button3);
        listView = findViewById(R.id.list0);
        //adapter = new TextViewAdapter(getBaseContext());
        adapter = new ImageTextAdapter(getBaseContext());
        listView.setAdapter(adapter);
        listView.setDivider(null);  //要素分割の水平線を無し

        createService();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageResponse response;
                String message = textView2.getText().toString();
                //Log.d("message", message);

                //TextView newTextView = new TextView(getBaseContext());
                //newTextView.setText(message);
                //newTextView.setGravity(Gravity.RIGHT);
                //newTextView.setTextColor(Color.BLACK);
                ImageText imageText = new ImageText();
                imageText.text = message;
                adapter.add(imageText);
                adapter.notifyDataSetChanged();
                textView2.setText("");
                response = sendMessage(message);
                setEditText(response);
                listView.setSelection(adapter.getCount()-1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        });

        textView2 = findViewById(R.id.editText2);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createService(){
        try{
            iamOptions = new IamOptions.Builder().apiKey(APIkey).build();
            watsonAssistant = new Assistant("2019-02-28", iamOptions);
            watsonAssistant.setEndPoint(endPoint);
            builder = new MessageOptions.Builder(WORKPLACE);
            // Invoke a Watson Assistant method
        } catch (NotFoundException e) {
            // Handle Not Found (404) exception
        } catch (RequestTooLargeException e) {
            // Handle Request Too Large (413) exception
        } catch (ServiceResponseException e) {
            // Base class for all exceptions caused by error responses from the service
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        }
    }

    private MessageResponse sendMessage(final String message){

        try{
        InputData input = new InputData.Builder(message).
                build();

        builder.input(input);
        if(watsonContext != null) {
            Log.d("message", "context is not null");
            builder.context(watsonContext);
        }

        MessageOptions options = builder.build();

        response = watsonAssistant.message(options).execute();
        watsonContext = response.getContext();

            // Invoke a Watson Assistant method
        } catch (NotFoundException e) {

            // Handle Not Found (404) exception
        } catch (RequestTooLargeException e) {

            // Handle Request Too Large (413) exception
        } catch (ServiceResponseException e) {

            // Base class for all exceptions caused by error responses from the service
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        }
        System.out.println(response);
        return response;
    }

    private void setEditText(MessageResponse response){
        List text = null;
        ImageTextAdapter adapter=null;

        Map output = response.getOutput();
        if (output != null) {
            text = (List) output.get("text");
        }

        adapter = (ImageTextAdapter)listView.getAdapter();
        String message = "";
        for(Iterator<String> i = text.iterator(); i.hasNext(); ) {
            message += i.next() +"\n";
        }
        ImageText imageText = new ImageText();
        imageText.text = message;
        imageText.left = true;
        //newTextView.setTextColor(Color.DKGRAY);
        adapter.add(imageText);
        adapter.notifyDataSetChanged();
    }
}
