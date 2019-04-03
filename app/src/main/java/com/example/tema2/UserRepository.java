package com.example.tema2;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private AppDatabase appDatabase;

    public UserRepository(Context context) {
        appDatabase = ApplicationController.getAppDatabase();
    }

    private class InsertTask extends AsyncTask<User, Void, Void> {

        OnInsertTaskListener listener;

        InsertTask(OnInsertTaskListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(User... users) {
            appDatabase.userDao().insert(users[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.actionSuccess();
        }
    }

    private class DeleteTask extends AsyncTask<Integer, Void, Void> {

        OnDeleteTaskListener listener;

        DeleteTask(OnDeleteTaskListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            appDatabase.userDao().delete(ids[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.actionSuccess();
        }
    }

    private class GetAllTask extends AsyncTask<Void, Void, List<User>> {

        OnGetAllListener listener;

        GetAllTask(OnGetAllListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            List<User> all = new ArrayList<>(appDatabase.userDao().getAll());
            return all;
        }

        @Override
        protected void onPostExecute(List<User> result) {
            super.onPostExecute(result);
            listener.actionSuccess(result);
        }

    }

    public void insert(final User user,
                       final OnInsertTaskListener listener) {
        new InsertTask(listener).execute(user);
    }

    public void delete(final Integer id,
                       final OnDeleteTaskListener listener) {
        new DeleteTask(listener).execute(id);
    }

    public void getAll(final OnGetAllListener listener) {
        new GetAllTask(listener).execute();
    }


}