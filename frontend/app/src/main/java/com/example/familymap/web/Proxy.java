package com.example.familymap.web;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Request.RegisterRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.RegisterResult;

public class Proxy {

    private static Proxy instance = new Proxy();

    public static Proxy getInstance(){
        return instance;
    }
    private Proxy(){

    }
    public void setServer(String server){
        Proxy.server = server;
    }
    private static String server;

    public LoginResult login(LoginRequest r){
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + server + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            OutputStream reqBody = http.getOutputStream();
            String reqData = gson.toJson(r);
            write(reqBody, reqData);
            reqBody.close();
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = read(respBody);
                return gson.fromJson(respData, LoginResult.class);
            }
            else {
                return new LoginResult(false, http.getResponseMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new LoginResult(false, "Failed to login");
        }
    }

    public RegisterResult register(RegisterRequest r) {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + server + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            OutputStream reqBody = http.getOutputStream();
            String reqData = gson.toJson(r);
            write(reqBody, reqData);
            reqBody.close();
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = read(respBody);
                return gson.fromJson(respData, RegisterResult.class);
            }
            else {
                return new RegisterResult(false, http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new RegisterResult(false, "Failed to register");
        }
    }

    public PersonResult person(PersonRequest r){
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + server + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", r.getAuthToken());
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = read(respBody);
                return gson.fromJson(respData, PersonResult.class);
            }
            else {
                return new PersonResult(false, http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new PersonResult(false, "Failed to retrieve persons");
        }
    }

    public EventResult event(EventRequest r) {
        Gson gson = new Gson();
        try {
            URL url = new URL("http://" + server + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", r.getAuthToken());
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = read(respBody);
                return gson.fromJson(respData, EventResult.class);
            }
            else {
                return new EventResult(false, http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new EventResult(false, "Failed to retrieve events");
        }
    }

    private static String read(InputStream is){
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private static void write(OutputStream os, String input) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(os);
        outputStreamWriter.write(input);
        outputStreamWriter.flush();
    }
}
