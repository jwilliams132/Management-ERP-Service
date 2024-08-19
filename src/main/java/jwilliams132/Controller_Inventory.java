package jwilliams132;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class Controller_Inventory {

    private Stored_Files_Manager storageManager;
    @FXML
    private BorderPane inventoryRoot;

    @FXML
    private TableView<Tire> tireTableView; // Ensure this is parameterized with Tire
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private NumberAxis xAxis, yAxis;

    @FXML
    private TableColumn<Tire, String> itemIdColumn;
    @FXML
    private TableColumn<Tire, ModelType> modelColumn;
    @FXML
    private TableColumn<Tire, String> tireSizeColumn;
    @FXML
    private TableColumn<Tire, Integer> tireCountColumn;

    private ObservableList<Tire> tireList = FXCollections.observableArrayList();
    private Map<XYChart.Series<Number, Number>, Integer> seriesMap = new HashMap<XYChart.Series<Number, Number>, Integer>();

    public void setup() {

        // Set up TableColumn properties
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("skuNumber"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        tireSizeColumn.setCellValueFactory(new PropertyValueFactory<>("tireSize"));
        tireCountColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryCount"));

        setRightAlignment(tireCountColumn);

        tireList = storageManager.getTireInventory();
        tireTableView.setItems(tireList);

        // populateLineChart(); // sample data
        for (Tire tire : tireList) {

            generateInventoryGraph(tire);
        }
        for (XYChart.Series<Number, Number> series : lineChart.getData()) {
            addHoverEffect(lineChart, series);
        }
    }

    private <S, T> void setRightAlignment(TableColumn<S, T> column) {

        column.setCellFactory(new Callback<TableColumn<S, T>, TableCell<S, T>>() {

            @Override
            public TableCell<S, T> call(TableColumn<S, T> param) {

                return new TableCell<S, T>() {

                    @Override
                    protected void updateItem(T item, boolean empty) {

                        super.updateItem(item, empty);
                        if (empty || item == null) {

                            setText(null);
                        } else {

                            setText(item.toString());
                        }
                        setAlignment(Pos.CENTER_RIGHT);
                    }
                };
            }
        });
    }

    public void populateLineChart() {

        // Create Series 1
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Portfolio 1");
        series1.getData().add(new XYChart.Data<>(0, 25));
        series1.getData().add(new XYChart.Data<>(1, 23));
        series1.getData().add(new XYChart.Data<>(2, 14));
        series1.getData().add(new XYChart.Data<>(3, 15));
        series1.getData().add(new XYChart.Data<>(4, 24));
        series1.getData().add(new XYChart.Data<>(5, 34));
        series1.getData().add(new XYChart.Data<>(6, 36));
        series1.getData().add(new XYChart.Data<>(7, 22));
        series1.getData().add(new XYChart.Data<>(8, 70));
        series1.getData().add(new XYChart.Data<>(9, 43));
        series1.getData().add(new XYChart.Data<>(10, 17));
        series1.getData().add(new XYChart.Data<>(11, 29));
        series1.getData().add(new XYChart.Data<>(12, 25));
        series1.getData().add(new XYChart.Data<>(13, 25));

        // Create Series 2
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Portfolio 2");
        series2.getData().add(new XYChart.Data<>(1, 12));
        series2.getData().add(new XYChart.Data<>(2, 18));
        series2.getData().add(new XYChart.Data<>(3, 29));
        series2.getData().add(new XYChart.Data<>(4, 28));
        series2.getData().add(new XYChart.Data<>(5, 22));
        series2.getData().add(new XYChart.Data<>(6, 32));
        series2.getData().add(new XYChart.Data<>(7, 25));
        series2.getData().add(new XYChart.Data<>(8, 30));
        series2.getData().add(new XYChart.Data<>(9, 27));
        series2.getData().add(new XYChart.Data<>(10, 20));
        series2.getData().add(new XYChart.Data<>(11, 34));
        series2.getData().add(new XYChart.Data<>(12, 31));

        // Create Series 3
        XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
        series3.setName("Portfolio 3");
        series3.getData().add(new XYChart.Data<>(1, 16));
        series3.getData().add(new XYChart.Data<>(2, 22));
        series3.getData().add(new XYChart.Data<>(3, 30));
        series3.getData().add(new XYChart.Data<>(4, 20));
        series3.getData().add(new XYChart.Data<>(5, 29));
        series3.getData().add(new XYChart.Data<>(6, 37));
        series3.getData().add(new XYChart.Data<>(7, 24));
        series3.getData().add(new XYChart.Data<>(8, 41));
        series3.getData().add(new XYChart.Data<>(9, 35));
        series3.getData().add(new XYChart.Data<>(10, 19));
        series3.getData().add(new XYChart.Data<>(11, 27));
        series3.getData().add(new XYChart.Data<>(12, 28));

        // Create Series 4
        XYChart.Series<Number, Number> series4 = new XYChart.Series<>();
        series4.setName("Portfolio 4");
        series4.getData().add(new XYChart.Data<>(1, 25));
        series4.getData().add(new XYChart.Data<>(2, 20));
        series4.getData().add(new XYChart.Data<>(3, 35));
        series4.getData().add(new XYChart.Data<>(4, 30));
        series4.getData().add(new XYChart.Data<>(5, 28));
        series4.getData().add(new XYChart.Data<>(6, 40));
        series4.getData().add(new XYChart.Data<>(7, 33));
        series4.getData().add(new XYChart.Data<>(8, 44));
        series4.getData().add(new XYChart.Data<>(9, 37));
        series4.getData().add(new XYChart.Data<>(10, 22));
        series4.getData().add(new XYChart.Data<>(11, 31));
        series4.getData().add(new XYChart.Data<>(12, 26));

        // Create Series 5
        XYChart.Series<Number, Number> series5 = new XYChart.Series<>();
        series5.setName("Portfolio 5");
        series5.getData().add(new XYChart.Data<>(1, 18));
        series5.getData().add(new XYChart.Data<>(2, 21));
        series5.getData().add(new XYChart.Data<>(3, 26));
        series5.getData().add(new XYChart.Data<>(4, 24));
        series5.getData().add(new XYChart.Data<>(5, 30));
        series5.getData().add(new XYChart.Data<>(6, 35));
        series5.getData().add(new XYChart.Data<>(7, 29));
        series5.getData().add(new XYChart.Data<>(8, 37));
        series5.getData().add(new XYChart.Data<>(9, 32));
        series5.getData().add(new XYChart.Data<>(10, 20));
        series5.getData().add(new XYChart.Data<>(11, 28));
        series5.getData().add(new XYChart.Data<>(12, 22));

        // Create Series 6
        XYChart.Series<Number, Number> series6 = new XYChart.Series<>();
        series6.setName("Portfolio 6");
        series6.getData().add(new XYChart.Data<>(1, 20));
        series6.getData().add(new XYChart.Data<>(2, 25));
        series6.getData().add(new XYChart.Data<>(3, 30));
        series6.getData().add(new XYChart.Data<>(4, 28));
        series6.getData().add(new XYChart.Data<>(5, 34));
        series6.getData().add(new XYChart.Data<>(6, 38));
        series6.getData().add(new XYChart.Data<>(7, 27));
        series6.getData().add(new XYChart.Data<>(8, 33));
        series6.getData().add(new XYChart.Data<>(9, 29));
        series6.getData().add(new XYChart.Data<>(10, 24));
        series6.getData().add(new XYChart.Data<>(11, 31));
        series6.getData().add(new XYChart.Data<>(12, 26));

        // Create Series 7
        XYChart.Series<Number, Number> series7 = new XYChart.Series<>();
        series7.setName("Portfolio 7");
        series7.getData().add(new XYChart.Data<>(1, 15));
        series7.getData().add(new XYChart.Data<>(2, 18));
        series7.getData().add(new XYChart.Data<>(3, 22));
        series7.getData().add(new XYChart.Data<>(4, 26));
        series7.getData().add(new XYChart.Data<>(5, 30));
        series7.getData().add(new XYChart.Data<>(6, 34));
        series7.getData().add(new XYChart.Data<>(7, 28));
        series7.getData().add(new XYChart.Data<>(8, 32));
        series7.getData().add(new XYChart.Data<>(9, 27));
        series7.getData().add(new XYChart.Data<>(10, 20));
        series7.getData().add(new XYChart.Data<>(11, 24));
        series7.getData().add(new XYChart.Data<>(12, 22));

        // Create Series 8
        XYChart.Series<Number, Number> series8 = new XYChart.Series<>();
        series8.setName("Portfolio 8");
        series8.getData().add(new XYChart.Data<>(1, 22));
        series8.getData().add(new XYChart.Data<>(2, 26));
        series8.getData().add(new XYChart.Data<>(3, 28));
        series8.getData().add(new XYChart.Data<>(4, 32));
        series8.getData().add(new XYChart.Data<>(5, 35));
        series8.getData().add(new XYChart.Data<>(6, 39));
        series8.getData().add(new XYChart.Data<>(7, 31));
        series8.getData().add(new XYChart.Data<>(8, 37));
        series8.getData().add(new XYChart.Data<>(9, 33));
        series8.getData().add(new XYChart.Data<>(10, 27));
        series8.getData().add(new XYChart.Data<>(11, 30));
        series8.getData().add(new XYChart.Data<>(12, 29));

        // Create Series 9
        XYChart.Series<Number, Number> series9 = new XYChart.Series<>();
        series9.setName("Portfolio 9");
        series9.getData().add(new XYChart.Data<>(1, 14));
        series9.getData().add(new XYChart.Data<>(2, 19));
        series9.getData().add(new XYChart.Data<>(3, 23));
        series9.getData().add(new XYChart.Data<>(4, 28));
        series9.getData().add(new XYChart.Data<>(5, 33));
        series9.getData().add(new XYChart.Data<>(6, 37));
        series9.getData().add(new XYChart.Data<>(7, 29));
        series9.getData().add(new XYChart.Data<>(8, 34));
        series9.getData().add(new XYChart.Data<>(9, 31));
        series9.getData().add(new XYChart.Data<>(10, 22));
        series9.getData().add(new XYChart.Data<>(11, 25));
        series9.getData().add(new XYChart.Data<>(12, 23));

        // Create Series 10
        XYChart.Series<Number, Number> series10 = new XYChart.Series<>();
        series10.setName("Portfolio 10");
        series10.getData().add(new XYChart.Data<>(1, 21));
        series10.getData().add(new XYChart.Data<>(2, 26));
        series10.getData().add(new XYChart.Data<>(3, 31));
        series10.getData().add(new XYChart.Data<>(4, 30));
        series10.getData().add(new XYChart.Data<>(5, 37));
        series10.getData().add(new XYChart.Data<>(6, 41));
        series10.getData().add(new XYChart.Data<>(7, 35));
        series10.getData().add(new XYChart.Data<>(8, 40));
        series10.getData().add(new XYChart.Data<>(9, 32));
        series10.getData().add(new XYChart.Data<>(10, 28));
        series10.getData().add(new XYChart.Data<>(11, 31));
        series10.getData().add(new XYChart.Data<>(12, 29));

        // Create Series 11
        XYChart.Series<Number, Number> series11 = new XYChart.Series<>();
        series11.setName("Portfolio 11");
        series11.getData().add(new XYChart.Data<>(1, 17));
        series11.getData().add(new XYChart.Data<>(2, 22));
        series11.getData().add(new XYChart.Data<>(3, 25));
        series11.getData().add(new XYChart.Data<>(4, 29));
        series11.getData().add(new XYChart.Data<>(5, 33));
        series11.getData().add(new XYChart.Data<>(6, 38));
        series11.getData().add(new XYChart.Data<>(7, 31));
        series11.getData().add(new XYChart.Data<>(8, 37));
        series11.getData().add(new XYChart.Data<>(9, 34));
        series11.getData().add(new XYChart.Data<>(10, 26));
        series11.getData().add(new XYChart.Data<>(11, 28));
        series11.getData().add(new XYChart.Data<>(12, 23));

        // Create Series 12
        XYChart.Series<Number, Number> series12 = new XYChart.Series<>();
        series12.setName("Portfolio 12");
        series12.getData().add(new XYChart.Data<>(1, 26));
        series12.getData().add(new XYChart.Data<>(2, 30));
        series12.getData().add(new XYChart.Data<>(3, 34));
        series12.getData().add(new XYChart.Data<>(4, 38));
        series12.getData().add(new XYChart.Data<>(5, 42));
        series12.getData().add(new XYChart.Data<>(6, 45));
        series12.getData().add(new XYChart.Data<>(7, 37));
        series12.getData().add(new XYChart.Data<>(8, 43));
        series12.getData().add(new XYChart.Data<>(9, 41));
        series12.getData().add(new XYChart.Data<>(10, 32));
        series12.getData().add(new XYChart.Data<>(11, 35));
        series12.getData().add(new XYChart.Data<>(12, 29));

        // Create Series 13
        XYChart.Series<Number, Number> series13 = new XYChart.Series<>();
        series13.setName("Portfolio 13");
        series13.getData().add(new XYChart.Data<>(1, 19));
        series13.getData().add(new XYChart.Data<>(2, 24));
        series13.getData().add(new XYChart.Data<>(3, 28));
        series13.getData().add(new XYChart.Data<>(4, 32));
        series13.getData().add(new XYChart.Data<>(5, 36));
        series13.getData().add(new XYChart.Data<>(6, 40));
        series13.getData().add(new XYChart.Data<>(7, 30));
        series13.getData().add(new XYChart.Data<>(8, 35));
        series13.getData().add(new XYChart.Data<>(9, 32));
        series13.getData().add(new XYChart.Data<>(10, 27));
        series13.getData().add(new XYChart.Data<>(11, 30));
        series13.getData().add(new XYChart.Data<>(12, 25));

        // Create Series 14
        XYChart.Series<Number, Number> series14 = new XYChart.Series<>();
        series14.setName("Portfolio 14");
        series14.getData().add(new XYChart.Data<>(1, 22));
        series14.getData().add(new XYChart.Data<>(2, 28));
        series14.getData().add(new XYChart.Data<>(3, 33));
        series14.getData().add(new XYChart.Data<>(4, 36));
        series14.getData().add(new XYChart.Data<>(5, 40));
        series14.getData().add(new XYChart.Data<>(6, 44));
        series14.getData().add(new XYChart.Data<>(7, 32));
        series14.getData().add(new XYChart.Data<>(8, 39));
        series14.getData().add(new XYChart.Data<>(9, 35));
        series14.getData().add(new XYChart.Data<>(10, 27));
        series14.getData().add(new XYChart.Data<>(11, 30));
        series14.getData().add(new XYChart.Data<>(12, 26));

        // Create Series 15
        XYChart.Series<Number, Number> series15 = new XYChart.Series<>();
        series15.setName("Portfolio 15");
        series15.getData().add(new XYChart.Data<>(1, 27));
        series15.getData().add(new XYChart.Data<>(2, 33));
        series15.getData().add(new XYChart.Data<>(3, 39));
        series15.getData().add(new XYChart.Data<>(4, 42));
        series15.getData().add(new XYChart.Data<>(5, 45));
        series15.getData().add(new XYChart.Data<>(6, 48));
        series15.getData().add(new XYChart.Data<>(7, 40));
        series15.getData().add(new XYChart.Data<>(8, 44));
        series15.getData().add(new XYChart.Data<>(9, 38));
        series15.getData().add(new XYChart.Data<>(10, 30));
        series15.getData().add(new XYChart.Data<>(11, 34));
        series15.getData().add(new XYChart.Data<>(12, 29));

        // Create Series 16
        XYChart.Series<Number, Number> series16 = new XYChart.Series<>();
        series16.setName("Portfolio 16");
        series16.getData().add(new XYChart.Data<>(1, 23));
        series16.getData().add(new XYChart.Data<>(2, 28));
        series16.getData().add(new XYChart.Data<>(3, 34));
        series16.getData().add(new XYChart.Data<>(4, 38));
        series16.getData().add(new XYChart.Data<>(5, 42));
        series16.getData().add(new XYChart.Data<>(6, 46));
        series16.getData().add(new XYChart.Data<>(7, 36));
        series16.getData().add(new XYChart.Data<>(8, 41));
        series16.getData().add(new XYChart.Data<>(9, 37));
        series16.getData().add(new XYChart.Data<>(10, 32));
        series16.getData().add(new XYChart.Data<>(11, 30));
        series16.getData().add(new XYChart.Data<>(12, 28));

        // Create Series 17
        XYChart.Series<Number, Number> series17 = new XYChart.Series<>();
        series17.setName("Portfolio 17");
        series17.getData().add(new XYChart.Data<>(1, 21));
        series17.getData().add(new XYChart.Data<>(2, 26));
        series17.getData().add(new XYChart.Data<>(3, 32));
        series17.getData().add(new XYChart.Data<>(4, 37));
        series17.getData().add(new XYChart.Data<>(5, 42));
        series17.getData().add(new XYChart.Data<>(6, 47));
        series17.getData().add(new XYChart.Data<>(7, 39));
        series17.getData().add(new XYChart.Data<>(8, 44));
        series17.getData().add(new XYChart.Data<>(9, 36));
        series17.getData().add(new XYChart.Data<>(10, 30));
        series17.getData().add(new XYChart.Data<>(11, 33));
        series17.getData().add(new XYChart.Data<>(12, 29));

        // Create Series 18
        XYChart.Series<Number, Number> series18 = new XYChart.Series<>();
        series18.setName("Portfolio 18");
        series18.getData().add(new XYChart.Data<>(1, 18));
        series18.getData().add(new XYChart.Data<>(2, 23));
        series18.getData().add(new XYChart.Data<>(3, 28));
        series18.getData().add(new XYChart.Data<>(4, 32));
        series18.getData().add(new XYChart.Data<>(5, 37));
        series18.getData().add(new XYChart.Data<>(6, 41));
        series18.getData().add(new XYChart.Data<>(7, 35));
        series18.getData().add(new XYChart.Data<>(8, 40));
        series18.getData().add(new XYChart.Data<>(9, 30));
        series18.getData().add(new XYChart.Data<>(10, 26));
        series18.getData().add(new XYChart.Data<>(11, 29));
        series18.getData().add(new XYChart.Data<>(12, 24));

        // Create Series 19
        XYChart.Series<Number, Number> series19 = new XYChart.Series<>();
        series19.setName("Portfolio 19");
        series19.getData().add(new XYChart.Data<>(1, 24));
        series19.getData().add(new XYChart.Data<>(2, 29));
        series19.getData().add(new XYChart.Data<>(3, 33));
        series19.getData().add(new XYChart.Data<>(4, 37));
        series19.getData().add(new XYChart.Data<>(5, 42));
        series19.getData().add(new XYChart.Data<>(6, 47));
        series19.getData().add(new XYChart.Data<>(7, 40));
        series19.getData().add(new XYChart.Data<>(8, 45));
        series19.getData().add(new XYChart.Data<>(9, 41));
        series19.getData().add(new XYChart.Data<>(10, 32));
        series19.getData().add(new XYChart.Data<>(11, 28));
        series19.getData().add(new XYChart.Data<>(12, 25));

        // Create Series 20
        XYChart.Series<Number, Number> series20 = new XYChart.Series<>();
        series20.setName("Portfolio 20");
        series20.getData().add(new XYChart.Data<>(1, 22));
        series20.getData().add(new XYChart.Data<>(2, 27));
        series20.getData().add(new XYChart.Data<>(3, 33));
        series20.getData().add(new XYChart.Data<>(4, 36));
        series20.getData().add(new XYChart.Data<>(5, 40));
        series20.getData().add(new XYChart.Data<>(6, 43));
        series20.getData().add(new XYChart.Data<>(7, 37));
        series20.getData().add(new XYChart.Data<>(8, 42));
        series20.getData().add(new XYChart.Data<>(9, 34));
        series20.getData().add(new XYChart.Data<>(10, 29));
        series20.getData().add(new XYChart.Data<>(11, 25));
        series20.getData().add(new XYChart.Data<>(12, 22));

        // Add all series to the chart
        lineChart.getData().add(series1);
        lineChart.getData().add(series2);
        lineChart.getData().add(series3);
        lineChart.getData().add(series4);
        lineChart.getData().add(series5);
        lineChart.getData().add(series6);
        lineChart.getData().add(series7);
        lineChart.getData().add(series8);
        lineChart.getData().add(series9);
        lineChart.getData().add(series10);
        lineChart.getData().add(series11);
        lineChart.getData().add(series12);
        lineChart.getData().add(series13);
        lineChart.getData().add(series14);
        lineChart.getData().add(series15);
        lineChart.getData().add(series16);
        lineChart.getData().add(series17);
        lineChart.getData().add(series18);
        lineChart.getData().add(series19);
        lineChart.getData().add(series20);
    }

    public void setupLineChart() {

    }

    public void generateInventoryGraph(Tire tire) {

        final int xAxisRange = 14;

        Map<LocalDate, Integer> inventoryOverTime = new HashMap<>();

        int runningTotal = 0;
        for (Transaction transaction : storageManager.getTransactionsHistoryOfTire(tire)) {

            if (transaction instanceof Sale)
                runningTotal -= transaction.getQuantity();

            if (transaction instanceof Purchase)
                runningTotal += transaction.getQuantity();

            inventoryOverTime.put(transaction.getTime().toLocalDate(), runningTotal);
        }

        // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

        ObservableList<XYChart.Data<Number, Number>> graphData = FXCollections.observableArrayList();
        LocalDate today = LocalDate.now();

        // set all points between now and 60 days ago
        Map.Entry<LocalDate, Integer> seriesStartPoint = null;
        for (Map.Entry<LocalDate, Integer> entry : inventoryOverTime.entrySet()) {

            if (entry.getKey().isBefore(today.minusDays(xAxisRange))) {

                // System.out.println("bitch");
                if (seriesStartPoint == null) {
                    seriesStartPoint = entry;
                    continue;
                }
                seriesStartPoint = entry.getKey().isAfter(seriesStartPoint.getKey()) ? entry : seriesStartPoint;
                continue;
            }

            graphData.add(
                    new XYChart.Data<>(
                            (ChronoUnit.DAYS.between(entry.getKey(), today)),
                            entry.getValue()));
        }

        // Check if there is already a point with the xAxisRange value
        boolean pointExistsAtXAxisRange = graphData.stream()
                .anyMatch(data -> data.getXValue().intValue() == xAxisRange);

        if (!pointExistsAtXAxisRange)
            graphData.add(new XYChart.Data<>(xAxisRange, tire.getInventoryCount())); // add the most current point

        graphData.add(new XYChart.Data<>(0, seriesStartPoint == null ? 0 : seriesStartPoint.getValue())); // set most
                                                                                                          // current
                                                                                                          // point

        // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

        XYChart.Series<Number, Number> series = new XYChart.Series<>(graphData);
        seriesMap.put(series, seriesMap.size());
        series.setName(tire.getSkuNumber());
        lineChart.getData().add(series);
    }

    private void addHoverEffect(LineChart<Number, Number> lineChart, XYChart.Series<Number, Number> series) {

        for (XYChart.Data<Number, Number> data : series.getData()) {

            data.getNode().setOnMouseEntered(event -> {

                System.out.println("Mouse entered: " + data.getXValue()); // Debugging line
                Tooltip.install(data.getNode(), new Tooltip(String.valueOf(data.getYValue())));

                highlightSeries(lineChart, series);
            });

            data.getNode().setOnMouseExited(event -> {

                removeHighlightFromSeries(lineChart, series);
            });
        }

        // Apply hover effect for legend items
        System.out.println(lineChart.lookupAll(".chart-legend-item").size());
        for (Node legendItem : lineChart.lookupAll(".chart-legend-item")) {
            Label label = (Label) legendItem;

            if (label.getText().equals(series.getName())) {

                System.out.println(label.getText());
                label.setOnMouseEntered(event -> {

                    System.out.println("Mouse entered on legend: " + series.getName()); // Debugging line

                    highlightSeries(lineChart, series);
                });

                label.setOnMouseExited(event -> {

                    System.out.println("Mouse exited from legend: " + series.getName()); // Debugging line

                    removeHighlightFromSeries(lineChart, series);
                });
            }
        }
    }

    private void removeHighlightFromSeries(LineChart<Number, Number> lineChart, XYChart.Series<Number, Number> series) {

        for (Map.Entry<XYChart.Series<Number, Number>, Integer> entry : seriesMap.entrySet()) {

            XYChart.Series<Number, Number> currentSeries = entry.getKey();
            if (!currentSeries.equals(series)) {

                Set<Node> nodes = lineChart.lookupAll(".series" + seriesMap.get(currentSeries));

                for (Node n : nodes) {
                    n.getStyleClass().add(String.format("default-color%d",
                            seriesMap.get(currentSeries) % 8));
                    n.getStyleClass().remove("greyed-out");
                }
            }
        }
    }

    private void highlightSeries(LineChart<Number, Number> lineChart, XYChart.Series<Number, Number> series) {

        for (Map.Entry<XYChart.Series<Number, Number>, Integer> entry : seriesMap.entrySet()) {

            XYChart.Series<Number, Number> currentSeries = entry.getKey();
            if (!currentSeries.equals(series)) {

                Set<Node> nodes = lineChart.lookupAll(".series" + seriesMap.get(currentSeries));

                for (Node n : nodes) {

                    n.getStyleClass().remove(
                            String.format("default-color%d", seriesMap.get(currentSeries) % 8));
                    n.getStyleClass().add("greyed-out");
                }
            }
        }
    }

    public void setStorageManager(Stored_Files_Manager storageManager) {

        this.storageManager = storageManager;
    }
}
