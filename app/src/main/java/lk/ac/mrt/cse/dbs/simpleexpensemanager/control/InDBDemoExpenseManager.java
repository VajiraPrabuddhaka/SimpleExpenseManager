package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDBTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Vajira Prabuddhaka on 11/19/2016.
 */

public class InDBDemoExpenseManager extends ExpenseManager {
    private Context con;
    public InDBDemoExpenseManager(Context context) throws ExpenseManagerException {
        this.con = context;
        setup();

    }
    @Override
    public void setup() throws ExpenseManagerException {
        //this method will create database if it is not exist or otherwise just open the database specified
        SQLiteDatabase mydatabase = con.openOrCreateDatabase("140254T", con.MODE_PRIVATE, null);

        //create two tables
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(account_no text PRIMARY KEY, bank_name text,Holder text,balance REAL);");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(account_no text,Type INTEGER,amount REAL,Log_date DATE);");

        AccountDAO inDBAccountDAO = new InDBAccountDAO(mydatabase);
        setAccountsDAO(inDBAccountDAO);

        TransactionDAO inDBTransactionDAO = new InDBTransactionDAO(mydatabase);
        setTransactionsDAO(inDBTransactionDAO);



        /*** End ***/

    }
}
