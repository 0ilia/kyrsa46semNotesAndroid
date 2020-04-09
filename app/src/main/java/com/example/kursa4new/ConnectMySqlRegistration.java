package com.example.kursa4new;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class ConnectMySqlRegistration {


    StringDataMysql stringDataMysql = new StringDataMysql();


    String findOneeUser;
    String stringInserAddUsers;
    int countUser = 0;


    MyTask myTask = (MyTask) new MyTask().execute();

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                //  Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(stringDataMysql.url, stringDataMysql.user, stringDataMysql.passwd);

                Statement st = conn.createStatement();

                String myTableName = "CREATE TABLE IF NOT EXISTS " + stringDataMysql.tablenameUsers + "(" +
                        stringDataMysql.id + " INT  PRIMARY KEY AUTO_INCREMENT," +
                        stringDataMysql.login + " VARCHAR(15) NOT NULL  ," +
                        stringDataMysql.password + "  VARCHAR(60) NOT NULL );";
                st.executeUpdate(myTableName);


                //проверка на логин
                ResultSet rs = st.executeQuery(findOneeUser);
                while (rs.next()) {
                    countUser = 1;
                    break;
                }
                if (countUser == 0) {
                    st.executeUpdate(stringInserAddUsers);//регистрируем
                }


//                conn.close();
            } catch (Exception e) {
                Log.e("AAAAA", e.getMessage());
            }
            return null;
        }

    }



}




