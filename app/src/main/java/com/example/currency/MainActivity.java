package com.example.currency;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lvItems;
    private ArrayAdapter<String> listAdapter;
    private EditText etCurrencyFilter;
    private ArrayList<String> originalCurrencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lvItems = findViewById(R.id.lv_items);
        this.etCurrencyFilter = findViewById(R.id.et_currency_filter);

        this.listAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        this.lvItems.setAdapter(this.listAdapter);

        originalCurrencies = new ArrayList<>();

        etCurrencyFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterCurrencies(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        getDataByAsyncTask();
    }

    public void onBtnGetDataClick(View view) {
        Toast.makeText(this, R.string.loading_data, Toast.LENGTH_SHORT).show();
        getDataByAsyncTask();
    }


    private void getDataByAsyncTask() {
        String apiUrl = Constants.FLOATRATES_API_URL;

        DataLoader.getValuesFromApi(apiUrl, new DataLoader.ApiDataListener() {
            @Override
            public void onDataDownloaded(String data) {
                updateCurrenciesList(data);
            }
        });
    }

    private void updateCurrenciesList(String data) {
        listAdapter.clear();
        originalCurrencies.clear();

        String[] currencies = data.split("\n");

        for (String currency : currencies) {
            listAdapter.add(currency);
            originalCurrencies.add(currency);
        }

        listAdapter.notifyDataSetChanged();
    }

    private void filterCurrencies(String query) {
        listAdapter.clear();

        for (String currency : originalCurrencies) {
            if (currency.toLowerCase().contains(query.toLowerCase())) {
                listAdapter.add(currency);
            }
        }

        listAdapter.notifyDataSetChanged();
    }
}