package com.example.loginyokaiinfection;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Base64;

import com.example.loginyokaiinfection.Characters.Character;
import com.example.loginyokaiinfection.Characters.Datos;

import java.util.ArrayList;
import java.util.List;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Base de datos SQLite local de Yokai Infection: usuarios y personajes.
 * Las contraseñas se guardan como hash con sal, nunca como texto plano.
 */
public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "yokai_users.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_CHARACTERS = "personajes";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD_HASH = "password_hash";
    private static final String COL_PASSWORD_SALT = "password_salt";

    private static final int SALT_BYTES = 16;
    private static final int HASH_BITS = 256;
    private static final int HASH_ITERATIONS = 100_000;

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUsersTable(db);
        createCharactersTable(db);
        insertInitialCharacters(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // La versión 1 solo tenía usuarios. Se añade la tabla de personajes
        // sin borrar la tabla users ni las cuentas ya registradas.
        if (oldVersion < 2) {
            createCharactersTable(db);
            insertInitialCharacters(db);
        }
    }

    private void createUsersTable(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT NOT NULL, "
                + COL_EMAIL + " TEXT NOT NULL COLLATE NOCASE UNIQUE, "
                + COL_PASSWORD_HASH + " TEXT NOT NULL, "
                + COL_PASSWORD_SALT + " TEXT NOT NULL"
                + ")";
        db.execSQL(createUsersTable);
    }

    private void createCharactersTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CHARACTERS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL UNIQUE, "
                + "tipo TEXT NOT NULL, "
                + "descripcion TEXT NOT NULL, "
                + "imagen TEXT NOT NULL"
                + ")");
    }

    /** Inserta el catálogo de Datos una única vez por personaje. */
    private void insertInitialCharacters(SQLiteDatabase db) {
        Datos datos = new Datos();
        for (Character character : datos.getDatos()) {
            ContentValues values = new ContentValues();
            values.put("nombre", character.getNombre());
            values.put("tipo", character.getTipo());
            values.put("descripcion", character.getDescripcion());
            values.put("imagen", character.getImagen());
            db.insertWithOnConflict(
                    TABLE_CHARACTERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    /** Devuelve los personajes desde SQLite para la pantalla de selección. */
    public List<Character> getCharacters() {
        List<Character> characters = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                "personajes",
                new String[]{"id", "nombre", "tipo", "descripcion", "imagen"},
                null, null, null, null, "id ASC")) {
            while (cursor.moveToNext()) {
                characters.add(new Character(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            }
        }
        return characters;
    }

    public boolean emailExists(String email) {
        String normalizedEmail = normalizeEmail(email);
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_ID},
                COL_EMAIL + " = ?",
                new String[]{normalizedEmail},
                null, null, null, "1")) {
            return cursor.moveToFirst();
        }
    }

    public boolean registerUser(String username, String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        String salt = generateSalt();
        String passwordHash;

        try {
            passwordHash = hashPassword(password, salt);
        } catch (GeneralSecurityException exception) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username.trim());
        values.put(COL_EMAIL, normalizedEmail);
        values.put(COL_PASSWORD_HASH, passwordHash);
        values.put(COL_PASSWORD_SALT, salt);

        SQLiteDatabase db = getWritableDatabase();
        long rowId = db.insertWithOnConflict(
                TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return rowId != -1;
    }

    public boolean authenticateUser(String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_PASSWORD_HASH, COL_PASSWORD_SALT},
                COL_EMAIL + " = ?",
                new String[]{normalizedEmail},
                null, null, null, "1")) {

            if (!cursor.moveToFirst()) {
                return false;
            }

            String savedHash = cursor.getString(0);
            String salt = cursor.getString(1);

            try {
                String enteredHash = hashPassword(password, salt);
                byte[] savedBytes = Base64.decode(savedHash, Base64.NO_WRAP);
                byte[] enteredBytes = Base64.decode(enteredHash, Base64.NO_WRAP);
                return MessageDigest.isEqual(savedBytes, enteredBytes);
            } catch (GeneralSecurityException | IllegalArgumentException exception) {
                return false;
            }
        }
    }

    public String getUsernameByEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USERNAME},
                COL_EMAIL + " = ?",
                new String[]{normalizeEmail(email)},
                null, null, null, "1")) {
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        }
        return "Jugador";
    }

    /**
     * Migra usuarios de la versión anterior, que usaba SharedPreferences.
     * Si la migración termina correctamente, elimina las contraseñas antiguas
     * guardadas en texto plano de ese archivo de preferencias.
     */
    public void migrateLegacyUsers(SharedPreferences legacyPreferences) {
        Map<String, ?> entries = legacyPreferences.getAll();
        boolean foundLegacyUser = false;
        boolean migrationSucceeded = true;

        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (!key.startsWith("password_") || !(value instanceof String)) {
                continue;
            }

            String email = normalizeEmail(key.substring("password_".length()));
            String password = (String) value;
            if (email.isEmpty() || !email.contains("@") || password.isEmpty()) {
                continue;
            }

            foundLegacyUser = true;
            if (!emailExists(email)) {
                String username = legacyPreferences.getString("username_" + email, "Jugador");
                if (!registerUser(username == null ? "Jugador" : username, email, password)) {
                    // Puede que otro ciclo ya haya insertado el correo.
                    if (!emailExists(email)) {
                        migrationSucceeded = false;
                    }
                }
            }
        }

        if (foundLegacyUser && migrationSucceeded) {
            legacyPreferences.edit().clear().apply();
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private String generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return Base64.encodeToString(salt, Base64.NO_WRAP);
    }

    private String hashPassword(String password, String encodedSalt)
            throws GeneralSecurityException {
        byte[] salt = Base64.decode(encodedSalt, Base64.NO_WRAP);
        PBEKeySpec specification = new PBEKeySpec(
                password.toCharArray(), salt, HASH_ITERATIONS, HASH_BITS);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(specification).getEncoded();
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } finally {
            specification.clearPassword();
        }
    }
}
