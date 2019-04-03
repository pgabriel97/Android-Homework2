package com.example.tema2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private Button mAddButton;
    private Button mRemoveButton;

    private EditText mFirstNameText;
    private EditText mLastNameText;

    private List<User> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new RecyclerViewAdapter(this.getContext(), userList);
        mRecyclerView.setAdapter(mAdapter);

        mAddButton = view.findViewById(R.id.add_user_button);
        mRemoveButton = view.findViewById(R.id.remove_user_button);
        mFirstNameText = view.findViewById(R.id.first_name_edit);
        mLastNameText = view.findViewById(R.id.last_name_edit);

        final UserRepository userRepository = new UserRepository(getContext());
        userRepository.getAll(new OnGetAllListener() {
            @Override
            public void actionSuccess(List<User> users) {
                userList.clear();
                userList.addAll(users);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void actionFailed() {
                Toast.makeText(getContext(), "Can't load users!", Toast.LENGTH_SHORT);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = mFirstNameText.getText().toString();
                String lastName = mLastNameText.getText().toString();
                final User userToAdd = new User(firstName, lastName);
                if (!userToAdd.getFirstName().equals("") && !userToAdd.getLastName().equals("")) {
                    userRepository.insert(userToAdd, new OnInsertTaskListener() {
                        @Override
                        public void actionSuccess() {
                            Toast.makeText(getContext(), "The user was added!", Toast.LENGTH_SHORT).show();
                            userList.add(userToAdd);
                            mAdapter.notifyDataSetChanged();

                            mRecyclerView.scrollToPosition(userList.size() - 1);
                            mFirstNameText.setText("");
                            mLastNameText.setText("");
                        }

                        @Override
                        public void actionFailed() {
                            Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "You must complete both names!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = mFirstNameText.getText().toString();
                String lastName = mLastNameText.getText().toString();

                Integer id = -1;
                int removePosition = -1;

                for (User u : userList) {
                    if (u.getFirstName().equals(firstName)
                            && u.getLastName().equals(lastName)) {
                        id = u.getUserID();
                        removePosition = userList.indexOf(u);
                    }

                    if (id != -1) {
                        final int finalRemovePosition = removePosition;
                        userRepository.delete(id, new OnDeleteTaskListener() {

                            @Override
                            public void actionSuccess() {
                                Toast.makeText(getContext(), "The user was removed!", Toast.LENGTH_SHORT).show();
                                userList.remove(finalRemovePosition);
                                mAdapter.notifyDataSetChanged();

                                mFirstNameText.setText("");
                                mLastNameText.setText("");
                            }

                            @Override
                            public void actionFailed() {
                                Toast.makeText(getContext(), "Could not remove this user!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(), "Could not find this user!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
}

