package com.example.expensemanager.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.expensemanager.models.Notes;
import com.example.expensemanager.models.Summary;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.myHandler.transDBHandler;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainViewModel extends AndroidViewModel {

    transDBHandler db= new transDBHandler(MainViewModel.this.getApplication());
    Calendar calendar;

    public MutableLiveData<ArrayList<Transaction>> transactionMutableLiveData= new MutableLiveData<>();
    public MutableLiveData<Double> totalIncome= new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense= new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount= new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
    }
    public void getTransactions(Calendar calendar){
        this.calendar= calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        double income=0;
        double expense=0;
        double total=0;

        if(Constants.SELECTED_TAB==Constants.DAILY){
            //ArrayList<Transaction> newTransactions= db.getAllTransactions();
            //transactionMutableLiveData.setValue(newTransactions);

            long start = calendar.getTimeInMillis();
            Date date = new Date(calendar.getTime().getTime() + (24 * 60 * 60 * 1000));
            long end = date.getTime();

            ArrayList<Transaction> transactions = db.getTransactionsByDate(start, end);
            transactionMutableLiveData.setValue(transactions);

            income= db.getSumOfIncome(transactions);
            expense= db.getSumOfExpense(transactions);
            total= db.getSumOfTotal(transactions);

        }

        else if (Constants.SELECTED_TAB==Constants.MONTHLY) {
            calendar.set(Calendar.DAY_OF_MONTH,0);

            long start= calendar.getTimeInMillis();
            calendar.add(Calendar.MONTH, 1);
            long end= calendar.getTimeInMillis();

            ArrayList<Transaction> transactions = db.getTransactionsByDate(start, end);
            transactionMutableLiveData.setValue(transactions);

            income= db.getSumOfIncome(transactions);
            expense= db.getSumOfExpense(transactions);
            total= db.getSumOfTotal(transactions);
        }
        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);

    }

    public void addTransactions(Transaction transaction){
        db.addTransaction(transaction);
        //db.addTransaction(new Transaction(Constants.INCOME,"Business","Cash","Some note here",new Date(),500));
    }

    public void deleteTransaction(Transaction transaction){
        db.deleteTransaction(transaction);
        getTransactions(calendar);
    }

    public ArrayList<Integer> TotalExpense() {
        ArrayList<Integer> expenseSumList = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);


        int i = 0;
        do {
            long start = calendar.getTimeInMillis();
            calendar.add(Calendar.MONTH, 1); // Move to the next month
            long end = calendar.getTimeInMillis();

            int expenseSum =  (int) db.getExpenseSumForMonth(start, end);
            expenseSum= expenseSum*(-1);
            expenseSumList.add(expenseSum);

            i++;
            calendar.add(Calendar.MONTH, -2);
        } while (i < 6);

        return expenseSumList;
    }

    public HashMap<String, Double> getCategoryPercentage(Calendar calendar){
        this.calendar= calendar;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);

        long start= calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        long end= calendar.getTimeInMillis();

        return db.getCategory(start, end);
    }

    public ArrayList<Notes> getNotes(){
        return db.getNotes();
    }
}
