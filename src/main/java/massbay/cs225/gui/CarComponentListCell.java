package massbay.cs225.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import massbay.cs225.api.carbuild.CarComponent;

import static massbay.cs225.util.I18n.s;

/**
 * @param <T>
 * @implNote Based on JavaFX's TextFieldListCell and CellUtils, but heavily modified.
 */
public class CarComponentListCell<T extends CarComponent> extends ListCell<T> {
    private final boolean multiType;
    private TextField textField;

    public static <T extends CarComponent> Callback<ListView<T>, ListCell<T>> factory(boolean multiType) {
        return v -> new CarComponentListCell(multiType);
    }

    public CarComponentListCell(boolean multiType) {
        this.multiType = multiType;

        itemProperty().addListener(o -> update());

        if (getItem() != null) {
            update();
        }
    }

    private void update() {
        StringExpression text = ComponentBindings.name(itemProperty());

        if (multiType) {
            text = text.concat(" " + (getItem() == null ? s("api.empty") : " (" + getItem().getClass().getSimpleName() + ")"));
        }

        editableProperty().bind(ComponentBindings.isRenameable(itemProperty()));
        textProperty().bind(Bindings
                .when(editingProperty().or(emptyProperty()).or(Bindings.isNotNull(graphicProperty())))
                .then((String) null)
                .otherwise(text)
        );
    }

    @Override
    public void startEdit() {
        if (!isEditable() || !getListView().isEditable()) {
            return;
        }

        super.startEdit();

        if (!isEditing()) {
            return;
        }

        if (textField == null) {
            textField = new TextField(getItem().getName());
        }

        textField.setText(getItem().getName());
        setGraphic(textField);
        textField.setOnAction(event -> commitEdit(getItem()));
        textField.selectAll();
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setGraphic(null);
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (isEmpty() || !isEditing()) {
            setGraphic(null);
        } else {
            setGraphic(textField);
        }
    }

    @Override
    public void commitEdit(T newValue) {
        super.commitEdit(newValue);

        String text = textField.getText();
        getItem().setName(text.isEmpty() ? s("api.empty") : text);
    }
}
