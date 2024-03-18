package com.example.stacksats.views.main;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

import com.example.stacksats.BtcPriceService;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.HorizontalAlign;
import com.vaadin.flow.component.charts.model.IntervalUnit;
import com.vaadin.flow.component.charts.model.LayoutDirection;
import com.vaadin.flow.component.charts.model.Legend;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Navigator;
import com.vaadin.flow.component.charts.model.PlotOptionsSeries;
import com.vaadin.flow.component.charts.model.VerticalAlign;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

  @SuppressWarnings("unused")
  private final  BtcPriceService btcPriceService;

  public MainView(BtcPriceService btcPriceService) {
    this.btcPriceService = btcPriceService;
    VerticalLayout verticalLayout = new VerticalLayout();

    add(
        new H1(
            "Rather stack some sats than to pay for Twiteer/X monthly verified account fee, here's why:"),
            verticalLayout,
        new HorizontalLayout());

    Chart chart = new Chart(ChartType.LINE);

    Configuration configuration = chart.getConfiguration();

    configuration.setTitle("Value of $8/month invested in Bitcoin since November 5, 2022");
    configuration.setSubTitle("Source: coingecko.com");

    YAxis yAxis = configuration.getyAxis();
    yAxis.setTitle("Amount (sats)");

    Legend legend = configuration.getLegend();
    legend.setLayout(LayoutDirection.VERTICAL);
    legend.setVerticalAlign(VerticalAlign.MIDDLE);
    legend.setAlign(HorizontalAlign.RIGHT);

    PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
    plotOptionsSeries.setPointStart(2022);
    plotOptionsSeries.setPointIntervalUnit(IntervalUnit.MONTH);
    plotOptionsSeries.setPointInterval(1);
    configuration.setPlotOptions(plotOptionsSeries);

    HashMap<LocalDate, BigDecimal> priceForDateMap = btcPriceService.priceForDateMap();
    Collection<Number> satsList = btcPriceService.convertToSatsList(priceForDateMap);
    configuration.addSeries(new ListSeries("Sats", satsList));

    Navigator navigator = configuration.getNavigator();
    navigator.setEnabled(true);
    navigator.setMargin(75);
    verticalLayout.add(chart);
  }
}
