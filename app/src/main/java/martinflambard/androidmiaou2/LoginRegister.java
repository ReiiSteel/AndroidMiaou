package martinflambard.androidmiaou2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginRegister extends AppCompatActivity {

    private boolean inLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
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
        final TextView login_register_switch = (TextView)findViewById(R.id.login_register_switch);
        login_register_switch.setText(R.string.register);
        login_register_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeLoginOrRegister(inLogin, login_register_switch);
            }
        });
    }


    private void setInLogin(boolean value){
        this.inLogin = value;
    }

    private void chargeLoginOrRegister(boolean inLogin, TextView login_register_switch){
        if (inLogin){
            login_register_switch.setText(R.string.login);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_register_container, new Register())
                    .commit();
            setInLogin(false);
        }
        else{
            login_register_switch.setText(R.string.register);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_register_container, new Login())
                    .commit();
            setInLogin(true);
        }
    }

}