package com.example.familymap.web;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymap.core.DataCache;
import com.example.familymap.web.Proxy;

import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Request.RegisterRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.RegisterResult;
import Result.Result;
import model.Person;

public class LoginTask implements Runnable{
    private Handler messageHandler;
    private LoginRequest loginRequest = null;
    private RegisterRequest registerRequest = null;
    private String server;
    private static final String STATUS_KEY = "StatusKey";

    public LoginTask(Handler messageHandler, LoginRequest loginRequest, String server) {
        this.messageHandler = messageHandler;
        this.loginRequest = loginRequest;
        this.server = server;
    }

    public LoginTask(Handler messageHandler, RegisterRequest registerRequest, String server) {
        this.messageHandler = messageHandler;
        this.registerRequest = registerRequest;
        this.server = server;
    }

    @Override
    public void run() {
        Proxy proxy = Proxy.getInstance();
        proxy.setServer(server);
        if(registerRequest == null){
            LoginResult result = proxy.login(loginRequest);
            if(result.getMessage() == null){
                PersonResult personResult = proxy.person(new PersonRequest(result.getAuthtoken()));
                DataCache dataCache = DataCache.getInstance();
                dataCache.setPeople(personResult);
                EventResult eventResult = proxy.event(new EventRequest(result.getAuthtoken()));
                dataCache.setEvents(eventResult);
                dataCache.setUser(result.getPersonID());
                Person person = dataCache.getUser();
                sendMessage("Login Successful");
            }
            else {
                sendMessage("Login Failed");
            }
        }
        else {
            RegisterResult result = proxy.register(registerRequest);
            if(result.getMessage() == null){
                PersonResult personResult = proxy.person(new PersonRequest(result.getAuthtoken()));
                DataCache dataCache = DataCache.getInstance();
                dataCache.setPeople(personResult);
                EventResult eventResult = proxy.event(new EventRequest(result.getAuthtoken()));
                dataCache.setEvents(eventResult);
                dataCache.setUser(result.getPersonID());
                Person person = dataCache.getUser();
                sendMessage("Register Successful");
            }
            else {
                sendMessage("Register Failed");
            }
        }
    }
    private void sendMessage(String msg){
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString(STATUS_KEY, msg);
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
