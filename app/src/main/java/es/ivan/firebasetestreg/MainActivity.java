package es.ivan.firebasetestreg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    //Elementos de la vista principal incializados en el main
    private Button btnRegister;
    private EditText edtxtEmail;
    private EditText edtxtPassword;
    private TextView txtViewSignIn;

    //ProgessDialog para que nos de el mensaje cuando un usuario se esta registrando
    private ProgressDialog progressDialog;

    //Inicializamos la variable de identificación firebaseAuth de goodle
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        //Localizamos elementos de la vista del registro
        btnRegister = (Button) findViewById(R.id.buttonRegister);
        edtxtEmail = (EditText) findViewById(R.id.edidTextEmail);
        edtxtPassword = (EditText) findViewById(R.id.edidTextPassword);
        txtViewSignIn = (TextView) findViewById(R.id.textViewSignin);

        //Activamos los botones de registrar y el de por si no te has registrado todavia
        btnRegister.setOnClickListener(this);
        txtViewSignIn.setOnClickListener(this);
    }

    //Metodo de registro de usuario
    private void registerUser(){
        //Obtenemos el texto escrito en los elementos mail y pass del registro
        String email = edtxtEmail.getText().toString().trim();
        String password = edtxtPassword.getText().toString().trim();

        //Si el mail esta vacio pedimos a traves de un mensaje Toast la ingresion del email para que no se quede
        //el campo vacio
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stopping the function further
            return;
        }
        //Lo mismo que para el mail, para la password
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping the function further
            return;
        }
        //if validation are ok
        //we will first show a progressbar

        //configuramos el progressdialog para que mientras se realiza el proceso de registro muestre un mensaje
        //avisando de que se esta registrando usuario
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //metodo de creación de usuario
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Si el registro a sido realizado comenzamos la nueva actividad y cargamos la pantalla inicial
                            //user is succesfully registered and logged in
                            //we will start the profile activity here
                            //right now lets display a toast only
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }else{
                            //En caso contrario lanzamos un mensaje de error y finalizamos el mensaje del progressDialog de "Registering user"
                            Toast.makeText(MainActivity.this, "Registration Error", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    //Si el usuario clica en btn de registro usa el metodo registrar usuario
    public void onClick(View view) {

        if(view == btnRegister){
            registerUser();
        }
     //Si el usuario clica en btn  ya registrado? cargamos la pantalla de LoginActivity
        if (view == txtViewSignIn){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
