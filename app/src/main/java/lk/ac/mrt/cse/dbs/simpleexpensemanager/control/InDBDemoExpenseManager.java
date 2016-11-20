package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

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
        AccountDAO inDBAccountDAO = new InDBAccountDAO(con);
        setAccountsDAO(inDBAccountDAO);

        TransactionDAO inDBTransactionDAO = new InDBTransactionDAO(con);
        setTransactionsDAO(inDBTransactionDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

        /*** End ***/

    }
}
