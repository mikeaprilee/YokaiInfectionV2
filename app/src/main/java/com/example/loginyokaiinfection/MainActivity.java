
package com.example.loginyokaiinfection;

import android.os.Bundle;
import android.media.MediaPlayer;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private EditText etUsername, etRegisterEmail;
    private EditText etRegisterPassword, etRepeatPassword;
    private CheckBox cbAcceptCookies;

    private LinearLayout loginLayout, registerLayout;

    private Button btnLogin, btnGoRegister;
    private Button btnCreateAccount, btnBackLogin;

    private UserDatabaseHelper userDatabase;
    private MediaPlayer bgMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Música ambiental del login, en bucle y a volumen moderado.
        startBackgroundMusic();

        // Base de datos SQLite local. También migra cuentas antiguas si existen.
        userDatabase = new UserDatabaseHelper(this);
        SharedPreferences legacyUsers = getSharedPreferences("YokaiUsers", MODE_PRIVATE);
        userDatabase.migrateLegacyUsers(legacyUsers);

        // Campos de inicio de sesión
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        // Campos de registro
        etUsername = findViewById(R.id.etUsername);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRepeatPassword = findViewById(R.id.etRepeatPassword);
        cbAcceptCookies = findViewById(R.id.cbAcceptCookies);

        // Contenedores
        loginLayout = findViewById(R.id.loginLayout);
        registerLayout = findViewById(R.id.registerLayout);

        // Botones
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnBackLogin = findViewById(R.id.btnBackLogin);

        // Abrir pantalla de registro
        btnGoRegister.setOnClickListener(v -> {
            loginLayout.setVisibility(View.GONE);
            registerLayout.setVisibility(View.VISIBLE);
        });

        // Volver al inicio de sesión
        btnBackLogin.setOnClickListener(v -> {
            registerLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
        });

        // Crear cuenta
        btnCreateAccount.setOnClickListener(v -> registerUser());

        // Iniciar sesión
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void startBackgroundMusic() {
        if (bgMusic == null) {
            bgMusic = MediaPlayer.create(this, R.raw.yokai_login_theme);
            if (bgMusic == null) {
                return;
            }
            bgMusic.setLooping(true);
            bgMusic.setVolume(0.25f, 0.25f);
        }

        if (!bgMusic.isPlaying()) {
            bgMusic.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundMusic();
    }

    @Override
    protected void onPause() {
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (bgMusic != null) {
            bgMusic.release();
            bgMusic = null;
        }
        if (userDatabase != null) {
            userDatabase.close();
        }
        super.onDestroy();
    }

    // MÉTODO DE REGISTRO
    private void registerUser() {

        String username = etUsername.getText().toString().trim();
        String email = etRegisterEmail.getText().toString()
                .trim().toLowerCase();
        String password = etRegisterPassword.getText().toString();
        String repeatPassword = etRepeatPassword.getText().toString();

        // Comprobar campos vacíos
        if (username.isEmpty() || email.isEmpty()
                || password.isEmpty() || repeatPassword.isEmpty()) {

            Toast.makeText(this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cbAcceptCookies.isChecked()) {
            cbAcceptCookies.setError("Debes aceptar las cookies para continuar");
            Toast.makeText(this,
                    "Acepta el uso de cookies para continuar",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar formato del correo
        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()) {

            etRegisterEmail.setError("Correo no válido");
            return;
        }

        // Comprobar longitud de contraseña
        if (password.length() < 6) {

            etRegisterPassword.setError(
                    "Mínimo 6 caracteres");
            return;
        }

        // Comprobar que las contraseñas coinciden
        if (!password.equals(repeatPassword)) {

            etRepeatPassword.setError(
                    "Las contraseñas no coinciden");
            return;
        }

        // Comprobar si el correo ya existe en SQLite
        if (userDatabase.emailExists(email)) {
            etRegisterEmail.setError("Este correo ya está registrado");
            return;
        }

        // Guardar la cuenta en SQLite (la contraseña se almacena como hash con sal).
        if (!userDatabase.registerUser(username, email, password)) {
            Toast.makeText(this,
                    "No se pudo crear la cuenta. Inténtalo de nuevo.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this,
                "¡Cuenta creada correctamente!",
                Toast.LENGTH_LONG).show();

        // Pasar el correo al login
        etEmail.setText(email);
        etPassword.setText("");

        // Limpiar los campos de registro
        etUsername.setText("");
        etRegisterEmail.setText("");
        etRegisterPassword.setText("");
        etRepeatPassword.setText("");
        cbAcceptCookies.setChecked(false);

        // Mostrar el login
        registerLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }

    // MÉTODO DE INICIO DE SESIÓN
    private void loginUser() {

        String email = etEmail.getText().toString()
                .trim().toLowerCase();
        String password = etPassword.getText().toString();

        // Comprobar campos vacíos
        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(this,
                    "Introduce tu correo y contraseña",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprobar las credenciales usando la base de datos SQLite
        if (userDatabase.authenticateUser(email, password)) {

            String username = userDatabase.getUsernameByEmail(email);

            Toast.makeText(this,
                    "¡Bienvenido, " + username + "!",
                    Toast.LENGTH_SHORT).show();

            // Transición breve antes de abrir la selección de yokai.
            Intent intent = new Intent(MainActivity.this, TransitionActivity.class);
            intent.putExtra(TransitionActivity.EXTRA_USERNAME, username);
            startActivity(intent);
            finish();

        } else {

            Toast.makeText(this,
                    "Correo o contraseña incorrectos",
                    Toast.LENGTH_SHORT).show();
        }
    }
}