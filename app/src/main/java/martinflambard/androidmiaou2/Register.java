package martinflambard.androidmiaou2;

/**
 * Created by wilfi on 13/01/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends Fragment {
    EditText username, password;
    Button registerButton;
    Button goToLoginButton;
    String user, pass;
    TextView login;
    // AIzaSyDagkXxYqjPOe30V-X1AHvfdiuBalY6NZM

    public Register() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View registerView = inflater.inflate(R.layout.activity_register, container, false);

        username = (EditText) registerView.findViewById(R.id.username);
        password = (EditText) registerView.findViewById(R.id.password);
        registerButton = (Button) registerView.findViewById(R.id.registerButton);
        goToLoginButton = (Button) registerView.findViewById(R.id.goToLoginButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("only alphabet or number allowed");
                }
                else if(user.length()<5){
                    username.setError("at least 5 characters long");
                }
                else if(pass.length()<5){
                    password.setError("at least 5 characters long");
                }
                else {
                    final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.setMessage("Loading...");
                    pd.show();

                    String url = "https://androidmiaou-b586c.firebaseio.com/users.json";
                    //Toast.makeText(getActivity(), "Before request", Toast.LENGTH_LONG).show();
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){

                        @Override
                        public void onResponse(String s) {
                            //Toast.makeText(getActivity(), "After request" + s, Toast.LENGTH_LONG).show();
                            Firebase reference = new Firebase("https://androidmiaou-b586c.firebaseio.com/users");

                            if(s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(getActivity(), "registration successful", Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.login_register_container, new Login())
                                        .commit();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(getActivity(), "registration successful", Toast.LENGTH_LONG).show();
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.login_register_container, new Login())
                                                .commit();
                                    } else {
                                        Toast.makeText(getActivity(), "username already exists", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }

                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(getActivity());
                    rQueue.add(request);
                }
            }
        });

        goToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_register_container, new Login())
                        .commit();
            }
        });

        return registerView;
    }
}