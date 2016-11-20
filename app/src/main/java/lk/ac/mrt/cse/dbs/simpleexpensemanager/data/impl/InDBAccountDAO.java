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

public class InDBAccountDAO implements AccountDAO {
    public static final String DATABASE_NAME = "ExpMgr.db";
    public static final String TABLE_NAME = "Account";
    public static final String COL_1 = "account_no";
    public static final String COL_2 = "bank_name";
    public static final String COL_3 = "accountHolderName";
    public static final String COL_4 = "balance";

    private SQLiteDatabase db;

    public InDBAccountDAO(SQLiteDatabase db) {
        this.db = db;
        //SQLiteDatabase mydatabase = openOrCreateDatabase("your database name",MODE_PRIVATE,null);
    }

    @Override
    public List<String> getAccountNumbersList() {

        ArrayList<String> data=new ArrayList<String>();
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
        Cursor resultSet = db.rawQuery("SELECT * FROM Account",null);
        List<Account> accounts = new ArrayList<Account>();

        if(resultSet.moveToFirst()) {
            do {
                Account account = new Account(resultSet.getString(resultSet.getColumnIndex("account_no")),
                        resultSet.getString(resultSet.getColumnIndex("bank_name")),
                        resultSet.getString(resultSet.getColumnIndex("Holder")),
                        resultSet.getDouble(resultSet.getColumnIndex("balance")));
                accounts.add(account);
            } while (resultSet.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = db.rawQuery("SELECT * FROM Account WHERE account_no = " + accountNo,null);
        Account account = null;

        if(resultSet.moveToFirst()) {
            do {
                account = new Account(resultSet.getString(resultSet.getColumnIndex("account_no")),
                        resultSet.getString(resultSet.getColumnIndex("bank_name")),
                        resultSet.getString(resultSet.getColumnIndex("Holder")),
                        resultSet.getDouble(resultSet.getColumnIndex("balance")));
            } while (resultSet.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {

        ContentValues content = new ContentValues();
        content.put("account_no",account.getAccountNo());
        content.put("bank_name", account.getBankName());
        content.put("Holder",account.getAccountHolderName());
        content.put("balance", account.getBalance());
        db.insert("Account",null, content);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        db.execSQL("delete from "+"Account"+" where account_no=" + accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET balance = balance + ?";
        SQLiteStatement statement = db.compileStatement(sql);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();
    }

}
