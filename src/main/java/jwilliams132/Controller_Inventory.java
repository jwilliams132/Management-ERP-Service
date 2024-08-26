package jwilliams132;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
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
    private LineChart<String, Number> chart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;

    @FXML
    private TableColumn<Tire, String> itemIdColumn;
    @FXML
    private TableColumn<Tire, ModelType> modelColumn;
    @FXML
    private TableColumn<Tire, String> tireSizeColumn;
    @FXML
    private TableColumn<Tire, Integer> tireCountColumn;

    @FXML
    private Button oneWeek, twoWeeks, threeWeeks, fourWeeks, fiveWeeks, sixWeeks;
    private List<Button> timeFrameOptions = new ArrayList<Button>();

    private int timeFrameVisible;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
    private ObservableList<Tire> tireList = FXCollections.observableArrayList();
    private Map<XYChart.Series<String, Number>, Integer> seriesMap = new HashMap<XYChart.Series<String, Number>, Integer>();

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

        // timeFrameOptions.addAll(List.of(oneWeek, twoWeeks, threeWeeks, fourWeeks, fiveWeeks, sixWeeks));
        timeFrameOptions.add(oneWeek);
        timeFrameOptions.add(twoWeeks);
        timeFrameOptions.add(threeWeeks);
        timeFrameOptions.add(fourWeeks);
        timeFrameOptions.add(fiveWeeks);
        timeFrameOptions.add(sixWeeks);
        timeFrameVisible = 6;
        updateButtonStyles(sixWeeks);
        setupXAxis();
        populateLineChart();
    }

    // ====================================================================================================

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

    // ====================================================================================================

    private void updateButtonStyles(Button targetButton) {

		timeFrameOptions.forEach(button -> button.getStyleClass().remove("dashboard-card-selected-category"));
		targetButton.getStyleClass().add("dashboard-card-selected-category");
	}

    // ====================================================================================================

    private void setupXAxis() {

        LocalDate startDate = LocalDate.now().minusWeeks(timeFrameVisible);
        LocalDate endDate = LocalDate.now();
        List<String> allDates = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            allDates.add(date.format(formatter));
        }
        xAxis.getCategories().clear();
        xAxis.setCategories(javafx.collections.FXCollections.observableArrayList(allDates));
        xAxis.setTickLabelRotation(-60);
    }

    private void populateLineChart() {

        seriesMap = new HashMap<>();

        for (Tire tire : storageManager.getTireInventory()) {

            XYChart.Series<String, Number> series = getSeries(tire);
            series.setName(tire.getSkuNumber());
            seriesMap.put(series, seriesMap.size());

            chart.getData().add(series);
            addHoverEffect(chart, series);
        }
    }

    private XYChart.Series<String, Number> getSeries(Tire tire) {

        List<Transaction> transactions = storageManager.getTransactionsHistoryOfTire(tire);
        Map<LocalDate, Integer> inventoryOverTime = getInventoryOverTimeMap(tire, transactions);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<LocalDate, Integer> entry : inventoryOverTime.entrySet()) {

            series.getData().add(new XYChart.Data<>(entry.getKey().format(formatter), entry.getValue()));
        }
        return series;
    }

    private Map<LocalDate, Integer> getInventoryOverTimeMap(Tire tire, List<Transaction> transactions) {

        Map<LocalDate, Integer> inventoryOverTime = new HashMap<>();
        int runningTotal = tire.getInventoryCount();
        for (Transaction transaction : transactions) {

            if (transaction instanceof Sale)
                runningTotal += transaction.getQuantity();

            if (transaction instanceof Purchase)
                runningTotal -= transaction.getQuantity();

            inventoryOverTime.put(transaction.getTime().toLocalDate(), runningTotal);
        }
        return inventoryOverTime;
    }

    // ====================================================================================================

    private void addHoverEffect(LineChart<String, Number> lineChart, XYChart.Series<String, Number> series) {

        for (XYChart.Data<String, Number> data : series.getData()) {

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

    private void removeHighlightFromSeries(LineChart<String, Number> lineChart, XYChart.Series<String, Number> series) {

        for (Map.Entry<XYChart.Series<String, Number>, Integer> entry : seriesMap.entrySet()) {

            XYChart.Series<String, Number> currentSeries = entry.getKey();
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

    private void highlightSeries(LineChart<String, Number> lineChart, XYChart.Series<String, Number> series) {

        for (Map.Entry<XYChart.Series<String, Number>, Integer> entry : seriesMap.entrySet()) {

            XYChart.Series<String, Number> currentSeries = entry.getKey();
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

    @FXML
    public void handleCategoryButtons(ActionEvent event) {

        Button source = (Button) event.getSource();
        updateButtonStyles(source);

        if (source == oneWeek) {
            timeFrameVisible = 1;
        } else if (source == twoWeeks) {
            timeFrameVisible = 2;
        } else if (source == threeWeeks) {
            timeFrameVisible = 3;
        } else if (source == fourWeeks) {
            timeFrameVisible = 4;
        } else if (source == fiveWeeks) {
            timeFrameVisible = 5;
        } else if (source == sixWeeks) {
            timeFrameVisible = 6;
        }

        setupXAxis();
    }
}
