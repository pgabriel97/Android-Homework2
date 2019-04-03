package com.example.tema2;

import java.util.List;

public interface OnGetAllListener {
    void actionSuccess(List<User> users);
    void actionFailed();
}
