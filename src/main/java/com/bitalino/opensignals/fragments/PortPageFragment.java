package com.bitalino.opensignals.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidplot.Plot;
import com.androidplot.PlotListener;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.bitalino.opensignals.BITalinoSeries;
import com.bitalino.opensignals.BITalinoSeriesObserver;
import com.bitalino.opensignals.R;
import com.bitalino.opensignals.model.Port;

import java.text.DecimalFormat;

/**
 * Where plot magic happens.
 */
public class PortPageFragment extends Fragment {
  private static final String TAG = PortPageFragment.class.getSimpleName();

  // The port entity associated with this fragment
  private Port port;
  private BITalinoSeries series;
  private BITalinoSeriesObserver observer;

  // views
  private TextView labelPortDescription;
  private XYPlot plot;
  private int frameCounter = 0;

  public PortPageFragment() {
  }

  /**
   * Instantiates a new readings pagefragment.
   */
  public PortPageFragment(Port port, BITalinoSeries series, BITalinoSeriesObserver observer) {
    this.port = port;
    this.series = series;
    this.observer = observer;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_port_page, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // get views
    labelPortDescription = (TextView) getView().findViewById(R.id.label_port_description);
    labelPortDescription.setText(getString(R.string.label_port_description, port.getName(), port.getUnit()));

    plot = (XYPlot) getView().findViewById(R.id.dynamicPlot);

    // scroll automatically
    plot.addListener(new PlotListener() {
      @Override
      public void onBeforeDraw(Plot source, Canvas canvas) {
        // ignore
      }

      @Override
      public void onAfterDraw(Plot source, Canvas canvas) {
        plot.setDomainBoundaries(frameCounter - 5000, frameCounter, BoundaryMode.FIXED);
        frameCounter += 1000;
      }
    });
    observer.setPlot(plot);

    setupGraphWidget();
    setupDomainWidget();
    setupRangeWidget();

    LineAndPointFormatter format = new LineAndPointFormatter(Color.rgb(118, 182, 200), Color.rgb(118, 182, 200), Color.TRANSPARENT, new PointLabelFormatter());
    plot.addSeries(series, format);
  }

  private void setupGraphWidget() {
    // Customize the graphwidget
    // Remove the border lines
    plot.setBorderStyle(Plot.BorderStyle.NONE, null, null);
    XYGraphWidget graphWidget = plot.getGraphWidget();

    // Some tune ups tp the padding
    graphWidget.setPaddingTop(45.0f);
    graphWidget.setPaddingLeft(60.0f);
    graphWidget.setPaddingBottom(45.0f);
    graphWidget.setPaddingRight(20.0f);

    // Set transparent bagrounds
    graphWidget.getBackgroundPaint().setColor(Color.TRANSPARENT);
    graphWidget.getGridBackgroundPaint().setColor(Color.TRANSPARENT);

    // Customize the origin domain label, set the applications color, fix up the sizes and tweaks to the gridlines
    graphWidget.getDomainOriginLabelPaint().setColor(getResources().getColor(R.color.opensignals_text_field));
    graphWidget.getDomainOriginLabelPaint().setTextSize(40.0f);
    graphWidget.getDomainOriginLinePaint().setColor(getResources().getColor(R.color.opensignals_text_field));
    graphWidget.getDomainOriginLinePaint().setStrokeWidth(0.5f);
    graphWidget.getDomainGridLinePaint().setColor(Color.TRANSPARENT);

    // Customize the origin range label, set the applications color, fix up the sizes and tweaks to the gridlines
    graphWidget.getRangeOriginLabelPaint().setColor(getResources().getColor(R.color.opensignals_text_field));
    graphWidget.getRangeOriginLabelPaint().setTextSize(40.0f);
    graphWidget.getRangeOriginLinePaint().setColor(getResources().getColor(R.color.opensignals_text_field));
    graphWidget.getRangeOriginLinePaint().setStrokeWidth(0.5f);
    graphWidget.getRangeGridLinePaint().setStrokeWidth(1.0f);

    // Remove unnecessary widgets
    plot.getLayoutManager().remove(plot.getLegendWidget());
    plot.getLayoutManager().remove(plot.getTitleWidget());
  }

  private void setupDomainWidget() {
    // Set up the domain label text, color, size, position, boundaries and step
    plot.setDomainLabel(getString(R.string.label_domain));
    plot.getDomainLabelWidget().getLabelPaint().setColor(getResources().getColor(R.color.opensignals_text_field));
    plot.getDomainLabelWidget().getLabelPaint().setTextSize(40.0f);
    plot.getDomainLabelWidget().setWidth(1.0f, SizeLayoutType.RELATIVE);
    plot.getDomainLabelWidget().getPositionMetrics().getXPositionMetric().set(0, XLayoutStyle.RELATIVE_TO_LEFT);
    plot.getDomainLabelWidget().setClippingEnabled(false);
    plot.setDomainBoundaries(0, 5000, BoundaryMode.FIXED);
    plot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
    plot.setDomainStepValue(10);
    plot.getGraphWidget().setDomainLabelPaint(null);
    plot.getGraphWidget().setDomainOriginLabelPaint(null);
  }

  private void setupRangeWidget() {
    // Set up the range label size, position, boundaries and step
    // All according to the the type of port specified
    switch (port.getPort()) {
      case ECG:
        plot.setRangeBoundaries(-2f, 2f, BoundaryMode.FIXED);
        plot.setRangeValueFormat(new DecimalFormat("0.00"));
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 0.5);
        plot.setRangeStepValue(0.5);
        break;
      case EMG:
        plot.setRangeBoundaries(-2.0f, 2.0f, BoundaryMode.FIXED);
        plot.setRangeValueFormat(new DecimalFormat("0.00"));
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 0.5);
        plot.setRangeStepValue(0.5);
        break;
      case EDA:
        plot.setRangeBoundaries(0, 1000, BoundaryMode.FIXED);
        plot.setRangeValueFormat(new DecimalFormat("0"));
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 100);
        plot.setRangeStepValue(100);
        break;
      case LUX:
        plot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        plot.setRangeValueFormat(new DecimalFormat("0"));
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 10);
        plot.setRangeStepValue(10);
        break;
      case ACC:
        plot.setRangeBoundaries(-5, 5, BoundaryMode.FIXED);
        plot.setRangeValueFormat(new DecimalFormat("0.00"));
        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 1);
        plot.setRangeStepValue(1);
        break;
    }

    plot.getRangeLabelWidget().setVisible(false);
    plot.getGraphWidget().getRangeLabelPaint().setColor(getResources().getColor(R.color.opensignals_text_field));
    plot.getGraphWidget().getRangeLabelPaint().setTextSize(40.0f);

    plot.getGraphWidget().setRangeLabelHorizontalOffset(10.0f);
  }
}
