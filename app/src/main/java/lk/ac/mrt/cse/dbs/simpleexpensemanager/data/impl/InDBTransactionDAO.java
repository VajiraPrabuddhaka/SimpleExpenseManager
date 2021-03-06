package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

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

public class InDBTransactionDAO implements TransactionDAO {
    //public static final String DATABASE_NAME = "ExpMgr.db";

    private SQLiteDatabase db;

    public InDBTransactionDAO(SQLiteDatabase database) {
        this.db = database;
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql = "INSERT INTO TransactionLog (account_no,Type,amount,Log_date) VALUES (?,?,?,?)";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindString(1,accountNo);
        statement.bindLong(2,(expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3,amount);
        statement.bindLong(4,date.getTime());

        statement.executeInsert();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor resultSet = db.rawQuery("SELECT * FROM TransactionLog",null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(resultSet.moveToFirst()) {
            do{
                Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
                        resultSet.getString(resultSet.getColumnIndex("account_no")),
                        (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultSet.getDouble(resultSet.getColumnIndex("amount")));
                transactions.add(t);
            }while (resultSet.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor resultSet = db.rawQuery("SELECT * FROM TransactionLog LIMIT " + limit,null);
        List<Transaction> transactions = new ArrayList<Transaction>();

        if(resultSet.moveToFirst()) {
            do {
                Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
                        resultSet.getString(resultSet.getColumnIndex("account_no")),
                        (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                        resultSet.getDouble(resultSet.getColumnIndex("amount")));
                transactions.add(t);
            } while (resultSet.moveToNext());
        }

        return transactions;
    }
}
