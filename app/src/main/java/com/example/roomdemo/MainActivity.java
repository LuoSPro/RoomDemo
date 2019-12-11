package com.example.roomdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAdd,btnDelete,btnUpdate,btnQuery;
    private PersonViewModel mPersonViewModel;
    private RecyclerView mRecyclerView;
    private PersonAdapter mPersonAdapter;
    private EditText etId;
    private EditText etName;
    private EditText etSex;
    private EditText etAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);

        mPersonViewModel.getAllPersonLive().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> people) {
                int temp = mPersonAdapter.getItemCount();
                mPersonAdapter.setAllPerson(people);
                if (temp != people.size()){
                    mPersonAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initData(){
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnQuery = findViewById(R.id.btnQuery);
        mRecyclerView = findViewById(R.id.recyclerView);
        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etSex = findViewById(R.id.etSex);
        etAge = findViewById(R.id.etAge);
        mPersonViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);
        mPersonAdapter = new PersonAdapter(mPersonViewModel);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mPersonAdapter);
    }

    @Override
    public void onClick(View view) {
        Person person = new Person();
        List<Person> allList = mPersonAdapter.getAllPerson();
        if ("".equals(etId.getText().toString())){
            person.setId(-1);
        }else{
            person.setId(Integer.parseInt(etId.getText().toString()));
        }
        if ("".equals(etName.getText().toString())){
            person.setName("-1");
        }else{
            person.setName(etName.getText().toString());
        }
        if ("".equals(etSex.getText().toString())){
            person.setSex("-1");
        }else{
            person.setSex(etSex.getText().toString());
        }
        if ("".equals(etAge.getText().toString())){
            person.setAge(-1);
        }else{
            person.setAge(Integer.parseInt(etAge.getText().toString()));
        }
        switch (view.getId()){
            case R.id.btnAdd:
                if (person.getId()==-1||"-1".equals(person.getName())
                        ||"-1".equals(person.getSex())||person.getAge()==-1){
                    Toast.makeText(this,"请将信息输入完整",Toast.LENGTH_SHORT).show();
                    break;
                }
                for (int i = 0; i < allList.size(); i++) {
                    if (allList.get(i).getId() == person.getId()){
                        Toast.makeText(this,"此ID已被使用，请换一个ID",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                mPersonViewModel.insertPerson(person);
                break;
            case R.id.btnDelete:
                if (person.getId() == -1){
                    Toast.makeText(this,"请输入ID",Toast.LENGTH_SHORT).show();
                    break;
                }
                mPersonViewModel.deletePerson(person);
                break;
            case R.id.btnUpdate:
                if (person.getId()==-1||"-1".equals(person.getName())
                        ||"-1".equals(person.getSex())||person.getAge()==-1){
                    Toast.makeText(this,"请将信息输入完整",Toast.LENGTH_SHORT).show();
                    break;
                }
                mPersonViewModel.updatePerson(person);
                mPersonAdapter.notifyDataSetChanged();
                break;
            case R.id.btnQuery:
                if (person.getId() == -1){
                    Toast.makeText(this,"请输入ID",Toast.LENGTH_SHORT).show();
                    break;
                }
                List<Person> tempList = new ArrayList<>();
                for (int i = 0; i < allList.size(); i++) {
                    if (person.getId() == allList.get(i).getId()){
                        tempList.add(allList.get(i));
                    }
                }
                mPersonAdapter.setAllPerson(tempList);
                mPersonAdapter.notifyDataSetChanged();
                break;
        }
    }
}
