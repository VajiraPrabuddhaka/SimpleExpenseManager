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
        //To loop through results, we first acquire a cursor to the result set.
        //Cursor is just an iterator
        Cursor resultSet = db.rawQuery("SELECT Account_no FROM Account",null);
        //We point the cursor to the first record before looping

        //Initialize a list to store the relevant data
        List<String> accounts = new ArrayList<String>();

        //Loop the iterator and add data to the List
        if(resultSet.moveToFirst()) {
            do {
                accounts.add(resultSet.getString(resultSet.getColumnIndex("Account_no")));
            } while (resultSet.moveToNext());
        }
        //Return the list
        return accounts;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor resultSet = db.rawQuery("SELECT * FROM Account",null);
        List<Account> accounts = new ArrayList<Account>();

        if(resultSet.moveToFirst()) {
            do {
                Account account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        resultSet.getString(resultSet.getColumnIndex("Bank")),
                        resultSet.getString(resultSet.getColumnIndex("Holder")),
                        resultSet.getDouble(resultSet.getColumnIndex("Initial_amt")));
                accounts.add(account);
            } while (resultSet.moveToNext());
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = db.rawQuery("SELECT * FROM Account WHERE Account_no = " + accountNo,null);
        Account account = null;

        if(resultSet.moveToFirst()) {
            do {
                account = new Account(resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        resultSet.getString(resultSet.getColumnIndex("Bank")),
                        resultSet.getString(resultSet.getColumnIndex("Holder")),
                        resultSet.getDouble(resultSet.getColumnIndex("Initial_amt")));
            } while (resultSet.moveToNext());
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        //For inserting we use prepared statements
        //First we prepare the sql with the variables to be hold
        String sql = "INSERT INTO Account (Account_no,Bank,Holder,Initial_amt) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);


        //Bind the values correctly. First holder is index 1
        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());

        //Execute it
        statement.executeInsert();


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM Account WHERE Account_no = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE Account SET Initial_amt = Initial_amt + ?";
        SQLiteStatement statement = db.compileStatement(sql);
        if(expenseType == ExpenseType.EXPENSE){
            statement.bindDouble(1,-amount);
        }else{
            statement.bindDouble(1,amount);
        }

        statement.executeUpdateDelete();
    }

}
