package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Vajira Prabuddhaka on 11/19/2016.
 */

public class InDBTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    public static final String DATABASE_NAME = "ExpMgr.db";


    public InDBTransactionDAO(Context context) {
        super(context,DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table TransactionLog " + "(date text, account_no text, type text, amount real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TransactionLog");
        onCreate(db);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        ContentValues content = new ContentValues();
        content.put("date",date.toString());
        content.put("accountNo", accountNo);
        content.put("type",expenseType.toString());
        content.put("amount", amount);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("TransactionLog",null,content);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resultSet = db.rawQuery("select * from TransactionLog",null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(resultSet.moveToFirst()) {
            do{
                Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
                        resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultSet.getDouble(resultSet.getColumnIndex("Amt")));
                transactions.add(t);
            }while (resultSet.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resultSet = db.rawQuery("select * from TransactionLog limit " + limit,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(resultSet.moveToFirst()) {
            do {
                Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
                        resultSet.getString(resultSet.getColumnIndex("Account_no")),
                        (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultSet.getDouble(resultSet.getColumnIndex("Amt")));
                transactions.add(t);
            } while (resultSet.moveToNext());
        }

        return transactions;
    }
}
