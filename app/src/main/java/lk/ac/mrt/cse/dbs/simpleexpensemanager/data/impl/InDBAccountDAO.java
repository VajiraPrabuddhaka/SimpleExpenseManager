package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vajira Prabuddhaka on 11/18/2016.
 */

public class InDBAccountDAO extends SQLiteOpenHelper implements AccountDAO {
    public static final String DATABASE_NAME = "ExpMgr.db";
    public static final String TABLE_NAME = "Account";
    public static final String COL_1 = "account_no";
    public static final String COL_2 = "bank_name";
    public static final String COL_3 = "accountHolderName";
    public static final String COL_4 = "balance";


    public InDBAccountDAO(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase mydatabase = openOrCreateDatabase("your database name",MODE_PRIVATE,null);
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Account", new String[]{"account_no"},null, null, null, null, null);
        String fieldToAdd=null;
        while(cursor.moveToNext()){
            fieldToAdd=cursor.getString(0);
            data.add(fieldToAdd);
        }
        cursor.close();  // dont forget to close the cursor after operation done
        return data;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> data=new ArrayList<Account>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Account",new String[]{"account_no","bank_name","accountHolderName","balance"},null,null,null,null,null);
        while(cursor.moveToNext()){
            Account acc = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),Double.valueOf(cursor.getString(3)));
            data.add(acc);
        }
        return data;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        //need to add validation on account existence
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Account WHERE account_no="+accountNo, null);
        Account account = new Account(cursor.getString(0),cursor.getColumnName(1),cursor.getColumnName(2),Double.valueOf(cursor.getString(3)));
        return account;
    }

    @Override
    public boolean addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("account_no",account.getAccountNo());
        content.put("bank_name", account.getBankName());
        content.put("accountHolderName",account.getAccountHolderName());
        content.put("balance", account.getBalance());
        long result = db.insert("Account",null, content);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+"Account"+" where account_no=" + accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET Initial_amt = Initial_amt + ?";
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(sql);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a table for store account data
        //create table contacts " +"(id integer primary key, name text,phone text,email text, street text,place text)
        db.execSQL("create table Account " + "(account_no text primary key, bank_name text, accountHolderName text, balance real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Account");
        onCreate(db);
    }
}
