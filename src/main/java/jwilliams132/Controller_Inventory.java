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

        storageManager = Stored_Files_Manager.getInstance();

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

                Tooltip.install(data.getNode(), new Tooltip(String.valueOf(data.getYValue())));

                highlightSeries(lineChart, series);
            });

            data.getNode().setOnMouseExited(event -> {

                removeHighlightFromSeries(lineChart, series);
            });
        }

        // Apply hover effect for legend items
        for (Node legendItem : lineChart.lookupAll(".chart-legend-item")) {
            Label label = (Label) legendItem;

            if (label.getText().equals(series.getName())) {

                label.setOnMouseEntered(event -> {

                    highlightSeries(lineChart, series);
                });

                label.setOnMouseExited(event -> {

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
}
