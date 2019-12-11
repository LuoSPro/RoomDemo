package com.example.roomdemo;

import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PersonRepository {
    private LiveData<List<Person>>allPersonLive;
    private PersonDao mPersonDao;

    public PersonRepository(){
    }

    PersonRepository(Context context){
        PersonDatabase personDatabase = PersonDatabase.getDatabase(context.getApplicationContext());
        mPersonDao = personDatabase.getPersonDao();
        allPersonLive = mPersonDao.getAllPerson();
    }

    LiveData<List<Person>> getAllPersonLive(){
        return allPersonLive;
    }

    //给外界提供接口
    void insertPerson(Person...people){
        new InsertAsyncTask(mPersonDao).execute(people);
    }

    void deletePerson(Person...people){
        new DeleteAsyncTask(mPersonDao).execute(people);
    }

    void updatePerson(Person...people){
        new UpdateAsyncTask(mPersonDao).execute(people);
    }

//    public void getPerson(PersonAdapter adapter,int id){
//        AsyncTask<Integer, Void, Void> task = new QueryAsyncTask(adapter, mPersonDao).execute(id);
//        //execute()中的参数是AsyncTask这个内部类的泛型参数
//        task.execute(id);
//        CompletableFuture future = new CompletableFuture();
//    }


    //插入
    static class InsertAsyncTask extends AsyncTask<Person,Void,Void>{
        private PersonDao mPersonDao;

        private InsertAsyncTask(PersonDao personDao) {
            mPersonDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            mPersonDao.insertPerson(people);
            return null;
        }
    }

    //删除
    static class DeleteAsyncTask extends AsyncTask<Person,Void,Void>{
        private PersonDao mPersonDao;

        private DeleteAsyncTask(PersonDao personDao) {
            mPersonDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            mPersonDao.deletePerson(people);
            return null;
        }
    }

    //修改
    static class UpdateAsyncTask extends AsyncTask<Person,Void,Void>{
        private PersonDao mPersonDao;

        private UpdateAsyncTask(PersonDao personDao) {
            mPersonDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            mPersonDao.updatePerson(people);
            return null;
        }
    }

//    //查询
//    public static class QueryAsyncTask extends AsyncTask<Integer,Void,Void>{
//        private PersonDao mPersonDao;
//        private PersonAdapter mPersonAdapter;
//
//        private QueryAsyncTask(PersonAdapter adapter, PersonDao personDao) {
//            this.mPersonAdapter = adapter;
//            mPersonDao = personDao;
//        }
//
//        @Override
//        public Void doInBackground(Integer... id) {
//            Person person = mPersonDao.queryPerson(id[0]);
//            List<Person> list = new ArrayList<>(1);
//            list.add(person);
//            mPersonAdapter.setAllPerson(list);
//            mPersonAdapter.notifyDataSetChanged();
//            return null;
//        }
//    }

}
