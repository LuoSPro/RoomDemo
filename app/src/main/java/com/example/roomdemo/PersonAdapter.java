package com.example.roomdemo;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {
    private List<Person> allPerson = new ArrayList<>();
    private PersonViewModel mPersonViewModel;

    public PersonAdapter(PersonViewModel personViewModel){
        this.mPersonViewModel = personViewModel;
    }

    public void setAllPerson(List<Person> allPerson){
        this.allPerson = allPerson;
    }

    public List<Person> getAllPerson(){
        return allPerson;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.person_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Person person = allPerson.get(position);
        holder.tvId.setText(String.valueOf(person.getId()));
        holder.tvName.setText(person.getName());
        holder.tvSex.setText(person.getSex());
        holder.tvAge.setText(String.valueOf(person.getAge()));
    }

    @Override
    public int getItemCount() {
        return allPerson.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvId;
        TextView tvName;
        TextView tvSex;
        TextView tvAge;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.personId_list);
            tvName = itemView.findViewById(R.id.personName_list);
            tvSex = itemView.findViewById(R.id.personSex_list);
            tvAge = itemView.findViewById(R.id.personAge_list);
        }
    }
}
