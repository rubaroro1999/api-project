package com.example.rubafikri.dataPro;

public class Api {

    private static final String ROOT_URL = "HTTP://192.168.50.16/proApp/v1/Api.php?apicall=";

    public static final String URL_CREATE_PROUDCT = ROOT_URL + "createProducts";
    public static final String URL_READ_PROUDCT = ROOT_URL + "getPro";
    public static final String URL_UPDATE_PROUDCT = ROOT_URL + "updatePro";
    public static final String URL_DELETE_PROUDCT = ROOT_URL + "deletePro&pid=";

    public static final String URL_CREATE_USER = ROOT_URL + "createUsers";
    public static final String URL_READ_USER = ROOT_URL + "getUser";
    public static final String URL_UPDATE_USER = ROOT_URL + "updateUser";
    public static final String URL_DELETE_USER = ROOT_URL + "deleteUser&uid=";

}