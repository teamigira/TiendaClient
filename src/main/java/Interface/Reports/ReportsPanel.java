package Interface.Reports;

import Authentication.LicenseManager;
import Classes.Functions.SalesReports;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Reports dashboard: headline totals plus sales charts (daily, monthly, and —
 * for Premium licences — breakdowns by category and brand).
 *
 * <p>Built in plain Swing + JFreeChart so it can be dropped into the main
 * window's card area. Everything reads live from {@link SalesReports}; call
 * {@link #reload()} to refresh after new sales.
 *
 * @author Nkanabo
 */
public class ReportsPanel extends JPanel {

    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.##");
    private static final DateTimeFormatter DAY_LABEL = DateTimeFormatter.ofPattern("dd MMM");

    private final String currency;
    private final JPanel kpiRow = new JPanel(new GridLayout(1, 0, 10, 0));
    private final JPanel charts = new JPanel(new GridLayout(0, 2, 12, 12));

    public ReportsPanel() {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        this.currency = readCurrency();

        JLabel title = new JLabel("Reports");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> reload());

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.WEST);
        header.add(refresh, BorderLayout.EAST);

        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        header.setAlignmentX(LEFT_ALIGNMENT);
        kpiRow.setAlignmentX(LEFT_ALIGNMENT);
        north.add(header);
        north.add(javax.swing.Box.createVerticalStrut(10));
        north.add(kpiRow);
        add(north, BorderLayout.NORTH);

        add(new JScrollPane(charts), BorderLayout.CENTER);

        reload();
    }

    /**
     * Rebuild all KPIs and charts. The (single) database read and all chart
     * assembly happen on a background thread so the window never freezes; a
     * light "Loading…" placeholder shows until the data is ready.
     */
    public final void reload() {
        kpiRow.removeAll();
        kpiRow.add(loadingCard());
        charts.removeAll();
        charts.add(loadingCard());
        revalidate();
        repaint();

        new javax.swing.SwingWorker<SalesReports.Snapshot, Void>() {
            @Override
            protected SalesReports.Snapshot doInBackground() throws Exception {
                return SalesReports.snapshot(30, 12); // one DB pass
            }

            @Override
            protected void done() {
                try {
                    SalesReports.Snapshot s = get();
                    buildKpis(s);
                    buildCharts(s);
                } catch (Exception ex) {
                    Classes.Utilities.Log.error(ex);
                    JOptionPane.showMessageDialog(ReportsPanel.this,
                            "Could not build reports:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    revalidate();
                    repaint();
                }
            }
        }.execute();
    }

    private void buildKpis(SalesReports.Snapshot s) {
        kpiRow.removeAll();
        double[] t = s.totals; // day, week, month, year, all
        kpiRow.add(kpiCard("Today", money(t[0])));
        kpiRow.add(kpiCard("This week", money(t[1])));
        kpiRow.add(kpiCard("This month", money(t[2])));
        kpiRow.add(kpiCard("This year", money(t[3])));
        kpiRow.add(kpiCard("All time", money(t[4])));
        kpiRow.add(kpiCard("Items sold", String.valueOf(s.itemCount)));
    }

    private void buildCharts(SalesReports.Snapshot s) {
        charts.removeAll();

        // Daily revenue (last 30 days) — available to everyone.
        DefaultCategoryDataset daily = new DefaultCategoryDataset();
        for (Map.Entry<LocalDate, Double> e : s.daily.entrySet()) {
            daily.addValue(e.getValue(), "Sales", e.getKey().format(DAY_LABEL));
        }
        JFreeChart dailyChart = ChartFactory.createBarChart(
                "Daily sales (last 30 days)", "Day", currency, daily,
                PlotOrientation.VERTICAL, false, true, false);
        styleCategory(dailyChart);
        charts.add(new ChartPanel(dailyChart));

        // Monthly revenue (last 12 months) — available to everyone.
        DefaultCategoryDataset monthly = new DefaultCategoryDataset();
        for (Map.Entry<String, Double> e : s.monthly.entrySet()) {
            monthly.addValue(e.getValue(), "Sales", e.getKey());
        }
        JFreeChart monthlyChart = ChartFactory.createLineChart(
                "Monthly sales (last 12 months)", "Month", currency, monthly,
                PlotOrientation.VERTICAL, false, true, false);
        styleCategory(monthlyChart);
        charts.add(new ChartPanel(monthlyChart));

        // Premium: breakdowns by category and brand.
        if (LicenseManager.isPremium()) {
            DefaultPieDataset<String> cat = new DefaultPieDataset<>();
            s.byCategory.forEach((k, v) -> {
                if (v > 0) {
                    cat.setValue(k, v);
                }
            });
            JFreeChart catChart = ChartFactory.createPieChart(
                    "Sales by category", cat, true, true, false);
            stylePie(catChart);
            charts.add(new ChartPanel(catChart));

            DefaultCategoryDataset brand = new DefaultCategoryDataset();
            s.byBrand.forEach((k, v) -> brand.addValue(v, "Sales", k));
            JFreeChart brandChart = ChartFactory.createBarChart(
                    "Sales by brand", "Brand", currency, brand,
                    PlotOrientation.HORIZONTAL, false, true, false);
            styleCategory(brandChart);
            charts.add(new ChartPanel(brandChart));
        } else {
            charts.add(premiumPlaceholder("Sales by category"));
            charts.add(premiumPlaceholder("Sales by brand"));
        }
    }

    private JPanel loadingCard() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel l = new JLabel("Loading…", SwingConstants.CENTER);
        l.setForeground(new Color(0x9E9E9E));
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    // ---- small UI helpers ------------------------------------------

    private JPanel kpiCard(String label, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x90A4AE)),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        JLabel v = new JLabel(value);
        v.setFont(v.getFont().deriveFont(Font.BOLD, 18f));
        v.setAlignmentX(CENTER_ALIGNMENT);
        JLabel l = new JLabel(label);
        l.setForeground(new Color(0x607D8B));
        l.setAlignmentX(CENTER_ALIGNMENT);
        card.add(v);
        card.add(l);
        return card;
    }

    private JPanel premiumPlaceholder(String what) {
        JPanel p = new JPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(360, 240));
        p.setBorder(BorderFactory.createDashedBorder(new Color(0xBDBDBD)));
        JLabel l = new JLabel("<html><div style='text-align:center'>"
                + "🔒 " + what + "<br><br>"
                + "Available with a <b>Premium</b> activation code</div></html>",
                SwingConstants.CENTER);
        l.setForeground(new Color(0x9E9E9E));
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    private String money(double v) {
        return currency + " " + MONEY.format(v);
    }

    private static void styleCategory(JFreeChart chart) {
        chart.setBackgroundPaint(null);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setRangeGridlinePaint(new Color(0x90A4AE));
        plot.getDomainAxis().setCategoryLabelPositions(
                org.jfree.chart.axis.CategoryLabelPositions.UP_45);
    }

    private static void stylePie(JFreeChart chart) {
        chart.setBackgroundPaint(null);
        @SuppressWarnings("unchecked")
        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setBackgroundPaint(new Color(0, 0, 0, 0));
    }

    private static String readCurrency() {
        try {
            return java.util.prefs.Preferences.userRoot().node("UserSettings")
                    .get("Currency", "Tsh");
        } catch (Exception e) {
            return "Tsh";
        }
    }
}
