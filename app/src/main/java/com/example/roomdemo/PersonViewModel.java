package com.example.roomdemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {
    private PersonRepository mPersonRepository;
    public PersonViewModel(@NonNull Application application) {
        super(application);
        mPersonRepository = new PersonRepository(application);
    }

    LiveData<List<Person>> getAllPersonLive(){
        return mPersonRepository.getAllPersonLive();
    }

    void insertPerson(Person...people){
        mPersonRepository.insertPerson(people);
    }

    void deletePerson(Person...people){
        mPersonRepository.deletePerson(people);
    }

    void updatePerson(Person...people){
        mPersonRepository.updatePerson(people);
    }
}
