package martinflambard.androidmiaou2;

import android.app.Fragment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginRegister extends AppCompatActivity {

    private Toolbar toolbar;
    private Button login_register_switch;
    private boolean inLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.mipmap.polygon);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_register_container, new Login())
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setInLogin(true);
        login_register_switch = (Button)findViewById(R.id.login_register_switch);
        login_register_switch.setText(R.string.goToRegister);
        login_register_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeLoginOrRegister();
            }
        });
    }


    private void setInLogin(boolean value){this.inLogin = value;}
    private boolean getInLogin(){return this.inLogin;}

    private Button getLoginRegisterSwitch(){return this.login_register_switch;}


    private void chargeLoginOrRegister(){
        if (getInLogin()){
            getLoginRegisterSwitch().setText(R.string.goToLogin);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_register_container, new Register())
                    .commit();
            setInLogin(false);
        }
        else{
            getLoginRegisterSwitch().setText(R.string.goToRegister);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_register_container, new Login())
                    .commit();
            setInLogin(true);
        }
    }

    //Firebase.setAndroidContext(this);
}