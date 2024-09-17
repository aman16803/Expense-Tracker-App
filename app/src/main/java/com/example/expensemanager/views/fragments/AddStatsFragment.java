package com.example.expensemanager.views.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentAddStatsBinding;
import com.example.expensemanager.viewmodels.MainViewModel;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class AddStatsFragment extends Fragment {

    FragmentAddStatsBinding binding;
    Calendar calendar;
    public MainViewModel viewModel;
    public AddStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentAddStatsBinding.inflate(inflater);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ArrayList<Integer> list= viewModel.TotalExpense();
        ArrayList<BarEntry> expenses= new ArrayList<>();
        ArrayList<String> monthList= new ArrayList<>();

        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        for(int i=0;i<list.size();i++){
            DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
            String[] monthNames = dateFormatSymbols.getMonths();
            String monthName = monthNames[month];

            monthList.add(monthName);
            expenses.add(new BarEntry(i,list.get(i)));
            month--;
        }

        BarDataSet barDataSet= new BarDataSet(expenses, "Expenses");
        //barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setColor( ContextCompat.getColor(getContext(), R.color.light_blue));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(20f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData barData= new BarData(barDataSet);

        //binding.barChart.setFitBars(false);
        binding.barChart.setData(barData);
        //binding.barChart.getDescription().setText("Bar chart eg.");
        binding.barChart.animateY(1000);
        binding.barChart.getAxisLeft().setTextSize(15f);

        //binding.barChart.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(monthList));
        binding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(monthList));
        binding.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.barChart.getXAxis().setDrawGridLines(false);
        //binding.barChart.getAxisLeft().setDrawAxisLine(false);
        binding.barChart.getXAxis().setGranularity(1f);
        binding.barChart.getXAxis().setLabelCount(monthList.size());
        binding.barChart.getXAxis().setLabelRotationAngle(270);
        binding.barChart.getXAxis().setTextSize(15f);
        binding.barChart.invalidate();

        binding.barChart.getAxisRight().setEnabled(false);

        return binding.getRoot();
    }
}