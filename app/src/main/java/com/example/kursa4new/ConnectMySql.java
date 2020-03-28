package com.example.kursa4new;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

class ConnectMySql {


    public String url = "jdbc:mysql://10.0.2.2:3306/notes";
    public String user = "root";
    public String passwd = "";


    String login = "login";
    String id = "id";
    String password = "password";
    String tablenameUsers = "users";
    String stringInserAddUsers;
    String findOneeUser;
    int countUser;


    MyTask myTask = (MyTask) new MyTask().execute();

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                //  Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, user, passwd);

                Statement st = conn.createStatement();

                String myTableName = "CREATE TABLE IF NOT EXISTS " + tablenameUsers + " (" +
                        id + " INT  PRIMARY KEY AUTO_INCREMENT," +
                        login + " VARCHAR(15) NOT NULL  ," +
                        password + "  VARCHAR(63) NOT NULL );";
                st.executeUpdate(myTableName);
                ResultSet rs = st.executeQuery(findOneeUser);
                countUser = 0;
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




