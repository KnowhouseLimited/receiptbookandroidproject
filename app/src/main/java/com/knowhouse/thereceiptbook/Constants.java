package com.knowhouse.thereceiptbook;

public class Constants {

    private static final String ROOT_URL = "http://192.168.43.110/thereceiptbook/";

    static final String URL_LOGIN = ROOT_URL+"UserLogin.php";
    static final String URL_REGISTER = ROOT_URL+"registerUser.php";
    static final String URL_SPLASH_SCREEN = ROOT_URL+"Main.php";
    public static final String URL_TRANSACTIONFRAGMENT = ROOT_URL+"GetUserTransactions.php";
    static final String URL_RECEIPT_ISSUE = ROOT_URL+"InsertTransaction.php";
    static final String URL_USERPROFILE_UPDATE =ROOT_URL+"getUserUpdatedInfo.php" ;
    public static final String URL_GET_GRAPH_DATA = ROOT_URL+"graphRetreival.php";
    public static final String URL_GET_DATE_FEED = ROOT_URL+"dataFeedRetreival.php";
    public static final String URL_GET_PIE_CHART_DATA = ROOT_URL+"getPieChartData.php";
}
